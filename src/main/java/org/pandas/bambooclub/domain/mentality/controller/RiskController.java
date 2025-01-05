package org.pandas.bambooclub.domain.mentality.controller;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.pandas.bambooclub.domain.board.service.BoardService;
import org.pandas.bambooclub.domain.mentality.dto.RiskHistory;
import org.pandas.bambooclub.domain.mentality.service.DataSetService;
import org.pandas.bambooclub.domain.mentality.service.PineconeService;
import org.pandas.bambooclub.domain.mentality.service.RiskService;
import org.pandas.bambooclub.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risk")
public class RiskController {
    private final DataSetService dataSetService = new DataSetService();
    private final PineconeService pineconeService = new PineconeService();
    private final RiskService riskService = new RiskService();

    private final BoardService BoardService;

    public RiskController(org.pandas.bambooclub.domain.board.service.BoardService boardService) {
        BoardService = boardService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<?>> evaluateRisk(@PathVariable String id, @RequestParam TextType type) throws Exception {
//        //id를 통해 해당하는 텍스트 내용을 가져옴
//        return new ResponseEntity<>(
//                ApiResponse.builder().status(HttpStatus.OK)
//                        .data(Map.of("riskScore", RiskService.getRiskScore()))
//                        .build(), HttpStatus.OK);
//    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<?>> getRiskHistory(@PathVariable String userId) {
        List<PostDetail> userPosts = BoardService.getPostPast12Months(userId);

        return new ResponseEntity<>(
                ApiResponse.builder().status(HttpStatus.OK)
                        .data(RiskHistory.builder().risks(riskService.getRiskHistory(userPosts)).build())
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/upsert/{text}")
    public ResponseEntity<ApiResponse<?>> upsert(@PathVariable String text) throws Exception {
        return new ResponseEntity<>(ApiResponse.builder().status(HttpStatus.OK)
                .data(pineconeService.upsertVectors(pineconeService.makeVectors(text)))
                .build(), HttpStatus.OK);
    }


}
