package com.talk.memberService.chatParticipant.repository

import com.talk.memberService.chatParticipant.ChatParticipant.Companion.chatParticipant
import com.talk.memberService.chatParticipant.ChatParticipantRepository
import com.talk.memberService.r2dbc.RepositoryTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.UUID

@RepositoryTest
class ChatParticipantRepositoryTests(
        private val repository: ChatParticipantRepository
) : BehaviorSpec({

    Given("채팅 참가자 생성 하기") {
        When("채팅 참가자 생성 성공한 경우") {
            val uuid = UUID.randomUUID()
            beforeEach {
                repository.deleteAll()
            }
            Then("DB 에 채팅 참가자 데이터가 존재한다") {
                val chat = chatParticipant {
                    this.chatId = uuid
                    this.profileSequenceId = 1
                    this.roomName = "room"
                }
                repository.save(chat).should {
                    it.id shouldNotBe null
                    it.chatId shouldBe uuid
                    it.profileSequenceId shouldBe 1
                    it.roomName shouldBe "room"
                    it.createdAt shouldNotBe null
                    it.updatedAt shouldNotBe null
                }
            }
        }
    }
})