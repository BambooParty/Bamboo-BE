package org.pandas.bambooclub.global.exception

class GlobalException(private val errorCode: ErrorCode) : RuntimeException() {
    fun getErrorCode(): ErrorCode {
        return errorCode
    }
}
