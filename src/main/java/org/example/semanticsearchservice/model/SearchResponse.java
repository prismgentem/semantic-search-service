package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private String title;
    private String price;
    private String date;
    private String phone;
    private String region;
    private String city;
    private String address;
    private String description;
    private String adType;
    private String category1;
    private String category2;
    private String source;
    private String additionalParams;
    private String url;
}
