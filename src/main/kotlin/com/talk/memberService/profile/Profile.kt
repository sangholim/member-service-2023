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
        var email: String,
        var name: String? = null,
        @CreatedDate
        var createdAt: Instant? = null,
        @CreatedBy
        var createdBy: String? = null,
        @LastModifiedDate
        var updatedAt: Instant? = null,
        @LastModifiedBy
        var updatedBy: String? = null
)