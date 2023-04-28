package com.talk.memberService.profile.api

import com.talk.memberService.chat.Chat
import com.talk.memberService.chat.ChatRepository
import com.talk.memberService.chatParticipant.ChatParticipant
import com.talk.memberService.chatParticipant.ChatParticipantRepository
import com.talk.memberService.friend.FriendRepository
import com.talk.memberService.profile.Profile
import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileChatView
import com.talk.memberService.profile.ProfileRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect
import oauth2.Oauth2Constants
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class ProfileChatViewApiTests(
        private val webClient: WebTestClient,
        private val profileRepository: ProfileRepository,
        private val friendRepository: FriendRepository,
        private val chatRepository: ChatRepository,
        private val chatParticipantRepository: ChatParticipantRepository
) : BehaviorSpec({

    fun request() = webClient
            .mutateWith(
                    SecurityMockServerConfigurers.mockOpaqueToken().attributes {
                        it["sub"] = Oauth2Constants.SUBJECT
                    }.authorities(Oauth2Constants.ROLES)
            )
            .get().uri { builder ->
                builder.path("/member-service/profile/chats")
                builder.build()
            }

    suspend fun createChats(profiles: List<Profile>, roomName: String? = null) {
        val profileSequenceIds = profiles.map { it.sequenceId }
        val chat = Chat.chat {
            this.image = "a"
            this.participantCount = profiles.size
            this.combinedParticipantProfileSequenceId = profileSequenceIds.joinToString(",")
        }.run { chatRepository.save(this) }
        profileSequenceIds.map { profileSequenceId ->
            ChatParticipant.chatParticipant {
                this.chatId = chat.id!!
                this.roomName = roomName
                this.profileSequenceId = profileSequenceId
            }
        }.run { chatParticipantRepository.saveAll(this) }.collect()
    }

    Given("프로필 기준 채팅창 리스트 조회") {
        When("채팅방 참가자가 방 이름을 설정한 경우") {
            beforeTest {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatRepository.deleteAll()
                chatParticipantRepository.deleteAll()
                val profile1 = profile {
                    this.userId = Oauth2Constants.SUBJECT
                    this.sequenceId = 1
                    this.email = "user@test"
                    this.name = "user"
                }.run { profileRepository.save(this) }

                val profile2 = profile {
                    this.userId = "b"
                    this.sequenceId = 2
                    this.email = "b@test"
                    this.name = "b"
                }.run { profileRepository.save(this) }

                val profile3 = profile {
                    this.userId = "c"
                    this.sequenceId = 3
                    this.email = "c@test"
                    this.name = "c"
                }.run { profileRepository.save(this) }

                createChats(listOf(profile1, profile2), "a")
                createChats(listOf(profile1, profile3), "a")
                createChats(listOf(profile2, profile3), "a")
            }
            Then("status: 200 Ok") {
                val exchanged = request().exchange()
                exchanged.expectStatus().isOk
                exchanged.expectBodyList(ProfileChatView::class.java).returnResult().responseBody.shouldNotBeNull().should { chats ->
                    chats.filter { it.roomName == "a" }.size shouldBe 2
                }
            }
        }

        When("채팅방 참가자들이 친구가 없고, 방 이름 설정 안한 경우") {
            beforeTest {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatRepository.deleteAll()
                chatParticipantRepository.deleteAll()
                val profile1 = profile {
                    this.userId = Oauth2Constants.SUBJECT
                    this.sequenceId = 1
                    this.email = "user@test"
                    this.name = "user"
                }.run { profileRepository.save(this) }

                val profile2 = profile {
                    this.userId = "b"
                    this.sequenceId = 2
                    this.email = "b@test"
                    this.name = "b"
                }.run { profileRepository.save(this) }

                val profile3 = profile {
                    this.userId = "c"
                    this.sequenceId = 3
                    this.email = "c@test"
                    this.name = "c"
                }.run { profileRepository.save(this) }

                val profile4 = profile {
                    this.userId = "d"
                    this.sequenceId = 4
                    this.email = "d@test"
                    this.name = "d"
                }.run { profileRepository.save(this) }

                createChats(listOf(profile1, profile2, profile3))
                createChats(listOf(profile1, profile3, profile4))
                createChats(listOf(profile2, profile3))
            }
            Then("status: 200 Ok, 방 이름은 ',' 으로 구분된다") {
                val exchanged = request().exchange()
                exchanged.expectStatus().isOk
                exchanged.expectBodyList(ProfileChatView::class.java).returnResult().responseBody.shouldNotBeNull()
                        .should { chats ->
                            chats.filter { it.roomName.contains(",")}.size shouldBe 2
                        }
            }
        }
    }
})