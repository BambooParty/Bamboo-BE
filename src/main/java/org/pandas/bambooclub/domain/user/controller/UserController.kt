package org.pandas.bambooclub.domain.user.controller

import org.pandas.bambooclub.domain.user.service.UserService
import org.pandas.bambooclub.global.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun getProfile(
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<*> {
        return ResponseEntity(userService.getProfile(principal), HttpStatus.OK)
    }
}
