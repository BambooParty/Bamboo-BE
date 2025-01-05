package org.pandas.bambooclub.domain.chatroom.repository

import org.pandas.bambooclub.domain.chatroom.model.ChatRoom
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : MongoRepository<ChatRoom, String> {
    fun findAllByUserId(
        userId: String,
        pageable: Pageable,
    ): Page<ChatRoom>

    fun findByUserIdAndYearAndMonth(
        userId: String,
        year: Int,
        month: Int,
    ): ChatRoom?
}
