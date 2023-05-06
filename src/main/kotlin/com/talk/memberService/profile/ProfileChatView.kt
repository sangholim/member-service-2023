package com.talk.memberService.profile

import com.talk.memberService.chat.ChatConstant
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

fun ProfileChatDto.toDetailView() = ProfileChatView(
        id = this.id,
        chatId = this.chatId,
        roomName = this.roomName ?: ChatConstant.DEFAULT_CHAT_ROOM_NAME,
        image = this.image,
        participantCount = this.participantCount
)