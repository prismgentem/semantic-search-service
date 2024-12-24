package org.example.semanticsearchservice.model;

import lombok.Data;

@Data
public class SearchRequest {
    private String query;
    private String databaseName;
}
