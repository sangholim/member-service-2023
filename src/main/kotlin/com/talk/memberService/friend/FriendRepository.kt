package com.talk.memberService.friend

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface FriendRepository: CoroutineCrudRepository<Friend, UUID> {
    fun findAllBySubjectProfileSequenceId(subjectProfileSequenceId: Long): Flow<Friend>

    fun findAllByIdInAndSubjectProfileSequenceId(ids:List<UUID>, subjectProfileSequenceId: Long): Flow<Friend>

    suspend fun findBySubjectProfileSequenceIdAndObjectProfileSequenceId(subjectProfileSequenceId: Long, objectProfileSequenceId: Long): Friend?
}