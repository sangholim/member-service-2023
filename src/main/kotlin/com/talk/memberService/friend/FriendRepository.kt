package com.talk.memberService.friend

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FriendRepository: CoroutineCrudRepository<Friend, String> {
    fun findAllBySubjectProfileId(subjectProfileId: String): Flow<Friend>

    suspend fun findBySubjectProfileIdAndObjectProfileId(subjectProfileId: String, objectProfileId: String): Friend?
}