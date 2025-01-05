package org.pandas.bambooclub.domain.mentality.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PineconeService {
    private static final String PINECONE_API_KEY = "pcsk_5g3Gpt_FitZWX4ScBJgkzrKAnKLYN3TVfQhYQTzwDRZimbzR7Ys549Vkbs8HXKxpZaPg79";
    private static final String PINECONE_URL_QUERY = "https://bamboo-xss5t7t.svc.aped-4627-b74a.pinecone.io/query";
    private static final String PINECONE_URL_UPSERT = "https://bamboo-xss5t7t.svc.aped-4627-b74a.pinecone.io/vectors/upsert";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    public List<Float> queryEmbedding(float[] embedding) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", PINECONE_API_KEY);

        String requestBody = "{ \"vector\": " + Arrays.toString(embedding) + ", \"topK\": 3, \"includeMetadata\": true }";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(PINECONE_URL_QUERY, HttpMethod.POST, entity, String.class);

        return parseSimilarityScoresAdjusted(response.getBody());
    }

    private List<Float> parseSimilarityScores(String jsonResponse) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        List<Float> similarities = new ArrayList<>();

        for (JsonNode match : rootNode.path("matches")) {
            similarities.add((float) match.path("score").asDouble());
        }
        return similarities;
    }

    private List<Float> parseSimilarityScoresAdjusted(String jsonResponse) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        List<Float> adjustedSimilarities = new ArrayList<>();

        for (JsonNode match : rootNode.path("matches")) {
            // 유사도 점수 가져오기
            float similarityScore = (float) match.path("score").asDouble();

            // 감정 점수 가져오기 (예: -1: 부정적, 0: 중립, 1: 긍정적)
            int emotionScore = match.path("metadata").path("emotion").asInt();

            // 점수 조정: 부정적 감정(-1)에 더 높은 가중치 부여
            float adjustedScore = similarityScore * (1 - emotionScore);
            // 감정 점수:
            // -1 (부정적): 1 - (-1) = 2 → 유사도 × 2 (가중치 증가)
            //  0 (중립): 1 - 0 = 1 → 유사도 그대로
            //  1 (긍정적): 1 - 1 = 0 → 위험 점수 0

            adjustedSimilarities.add(adjustedScore);
        }
        return adjustedSimilarities;
    }
    public String upsertVectors(List<Map<String, Object>> vectors) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", PINECONE_API_KEY);

        Map<String, Object> requestBody = Map.of(
                "vectors", vectors
        );

        System.out.println("requestBody :" + requestBody);
        System.out.println("vectors. :" + vectors.get(0).get("values"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PINECONE_URL_UPSERT, HttpMethod.POST, entity, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to upsert vectors: " + response.getBody());
        }
        return response.getBody();
    }

    public List<Map<String, Object>> makeVectors(String sentence) throws JsonProcessingException {
        List<String> phrases = splitIntoPhrases(sentence); // 문장을 어절 단위로 나눔
        List<Map<String, Object>> vectors = new ArrayList<>();

        for (String phrase : phrases) {
            // 어절마다 임베딩 생성
            float[] embedding = OpenAiEmbeddingService.getEmbedding(phrase);

            // 벡터 데이터 구성
            Map<String, Object> vector = Map.of(

                    "id", String.valueOf(phrase.hashCode()),
                    "values", embedding,
                    "metadata", Map.of("text", phrase)
            );
            vectors.add(vector);
        }
        return vectors;
    }

    private List<String> splitIntoPhrases(String sentence) {
        return Arrays.asList(sentence.split("\\s+")); // 공백 기준 분리
    }
}
