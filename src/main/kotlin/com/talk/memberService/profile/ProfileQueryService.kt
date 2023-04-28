package com.talk.memberService.profile

import com.talk.memberService.friend.FriendService
import com.talk.memberService.profile.ProfileView.Companion.profileViewBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

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
            builder.friends(friendService.getAllBySubjectProfileId(profile.id.toString()).toList())
        }
        return builder.build()
    }

    /**
     * 프로필의 채팅방을 조회한다
     * 채팅방 참가자가 이름을 설정하지 않은 경우
     * 자신의 이름을 제외한 참가자 아이디를 문자열로 생성한다 (구분자 ,)
     */
    suspend fun getAllWithChats(userId: String?): Flow<ProfileChatDto> =
            profileService.getAllWithChatsByUserId(userId).map { chat ->
                if (chat.roomName != null) return@map chat
                val roomName = chat.profileSequenceIds.minus(chat.sequenceId)
                        .createRoomNameBySequenceIds()
                chat.copy(roomName = roomName)
            }

    private suspend fun List<Long>.createRoomNameBySequenceIds(): String =
            profileService.getAllBySequenceIds(this).toList()
                    .joinToString(separator = ",") { it.name }

}