package org.pandas.bambooclub.domain.chatroom.controller

import org.pandas.bambooclub.domain.chatroom.dto.ChatRoomRequest
import org.pandas.bambooclub.domain.chatroom.service.ChatRoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {
    @PostMapping("/chatroom")
    fun createChatRoom(
        @Validated @RequestBody request: ChatRoomRequest,
    ): ResponseEntity<*> {
        return ResponseEntity(chatRoomService.createChatRoom(request), HttpStatus.OK)
    }
}
