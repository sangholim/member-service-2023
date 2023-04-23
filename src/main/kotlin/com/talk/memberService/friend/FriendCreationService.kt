package com.talk.memberService.friend

import com.talk.memberService.profile.Profile
import com.talk.memberService.profile.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class FriendCreationService(
        private val profileService: ProfileService,
        private val friendService: FriendService
) {
    /**
     * 친구 생성하기
     */

    suspend fun create(userId: String?, payload: FriendCreationPayload): Friend {
        val myProfile = profileService.getByUserId(userId)
        val friendProfile = getFriendProfile(payload)
        if (friendService.getBySubjectProfileIdAndObjectProfileId(myProfile.id!!, friendProfile.id!!) != null)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 친구입니다")
        return friendService.createBy(myProfile, friendProfile)
    }

    private suspend fun getFriendProfile(payload: FriendCreationPayload): Profile =
            when (payload.type) {
                FriendRegisterType.EMAIL -> profileService.getByEmail(payload.email)
                else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 형식입니다")
            }
}