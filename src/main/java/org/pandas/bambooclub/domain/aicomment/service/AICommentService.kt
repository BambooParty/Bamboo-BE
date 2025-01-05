package org.pandas.bambooclub.domain.aicomment.service

import kotlinx.coroutines.runBlocking
import org.pandas.bambooclub.global.config.OpenAIClient
import org.springframework.stereotype.Service

@Service
class AICommentService(
    private val openAIClient: OpenAIClient,
) {
    fun getComment(
        content: String,
        mbti: String,
    ): String {
        return runBlocking {
            val response = openAIClient.getComment(mbti, content)
            val content: String = response?.choices?.get(0)?.message?.content ?: ""
            content
        }
    }
}
