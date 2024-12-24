package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateAd {
    private String title;//0
    private String price;//1
    private String date;//2
    private String phone;//3
    private String operator;
    private String contactPerson;
    private String authorType;
    private String region;//7
    private String city;//8
    private String metro;
    private String address;//10
    private String description;//11
    private String adType;//12
    private String category1;//13
    private String category2;//14
    private long idOnSite;
    private String source;//16
    private double lat;
    private double lng;
    private String contactPerson2;
    private String additionalParams;//20
    private String url;//21
    private String imageLinks;
    private String mobilePhoneRegion;
    private String phoneSubstitution;
    private String distanceToMetro;
}

