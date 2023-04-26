package com.talk.memberService.friend

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

/**
 * 현재 프로필 과 다른 프로필이 연결되는 도메인
 * 친구를 맺는 주체 - 친구를 맺는 대상 으로 동일한 프로필 ID 를 구분
 */
@Table
class Friend(
        /**
         * 친구 ID
         */
        @Id
        var id: UUID? = null,
        /**
         * 친구 주체 프로필 ID
         */
        var subjectProfileId: String,
        /**
         * 친구 대상 프로필 ID
         */
        var objectProfileId: String,
        /**
         * 친구 이름
         */
        var name: String,
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
            this(builder.id, builder.subjectProfileId!!, builder.objectProfileId!!, builder.name!!, builder.createdAt, builder.createdBy, builder.updatedAt, builder.updatedBy)

    companion object {
        inline fun friend(block: Builder.() -> Unit) = Builder().apply(block).build()

        fun from(friend: Friend): Builder = Builder().apply {
            this.id = friend.id
            this.subjectProfileId = friend.subjectProfileId
            this.objectProfileId = friend.objectProfileId
            this.name = friend.name
            this.createdAt = friend.createdAt
            this.createdBy = friend.createdBy
            this.updatedAt = friend.updatedAt
            this.updatedBy = friend.updatedBy
        }
    }

    class Builder {
        var id: UUID? = null
        var subjectProfileId: String? = null
        var objectProfileId: String? = null
        var name: String? = ""
        var createdAt: Instant? = null
        var createdBy: String? = null
        var updatedAt: Instant? = null
        var updatedBy: String? = null
        fun build(): Friend {
            return Friend(this)
        }
    }
}