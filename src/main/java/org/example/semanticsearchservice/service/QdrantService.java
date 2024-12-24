package org.example.semanticsearchservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.PointStruct;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semanticsearchservice.model.RealEstateAd;
import org.example.semanticsearchservice.model.SearchResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QdrantService implements AutoCloseable {

    private static final String COLLECTION_NAME = "real_estate_ads";
    private static final int VECTOR_SIZE = 1536;
    private static final int MAX_AD_COUNT = 2000;

    private final QdrantClient qdrantClient;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    private int adCounter = 0;

    @PostConstruct
    private void initialize() {
        createCollection(COLLECTION_NAME);
    }

    public boolean collectionExists(String collectionName) {
        try {
            return qdrantClient.collectionExistsAsync(collectionName).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error checking collection existence: " + collectionName, e);
        }
    }

    public void createCollection(String collectionName) {
        if (collectionExists(collectionName)) {
            log.info("Collection already exists: {}", collectionName);
            return;
        }

        try {
            qdrantClient.createCollectionAsync(
                    collectionName,
                    VectorParams.newBuilder()
                            .setDistance(Distance.Cosine)
                            .setSize(VECTOR_SIZE)
                            .build()
            ).get();
            log.info("Collection created: {}", collectionName);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error creating collection: " + collectionName, e);
        }
    }

    public void saveRealEstateAd(RealEstateAd ad) {
        validateAdCounter();

        List<Float> embedding = openAiService.getEmbedding(realEstateAdToString(ad));
        Map<String, Object> payload = extractPayload(ad);

        PointStruct point = buildPoint(adCounter++, embedding, payload);

        try {
            qdrantClient.upsertAsync(COLLECTION_NAME, List.of(point)).get();
            log.info("Saved RealEstateAd with ID: {}", adCounter);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error saving RealEstateAd to Qdrant", e);
        }
    }

    public String realEstateAdToString(RealEstateAd ad) {
        return ad.getTitle() + " " + ad.getPrice() + " " + ad.getDate() + " "  + ad.getRegion() + " " + ad.getCity() + " " + ad.getAddress() + " " + ad.getDescription() + " " + ad.getAdType() + " " + ad.getCategory1() + " " + ad.getCategory2() + " "  + ad.getAdditionalParams();
    }

    public void saveRealEstateAd(RealEstateAd ad, String databaseName) {
        createCollection(databaseName);
        validateAdCounter();

        List<Float> embedding = openAiService.getEmbedding(ad.toString());
        Map<String, Object> payload = extractPayload(ad);

        PointStruct point = buildPoint(adCounter++, embedding, payload);

        try {
            qdrantClient.upsertAsync(databaseName, List.of(point)).get();
            log.info("Saved RealEstateAd with ID: {}", adCounter);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error saving RealEstateAd to Qdrant", e);
        }
    }

    public List<SearchResponse> searchSimilarAds(String databaseName, String query, int limit) {
        List<Float> embedding = openAiService.getEmbedding(query);

        try {
            List<Points.ScoredPoint> points = qdrantClient.searchAsync(
                    Points.SearchPoints.newBuilder()
                            .setCollectionName(databaseName)
                            .addAllVector(embedding)
                            .setLimit(limit)
                            .setWithPayload(Points.WithPayloadSelector.newBuilder().setEnable(true).build())
                            .build()
            ).get();

            return points.stream()
                    .map(this::mapToSearchResponse)
                    .toList();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error searching similar ads", e);
        }
    }

    private void validateAdCounter() {
        if (adCounter >= MAX_AD_COUNT) {
            adCounter = 0;
            throw new IllegalStateException("Ad count exceeds maximum limit of " + MAX_AD_COUNT);
        }
    }

    private Map<String, Object> extractPayload(RealEstateAd ad) {
        Map<String, Object> payload = new HashMap<>();
        for (Field field : RealEstateAd.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(ad);
                if (value != null) {
                    payload.put(field.getName(), value.toString());
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error accessing field value: " + field.getName(), e);
            }
        }
        return payload;
    }

    private PointStruct buildPoint(int id, List<Float> embedding, Map<String, Object> payload) {
        return PointStruct.newBuilder()
                .setId(id(id))
                .setVectors(vectors(embedding))
                .putAllPayload(
                        payload.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> value(entry.getValue().toString())
                                ))
                )
                .build();
    }

    private SearchResponse mapToSearchResponse(Points.ScoredPoint scoredPoint) {
        Map<String, Object> transformedPayload = scoredPoint.getPayload().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            JsonWithInt.Value value = entry.getValue();
                            if (value.hasStringValue()) {
                                return value.getStringValue();
                            } else if (value.hasIntegerValue()) {
                                return value.getIntegerValue();
                            }
                            return null;
                        }
                ));
        return objectMapper.convertValue(transformedPayload, SearchResponse.class);
    }

    @Override
    public void close() {
        qdrantClient.close();
    }
}
