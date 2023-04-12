package com.talk.memberService.profile

/**
 * 프로필 생성 데이터
 */
data class ProfileCreationPayload(
        /**
         * 이메일
         */
        val email: String,
        /**
         * 이름
         */
        val name: String
)
