package org.pandas.bambooclub.domain.mentality.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pandas.bambooclub.domain.mentality.service.DataSetService;
import org.pandas.bambooclub.domain.mentality.service.OpenAiEmbeddingService;
import org.pandas.bambooclub.domain.mentality.service.PineconeService;
import org.pandas.bambooclub.domain.mentality.service.RiskCalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.pinecone.clients.Pinecone;
import io.pinecone.clients.Index;
import io.pinecone.proto.DescribeIndexStatsResponse;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import org.openapitools.db_control.client.model.IndexModel;
import org.openapitools.db_control.client.model.DeletionProtection;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/evaluate")
public class RiskEvaluationController {
    private final OpenAiEmbeddingService embeddingService = new OpenAiEmbeddingService();
    private final DataSetService dataSetService = new DataSetService();
    private final PineconeService pineconeService = new PineconeService();
    private final RiskCalculator riskCalculator = new RiskCalculator();

    @GetMapping("/{id}")
    public ResponseEntity<?> evaluateRisk(@PathVariable String id) throws Exception {
        //id를 통해 해당하는 텍스트 내용을 가져옴

        //데이터셋 생성(임시)
//        String dataSet = dataSetService.createDataSet();
        //dataSet.replaceAll("[^가-힣a-zA-Z\\s]", "")
        float[] embedding = embeddingService.getEmbedding("죽고싶어");
        List<Float> similarityScores = pineconeService.queryEmbedding(embedding);
        int riskScore = riskCalculator.calculateWeightedRiskScore(similarityScores);


        return ResponseEntity.ok(Map.of(
                "risk_score", riskScore,
                "recommendation", riskScore > 80 ? "즉각적인 상담 권장" : "관찰 필요"
        ));
    }

    @GetMapping("/test")
    public ResponseEntity<?> testRisk() throws InterruptedException {
        Pinecone pc = new Pinecone.Builder("pcsk_5PTynA_6B8bS1YHzM5Z2rKU1GWPdh9MDKpqQ516GZ1CJP56pFfLFffCquQFz7xgx5CKHyW").build();

        String indexName = "quickstart";
        pc.createServerlessIndex(indexName, "cosine", 2, "aws", "us-east-1", DeletionProtection.DISABLED);

        //Add to the main function:

        Index index = pc.getIndexConnection(indexName);

// Check if index is ready
        while(!pc.describeIndex(indexName).getStatus().getReady()) {
            Thread.sleep(3000);
        }

        List<Float> values1 = Arrays.asList(1.0f, 1.5f);
        List<Float> values2 = Arrays.asList(2.0f, 1.0f);
        List<Float> values3 = Arrays.asList(0.1f, 3.0f);
        List<Float> values4 = Arrays.asList(1.0f, -2.5f);
        List<Float> values5 = Arrays.asList(3.0f, -2.0f);
        List<Float> values6 = Arrays.asList(0.5f, -1.5f);

        index.upsert("vec1", values1, "ns1");
//        index.upsert("vec2", values2, "ns1");
//        index.upsert("vec3", values3, "ns1");
//        index.upsert("vec1", values4, "ns2");
//        index.upsert("vec2", values5, "ns2");
//        index.upsert("vec3", values6, "ns2");

        // Add to the main function:

// Wait for the upserted vectors to be indexed
        Thread.sleep(10000);

        DescribeIndexStatsResponse indexStatsResponse = index.describeIndexStats(null);
        System.out.println(indexStatsResponse);

        // Add to the main function:

        List<Float> queryVector1 = Arrays.asList(1.0f, 1.5f);
//        List<Float> queryVector2 = Arrays.asList(1.0f, -2.5f);

        QueryResponseWithUnsignedIndices queryResponse1 = index.query(3, queryVector1, null, null, null, "ns1", null, false, true);
//        QueryResponseWithUnsignedIndices queryResponse2 = index.query(3, queryVector2, null, null, null, "ns2", null, false, true);

        System.out.println(queryResponse1);
//        System.out.println(queryResponse2);

        return  ResponseEntity.ok(Map.of(
                "message", "hi"
        ));
    }
}
