package com.talk.memberService.profile

import com.talk.memberService.chat.ChatConstant
import java.util.*

data class ProfileChatDetailView(
        val id: UUID,
        val chatId: UUID,
        val chatParticipantId: UUID,
        val roomName: String,
        val image: String,
        val participantCount: Int
)

fun ProfileChatDetailDto.toView() = ProfileChatDetailView(
        id = this.id,
        chatId = this.chatId,
        chatParticipantId = this.chatParticipantId,
        roomName = this.roomName ?: ChatConstant.DEFAULT_CHAT_ROOM_NAME,
        image = this.image,
        participantCount = this.participantCount
)