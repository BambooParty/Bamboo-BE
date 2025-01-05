package org.pandas.bambooclub.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostList {
    private List<PostDetail> posts;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSummary {
        private String title;
        private String contentSummary;
        private int commentCount;
        private String date;
    }
}
