package org.example.semanticsearchservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.semanticsearchservice.model.RealEstateAd;
import org.example.semanticsearchservice.util.SimpleVectorizer;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateAdService {
    private final QdrantService qdrantService;
    private final CsvReaderService csvReaderService;

    // Метод с аннотацией @PostConstruct для выполнения инициализационной логики
    @PostConstruct
    @SneakyThrows
    public void init(){
        // Имя коллекции
        String collectionName = "real_estate_ads";

        // Проверяем, существует ли коллекция
        if (!qdrantService.collectionExists(collectionName)) {
            // Если коллекции нет, создаем её
            qdrantService.createCollection(collectionName);

            // Получаем все объявления из CSV (файл в папке ресурсов)
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("real_estate_ads.csv");

            if (inputStream == null) {
                throw new RuntimeException("CSV file not found in resources.");
            }

            List<RealEstateAd> ads = csvReaderService.readCsv(inputStream);

            // Загружаем векторы объявлений в Qdrant
            qdrantService.addVectors(collectionName, ads);
        }
    }

    @SneakyThrows
    public List<RealEstateAd> search(String searchRequest) {
        // Векторизация поискового запроса
        List<Float> queryVector = SimpleVectorizer.vectorize(searchRequest);

        // Имя коллекции
        String collectionName = "real_estate_ads";

        // Выполняем поиск похожих объявлений в Qdrant
        qdrantService.searchVectors(collectionName, queryVector);

        // Пока что возвращаем все объявления для примера
        return null; // Можете добавить логику для возврата похожих объявлений
    }
}

