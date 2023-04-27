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
     * 프로필 ID 기준 조회
     */
    fun getAllByIds(ids: List<String>): Flow<Profile> =
            repository.findAllByIdIn(ids)

}