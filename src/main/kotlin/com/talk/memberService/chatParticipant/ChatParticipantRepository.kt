package com.talk.memberService.chatParticipant

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ChatParticipantRepository : CoroutineCrudRepository<ChatParticipant, UUID> {
    fun findAllByChatId(chatId: String): Flow<ChatParticipant>
}