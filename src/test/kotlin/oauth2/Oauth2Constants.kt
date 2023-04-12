package oauth2

import org.springframework.security.core.authority.SimpleGrantedAuthority

object Oauth2Constants {
    val SUBJECT = "user-id"
    val ROLES = listOf("ROLE_USER").map { SimpleGrantedAuthority(it) }
}