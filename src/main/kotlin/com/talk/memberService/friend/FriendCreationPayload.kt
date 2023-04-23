package com.talk.memberService.friend

/**
 * 친구 생성 필드 데이터
 */
data class FriendCreationPayload(
        /**
         * 친구 생성 구분
         */
        val type: FriendRegisterType,

        /**
         * 친구 이메일
         */
        val email: String?,

        /**
         * 친구 이름
         */
        val name: String?
)
