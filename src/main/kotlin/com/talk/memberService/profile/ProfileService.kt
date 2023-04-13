package com.talk.memberService.profile

import com.talk.memberService.profile.Profile.Companion.profile
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
        return repository.findByUserId(userId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "회원 번호가 존재하지 않습니다")
    }
}