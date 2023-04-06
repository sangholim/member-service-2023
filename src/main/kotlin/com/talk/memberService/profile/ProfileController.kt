package com.talk.memberService.profile

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/member-service"])
class ProfileController {

    @GetMapping(value = ["/profiles"])
    suspend fun test(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal) {
    }
}

