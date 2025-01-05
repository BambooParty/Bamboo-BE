package org.pandas.bambooclub.domain.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.pandas.bambooclub.domain.user.dto.LogInRequest
import org.pandas.bambooclub.domain.user.dto.SignUpRequest
import org.pandas.bambooclub.domain.user.dto.UserResponse
import org.pandas.bambooclub.domain.user.model.User
import org.pandas.bambooclub.domain.user.repository.UserRepository
import org.pandas.bambooclub.global.exception.ErrorCode
import org.pandas.bambooclub.global.exception.GlobalException
import org.pandas.bambooclub.global.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<String> {
        if (userRepository.findByUserId(request.userId) != null) {
            return ResponseEntity.badRequest().body("User ID already exists")
        }

        val user =
            User(
                userId = request.userId,
                password = passwordEncoder.encode(request.password),
                nickname = request.nickname,
                mbti = request.mbti,
            )
        userRepository.save(user)

        return ResponseEntity.ok("Signed up successfully")
    }

    @PostMapping("/log-in")
    fun login(
        @RequestBody request: LogInRequest,
        response: HttpServletResponse,
    ): ResponseEntity<UserResponse> {
        val user =
            userRepository.findByUserId(request.userId)
                ?: throw GlobalException(ErrorCode.RESOURCE_NOT_FOUND)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw GlobalException(ErrorCode.INVALID_REQUEST_PARAMETER)
        }

        val token = jwtUtil.generateToken(user)

        val cookie =
            Cookie("jwt", token).apply {
                isHttpOnly = true
                path = "/"
                maxAge = 24 * 60 * 60 // 24 hours
                // secure = true // activate when using https
            }
        response.addCookie(cookie)
        val userResponse =
            UserResponse(
                userId = user.userId,
                nickname = user.nickname,
                mbti = user.mbti,
            )

        return ResponseEntity(userResponse, HttpStatus.OK)
    }

    @PostMapping("/log-out")
    fun logout(response: HttpServletResponse): ResponseEntity<String> {
        val cookie =
            Cookie("jwt", null).apply {
                isHttpOnly = true
                path = "/"
                maxAge = 0
            }
        response.addCookie(cookie)

        return ResponseEntity.ok("Logged out successfully")
    }
}
