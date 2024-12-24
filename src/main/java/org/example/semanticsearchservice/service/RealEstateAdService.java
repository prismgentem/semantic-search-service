package org.example.semanticsearchservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.semanticsearchservice.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateAdService {
    private final QdrantService qdrantService;
    private final CsvReaderService csvReaderService;

    public void initData() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("data.csv");

        if (inputStream == null) {
            throw new RuntimeException("CSV file not found in resources.");
        }

        List<RealEstateAd> ads = csvReaderService.readCsv(inputStream);

        for (RealEstateAd ad : ads) {
            qdrantService.saveRealEstateAd(ad);
        }
    }

    @SneakyThrows
    public CreateDatabaseResponse processCsvAndSave(MultipartFile file, String databaseName) {
        try (InputStream inputStream = file.getInputStream()) {
            List<RealEstateAd> ads = csvReaderService.readCsv(inputStream);
            for (RealEstateAd ad : ads) {
                qdrantService.saveRealEstateAd(ad, databaseName);
            }
            return new CreateDatabaseResponse(databaseName, ads.size(), true);
        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    @SneakyThrows
    public CreateDatabaseResponse createDatabase(String databaseName) {
        qdrantService.createCollection(databaseName);
        return new CreateDatabaseResponse(databaseName, 0, true);
    }

    @SneakyThrows
    public CreateDatabaseResponse saveDataFromCsv(MultipartFile file, String databaseName) {
        try (InputStream inputStream = file.getInputStream()) {
            List<RealEstateAd> ads = csvReaderService.readCsv(inputStream);
            for (RealEstateAd ad : ads) {
                qdrantService.saveRealEstateAd(ad, databaseName);
            }
            return new CreateDatabaseResponse(databaseName, ads.size(), true);
        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    @SneakyThrows
    public UploadCsvResponse saveDataFromCsv(MultipartFile file, UploadCsvRequest request) {
        var databaseName = request.getDatabaseName();
        try (InputStream inputStream = file.getInputStream()) {
            List<RealEstateAd> ads = csvReaderService.readCsv(inputStream);
            for (int i = 0; i < request.getSavedObjectsCount(); i++) {
                qdrantService.saveRealEstateAd(ads.get(i), databaseName);
            }
            return new UploadCsvResponse(databaseName, request.getSavedObjectsCount());
        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    @SneakyThrows
    public List<SearchResponse> search(String databaseName, String searchRequest, int topN) {
        return qdrantService.searchSimilarAds(databaseName, searchRequest, topN);
    }
}

