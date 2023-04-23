package com.talk.memberService.friend

import com.talk.memberService.oauth2.subject
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
class FriendController(
        private val friendCreationService: FriendCreationService
) {
    /**
     * 친구 생성 API
     */
    @PostMapping(value = ["/friends"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal, @RequestBody payload: FriendCreationPayload): FriendView =
            friendCreationService.create(principal.subject(), payload).toView()

}