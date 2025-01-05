package org.pandas.bambooclub.domain.board.service;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostList;
import org.pandas.bambooclub.domain.mentality.dto.MbtiCharacters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    public PostList getPostList(String mbti, String userId) {
        if (userId == null) {//mbti를 MbtiCharacters으로 분류
            MbtiCharacters mbtiOption = MbtiCharacters.makeMbti(mbti);

            //DB에서 Mbti별로 조회
        } else {
            //DB에서 userId로 조회
        }

        PostList postList = PostList.builder()
                .posts(List.of(
                        PostList.PostSummary.builder()
                                .title("제목")
                                .contentSummary("내용 100자 까지만")
                                .commentCount(4)
                                .date("2025-01-01")
                                .build()
                ))
                .build();

        return postList;
    }

    public PostDetail getPostDetail(String postId) {
        //DB에서 postId로 조회

        return PostDetail.builder()
                .mbti("ENFP")
                .title("제목")
                .date("2025-01-01")
                .nickname("작성자")
                .commentCount(4)
                .comments(List.of(
                        PostDetail.Comment.builder()
                                .nickname("작성자")
                                .date("2025-01-01")
                                .content("내용")
                                .build()
                ))
                .build();
    }

    public Object getMyPostList(String postType, String userId) {
        return null;
    }
}
