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
            val profile = friend {
                this.profileId = "profile"
                this.name = "a"
            }
            Then("DB 에 친구 데이터가 존재한다") {
                repository.save(profile).should {
                    it.id shouldNotBe null
                    it.profileId shouldNotBe null
                    it.name shouldNotBe null
                    it.createdAt shouldNotBe null
                    it.updatedAt shouldNotBe null
                }
            }
        }
    }
})