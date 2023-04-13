package com.talk.memberService.profile

/**
 * 프로필 응답 데이터
 */
data class ProfileView(
        /**
         * 프로필 id
         */
        val id: String,
        /**
         * 이메일
         */
        val email: String,
        /**
         * 이름
         */
        val name: String
)

fun Profile.toView() = ProfileView(
        id = id!!,
        email = email,
        name = name
)