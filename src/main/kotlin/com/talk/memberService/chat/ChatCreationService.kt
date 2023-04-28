package com.talk.memberService.chat

import com.talk.memberService.chatParticipant.ChatParticipantService
import com.talk.memberService.friend.FriendService
import com.talk.memberService.profile.ProfileService
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ChatCreationService(
        private val profileService: ProfileService,
        private val friendService: FriendService,
        private val chatService: ChatService,
        private val chatParticipantService: ChatParticipantService
) {
    /**
     * 채팅방 생성하기
     */
    suspend fun create(userId: String?, payload: ChatCreationPayload) {
        val profile = profileService.getByUserId(userId)
        val friends = friendService.findAllByIdsAndSubjectProfileSequenceId(payload.friendIds, profile.sequenceId!!).toList()
        if (payload.friendIds.count() != friends.count()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 친구 정보입니다")
        val profileSequenceIds = friends.map { it.objectProfileSequenceId }.plus(profile.sequenceId!!).sorted()
        val combinedParticipantProfileSequenceId = profileSequenceIds.joinToString(separator = ",") { "$it" }
        val participantCount = profileSequenceIds.size
        if (chatService.existByCombinedParticipantProfileSequenceId(combinedParticipantProfileSequenceId)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "채팅방이 이미 존재합니다")
        val chat = chatService.create(combinedParticipantProfileSequenceId, participantCount)
        chatParticipantService.createAllBy(chat.id!!, profileSequenceIds)
    }
}