package org.example.semanticsearchservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.semanticsearchservice.model.RealEstateAd;
import org.example.semanticsearchservice.service.RealEstateAdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api")
@RequiredArgsConstructor
public class SearchController {
    private final RealEstateAdService realEstateAdService;

    @PostMapping("/search")
    public ResponseEntity<List<RealEstateAd>> search(String searchRequest){
        return ResponseEntity.ok(realEstateAdService.search(searchRequest));
    }
}
