package org.pandas.bambooclub.domain.mentality.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pandas.bambooclub.global.Define.OPENAI_API_KEY;
import static org.pandas.bambooclub.global.Define.TEXT_COMPLETION_URL;

public class DataSetService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public String createDataSet() throws Exception {
        // 위험 텍스트 생성
        String riskPrompt = "자살 위험이 높은 사람이 사용할 가능성이 있는 문장을 5개 작성하세요. 단, 응답할 때 문장 앞에 숫자를 붙이지 마세요.";
        String riskTexts = generateRiskTexts(riskPrompt, 5);

//        // 출력 위험 텍스트
        System.out.println("Generated Risk Texts:");
        System.out.println(riskTexts);

        return riskTexts;
    }

    // 텍스트 생성 함수
    public static String generateRiskTexts(String prompt, int n) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("max_tokens", 100);
        requestBody.put("n", n);
        requestBody.put("model", "gpt-4o-mini-2024-07-18");
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are an assistant generating risk-related text."));
        messages.add(Map.of("role", "user", "content", prompt));
        requestBody.put("messages", messages);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TEXT_COMPLETION_URL))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();

        // 요청 보내기 및 응답 처리
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch text completions: " + response.body());
        }

        // 응답에서 텍스트 추출
        JsonNode rootNode = objectMapper.readTree(response.body());
        List<String> texts = new ArrayList<>();
        for (JsonNode choiceNode : rootNode.get("choices")) {
            texts.add(choiceNode.get("message").get("content").asText());
        }
        return texts.toString(); // TODO 불필요한 기호 확인
    }
}
