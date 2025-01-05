package org.pandas.bambooclub.domain.board.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetail {
    private String id;
    // mbti, 제목, 코멘트수, 날짜, 작성자 닉네임, 댓글목록(댓글작성자, 날짜, 내용)
    private String mbti;
    private String title;
    private String date;
    private String nickname;
    private List<Comment> comments;
    private int commentCount;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private String nickname;
        private String date;
        private String content;
    }
}
