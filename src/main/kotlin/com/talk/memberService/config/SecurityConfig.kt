package com.talk.memberService.config

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
        private val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties
) {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
            http {
                authorizeExchange {
                    authorize(anyExchange, authenticated)
                }
                oauth2ResourceServer {
                    opaqueToken {
                        introspector = introspector()
                    }
                }
            }

    @Bean
    fun introspector(): ReactiveOpaqueTokenIntrospector {
        val opaque = oAuth2ResourceServerProperties.opaquetoken
        return NimbusReactiveOpaqueTokenIntrospector(opaque.introspectionUri, opaque.clientId, opaque.clientSecret)
    }

}