package com.talk.memberService.profile

import java.util.*

data class ProfileChatView(
        val id: UUID,
        val chatId: UUID,
        val roomName: String,
        val image: String,
        val participantCount: Int
)

fun ProfileChatDto.toView() = ProfileChatView(
        id = this.id,
        chatId = this.chatId,
        roomName = this.roomName!!,
        image = this.image,
        participantCount = this.participantCount
)
