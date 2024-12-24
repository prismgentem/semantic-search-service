package org.example.semanticsearchservice.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenAiResponse {
    private String object;
    private List<EmbeddingData> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmbeddingData {
        private String object;
        private int index;
        private List<Float> embedding;
    }
}
