@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.pandas.bambooclub.domain.chat.controller

import org.pandas.bambooclub.domain.chat.dto.ChatRequest
import org.pandas.bambooclub.domain.chat.dto.ChatResponse
import org.pandas.bambooclub.domain.chat.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping("/chat")
    suspend fun chat(
        @Validated @RequestBody request: ChatRequest,
    ): ResponseEntity<ChatResponse> {
        return ResponseEntity(chatService.chat(request), HttpStatus.OK)
    }
}
