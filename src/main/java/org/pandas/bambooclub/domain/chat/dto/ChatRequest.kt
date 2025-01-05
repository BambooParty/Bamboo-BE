package org.pandas.bambooclub.domain.chat.dto

data class ChatRequest(
    val chatRoomId: String,
    val content: String,
)
