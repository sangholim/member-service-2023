package com.talk.memberService.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ResponseStatusException

class ErrorResponse private constructor(
        val status: HttpStatus,
        val message: String? = null
) {
    companion object {

        fun from(e: ResponseStatusException) = ErrorResponse(
                status = HttpStatus.valueOf(e.statusCode.value()),
                message = e.reason
        )

        fun from(e: WebExchangeBindException) = ErrorResponse(
                status = HttpStatus.BAD_REQUEST,
                message = "올바르지 않은 형식입니다"
        )
    }
}
