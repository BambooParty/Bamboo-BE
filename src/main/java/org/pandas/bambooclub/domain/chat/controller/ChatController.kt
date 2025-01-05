@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.pandas.bambooclub.domain.chat.controller

import kotlinx.coroutines.runBlocking
import org.pandas.bambooclub.domain.chat.dto.ChatResponse
import org.pandas.bambooclub.domain.chat.service.ChatService
import org.pandas.bambooclub.global.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping("/chats")
    fun chat(
        @RequestParam content: String,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ChatResponse> {
        return runBlocking {
            ResponseEntity(chatService.chat(principal, content), HttpStatus.OK)
        }
    }

    @GetMapping("/chats")
    fun getRecentChats(
        @RequestParam page: Int,
        @RequestParam size: Int,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<*> {
        return ResponseEntity(chatService.getRecentChats(principal, page, size), HttpStatus.OK)
    }
}
