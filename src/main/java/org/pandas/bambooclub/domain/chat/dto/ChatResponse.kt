package org.pandas.bambooclub.domain.chat.dto

import org.pandas.bambooclub.domain.chat.model.ChatType

data class ChatResponse(
    val id: String?,
    val userId: String,
    val mbti: String,
    val chatRoomId: String,
    val content: String?,
    val chatType: ChatType?,
    val createdAt: String,
)
