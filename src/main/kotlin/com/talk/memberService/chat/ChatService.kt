package com.talk.memberService.chat

import com.talk.memberService.chat.Chat.Companion.chat
import org.springframework.stereotype.Service

@Service
class ChatService(
        private val repository: ChatRepository
) {
    /**
     * 묶여진 프로필 순차 id 리스트 기반 조회
     */
    suspend fun getByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId: String): Chat? =
            repository.findByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId)

    /**
     * 묶여진 프로필 순차 id 기준 존재 여부 체크
     */
    suspend fun existByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId: String): Boolean =
            getByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId) != null

    /**
     * 채팅 도메인 생성
     */
    suspend fun create(combinedParticipantProfileSequenceId: String, participantCount: Int): Chat = chat {
        this.image = ChatConstant.DEFAULT_CHAT_IMAGE
        this.combinedParticipantProfileSequenceId = combinedParticipantProfileSequenceId
        this.participantCount = participantCount
    }.run { repository.save(this) }
}