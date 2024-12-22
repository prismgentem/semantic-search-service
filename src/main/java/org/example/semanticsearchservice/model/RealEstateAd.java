package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateAd {
    private String title;//
    private double price;
    private String date;
    private String phone;
    private String operator;
    private String contactPerson;
    private String authorType;
    private String region;//7
    private String city;//8
    private String metro;
    private String address;//
    private String description;//
    private String adType;
    private String category1;//13
    private String category2;//14
    private long idOnSite;
    private String source;
    private double lat;
    private double lng;
    private String contactPerson2;
    private String additionalParams;//20
    private String url;
    private String imageLinks;
    private String mobilePhoneRegion;
    private String phoneSubstitution;
    private String distanceToMetro;
}

