package org.pandas.bambooclub.domain.board.controller;


import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostRegister;
import org.pandas.bambooclub.domain.board.repository.BoardRepository;
import org.pandas.bambooclub.domain.board.service.BoardService;
import org.pandas.bambooclub.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody PostRegister.PostRegisterRequest post) {
        //등록 전 위험도 평가 후 같이 등록
        boardService.createPost(post);
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.CREATED)
                        .data(PostRegister.builder()
                                .post(post)
                                .riskScore(70)
                                .build())
                        .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> deletePost(@PathVariable String postId) {
        //TODO: postService.deletePost(postId);

        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.NO_CONTENT)
                        .build(), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<?>> createComment(@RequestBody PostDetail.Comment comment) {
        //TODO: postService.createComment();
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.CREATED)
                        .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable String commentId) {
        //TODO: postService.deleteComment();
        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.NO_CONTENT)
                        .build(), HttpStatus.NO_CONTENT);
    }
}
