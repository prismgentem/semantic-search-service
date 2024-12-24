package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDatabaseResponse {
    private String databaseName;
    private Integer dbSize;
    private Boolean isCreated;
}
