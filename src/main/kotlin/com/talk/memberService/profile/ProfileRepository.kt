package com.talk.memberService.profile

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ProfileRepository : CoroutineCrudRepository<Profile, UUID> {
    suspend fun countByUserId(userId: String): Int

    suspend fun countByEmail(email: String): Int

    suspend fun findByUserId(userId: String): Profile?

    suspend fun findByEmail(email: String): Profile?

    fun findAllByIdIn(ids: List<String>): Flow<Profile>

    @Query(value =
    "SELECT p.id AS id, p.sequence_id as sequence_id, cp.room_name AS room_name, c.image AS image, c.participant_count AS participant_count, STRING_TO_ARRAY(c.combined_participant_profile_sequence_id, ',') AS profile_sequence_ids " +
            "FROM profile p " +
            "INNER JOIN chat_participant cp " +
            "ON p.sequence_id = cp.profile_sequence_id " +
            "INNER JOIN chat c " +
            "ON cp.chat_id = c.id " +
            "WHERE p.user_id = $1")
    fun findAllWithChatsByUserId(userId: String): Flow<ProfileChatDto>

    @Query(value =
    "SELECT p.id AS id, p.sequence_id as sequence_id, cp.room_name AS room_name, c.image AS image, c.participant_count AS participant_count, STRING_TO_ARRAY(c.combined_participant_profile_sequence_id, ',') AS profile_sequence_ids " +
            "FROM profile p " +
            "INNER JOIN chat_participant cp " +
            "ON p.sequence_id = cp.profile_sequence_id " +
            "INNER JOIN chat c " +
            "ON cp.chat_id = c.id " +
            "WHERE p.user_id = $1 " +
            "AND c.id = $2 ")
    suspend fun findWithChatsByUserIdAndChatId(userId: String, chatId: UUID): ProfileChatDto?

    fun findAllBySequenceIdIn(sequenceIds: List<Long>): Flow<Profile>
}