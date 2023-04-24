package com.talk.memberService.chat

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChatRepository: CoroutineCrudRepository<Chat, String>