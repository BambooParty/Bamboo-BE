package org.pandas.bambooclub.domain.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chats")
class Chat(
    @Id
    val id: String? = null,
    val userId: String,
    val mbti: String,
    val chatRoomId: String,
    var content: String?,
) {
    val createdAt: LocalDateTime = LocalDateTime.now()
}
