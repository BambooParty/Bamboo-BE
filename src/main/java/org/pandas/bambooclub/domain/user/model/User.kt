package org.pandas.bambooclub.domain.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    val userId: String,
    val password: String,
    val mbti: String,
)
