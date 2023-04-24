package com.talk.memberService.chatParticipant

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChatParticipantRepository : CoroutineCrudRepository<ChatParticipant, String>