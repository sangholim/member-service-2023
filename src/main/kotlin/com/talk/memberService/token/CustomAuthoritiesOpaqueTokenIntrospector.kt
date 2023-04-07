package com.talk.memberService.token

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector
import reactor.core.publisher.Mono

class CustomAuthoritiesOpaqueTokenIntrospector(
        opaqueTokenProperties: OAuth2ResourceServerProperties.Opaquetoken
) : ReactiveOpaqueTokenIntrospector {
    private val delegate: ReactiveOpaqueTokenIntrospector = NimbusReactiveOpaqueTokenIntrospector(opaqueTokenProperties.introspectionUri, opaqueTokenProperties.clientId, opaqueTokenProperties.clientSecret)

    override fun introspect(token: String): Mono<OAuth2AuthenticatedPrincipal> {
        return delegate.introspect(token)
                .map { principal ->
                    DefaultOAuth2AuthenticatedPrincipal(
                            principal.name, principal.attributes, extractAuthorities(principal))
                }
    }

    private fun extractAuthorities(principal: OAuth2AuthenticatedPrincipal): Collection<GrantedAuthority> {
        val scopes: List<String> = principal.getAttribute(OAuth2TokenIntrospectionClaimNames.SCOPE) ?: emptyList()
        val roles: List<String> = principal.getAttribute("roles") ?: emptyList()
        return scopes.plus(roles)
                .map { SimpleGrantedAuthority(it) }
    }
}