package org.pandas.bambooclub.domain.mentality.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PineconeService {
    private static final String PINECONE_API_KEY = "pcsk_5g3Gpt_FitZWX4ScBJgkzrKAnKLYN3TVfQhYQTzwDRZimbzR7Ys549Vkbs8HXKxpZaPg79";
    private static final String PINECONE_URL = "https://bamboo-xss5t7t.svc.aped-4627-b74a.pinecone.io";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public List<Float> queryEmbedding(float[] embedding) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(PINECONE_API_KEY);

        String requestBody = "{ \"vector\": " + Arrays.toString(embedding) + ", \"topK\": 5 }";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(PINECONE_URL, HttpMethod.POST, entity, String.class);

        return parseSimilarityScores(response.getBody());
    }

    private List<Float> parseSimilarityScores(String jsonResponse) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        List<Float> similarities = new ArrayList<>();

        for (JsonNode match : rootNode.path("matches")) {
            similarities.add((float) match.path("score").asDouble());
        }
        return similarities;
    }
}
