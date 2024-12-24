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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                RealEstateAd ad = new RealEstateAd();
                ad.setTitle(columns[0]);
                ad.setPrice(columns[1]);
                ad.setDate(columns[2]);
                ad.setPhone(columns[3]);
                ad.setRegion(columns[7]);
                ad.setCity(columns[8]);
                ad.setAddress(columns[10]);
                ad.setDescription(columns[11]);
                ad.setAdType(columns[12]);
                ad.setCategory1(columns[13]);
                ad.setCategory2(columns[14]);
                ad.setSource(columns[16]);
                ad.setAdditionalParams(columns[20]);
                ad.setUrl(columns[21]);
                ads.add(ad);
            }
        }
        return ads;
    }
}

