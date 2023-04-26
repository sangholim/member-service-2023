package com.talk.memberService.profile.api

import com.talk.memberService.profile.ProfileCreationPayload
import com.talk.memberService.profile.ProfileRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import oauth2.Oauth2Constants
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOpaqueToken
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest
@AutoConfigureWebTestClient
class ProfileCreationApiTests(
        private val webClient: WebTestClient,
        private val profileRepository: ProfileRepository
) : BehaviorSpec({

    fun request(payload: ProfileCreationPayload) = webClient
            .mutateWith(
                    mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .post().uri("/member-service/profiles").bodyValue(payload)

    Given("프로필 생성") {
        When("성공한 경우") {
            beforeEach {
                profileRepository.deleteAll()
            }
            val payload = ProfileCreationPayload(
                    "test@test",
                    "kkk"
            )
            Then("status 200") {
                request(payload).exchange().expectStatus().isOk
            }
            Then("프로필 데이터가 존재한다") {
                request(payload).exchange()
                profileRepository.findByUserId(Oauth2Constants.SUBJECT).shouldNotBeNull().should {
                    it.id shouldNotBe null
                    it.sequenceId shouldNotBe null
                    it.userId shouldNotBe null
                    it.email shouldNotBe null
                    it.name shouldNotBe null
                    it.createdAt shouldNotBe null
                    it.createdBy shouldNotBe null
                    it.updatedAt shouldNotBe null
                    it.updatedBy shouldNotBe null
                }
            }
        }
    }
})