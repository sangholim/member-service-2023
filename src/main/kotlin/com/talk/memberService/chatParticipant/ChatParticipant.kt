package com.talk.memberService.chatParticipant

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

/**
 * 채팅 참가자
 *
 * 참가자들은 개별 맞춤으로 채팅방 설정 가능하다
 */
@Table
class ChatParticipant(
        /**
         * 채팅 참가자 ID
         */
        @Id
        var id: UUID? = null,
        /**
         * 챗 ID
         */
        var chatId: String,
        /**
         * 프로필 숫자열 ID
         */
        var profileSequenceId: Long,
        /**
         * 채팅방 이름
         */
        var roomName: String?,
        /**
         * 생성 날짜
         */
        @CreatedDate
        var createdAt: Instant? = null,
        /**
         * 생성한 user id
         */
        @CreatedBy
        var createdBy: String? = null,
        /**
         * 수정 날짜
         */
        @LastModifiedDate
        var updatedAt: Instant? = null,
        /**
         * 수정 user id
         */
        @LastModifiedBy
        var updatedBy: String? = null
) {
    private constructor(builder: Builder) :
            this(builder.id, builder.chatId!!, builder.profileSequenceId!!, builder.roomName, builder.createdAt, builder.createdBy, builder.updatedAt, builder.updatedBy)

    companion object {
        inline fun chatParticipant(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var id: UUID? = null
        var chatId: String? = null
        var profileSequenceId: Long? = null
        var roomName: String? = null
        var createdAt: Instant? = null
        var createdBy: String? = null
        var updatedAt: Instant? = null
        var updatedBy: String? = null
        fun build(): ChatParticipant {
            return ChatParticipant(this)
        }
    }
}