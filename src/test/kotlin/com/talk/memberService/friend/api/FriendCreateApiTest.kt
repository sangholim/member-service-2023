package com.talk.memberService.friend.api

import com.talk.memberService.error.ErrorResponse
import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.friend.FriendCreationPayload
import com.talk.memberService.friend.FriendRegisterType
import com.talk.memberService.friend.FriendRepository
import com.talk.memberService.friend.FriendView
import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.collect
import oauth2.Oauth2Constants
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class FriendCreateApiTest(
        private val webClient: WebTestClient,
        private val profileRepository: ProfileRepository,
        private val friendRepository: FriendRepository
) : BehaviorSpec({

    fun request(payload: FriendCreationPayload) = webClient
            .mutateWith(
                    SecurityMockServerConfigurers.mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .post()
            .uri("/member-service/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)

    Given("친구 추가하기") {
        When("이메일로 추가하는 경우") {
            and("이메일이 존재하지 않는 경우") {
                beforeTest {
                    profileRepository.deleteAll()
                    friendRepository.deleteAll()
                    profile {
                        userId = Oauth2Constants.SUBJECT
                        email = "test@test.com"
                        name = "name"
                    }.run { profileRepository.save(this) }
                }
                Then("status: 400") {
                    val payload = FriendCreationPayload(
                            FriendRegisterType.EMAIL, null, null
                    )
                    val response = request(payload).exchange()
                    response.expectBody(ErrorResponse::class.java)
                            .returnResult().responseBody.shouldNotBeNull().should {
                                it.status shouldBe HttpStatus.BAD_REQUEST
                                it.message shouldBe "올바르지 않은 이메일입니다"
                            }

                }
            }
            and("조회시 친구의 프로필이 없는경우") {
                beforeTest {
                    profileRepository.deleteAll()
                    friendRepository.deleteAll()
                    profile {
                        userId = Oauth2Constants.SUBJECT
                        email = "test@test.com"
                        name = "name"
                    }.run { profileRepository.save(this) }
                }
                Then("status: 400") {
                    val payload = FriendCreationPayload(
                            FriendRegisterType.EMAIL, "abc@test.com", null
                    )
                    val response = request(payload).exchange()
                    response.expectBody(ErrorResponse::class.java)
                            .returnResult().responseBody.shouldNotBeNull().should {
                                it.status shouldBe HttpStatus.NOT_FOUND
                                it.message shouldBe "존재하지 않는 회원입니다"
                            }

                }
            }
            and("등록된 친구인 경우") {
                beforeTest {
                    profileRepository.deleteAll()
                    friendRepository.deleteAll()
                    listOf(
                            profile {
                                userId = Oauth2Constants.SUBJECT
                                email = "test@test.com"
                                name = "name"
                            },
                            profile {
                                userId = "friend-user-id"
                                email = "abc@test.com"
                                name = "abc"
                            }
                    ).run { profileRepository.saveAll(this).collect() }

                    val myProfile = profileRepository.findByEmail("test@test.com")!!
                    val friendProfile = profileRepository.findByEmail("abc@test.com")!!
                    friend {
                        subjectProfileId = myProfile.id!!
                        objectProfileId = friendProfile.id!!
                        name = friendProfile.name
                    }.run { friendRepository.save(this) }

                }
                Then("status: 400") {
                    val payload = FriendCreationPayload(
                            FriendRegisterType.EMAIL, "abc@test.com", null
                    )
                    val response = request(payload).exchange()
                    response.expectBody(ErrorResponse::class.java)
                            .returnResult().responseBody.shouldNotBeNull().should {
                                it.status shouldBe HttpStatus.BAD_REQUEST
                                it.message shouldBe "등록된 친구입니다"
                            }
                }
            }
            and("친구 추가 성공한 경우") {
                beforeTest {
                    profileRepository.deleteAll()
                    friendRepository.deleteAll()
                    listOf(
                            profile {
                                userId = Oauth2Constants.SUBJECT
                                email = "test@test.com"
                                name = "name"
                            },
                            profile {
                                userId = "friend-user-id"
                                email = "abc@test.com"
                                name = "abc"
                            }
                    ).run { profileRepository.saveAll(this).collect() }
                }
                Then("status: 201") {
                    val payload = FriendCreationPayload(
                            FriendRegisterType.EMAIL, "abc@test.com", null
                    )
                    val response = request(payload).exchange()
                    response.expectStatus().isCreated
                    response.expectBody(FriendView::class.java)
                            .returnResult().responseBody.shouldNotBeNull().should {
                                it.id shouldNotBe null
                                it.name shouldNotBe null
                            }
                }
            }
        }
    }
})