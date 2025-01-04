package org.pandas.bambooclub.domain.chatroom.repository

import org.pandas.bambooclub.domain.chatroom.model.ChatRoom
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : MongoRepository<ChatRoom, String>
