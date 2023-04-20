package com.talk.memberService.friend

import org.springframework.stereotype.Service

@Service
class FriendService(
        private val repository: FriendRepository
) {

    fun getBySubjectProfileId(subjectProfileId: String) = repository.findAllBySubjectProfileId(subjectProfileId)
}