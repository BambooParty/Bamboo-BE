package org.pandas.bambooclub.domain.chat.repository

import org.pandas.bambooclub.domain.chat.model.Chat
import org.pandas.bambooclub.domain.chat.model.ChatType
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String> {
    fun findAllByUserIdAndChatRoomIdAndChatType(
        userId: String,
        chatRoomId: String,
        chatType: ChatType,
    ): List<Chat>

    fun findAllByUserIdAndChatRoomIdAndChatType(
        userId: String,
        chatRoomId: String,
        chatType: ChatType,
        pageable: Pageable,
    ): List<Chat>

    fun findAllByUserIdAndChatRoomId(
        userId: String,
        chatRoomId: String,
        pageable: Pageable,
    ): List<Chat>
}
