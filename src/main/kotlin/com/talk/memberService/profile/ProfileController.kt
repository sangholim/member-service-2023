package com.talk.memberService.profile

import com.talk.memberService.oauth2.subject
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/member-service"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ProfileController(
        private val profileService: ProfileService,
        private val profileQueryService: ProfileQueryService
) {
    @GetMapping(value = ["/profiles"])
    suspend fun getView(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal, criteria: ProfileCriteria): ProfileView =
            profileQueryService.getByCriteria(principal.subject(), criteria)


    @PostMapping(value = ["/profiles"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal, @RequestBody payload: ProfileCreationPayload) {
        profileService.create(principal.subject(), payload)
    }
}

