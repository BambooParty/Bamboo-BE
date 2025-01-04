package org.pandas.bambooclub.domain.chatroom.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chatRooms")
class ChatRoom(
    @Id
    val id: String? = null,
    val userId: String,
)
