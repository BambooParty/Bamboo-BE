package org.pandas.bambooclub.domain.chat.dto

data class ChatRequest(
    val userId: String,
    val mbti: String,
    val chatRoomId: String,
    val content: String,
)
