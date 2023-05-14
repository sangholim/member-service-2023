package com.talk.memberService.chatParticipant.repository

import com.talk.memberService.chat.Chat
import com.talk.memberService.chat.Chat.Companion.chat
import com.talk.memberService.chat.ChatConstant
import com.talk.memberService.chat.ChatRepository
import com.talk.memberService.chatParticipant.ChatParticipant
import com.talk.memberService.chatParticipant.ChatParticipant.Companion.chatParticipant
import com.talk.memberService.chatParticipant.ChatParticipantRepository
import com.talk.memberService.friend.Friend
import com.talk.memberService.friend.Friend.Companion.friend
import com.talk.memberService.friend.FriendRepository
import com.talk.memberService.friend.FriendType
import com.talk.memberService.profile.Profile
import com.talk.memberService.profile.Profile.Companion.profile
import com.talk.memberService.profile.ProfileRepository
import com.talk.memberService.r2dbc.RepositoryTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import oauth2.Oauth2Constants
import java.util.UUID

@RepositoryTest
class ChatParticipantWithProfileAndFriendTests(
        private val profileRepository: ProfileRepository,
        private val friendRepository: FriendRepository,
        private val chatRepository: ChatRepository,
        private val chatParticipantRepository: ChatParticipantRepository
) : BehaviorSpec({

    suspend fun createProfile(userId: String? = null, name: String): Profile {
        return profile {
            this.userId = userId ?: UUID.randomUUID().toString()
            this.name = name
            this.email = "${this.userId}-email"
        }.run { profileRepository.save(this) }
    }


    suspend fun createFriend(subjectProfileId: UUID, objectProfileId: UUID, name: String): Friend {
        return friend {
            this.subjectProfileSequenceId = profileRepository.findById(subjectProfileId)?.sequenceId!!
            this.objectProfileSequenceId = profileRepository.findById(objectProfileId)?.sequenceId!!
            this.name = name
            this.type = FriendType.GENERAL
        }.run { friendRepository.save(this) }
    }

    suspend fun createChat(): Chat {
        return chat {
            this.image = ChatConstant.DEFAULT_CHAT_IMAGE
            this.combinedParticipantProfileSequenceId = "1"
            this.participantCount = 1
        }.run { chatRepository.save(this) }
    }

    suspend fun createParticipant(chatId: UUID, profileId: UUID, roomName: String): ChatParticipant {
        return chatParticipant {
            this.chatId = chatId
            this.profileSequenceId = profileRepository.findById(profileId)?.sequenceId!!
            this.roomName = roomName
        }.run { chatParticipantRepository.save(this) }
    }

    Given("채팅 참가자 - 프로필 - 친구 join 질의") {
        When("채팅 참가자 - 프로필 - 친구 join 질의") {
            val userId = Oauth2Constants.SUBJECT
            beforeEach {
                profileRepository.deleteAll()
                friendRepository.deleteAll()
                chatParticipantRepository.deleteAll()
                val me = createProfile(userId, "나")
                val profile1 = createProfile(null, "김")
                val profile2 = createProfile(null, "이")
                createFriend(me.id!!, profile1.id!!, "나-김 친구")
                createFriend(profile1.id!!, profile2.id!!, "김-이 친구")
                val chat = createChat()
                createParticipant(chat.id!!, me.id!!, "나챗방")
                createParticipant(chat.id!!, profile1.id!!, "김챗방")
                createParticipant(chat.id!!, profile2.id!!, "이챗방")

            }

            Then("DB 에 채팅 참가자 데이터가 존재한다") {
                val chatId = chatRepository.findAll().first().id!!
                val profileSequenceId = profileRepository.findByUserId(userId)?.sequenceId!!
                val dtos = chatParticipantRepository.findAllWithProfileAndFriendBy(chatId, profileSequenceId).toList()
                println(dtos)
                dtos.size shouldBe 3
            }
        }
    }
})