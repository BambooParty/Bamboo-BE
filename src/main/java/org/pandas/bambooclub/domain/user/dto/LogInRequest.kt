package org.pandas.bambooclub.domain.user.dto

data class LogInRequest(
    val userId: String,
    val password: String,
)
