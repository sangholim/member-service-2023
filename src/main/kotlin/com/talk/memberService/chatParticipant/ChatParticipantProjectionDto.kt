package com.talk.memberService.chatParticipant

import java.util.UUID

data class ChatParticipantProjectionDto(
        /**
         * 채팅 참가자 ID
         */
        val id: UUID,
        /**
         * chat ID
         */
        val chatId: UUID,
        /**
         * 방 이름
         */
        val roomName: String?,
        /**
         * 방 이미지
         */
        val roomImage: String,
        /**
         * 프로필 이름
         */
        val profileName: String,

        /**
         * 친구 이름
         */
        val friendName: String?
)
