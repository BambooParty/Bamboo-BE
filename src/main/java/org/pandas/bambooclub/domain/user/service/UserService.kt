package org.pandas.bambooclub.domain.user.service

import org.pandas.bambooclub.domain.user.dto.UserResponse
import org.pandas.bambooclub.domain.user.repository.UserRepository
import org.pandas.bambooclub.global.security.UserPrincipal
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getProfile(principal: UserPrincipal): UserResponse {
        val user = userRepository.findByUserId(principal.userId)!!
        return UserResponse(
            userId = user.userId,
            nickname = user.nickname,
            mbti = user.mbti,
        )
    }
}
