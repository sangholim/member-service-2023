package com.talk.memberService.oauth2

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal

fun OAuth2AuthenticatedPrincipal.subject(): String? = this.getAttribute("sub")