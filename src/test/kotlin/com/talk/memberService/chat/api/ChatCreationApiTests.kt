package com.talk.memberService.chat.api

import com.talk.memberService.chat.ChatCreationPayload
import com.talk.memberService.chat.ChatRepository
import com.talk.memberService.chatParticipant.ChatParticipantRepository
import com.talk.memberService.error.ErrorResponse
import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.friend.FriendRepository
import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import oauth2.Oauth2Constants
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOpaqueToken
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID


@SpringBootTest
@AutoConfigureWebTestClient
class ChatCreationApiTests(
        private val webClient: WebTestClient,
        private val profileRepository: ProfileRepository,
        private val friendRepository: FriendRepository,
        private val chatRepository: ChatRepository,
        private val chatParticipantRepository: ChatParticipantRepository
) : BehaviorSpec({

    fun request(payload: ChatCreationPayload) = webClient
            .mutateWith(
                    mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .post().uri("/member-service/chats").bodyValue(payload)

    Given("채팅방 생성") {
        When("친구 id 가 존재하지 않는 경우") {
            beforeTest {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatRepository.deleteAll()
                chatParticipantRepository.deleteAll()
            }
            Then("status 400") {
                val payload = ChatCreationPayload()
                val response = request(payload).exchange()
                response.expectBody(ErrorResponse::class.java)
                        .returnResult().responseBody.shouldNotBeNull().should {
                            it.status shouldBe HttpStatus.BAD_REQUEST
                            it.message shouldBe "올바르지 않은 형식입니다"
                        }
            }
        }
        When("친구 id 리스트가 실제 데이터랑 맞지 않는 경우") {
            beforeTest {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatRepository.deleteAll()
                chatParticipantRepository.deleteAll()
                val myProfile = profile {
                    userId = Oauth2Constants.SUBJECT
                    email = "test@test.com"
                    name = "name"
                }.run { profileRepository.save(this) }
                val otherProfile = profile {
                    userId = "other"
                    email = "other@test.com"
                    name = "other"
                }.run { profileRepository.save(this) }
                friend {
                    this.subjectProfileId = myProfile.id.toString()
                    this.objectProfileId = otherProfile.id.toString()
                    this.name = "친구"
                }.run { friendRepository.save(this) }

            }
            Then("status 400") {
                val payload = ChatCreationPayload(listOf(UUID.randomUUID()))
                val response = request(payload).exchange()
                response.expectBody(ErrorResponse::class.java)
                        .returnResult().responseBody.shouldNotBeNull().should {
                            it.status shouldBe HttpStatus.BAD_REQUEST
                            it.message shouldBe "올바르지 않은 친구 정보입니다"
                        }
            }
        }
        When("채팅방 생성 성공한 경우") {
            beforeTest {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatRepository.deleteAll()
                chatParticipantRepository.deleteAll()
                val myProfile = profile {
                    userId = Oauth2Constants.SUBJECT
                    email = "test@test.com"
                    name = "name"
                }.run { profileRepository.save(this) }
                val otherProfile = profile {
                    userId = "other"
                    email = "other@test.com"
                    name = "other"
                }.run { profileRepository.save(this) }
                friend {
                    this.subjectProfileId = myProfile.id.toString()
                    this.objectProfileId = otherProfile.id.toString()
                    this.name = "친구"
                }.run { friendRepository.save(this) }

            }
            Then("status 201") {
                val profile = profileRepository.findByUserId(Oauth2Constants.SUBJECT)
                val friends = friendRepository.findAllBySubjectProfileId(profile!!.id.toString()).map { it.id!! }.toList()
                val payload = ChatCreationPayload(friends)
                val response = request(payload).exchange()
                response.expectStatus().isCreated
                val chat = chatRepository.findAll().first()
                chat.id shouldNotBe null
                val chatParticipants = chatParticipantRepository.findAllByChatId(chat.id.toString()).toList()
                chatParticipants.size shouldBe 2
            }
        }
    }
})