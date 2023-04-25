package com.talk.memberService.chat

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface ChatRepository: CoroutineCrudRepository<Chat, UUID>