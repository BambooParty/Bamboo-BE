package org.pandas.bambooclub.domain.mentality.service;

import java.util.List;

public class RiskCalculator {
    // 평균 점수 계산
    public int calculateRisk(List<Float> similarityScores) {
        float averageScore = similarityScores.stream().reduce(0f, Float::sum) / similarityScores.size();
        return Math.round(averageScore * 100); // 0~100 점수로 변환
    }

    // 가중 평균 점수 계산
    public int calculateWeightedRiskScore(List<Float> similarities) {
        float weightedSum = 0;
        int weightSum = 0;
        int weight = similarities.size(); // 가중치: 가장 높은 유사도가 가장 큰 가중치

        for (float score : similarities) {
            weightedSum += score * weight; // 유사도 * 가중치
            weightSum += weight;          // 가중치 누적
            weight--;                     // 다음 유사도에 낮은 가중치 할당
        }

        return Math.round((weightedSum / weightSum) * 100); // 최종 점수를 100 단위로 변환
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
}
