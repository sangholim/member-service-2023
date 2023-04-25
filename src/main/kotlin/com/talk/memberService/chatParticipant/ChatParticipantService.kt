package com.talk.memberService.chatParticipant

import com.talk.memberService.chat.ChatConstant
import com.talk.memberService.chatParticipant.ChatParticipant.Companion.chatParticipant
import kotlinx.coroutines.flow.collect
import org.springframework.stereotype.Service

@Service
class ChatParticipantService(
        private val repository: ChatParticipantRepository
) {
    /**
     * 채팅 참가자 리스트 생성
     */
    suspend fun createAllBy(chatId: String, profileIds: List<String>) {
        profileIds.map { profileId ->
            chatParticipant {
                this.chatId = chatId
                this.profileId = profileId
                this.roomName = ChatConstant.DEFAULT_CHAT_ROOM_NAME
            }
        }.run {
            repository.saveAll(this)
        }.collect()
    }
}