package com.talk.memberService.friend

import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.r2dbc.RepositoryTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe

@RepositoryTest
class FriendRepositoryTests(
        private val repository: FriendRepository
)  : BehaviorSpec({

    Given("친구 생성 하기") {
        When("친구 생성 성공한 경우") {
            beforeEach {
                repository.deleteAll()
            }
            val friend = friend {
                this.subjectProfileSequenceId = 1
                this.objectProfileSequenceId = 2
                this.name = "a"
                this.type = FriendType.GENERAL
            }
            Then("DB 에 친구 데이터가 존재한다") {
                repository.save(friend).should {
                    it.id shouldNotBe null
                    it.subjectProfileSequenceId shouldNotBe null
                    it.objectProfileSequenceId shouldNotBe null
                    it.name shouldNotBe null
                    it.createdAt shouldNotBe null
                    it.updatedAt shouldNotBe null
                }
            }
        }
    }
})