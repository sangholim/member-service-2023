package com.talk.memberService.friend

import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.profile.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendService(
        private val repository: FriendRepository
) {

    fun getAllBySubjectProfileSequenceId(subjectProfileSequenceId: Long) = repository.findAllBySubjectProfileSequenceId(subjectProfileSequenceId)

    fun findAllByIdsAndSubjectProfileSequenceId(ids: List<UUID>, subjectProfileSequenceId: Long) =
            repository.findAllByIdInAndSubjectProfileSequenceId(ids, subjectProfileSequenceId)

    suspend fun getBySubjectProfileSequenceIdAndObjectProfileSequenceId(subjectProfileSequenceId: Long, objectProfileSequenceId: Long) =
            repository.findBySubjectProfileSequenceIdAndObjectProfileSequenceId(subjectProfileSequenceId, objectProfileSequenceId)

    suspend fun createBy(myProfile: Profile, friendProfile: Profile, type: FriendType): Friend =
            friend {
                this.subjectProfileSequenceId = myProfile.sequenceId
                this.objectProfileSequenceId = friendProfile.sequenceId
                this.name = friendProfile.name
                this.type = type
            }.run { repository.save(this) }
}