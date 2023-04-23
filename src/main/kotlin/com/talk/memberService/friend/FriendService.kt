package com.talk.memberService.friend

import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.profile.Profile
import org.springframework.stereotype.Service

@Service
class FriendService(
        private val repository: FriendRepository
) {

    fun getBySubjectProfileId(subjectProfileId: String) = repository.findAllBySubjectProfileId(subjectProfileId)

    suspend fun getBySubjectProfileIdAndObjectProfileId(subjectProfileId: String, objectProfileId: String) =
            repository.findBySubjectProfileIdAndObjectProfileId(subjectProfileId, objectProfileId)

    suspend fun createBy(myProfile: Profile, friendProfile: Profile): Friend = friend {
        this.subjectProfileId = myProfile.id!!
        this.objectProfileId = friendProfile.id!!
        this.name = friendProfile.name
    }.run { repository.save(this) }
}