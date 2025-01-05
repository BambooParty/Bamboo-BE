package org.pandas.bambooclub.domain.user.dto

data class SignUpRequest(
    val userId: String,
    val password: String,
    val nickname: String,
    val mbti: String,
)
