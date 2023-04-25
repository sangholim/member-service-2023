package com.talk.memberService.chat

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

/**
 * 인터넷을 통해, 서로간의 정보를 교환하는행위
 * chat 관련 도메인들을 관리한다
 */
@Table
class Chat(
        /**
         * chat ID
         */
        @Id
        var id: UUID? = null,
        /**
         * 채팅 이미지
         */
        var image: String,
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
            this(builder.id, builder.image!!, builder.createdAt, builder.createdBy, builder.updatedAt, builder.updatedBy)

    companion object {
        inline fun chat(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var id: UUID? = null
        var image: String? = null
        var createdAt: Instant? = null
        var createdBy: String? = null
        var updatedAt: Instant? = null
        var updatedBy: String? = null
        fun build(): Chat {
            return Chat(this)
        }
    }
}