package org.pandas.bambooclub.domain.chat.dto

data class ChatResponse(
    val id: String?,
    val userId: String,
    val mbti: String,
    val chatRoomId: String,
    val content: String?,
    val createdAt: String,
)
