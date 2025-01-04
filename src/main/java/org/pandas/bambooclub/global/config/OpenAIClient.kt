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
    ): OpenAIResponse {
        val requestBody =
            """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {
                        "role": "system",
                        "content": "mbti가 ${mbti}인 사람에게 잘 통하도록 상담해줘."
                    },
                    {
                        "role": "user",
                        "content": "$content"
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
