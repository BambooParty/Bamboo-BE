package org.pandas.bambooclub.domain.board.controller;


import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.dto.PostRegister;
import org.pandas.bambooclub.domain.board.service.PostService;
import org.pandas.bambooclub.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BoardController {
    private final PostService postService;

    public BoardController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts") //나의 목록, 전체목록 2개 케이스
    public ResponseEntity<ApiResponse<?>> getPostList(@RequestParam String mbti, @RequestParam String userId) {

        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.OK)
                        .data(postService.getPostList(mbti, userId))
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> getPostDetail(@PathVariable String postId) {

        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.OK)
                        .data(postService.getPostDetail(postId))
                        .build(), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody PostRegister post) {
        //TODO: postService.createPost(post);

        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.CREATED)
                        .data(post)
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
