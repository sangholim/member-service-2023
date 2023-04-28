package com.talk.memberService.profile

import java.util.*

data class ProfileChatView(
        val id: UUID,
        val roomName: String,
        val image: String
)


fun ProfileChatDto.toView() = ProfileChatView(
        id = this.id,
        roomName = this.roomName!!,
        image = this.image
)