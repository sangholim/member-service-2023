package com.talk.memberService.profile

import com.talk.memberService.chat.ChatCreationPayload
import com.talk.memberService.chat.ChatCreationService
import com.talk.memberService.oauth2.subject
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping(value = ["/member-service/profile"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ProfileChatController(
        private val chatCreationService: ChatCreationService,
        private val profileQueryService: ProfileQueryService
) {
    /**
     * 채팅방 생성하기
     */
    @PostMapping(value = ["/chats"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal, @Valid @RequestBody payload: ChatCreationPayload) {
        chatCreationService.create(principal.subject(), payload)
    }

    /**
     * 프로필 기준 채팅방 조회하기
     */
    @GetMapping(value = ["/chats"])
    suspend fun getViews(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal): Flow<ProfileChatView> {
        return profileQueryService.getAllWithChats(principal.subject()).map(ProfileChatDto::toView)
    }

    @GetMapping(value = ["/chats/{chatId}"])
    suspend fun getView(@PathVariable chatId: UUID, @AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal): ProfileChatDetailView {
        return profileQueryService.getWithChat(principal.subject(), chatId)
    }
}