package org.pandas.bambooclub.domain.board.service;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostList;
import org.pandas.bambooclub.domain.board.dto.PostRegister;
import org.pandas.bambooclub.domain.board.repository.BoardRepository;
import org.pandas.bambooclub.domain.mentality.dto.MbtiCharacters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public PostList getPostList(String mbti, String userId) {
        List<PostDetail> result = null;
        if (userId == null || userId.isBlank()) {//mbti를 MbtiCharacters으로 분류
            result = boardRepository.findByMbtiIn(MbtiCharacters.mbtis(mbti));
            //DB에서 Mbti별로 조회
        } else {
            //DB에서 userId로 조회
            result = boardRepository.findByUserId(userId);
        }

        PostList postList = PostList.builder()
                .posts(result)
                .build();

        return postList;
    }

    public PostDetail getPostDetail(String postId) {
        return boardRepository.findByPostId(postId);
//        PostDetail.builder()
//                .mbti("ENFP")
//                .title("제목")
//                .date("2025-01-01")
//                .nickname("작성자")
//                .commentCount(4)
//                .comments(List.of(
//                        PostDetail.Comment.builder()
//                                .nickname("작성자")
//                                .date("2025-01-01")
//                                .content("내용")
//                                .build()
//                ))
//                .build();
    }

    public Object getMyPostList(String postType, String userId) {
        return null;
    }

    public PostDetail createPost(PostDetail postRegister) {
        // MongoDB에 저장
        return boardRepository.save(postRegister);
    }
}
