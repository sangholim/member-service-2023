package com.talk.memberService.chat

import com.talk.memberService.chat.Chat.Companion.chat
import com.talk.memberService.r2dbc.RepositoryTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@RepositoryTest
class ChatRepositoryTests(
        private val repository: ChatRepository
) : BehaviorSpec({

    Given("채팅 생성 하기") {
        When("채팅 생성 성공한 경우") {
            beforeEach {
                repository.deleteAll()
            }
            Then("DB 에 채팅 데이터가 존재한다") {
                val chat = chat {
                    this.image = "test"
                    this.combinedParticipantProfileSequenceId = "test"
                    this.participantCount = 1
                }
                repository.save(chat).should {
                    it.id shouldNotBe null
                    it.image shouldBe "test"
                    it.combinedParticipantProfileSequenceId shouldNotBe null
                    it.participantCount shouldBe 1
                    it.createdAt shouldNotBe null
                    it.updatedAt shouldNotBe null
                }
            }
        }
    }
})