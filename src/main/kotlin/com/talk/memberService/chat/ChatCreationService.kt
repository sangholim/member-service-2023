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
        val friends = friendService.findAllByIdsAndSubjectProfileId(payload.friendIds, profile.id!!).toList()
        if (payload.friendIds.count() != friends.count()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 친구 정보입니다")
        val profileIds = friends.map { it.objectProfileId }.plus(profile.id!!)
        val chat = chatService.create()
        chatParticipantService.createAllBy(chat.id.toString(), profileIds)
    }
}