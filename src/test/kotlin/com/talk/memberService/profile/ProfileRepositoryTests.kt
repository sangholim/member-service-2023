package com.talk.memberService.profile

import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.r2dbc.RepositoryTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe

@RepositoryTest
class ProfileRepositoryTests(
        private val repository: ProfileRepository
) : BehaviorSpec({

    Given("프로필 생성 하기") {
        When("프로필 생성 성공한 경우") {
            val profile = profile {
                this.email = "test@test"
                this.name = "a"
            }
            Then("id, createdAt, updatedAt 이 존재한다") {
                repository.save(profile).should {
                    it.id shouldNotBe null
                    it.createdAt shouldNotBe null
                    it.updatedAt shouldNotBe null
                }
            }
        }
    }
})