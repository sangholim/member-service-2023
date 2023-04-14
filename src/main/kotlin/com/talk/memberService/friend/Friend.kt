package com.talk.memberService.friend

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
class Friend(
        @Id
        var id: String? = null,
        var profileId: String,
        var name: String,
        @CreatedDate
        var createdAt: Instant? = null,
        @CreatedBy
        var createdBy: String? = null,
        @LastModifiedDate
        var updatedAt: Instant? = null,
        @LastModifiedBy
        var updatedBy: String? = null
) {

    private constructor(builder: Builder) :
            this(builder.id, builder.profileId!!, builder.name!!, builder.createdAt, builder.createdBy, builder.updatedAt, builder.updatedBy)

    companion object {
        inline fun friend(block: Builder.() -> Unit) = Builder().apply(block).build()

        fun from(friend: Friend): Builder = Builder().apply {
            this.id = friend.id
            this.profileId = friend.profileId
            this.name = friend.name
            this.createdAt = friend.createdAt
            this.createdBy = friend.createdBy
            this.updatedAt = friend.updatedAt
            this.updatedBy = friend.updatedBy
        }
    }

    class Builder {
        var id: String? = null
        var profileId: String? = null
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