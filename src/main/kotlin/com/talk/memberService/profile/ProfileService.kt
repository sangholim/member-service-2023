package com.talk.memberService.profile

import com.talk.memberService.profile.Profile.Companion.profile
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProfileService(
        private val repository: ProfileRepository
) {
    /**
     * 프로필 생성
     */
    suspend fun create(userId: String?, payload: ProfileCreationPayload) {
        if (userId == null) throw Exception("회원 번호가 존재하지 않습니다")
        if (payload.name.isEmpty()) throw Exception("이름이 존재하지 않습니다")
        if (repository.countByUserId(userId) > 0) throw Exception("프로필이 존재합니다")
        if (repository.countByEmail(payload.email) > 0) throw Exception("프로필이 존재합니다")
        profile {
            this.userId = userId
            this.email = payload.email
            this.name = payload.name
        }.run { repository.save(this) }
    }

    /**
     * 회원 ID 기준 프로필 조회
     */
    suspend fun getByUserId(userId: String?): Profile {
        if (userId == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 번호가 존재하지 않습니다")
        return repository.findByUserId(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "회원 번호가 존재하지 않습니다")
    }

    /**
     * 이메일 기준 프로필 조회
     */
    suspend fun getByEmail(email: String?): Profile {
        if (email == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 이메일입니다")
        return repository.findByEmail(email) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다")
    }

    /**
     * 유저 id를 기준, 프로필 정보와 관련된 채팅방 정보 리스트 조회
     */
    fun getAllWithChatsByUserId(userId: String?): Flow<ProfileChatDto> {
        if (userId == null) throw Exception("회원 번호가 존재하지 않습니다")
        return repository.findAllWithChatsByUserId(userId)
    }

    /**
     * 프로필 순차 ID 기준 리스트 조회
     */
    fun getAllBySequenceIds(sequenceIds: List<Long>): Flow<Profile> =
            repository.findAllBySequenceIdIn(sequenceIds)

    /**
     * 이름 기준 프로필 조회
     */
    suspend fun getByName(name: String?): Profile {
        if (name == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 이름입니다")
        return repository.findByName(name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다")
    }
}