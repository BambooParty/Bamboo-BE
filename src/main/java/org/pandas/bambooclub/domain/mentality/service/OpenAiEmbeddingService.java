package org.pandas.bambooclub.domain.mentality.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.pandas.bambooclub.global.Define.EMBEDDING_URL;

public class OpenAiEmbeddingService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${openai.api-key}")
    private static String OPENAI_API_KEY;

    public static float[] getEmbedding(String text) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        String requestBody = "{ \"input\": \"" + text + "\", \"model\": \"text-embedding-ada-002\" }";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(EMBEDDING_URL, HttpMethod.POST, entity, String.class);

        return parseEmbedding(response.getBody());
    }

    private static float[] parseEmbedding(String jsonResponse) throws JsonProcessingException {
/*
{
  "object": "list",
  "data": [
    {
      "object": "embedding",
      "index": 0,
      "embedding": [
        -0.017843839,
        0.011641044,
        0.017750764,
        -0.005178968,
        -0.034544185, ...
      ]
    }
  ],
  "model": "text-embedding-ada-002",
  "usage": {
    "prompt_tokens": 3,
    "total_tokens": 3
  }
}
 */
        // JSON 응답 파싱
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // `data[0].embedding` 추출
        JsonNode embeddingNode = rootNode.path("data").get(0).path("embedding");

        // JSON 배열을 Java float 배열로 변환
        float[] embedding = new float[embeddingNode.size()];
        for (int i = 0; i < embeddingNode.size(); i++) {
            embedding[i] = (float) embeddingNode.get(i).asDouble();
        }

        return embedding; // 숫자 배열 반환
    }
}
