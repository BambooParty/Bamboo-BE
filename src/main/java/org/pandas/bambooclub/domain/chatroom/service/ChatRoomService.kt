package org.pandas.bambooclub.domain.chatroom.service

import org.pandas.bambooclub.domain.chatroom.dto.ChatRoomRequest
import org.pandas.bambooclub.domain.chatroom.dto.ChatRoomResponse
import org.pandas.bambooclub.domain.chatroom.model.ChatRoom
import org.pandas.bambooclub.domain.chatroom.repository.ChatRoomRepository
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun createChatRoom(request: ChatRoomRequest): ChatRoomResponse {
        val chatRoom =
            chatRoomRepository.save(
                ChatRoom(
                    userId = request.userId,
                ),
            )

        return ChatRoomResponse(
            userId = chatRoom.userId,
            id = chatRoom.id,
        )
    }
}
