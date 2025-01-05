package org.pandas.bambooclub.domain.chatroom.controller

import org.pandas.bambooclub.domain.chatroom.service.ChatRoomService
import org.pandas.bambooclub.global.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {
    @PostMapping("/chatrooms")
    fun createChatRoom(
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<*> {
        return ResponseEntity(chatRoomService.createChatRoom(principal), HttpStatus.OK)
    }

    @GetMapping("/chatrooms")
    fun getChatRooms(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): ResponseEntity<*> {
        return ResponseEntity(chatRoomService.getChatRooms(principal, page, size), HttpStatus.OK)
    }
}
