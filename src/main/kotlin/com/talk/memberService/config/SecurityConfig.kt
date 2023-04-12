package com.talk.memberService.config

import com.talk.memberService.token.CustomAuthoritiesOpaqueTokenIntrospector
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector

@Configuration
@Profile(value = ["!oas"])
@EnableWebFluxSecurity
class SecurityConfig(
        private val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties
) {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
            http {
                csrf { disable() }

                authorizeExchange {
                    authorize(anyExchange, hasRole("USER"))
                }
                oauth2ResourceServer {
                    opaqueToken {
                        introspector = introspector()
                    }
                }
            }

    @Bean
    fun introspector(): ReactiveOpaqueTokenIntrospector {
        return CustomAuthoritiesOpaqueTokenIntrospector(oAuth2ResourceServerProperties.opaquetoken)
    }

}