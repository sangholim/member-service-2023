package com.talk.memberService.friend

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface FriendRepository: CoroutineCrudRepository<Friend, UUID> {
    fun findAllBySubjectProfileId(subjectProfileId: String): Flow<Friend>

    fun findAllByIdInAndSubjectProfileId(ids:List<UUID>, subjectProfileId: String): Flow<Friend>

    suspend fun findBySubjectProfileIdAndObjectProfileId(subjectProfileId: String, objectProfileId: String): Friend?
}