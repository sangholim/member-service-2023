package com.talk.memberService.profile

import com.talk.memberService.friend.Friend
import com.talk.memberService.friend.FriendService
import com.talk.memberService.friend.FriendType
import com.talk.memberService.profile.ProfileView.Companion.profileViewBuilder
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class ProfileQueryService(
        private val profileService: ProfileService,
        private val friendService: FriendService
) {
    /**
     * 질의 필드 기준으로 프로필 및 관련 도메인의 데이터를 가져온다
     */
    suspend fun getByCriteria(userId: String?, criteria: ProfileCriteria?): ProfileView {
        if (userId == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 번호가 존재하지 않습니다")
        val profile = profileService.getByUserId(userId)
        val builder = profileViewBuilder {
            profile(profile)
        }
        if (criteria == null) return builder.build()
        if (criteria.containFriend) {
            builder.friends(friendService.getAllBySubjectProfileSequenceIdAndType(profile.sequenceId!!, FriendType.GENERAL).toList())
        }
        return builder.build()
    }

    /**
     * 프로필의 채팅방을 조회한다
     * 채팅방 참가자가 이름을 설정하지 않은 경우
     * 자신의 이름을 제외한 참가자 아이디를 문자열로 생성한다 (구분자 ,)
     * 참가자중 자신의 친구는 친구의 이름으로 문자열을 생성한다
     */
    suspend fun getAllWithChats(userId: String?): Flow<ProfileChatDto> {
        val profile = profileService.getByUserId(userId)
        val friends = friendService.getAllBySubjectProfileSequenceId(profile.sequenceId!!).toList()
        return profileService.getAllWithChatsByUserId(userId).map { chat ->
            if (chat.roomName != null) return@map chat
            val roomName = chat.profileSequenceIds.minus(chat.sequenceId)
                    .createRoomNameBySequenceIds(friends)
            chat.copy(roomName = roomName)
        }
    }

    /**
     * 채팅방 단일 조회
     */
    suspend fun getWithChat(userId: String?, chatId: UUID): ProfileChatDetailDto =
            profileService.getWithChatByUserIdAndChatId(userId, chatId)

    private suspend fun List<Long>.createRoomNameBySequenceIds(friends: List<Friend>): String {
        val participantFriends = friends.filter { this.contains(it.objectProfileSequenceId) }
        val remainParticipants = this.minus(participantFriends.map { it.objectProfileSequenceId }.toSet())
        val friendNames = participantFriends.map { it.name }
        if (remainParticipants.isEmpty()) {
            return friendNames.joinToString(separator = ",")
        }
        return profileService.getAllBySequenceIds(remainParticipants).map { it.name }.run {
            merge(this, friendNames.asFlow())
        }.toList().joinToString(separator = ",")
    }
}