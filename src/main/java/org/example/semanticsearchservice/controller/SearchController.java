package org.example.semanticsearchservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.semanticsearchservice.model.*;
import org.example.semanticsearchservice.service.RealEstateAdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchController {
    private final RealEstateAdService realEstateAdService;

    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(realEstateAdService.search(searchRequest.getDatabaseName(), searchRequest.getQuery(), 10));
    }

    @PostMapping("/upload-csv-and-create-db")
    public ResponseEntity<CreateDatabaseResponse> uploadCsvAndCreateDatabase(
            @RequestParam("file") MultipartFile file,
            @RequestBody CreateDatabaseRequest createDatabaseRequest
    ) {
        return ResponseEntity.ok(realEstateAdService.processCsvAndSave(file, createDatabaseRequest.getDatabaseName()));
    }

    @PostMapping("/create-db")
    public ResponseEntity<CreateDatabaseResponse> createDatabase(@RequestBody CreateDatabaseRequest createDatabaseRequest) {
        return ResponseEntity.ok(realEstateAdService.createDatabase(createDatabaseRequest.getDatabaseName()));
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<UploadCsvResponse> uploadCsv(@RequestParam("file") MultipartFile file, @ModelAttribute UploadCsvRequest uploadCsvRequest) {
        return ResponseEntity.ok(realEstateAdService.saveDataFromCsv(file, uploadCsvRequest));
    }

    @GetMapping("/add-data")
    public ResponseEntity<Void> addData(){
        realEstateAdService.initData();
        return ResponseEntity.ok().build();
    }
}
