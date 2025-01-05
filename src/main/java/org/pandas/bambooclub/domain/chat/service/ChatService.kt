
package org.pandas.bambooclub.domain.chat.service

import org.pandas.bambooclub.domain.chat.dto.ChatRequest
import org.pandas.bambooclub.domain.chat.dto.ChatResponse
import org.pandas.bambooclub.domain.chat.dto.OpenAIResponse
import org.pandas.bambooclub.domain.chat.model.Chat
import org.pandas.bambooclub.domain.chat.model.ChatType
import org.pandas.bambooclub.domain.chat.repository.ChatRepository
import org.pandas.bambooclub.domain.chatroom.service.ChatRoomService
import org.pandas.bambooclub.global.config.OpenAIClient
import org.pandas.bambooclub.global.security.UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

private val logger = LoggerFactory.getLogger(ChatService::class.java)

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatRoomService: ChatRoomService,
    private val openAIClient: OpenAIClient,
) {
    suspend fun chat(
        principal: UserPrincipal,
        request: ChatRequest,
    ): ChatResponse {
        val chatRoom = chatRoomService.getChatRoom(principal)
        val chatRoomId = chatRoom.id!!
        saveHumanChat(principal, request, chatRoomId)
        val list = getPreviousChats(principal.userId, chatRoomId)
        val previousContent = convertPreviousContent(list)
        val response = sendMessageToOpenAI(principal, request, previousContent)
        val content: String? = response?.choices?.get(0)?.message?.content ?: ""
        val aiChat = saveAiChat(principal, request, content, chatRoomId)
        return convertChatToDto(aiChat)
    }

    private fun getPreviousChats(
        userId: String,
        chatRoomId: String,
    ): List<Chat> {
        val pageable =
            PageRequest.of(
                0,
                10,
                Sort.by(
                    Sort.Order.desc("createdAt"),
                ),
            )
        val list = chatRepository.findAllByUserIdAndChatRoomIdAndChatType(userId, chatRoomId, ChatType.HUMAN, pageable)
        return list
    }

    private fun convertPreviousContent(list: List<Chat>): String {
        val result: String = list.joinToString(" ") { it.content ?: "" }
        logger.info("content")
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
        principal: UserPrincipal,
        request: ChatRequest,
        content: String?,
        chatRoomId: String,
    ): Chat {
        val chat =
            chatRepository.save(
                Chat(
                    userId = principal.userId,
                    mbti = principal.mbti,
                    chatRoomId = chatRoomId,
                    content = content,
                    chatType = ChatType.AI,
                ),
            )
        return chat
    }

    private suspend fun saveHumanChat(
        principal: UserPrincipal,
        request: ChatRequest,
        chatRoomId: String,
    ): Chat {
        val chat =
            chatRepository.save(
                Chat(
                    userId = principal.userId,
                    mbti = principal.mbti,
                    chatRoomId = chatRoomId,
                    content = request.content,
                    chatType = ChatType.HUMAN,
                ),
            )
        return chat
    }

    private suspend fun sendMessageToOpenAI(
        principal: UserPrincipal,
        request: ChatRequest,
        previousContent: String,
    ): OpenAIResponse {
        val response: OpenAIResponse =
            openAIClient.sendMessage(
                mbti = principal.mbti,
                content = request.content,
                previousContent = previousContent,
            )
        return response
    }

    fun getChats(
        principal: UserPrincipal,
        chatRoomId: String,
        page: Int,
        size: Int,
    ): List<ChatResponse> {
        val pageable =
            PageRequest.of(
                page,
                size,
                Sort.by(
                    Sort.Order.desc("createdAt"),
                ),
            )
        val chats = chatRepository.findAllByUserIdAndChatRoomId(principal.userId, chatRoomId, pageable)
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

    fun getRecentChats(
        principal: UserPrincipal,
        page: Int,
        size: Int,
    ): List<ChatResponse> {
        val chatRoom = chatRoomService.getChatRoom(principal)
        val chatRoomId = chatRoom.id!!
        val pageable =
            PageRequest.of(
                page,
                size,
                Sort.by(
                    Sort.Order.desc("createdAt"),
                ),
            )
        val chats = chatRepository.findAllByUserIdAndChatRoomId(principal.userId, chatRoomId, pageable)
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
            .reversed()
    }
}
