package org.example.semanticsearchservice.service;

import lombok.RequiredArgsConstructor;
import org.example.semanticsearchservice.client.OpenAIClient;
import org.example.semanticsearchservice.client.OpenAiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAIClient openAIClient;
    @Value("${proxy-api.model}")
    private String model;

    public List<Float> getEmbedding(String text) {
        var request = OpenAiRequest.builder()
                .model(model)
                .input(text)
                .build();

        var responce = openAIClient.postMessageToGpt(request).getData();
        return responce.get(0).getEmbedding();
    }
}
