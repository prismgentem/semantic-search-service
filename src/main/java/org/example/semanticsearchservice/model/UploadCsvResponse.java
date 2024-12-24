package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCsvResponse {
    private String databaseName;
    private Integer savedObjectsCount;
}
