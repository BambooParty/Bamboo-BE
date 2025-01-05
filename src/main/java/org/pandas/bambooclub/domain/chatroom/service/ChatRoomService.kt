package org.pandas.bambooclub.domain.chatroom.service

import org.pandas.bambooclub.domain.chatroom.dto.ChatRoomResponse
import org.pandas.bambooclub.domain.chatroom.model.ChatRoom
import org.pandas.bambooclub.domain.chatroom.repository.ChatRoomRepository
import org.pandas.bambooclub.global.security.UserPrincipal
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun createChatRoom(principal: UserPrincipal): ChatRoomResponse {
        val now = LocalDateTime.now()
        val year = now.year
        val month = now.month.value

        var chatRoom = chatRoomRepository.findByUserIdAndYearAndMonth(principal.userId, year, month)

        if (chatRoom == null) {
            chatRoom =
                chatRoomRepository.save(
                    ChatRoom(
                        userId = principal.userId,
                        year = now.year,
                        month = now.month.value,
                    ),
                )
        }

        return ChatRoomResponse(
            userId = chatRoom!!.userId,
            id = chatRoom.id,
        )
    }

    fun getChatRoom(principal: UserPrincipal): ChatRoom {
        val now = LocalDateTime.now()

        return chatRoomRepository.findByUserIdAndYearAndMonth(
            principal.userId,
            now.year,
            now.monthValue,
        ) ?: chatRoomRepository.save(
            ChatRoom(
                userId = principal.userId,
                year = now.year,
                month = now.monthValue,
            ),
        )
    }

    fun getChatRooms(
        principal: UserPrincipal,
        page: Int,
        size: Int,
    ): List<ChatRoomResponse> {
        val pageable = PageRequest.of(page, size)
        val chatRooms = chatRoomRepository.findAllByUserId(principal.userId, pageable)
        return chatRooms.stream()
            .map { chatRoom ->
                ChatRoomResponse(
                    id = chatRoom.id,
                    userId = chatRoom.userId,
                )
            }
            .toList()
    }
}
