package org.pandas.bambooclub.domain.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostRegister;
import org.pandas.bambooclub.domain.board.repository.BoardRepository;
import org.pandas.bambooclub.domain.board.service.BoardService;
import org.pandas.bambooclub.domain.mentality.service.RiskService;
import org.pandas.bambooclub.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;
private final BoardRepository boardRepository;
    public BoardController(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/posts") //나의 목록, 전체목록 2개 케이스
    public ResponseEntity<ApiResponse<?>> getPostList(@RequestParam String mbti, @RequestParam String userId) {
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.OK)
                        .data(boardService.getPostList(mbti, userId))
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> getPostDetail(@PathVariable String postId) {
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.OK)
                        .data(boardService.getPostDetail(postId))
                        .build(), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody PostDetail post) throws JsonProcessingException {
        //등록 전 위험도 평가 후 같이 등록
        int score = RiskService.getRiskScore(post.getContent());
        post.setRiskScore(score);
        post.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.CREATED)
                        .data(PostRegister.builder()
                                .post(boardService.createPost(post))
                                .build())
                        .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> deletePost(@PathVariable String postId) {
        boardService.deletePost(postId);
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.NO_CONTENT)
                        .build(), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<?>> createComment(@PathVariable String postId, @RequestBody PostDetail.Comment comment) {
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.CREATED)
                        .data(boardService.createComment(postId, comment))
                        .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable String commentId) {
        boardService.deleteComment(commentId);
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.NO_CONTENT)
                        .build(), HttpStatus.NO_CONTENT);
    }
}
