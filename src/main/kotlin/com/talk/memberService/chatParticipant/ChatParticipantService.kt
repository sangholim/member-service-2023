package com.talk.memberService.chatParticipant

import com.talk.memberService.chatParticipant.ChatParticipant.Companion.chatParticipant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatParticipantService(
        private val repository: ChatParticipantRepository
) {
    /**
     * 채팅 참가자 리스트 생성
     */
    suspend fun createAllBy(chatId: UUID, profileSequenceIds: List<Long>) {
        profileSequenceIds.map { profileSequenceId ->
            chatParticipant {
                this.chatId = chatId
                this.profileSequenceId = profileSequenceId
            }
        }.run {
            repository.saveAll(this)
        }.collect()
    }
}