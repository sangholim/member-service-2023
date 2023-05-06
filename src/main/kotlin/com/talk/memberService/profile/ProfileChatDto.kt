package com.talk.memberService.profile

import java.util.UUID

data class ProfileChatDto (
    val id: UUID,
    val sequenceId: Long,
    val roomName: String?,
    val image: String,
    val participantCount: Int,
    val profileSequenceIds: List<Long>
)