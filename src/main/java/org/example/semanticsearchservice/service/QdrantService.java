package org.example.semanticsearchservice.service;

import lombok.RequiredArgsConstructor;
import org.example.semanticsearchservice.model.RealEstateAd;
import org.example.semanticsearchservice.util.SimpleVectorizer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QdrantService {

    private static final String QDRANT_API_URL = "http://localhost:6333";
    private final RestTemplate restTemplate;

    public boolean collectionExists(String collectionName) {
        String url = QDRANT_API_URL + "/collections/" + collectionName;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;  // Если коллекция не найдена, то ошибка
        }
    }

    public void createCollection(String collectionName) {
        String url = QDRANT_API_URL + "/collections";
        String requestJson = String.format("{\"name\": \"%s\"}", collectionName);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestJson, String.class);
        handleResponse(response);
    }

    public void addVectors(String collectionName, List<RealEstateAd> ads) {
        String url = QDRANT_API_URL + "/collections/" + collectionName + "/points";

        // Преобразуем объявления в векторы
        List<List<Float>> vectors = ads.stream()
                .map(ad -> SimpleVectorizer.vectorize(ad.toString()))
                .toList();

        // Формируем запрос для добавления векторов в коллекцию
        String requestJson = String.format("{\"points\": %s}", vectors.toString());
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestJson, String.class);
        handleResponse(response);
    }

    // Поиск ближайших векторов по запросу
    public void searchVectors(String collectionName, List<Float> queryVector) {
        String url = QDRANT_API_URL + "/collections/" + collectionName + "/points/search";

        // Формируем JSON запрос для поиска
        String requestJson = String.format("{\"vector\": %s, \"top\": 5}", queryVector.toString());
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestJson, String.class);
        handleResponse(response);
    }

    // Обработка ответа
    private void handleResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Request was successful: " + response.getBody());
        } else {
            System.err.println("Request failed: " + response.getBody());
        }
    }
}
