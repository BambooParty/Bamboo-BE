package org.pandas.bambooclub.domain.user.dto

data class UserResponse(
    val userId: String,
    val nickname: String?,
    val mbti: String,
)
