package org.pandas.bambooclub.global.config

import org.pandas.bambooclub.domain.chat.dto.OpenAIResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class OpenAIClient(
    @Value("\${api.key}") apiKey: String,
) {
    private val webClient: WebClient =
        WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(
                "Authorization",
                "Bearer $apiKey",
            )
            .defaultHeader("Content-Type", "application/json")
            .build()

    suspend fun sendMessage(
        mbti: String,
        content: String,
        previousContent: String,
        @Value("\${api.script:}") script: String = "",
    ): OpenAIResponse {
        val requestBody =
            """
            {
                "model": "gpt-4",
                "messages": [
                    {
                        "role": "system",
                        "content": "Provide counseling that is effective for people with $mbti personality type, using the approach of $script"
                    },
                    {
                        "role": "user",
                        "content": "previousContext: $previousContent, currentContent: $content"
                    }
                ]
            }
            
            """.trimIndent()

        return webClient.post()
            .bodyValue(requestBody)
            .retrieve()
            .awaitBody()
    }
}
