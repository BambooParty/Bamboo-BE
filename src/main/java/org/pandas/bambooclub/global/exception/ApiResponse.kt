package org.pandas.bambooclub.global.exception

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val status: Int? = null,
    val message: String? = null,
    val data: T? = null,
)
