package com.talk.memberService.chat

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface ChatRepository : CoroutineCrudRepository<Chat, UUID> {
    /**
     * 참가자 프로필 순차 id 리스트 기반 조회
     */
    suspend fun findByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId: String): Chat?
}