package org.pandas.bambooclub.domain.mentality.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskHistory {
    public List<Risk> risks;
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Risk {
        // 위험도 점수
        // 등록월
        private int score;
        private String month;
    }
}
