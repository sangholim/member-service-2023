package com.talk.memberService.chatParticipant

import java.util.UUID

data class ChatParticipantView(
        val id: UUID,
        val name: String,
)

fun ChatParticipantProjectionDto.toView(): ChatParticipantView = ChatParticipantView(
        id = this.id,
        name = this.friendName ?: this.profileName
)