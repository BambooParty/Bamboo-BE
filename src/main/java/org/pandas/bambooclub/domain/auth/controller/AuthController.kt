package org.pandas.bambooclub.domain.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.pandas.bambooclub.domain.user.dto.LogInRequest
import org.pandas.bambooclub.domain.user.dto.SignUpRequest
import org.pandas.bambooclub.domain.user.model.User
import org.pandas.bambooclub.domain.user.repository.UserRepository
import org.pandas.bambooclub.global.security.JwtUtil
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
                mbti = request.mbti,
            )
        userRepository.save(user)

        return ResponseEntity.ok("Signed up successfully")
    }

    @PostMapping("/log-in")
    fun login(
        @RequestBody request: LogInRequest,
        response: HttpServletResponse,
    ): ResponseEntity<String> {
        val user =
            userRepository.findByUserId(request.userId)
                ?: return ResponseEntity.badRequest().body("User not found")

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.badRequest().body("Invalid password")
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

        return ResponseEntity.ok("Login successful")
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
