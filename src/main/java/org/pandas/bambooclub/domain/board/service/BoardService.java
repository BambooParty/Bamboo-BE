package org.pandas.bambooclub.domain.board.service;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostList;
import org.pandas.bambooclub.domain.board.repository.BoardRepository;
import org.pandas.bambooclub.domain.board.repository.CommentRepository;
import org.pandas.bambooclub.domain.mentality.dto.MbtiCharacters;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    public List<PostDetail> getPostPast12Months(String userId) {
        String startDate = LocalDateTime.now().minusMonths(12).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //DB에서 userId로 조회
        return boardRepository.findByDateBetweenAndUserId(startDate, endDate, userId);
    }

    public List<PostDetail> getPostList(String mbti, String userId) {
        List<PostDetail> result = new ArrayList<>();
        if (userId == null || userId.isBlank()) {//mbti를 MbtiCharacters으로 분류
            result = boardRepository.findByMbtiIn(MbtiCharacters.mbtis(mbti));
            //DB에서 Mbti별로 조회
        } else {
            //DB에서 userId로 조회
            result = boardRepository.findByUserId(userId);
        }
        result.forEach(post -> {
            post.setComments(commentRepository.findByPostId(post.getPostId()));
            post.setCommentCount(post.getComments().size());
        });
        return result;
    }

    public PostDetail getPostDetail(String postId) {
        List<PostDetail.Comment> comments = commentRepository.findByPostId(postId);
        PostDetail postDetail = boardRepository.findByPostId(postId);
        postDetail.setComments(comments);
        postDetail.setCommentCount(comments.size());
        return postDetail;
    }

    public PostDetail createPost(PostDetail postRegister) {
        // MongoDB에 저장
        return boardRepository.save(postRegister);
    }

    public PostDetail putPost(String postId, PostDetail post) {
        return boardRepository.save(post);
    }

    public void deletePost(String postId) {
        boardRepository.deleteById(postId);
    }


    public PostDetail.Comment createComment(String postId, PostDetail.Comment comment) {
        comment.setPostId(postId);
        comment.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return commentRepository.save(comment);
    }

    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
