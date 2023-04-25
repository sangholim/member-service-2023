package com.talk.memberService.chat

import jakarta.validation.constraints.NotEmpty
import java.util.UUID

/**
 * 채팅방 생성 필드 데이터
 */
data class ChatCreationPayload(
        /**
         * 친구 ID 리스트
         */
        @field:NotEmpty
        val friendIds: List<UUID> = emptyList()
)

