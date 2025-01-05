package org.pandas.bambooclub.domain.board.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "postDetail")
public class PostDetail {
    @Id
    private String postId;
    private String userId;
    private String mbti;
    private String title;
    private String date;
    private String nickname;
    private int riskScore;
    private String content;
    private List<Comment> comments;
    private int commentCount;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private String userId;
        private String postId;
        @Id
        private String commentId;
        private String nickname;
        private String date;
        private String content;
    }
}
