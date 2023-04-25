package com.talk.memberService.profile

import com.talk.memberService.friend.FriendService
import com.talk.memberService.profile.ProfileView.Companion.profileViewBuilder
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
            builder.friends(friendService.getAllBySubjectProfileId(profile.id!!).toList())
        }
        return builder.build()
    }
}