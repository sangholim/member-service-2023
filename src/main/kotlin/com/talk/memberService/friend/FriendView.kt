package com.talk.memberService.friend

/**
 * 친구 응답 데이터
 */
data class FriendView(
        val id: String,
        val name: String
)

fun Friend.toView() = FriendView(
        id = this.id!!,
        name = this.name
)