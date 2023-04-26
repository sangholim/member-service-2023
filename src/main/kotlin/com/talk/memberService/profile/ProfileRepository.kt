package com.talk.memberService.profile

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ProfileRepository : CoroutineCrudRepository<Profile, UUID> {
    suspend fun countByUserId(userId: String): Int

    suspend fun countByEmail(email: String): Int

    suspend fun findByUserId(userId: String): Profile?

    suspend fun findByEmail(email: String): Profile?

    fun findAllByIdIn(ids: List<UUID>): Flow<Profile>
}