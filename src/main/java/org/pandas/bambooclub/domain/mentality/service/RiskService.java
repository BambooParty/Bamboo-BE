package org.pandas.bambooclub.domain.mentality.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pandas.bambooclub.domain.mentality.dto.RiskHistory;

import java.util.List;

public class RiskService {
    public static int getRiskScore(String inputText) throws JsonProcessingException {
        float[] embedding = OpenAiEmbeddingService.getEmbedding(inputText);
        List<Float> similarityScores = PineconeService.queryEmbedding(embedding);
        return calculateWeightedRiskScore(similarityScores);
    }

    // 평균 점수 계산
    public int calculateRisk(List<Float> similarityScores) {
        float averageScore = similarityScores.stream().reduce(0f, Float::sum) / similarityScores.size();
        return Math.round(averageScore * 100); // 0~100 점수로 변환
    }

    // 가중 평균 점수 계산
    public static int calculateWeightedRiskScore(List<Float> similarities) {
        float weightedSum = 0;
        int weightSum = 0;
        int weight = similarities.size(); // 가중치: 가장 높은 유사도가 가장 큰 가중치

        for (float score : similarities) {
            weightedSum += score * weight; // 유사도 * 가중치
            weightSum += weight;          // 가중치 누적
            weight--;                     // 다음 유사도에 낮은 가중치 할당
        }
        return Math.min(100, Math.round((weightedSum / weightSum) * 100));
    }

    // 점수에 따른 상태 분류
    public String classifyRiskScore(int riskScore) {
        if (riskScore <= 20) {
            return "안정";
        } else if (riskScore <= 50) {
            return "주의";
        } else if (riskScore <= 80) {
            return "위험";
        } else {
            return "긴급";
        }
    }

    public List<RiskHistory.Risk> getRiskHistory(String userId) {
        // 최근 12개월 간의 사용자 위험도 기록을 가져옴

        // 월별로 평균을 냄

        // 위험도 점수와 등록월을 Risk 객체로 변환

        return List.of(
                RiskHistory.Risk.builder().score(50).month("1").build(),
                RiskHistory.Risk.builder().score(70).month("2").build(),
                RiskHistory.Risk.builder().score(90).month("3").build(),
                RiskHistory.Risk.builder().score(30).month("4").build(),
                RiskHistory.Risk.builder().score(40).month("5").build(),
                RiskHistory.Risk.builder().score(60).month("6").build(),
                RiskHistory.Risk.builder().score(80).month("7").build(),
                RiskHistory.Risk.builder().score(20).month("8").build(),
                RiskHistory.Risk.builder().score(10).month("9").build(),
                RiskHistory.Risk.builder().score(70).month("10").build(),
                RiskHistory.Risk.builder().score(90).month("11").build(),
                RiskHistory.Risk.builder().score(100).month("12").build()
        );

    }
}
