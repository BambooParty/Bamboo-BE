package org.pandas.bambooclub.global.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RiskApiConfig {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    @Value("${pincone.api-key}")
    private String pinconeApiKey;

    private static String OPENAI_API_KEY;
    private static String PINECONE_API_KEY;

    @PostConstruct
    private void init() {
        OPENAI_API_KEY = openaiApiKey;
        PINECONE_API_KEY = pinconeApiKey;
    }

    public static String getOpenAiKey() {
        return OPENAI_API_KEY;
    }
    public static String getPineconeApiKey() {
        return PINECONE_API_KEY;
    }
}
