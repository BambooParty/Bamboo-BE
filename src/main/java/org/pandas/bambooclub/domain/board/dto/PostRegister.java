package org.pandas.bambooclub.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegister {

    @JsonUnwrapped
    private PostDetail post;
    private int riskScore;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostRegisterRequest {
        private String title;
        private String content;
        private String mbti;
        private String nickname;
    }
}
