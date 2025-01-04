package org.pandas.bambooclub.domain.chat.service

import org.pandas.bambooclub.domain.chat.dto.ChatRequest
import org.pandas.bambooclub.domain.chat.dto.ChatResponse
import org.pandas.bambooclub.domain.chat.dto.OpenAIResponse
import org.pandas.bambooclub.domain.chat.model.Chat
import org.pandas.bambooclub.domain.chat.model.ChatType
import org.pandas.bambooclub.domain.chat.repository.ChatRepository
import org.pandas.bambooclub.global.config.OpenAIClient
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

private val logger = LoggerFactory.getLogger(ChatService::class.java)

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val openAIClient: OpenAIClient,
) {
    suspend fun chat(request: ChatRequest): ChatResponse {
        saveHumanChat(request)
        val list = chatRepository.findAllByUserIdAndChatRoomIdAndChatType(request.userId, request.chatRoomId, ChatType.HUMAN)
        val previousContent = "" // convertPreviousContent(list)
        val response = sendMessageToOpenAI(request, previousContent)
        val content: String? = response?.choices?.get(0)?.message?.content ?: ""
        val aiChat = saveAiChat(request, content)
        return convertChatToDto(aiChat)
    }

    private fun convertPreviousContent(list: List<Chat>): String {
        val result: String = list.joinToString("\n") { it.content ?: "" }
        logger.info(result)
        return result
    }

    private suspend fun convertChatToDto(chat: Chat): ChatResponse {
        return ChatResponse(
            id = chat.id,
            userId = chat.userId,
            mbti = chat.mbti,
            chatRoomId = chat.chatRoomId,
            content = chat.content,
            chatType = chat.chatType,
            createdAt = chat.createdAt.toString(),
        )
    }

    private suspend fun saveAiChat(
        request: ChatRequest,
        content: String?,
    ): Chat {
        val chat =
            chatRepository.save(
                Chat(
                    userId = request.userId,
                    mbti = request.mbti,
                    chatRoomId = request.chatRoomId,
                    content = content,
                    chatType = ChatType.AI,
                ),
            )
        return chat
    }

    private suspend fun saveHumanChat(request: ChatRequest): Chat {
        val chat =
            chatRepository.save(
                Chat(
                    userId = request.userId,
                    mbti = request.mbti,
                    chatRoomId = request.chatRoomId,
                    content = request.content,
                    chatType = ChatType.HUMAN,
                ),
            )
        return chat
    }

    private suspend fun sendMessageToOpenAI(
        request: ChatRequest,
        previousContent: String,
    ): OpenAIResponse {
        val response: OpenAIResponse =
            openAIClient.sendMessage(
                mbti = request.mbti,
                content = request.content,
                previousContent = previousContent,
            )
        return response
    }

    fun getChats(
        userId: String,
        chatRoomId: String,
        page: Int,
        size: Int,
    ): List<ChatResponse> {
        val pageable = PageRequest.of(page, size)
        val chats = chatRepository.findAllByUserIdAndChatRoomId(userId, chatRoomId, pageable)
        return chats.stream()
            .map { chat ->
                ChatResponse(
                    id = chat.id,
                    userId = chat.userId,
                    mbti = chat.mbti,
                    chatRoomId = chat.chatRoomId,
                    content = chat.content,
                    chatType = chat.chatType,
                    createdAt = chat.createdAt.toString(),
                )
            }
            .toList()
    }
}
