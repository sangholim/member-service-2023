package com.talk.memberService.profile

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
class Profile(
        @Id
        var id: String? = null,
        var userId: String,
        var email: String,
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
            this(builder.id, builder.userId!!, builder.email, builder.name!!, builder.createdAt, builder.createdBy, builder.updatedAt, builder.updatedBy)

    companion object {
        inline fun profile(block: Builder.() -> Unit) = Builder().apply(block).build()

        fun from(profile: Profile): Builder = Builder().apply {
            this.id = profile.id
            this.email = profile.email
            this.name = profile.name
            this.createdAt = profile.createdAt
            this.createdBy = profile.createdBy
            this.updatedAt = profile.updatedAt
            this.updatedBy = profile.updatedBy
        }
    }

    class Builder {
        var id: String? = null
        var userId: String? = null
        var email: String = ""
        var name: String? = ""
        var createdAt: Instant? = null
        var createdBy: String? = null
        var updatedAt: Instant? = null
        var updatedBy: String? = null
        fun build(): Profile {
            return Profile(this)
        }
    }
}