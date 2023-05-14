package com.talk.memberService.profile

import com.talk.memberService.chat.ChatConstant
import com.talk.memberService.chatParticipant.ChatParticipantProjectionDto
import com.talk.memberService.chatParticipant.ChatParticipantView
import java.util.*

data class ProfileChatDetailView(
        /**
         * 사용자 채팅 ID
         */
        val chatId: UUID,
        /**
         * 사용자 채팅 참가자 ID
         */
        val chatParticipantId: UUID,
        /**
         * 채팅방 이름
         */
        val roomName: String,
        /**
         * 채팅방 이미지
         */
        val roomImage: String,
        /**
         * 자신을 제외한 참가자들
         */
        val participants: List<ChatParticipantView>,
        /**
         * 참가자 전체 수
         */
        val participantCount: Int
) {
    companion object {
        fun from(me: ChatParticipantProjectionDto, participants: List<ChatParticipantView>): ProfileChatDetailView {
            return ProfileChatDetailView(
                    chatId = me.chatId,
                    chatParticipantId = me.id,
                    roomName = me.roomName ?: ChatConstant.DEFAULT_CHAT_ROOM_NAME,
                    roomImage = me.roomImage,
                    participants = participants,
                    participantCount = participants.size
            )
        }
    }
}

