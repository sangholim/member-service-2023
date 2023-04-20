package com.talk.memberService.profile.api

import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.friend.FriendRepository
import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileCriteria
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
        private val profileRepository: ProfileRepository,
        private val friendRepository: FriendRepository
) : BehaviorSpec({

    fun request(criteria: ProfileCriteria? = null) = webClient
            .mutateWith(
                    mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .get().uri { builder ->
                builder.path("/member-service/profiles")
                criteria?.let {
                    builder.queryParam("containFriend", criteria.containFriend)
                }
                builder.build()
            }

    Given("프로필 조회") {
        When("프로필이 없는 경우") {
            val email = "test@test"
            val name = "a"
            beforeEach {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                profile {
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
            and("질의 조건이 없는 경우") {
                val email = "test@test"
                val name = "a"
                beforeEach {
                    profileRepository.deleteAll()
                    friendRepository.deleteAll()
                    profile {
                        this.userId = Oauth2Constants.SUBJECT
                        this.email = email
                        this.name = name
                    }.run { profileRepository.save(this) }
                }
                Then("status 200") {
                    val exchanged = request().exchange()
                    exchanged.expectStatus().isOk
                    exchanged.expectBody<ProfileView>().returnResult().responseBody.shouldNotBeNull().should {
                        it.email shouldBe email
                        it.name shouldBe name
                    }
                }
            }
            and("질의 조건이 있는 경우") {
                val email = "test@test"
                val name = "a"
                beforeEach {
                    profileRepository.deleteAll()
                    val profile = profile {
                        this.userId = Oauth2Constants.SUBJECT
                        this.email = email
                        this.name = name
                    }.run { profileRepository.save(this) }
                    friend {
                        this.subjectProfileId = profile.id!!
                        this.objectProfileId = "object_profile_id"
                        this.name = "테스트"
                    }.run { friendRepository.save(this) }
                }
                Then("status 200") {
                    val criteria = ProfileCriteria(true)
                    val exchanged = request(criteria).exchange()
                    exchanged.expectStatus().isOk
                    exchanged.expectBody<ProfileView>().returnResult().responseBody.shouldNotBeNull().should {
                        it.email shouldBe email
                        it.name shouldBe name
                        it.friends.shouldNotBeNull().size shouldBe 1
                    }
                }
            }
        }
    }
})