package com.talk.memberService.chat

import com.talk.memberService.oauth2.subject
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/member-service"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ChatController(
        private val chatCreationService: ChatCreationService
) {
    /**
     * 채팅방 생성하기
     */
    @PostMapping(value = ["/chats"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal, @Valid @RequestBody payload: ChatCreationPayload) {
        chatCreationService.create(principal.subject(), payload)
    }
}