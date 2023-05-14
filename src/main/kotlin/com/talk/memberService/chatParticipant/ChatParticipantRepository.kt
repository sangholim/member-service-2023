package com.talk.memberService.chatParticipant

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ChatParticipantRepository : CoroutineCrudRepository<ChatParticipant, UUID> {
    fun findAllByChatId(chatId: UUID): Flow<ChatParticipant>

    /**
     * 참가자 - 채팅 - 프로필 - 친구
     * join 하여 데이터 조회
     */
    @Query(value = "SELECT cp.id AS id, cp.chat_id AS chat_id, cp.room_name AS room_name, c.image AS room_image, p.name AS profile_name, f.name AS friend_name " +
            "FROM chat_participant cp " +
            "INNER JOIN chat c " +
            "ON c.id = $1 " +
            "INNER JOIN profile p " +
            "ON cp.profile_sequence_id = p.sequence_id " +
            "LEFT JOIN friend f " +
            "ON cp.profile_sequence_id = f.object_profile_sequence_id " +
            "AND f.subject_profile_sequence_id = $2 "
    )
    fun findAllWithChatAndProfileAndFriendBy(chatId: UUID, subjectProfileSequenceId: Long): Flow<ChatParticipantProjectionDto>
}