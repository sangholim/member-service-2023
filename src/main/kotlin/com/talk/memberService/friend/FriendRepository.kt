package com.talk.memberService.friend

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FriendRepository: CoroutineCrudRepository<Friend, String>