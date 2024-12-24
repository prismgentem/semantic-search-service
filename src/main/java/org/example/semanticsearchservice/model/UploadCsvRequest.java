package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadCsvRequest {
    private String databaseName;
    private Integer savedObjectsCount;
}
