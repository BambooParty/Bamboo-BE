package org.pandas.bambooclub.domain.chat.service

import org.pandas.bambooclub.domain.chat.dto.ChatRequest
import org.pandas.bambooclub.domain.chat.dto.ChatResponse
import org.pandas.bambooclub.domain.chat.dto.OpenAIResponse
import org.pandas.bambooclub.domain.chat.model.Chat
import org.pandas.bambooclub.domain.chat.repository.ChatRepository
import org.pandas.bambooclub.global.config.OpenAIClient
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val openAIClient: OpenAIClient,
) {
    suspend fun chat(request: ChatRequest): ChatResponse {
        val humanChat =
            chatRepository.save(
                Chat(
                    userId = request.userId,
                    mbti = request.mbti,
                    chatRoomId = request.chatRoomId,
                    content = request.content,
                ),
            )

        val response: OpenAIResponse = openAIClient.sendMessage(request.mbti, request.content)
        val content: String? = response?.choices?.get(0)?.message?.content ?: ""

        val aiChat =
            chatRepository.save(
                Chat(
                    userId = request.userId,
                    mbti = request.mbti,
                    chatRoomId = request.chatRoomId,
                    content = content,
                ),
            )

        return ChatResponse(
            aiChat.id,
            request.userId,
            request.mbti,
            request.chatRoomId,
            content,
            aiChat.createdAt.toString(),
        )
    }
}
