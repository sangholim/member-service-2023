package com.talk.memberService.profile.api

import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileRepository
import com.talk.memberService.profile.ProfileView
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import oauth2.Oauth2Constants
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOpaqueToken
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody


@SpringBootTest
@AutoConfigureWebTestClient
class ProfileViewApiTests(
        private val webClient: WebTestClient,
        private val profileRepository: ProfileRepository
) : BehaviorSpec({

    fun request() = webClient
            .mutateWith(
                    mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .get().uri("/member-service/profiles")

    Given("프로필 조회") {
        When("프로필이 없는 경우") {
            val email = "test@test"
            val name = "a"
            beforeEach {
                profileRepository.deleteAll()
                profile{
                    this.userId = "test"
                    this.email = email
                    this.name = name
                }.run { profileRepository.save(this) }
            }
            Then("status 404") {
                request().exchange().expectStatus().isNotFound
            }
        }
        When("성공한 경우") {
            val email = "test@test"
            val name = "a"
            beforeEach {
                profileRepository.deleteAll()
                profile{
                    this.userId = Oauth2Constants.SUBJECT
                    this.email = email
                    this.name = name
                }.run { profileRepository.save(this) }
            }
            Then("status 200") {
                request().exchange().expectStatus().isOk
            }
            Then("프로필 데이터가 존재한다") {
                request().exchange().expectBody<ProfileView>().returnResult().responseBody.shouldNotBeNull().should {
                    it.email shouldBe email
                    it.name shouldBe name
                }
            }
        }
    }
})