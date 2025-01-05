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
                "model": "gpt-3.5-turbo",
                "messages": [
                    {
                        "role": "system",
                        "content": "keep in mind that you are dealing with $mbti personality type"
                    },
                    {
                        "role": "user",
                        "content": "이전 내용은 맥락파악에만 쓰고 답장은 이번 내용에만 해줘 한 문장으로 답장해줘 , previousContext: $previousContent, currentContent: $content"
                    }
                ]
            }
            
            """.trimIndent()

        return webClient.post()
            .bodyValue(requestBody)
            .retrieve()
            .awaitBody()
    }

    suspend fun getComment(
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
                        "content": "keep in mind that you are dealing with $mbti personality type"
                    },
                    {
                        "role": "user",
                        "content": "이전 내용은 맥락파악에만 쓰고 답장은 이번 내용에만 해줘 한 문장으로 답장해줘, 다음 내용에 대해 $mbti 인 사람에게 답글을 써줘: $content"
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
