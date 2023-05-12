package com.talk.memberService.profile

import java.util.UUID

data class ProfileChatDetailDto (
    val id: UUID,
    val chatId: UUID,
    val chatParticipantId: UUID,
    val sequenceId: Long,
    val roomName: String?,
    val image: String,
    val participantCount: Int,
    val profileSequenceIds: List<Long>
)