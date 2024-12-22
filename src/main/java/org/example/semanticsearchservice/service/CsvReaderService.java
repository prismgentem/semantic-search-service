package org.example.semanticsearchservice.service;

import lombok.SneakyThrows;
import org.example.semanticsearchservice.model.RealEstateAd;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    @SneakyThrows
    public List<RealEstateAd> readCsv(InputStream inputStream) {
        List<RealEstateAd> ads = new ArrayList<>();

        // Используем InputStreamReader для чтения из InputStream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            reader.readLine(); // Пропускаем заголовок CSV файла (если он есть)
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");

                // Создаем новый объект RealEstateAd
                RealEstateAd ad = new RealEstateAd();

                // Заполняем поля объекта из соответствующих столбцов CSV
                ad.setTitle(columns[0]);
                ad.setRegion(columns[7]);
                ad.setCity(columns[8]);  // Здесь можно также изменить логику, если город в другом столбце
                ad.setAddress(columns[10]);
                ad.setDescription(columns[11]);
                ad.setCategory1(columns[13]);
                ad.setCategory2(columns[14]);
                ad.setAdditionalParams(columns[20]);

                // Добавляем объект в список
                ads.add(ad);
            }
        }
        return ads;
    }
}

