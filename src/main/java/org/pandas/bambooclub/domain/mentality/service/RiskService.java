package org.pandas.bambooclub.domain.mentality.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostList;
import org.pandas.bambooclub.domain.board.service.BoardService;
import org.pandas.bambooclub.domain.mentality.dto.RiskHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public List<RiskHistory.Risk> getRiskHistory(List<PostDetail> userPosts) {
        // 월별로 riskScore 평균을 냄
        List<RiskHistory.Risk> result = calculateMonthlyRiskScore(userPosts);
        return result;

    }

    private List<RiskHistory.Risk> calculateMontlyRiskScore(List<PostDetail> userPosts) {
        List<RiskHistory.Risk> result = new ArrayList<>();
        // 월별로 해서 12개의 Risk 객체를 만들어야 함
        Map<String, RiskHistory.Risk> montlyRiskScore = new HashMap<>();
        userPosts.forEach(post -> {
            String month = post.getDate();
            int riskScore = post.getRiskScore();
            if (montlyRiskScore.containsKey(month)) {
                RiskHistory.Risk risk = montlyRiskScore.get(month);
                risk.setScore(risk.getScore() + riskScore);
            } else {
                montlyRiskScore.put(month, RiskHistory.Risk.builder().score(riskScore).month(month).build());
            }
        });
        montlyRiskScore.forEach((key, value) -> {
            value.setScore(value.getScore() / userPosts.size());
            result.add(value);
        });
        return result;
    }

    private List<RiskHistory.Risk> calculateMonthlyRiskScore(List<PostDetail> userPosts) {
        List<RiskHistory.Risk> result = new ArrayList<>();

        // 시작 월과 종료 월 설정
        LocalDate startDate = LocalDate.now().minusMonths(11).withDayOfMonth(1); // 11개월 전의 첫날
        LocalDate endDate = LocalDate.now().withDayOfMonth(1); // 현재 월의 첫날

        // 모든 월을 초기화 (점수 0)
        Map<String, RiskHistory.Risk> monthlyRiskScore = new LinkedHashMap<>();
        Map<String, Integer> monthlyRiskScoreCount = new LinkedHashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String yearMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            monthlyRiskScore.put(yearMonth, RiskHistory.Risk.builder().month(yearMonth).score(0).build());
            monthlyRiskScoreCount.put(yearMonth, 0);
            currentDate = currentDate.plusMonths(1); // 다음 달로 이동
        }

        // 사용자 게시물 데이터를 기반으로 점수 계산

        userPosts.forEach(post -> {
            String postYearMonth = post.getDate().substring(0, 6); // yyyyMM 추출
            if (monthlyRiskScore.containsKey(postYearMonth)) {
                RiskHistory.Risk risk = monthlyRiskScore.get(postYearMonth);
                risk.setScore(risk.getScore() + post.getRiskScore());
                monthlyRiskScoreCount.put(postYearMonth, monthlyRiskScoreCount.get(postYearMonth) + 1);
            }
        });

        // 평균 점수 계산
        monthlyRiskScore.forEach((key, value) -> {
            int count = monthlyRiskScoreCount.get(key);
            if (count > 0) {
                value.setScore(value.getScore() / count);
            }
        });

        // 결과 리스트로 변환
        result.addAll(monthlyRiskScore.values());

        return result;
    }
}
