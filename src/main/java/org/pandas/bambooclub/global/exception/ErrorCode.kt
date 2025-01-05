package org.pandas.bambooclub.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val httpStatus: Int, val message: String) {
    // 400 Bad Request
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST.value(), "Invalid request parameter"),

    // 401 Unauthorized
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED.value(), "Authentication failed"),

    // 403 Forbidden
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "Access denied"),

    // 404 Not Found
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Requested resource not found"),

    // 500 Internal Server Error
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error occurred"),
}
