package com.talk.memberService

import com.talk.memberService.error.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ExceptionTranslator {

    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> =
            ResponseEntity(ErrorResponse.from(e), e.statusCode)
}