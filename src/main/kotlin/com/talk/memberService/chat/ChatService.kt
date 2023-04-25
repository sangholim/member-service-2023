package com.talk.memberService.chat

import com.talk.memberService.chat.Chat.Companion.chat
import org.springframework.stereotype.Service

@Service
class ChatService(
        private val repository: ChatRepository
) {
    /**
     * 채팅 도메인 생성
     */
    suspend fun create(): Chat = chat {
        this.image = ChatConstant.DEFAULT_CHAT_IMAGE
    }.run { repository.save(this) }
}