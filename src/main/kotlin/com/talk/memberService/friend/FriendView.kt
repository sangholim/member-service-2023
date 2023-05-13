package com.talk.memberService.friend

import java.util.UUID

/**
 * 친구 응답 데이터
 */
data class FriendView(
        val id: UUID,
        val name: String,
        val type: FriendType
)

fun Friend.toView() = FriendView(
        id = this.id!!,
        name = this.name,
        type = this.type
)