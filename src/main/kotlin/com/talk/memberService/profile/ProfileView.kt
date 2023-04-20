package com.talk.memberService.profile

import com.talk.memberService.friend.Friend
import com.talk.memberService.friend.FriendView
import com.talk.memberService.friend.toView

data class ProfileView(
        /**
         * 프로필 id
         */
        val id: String,
        /**
         * 이메일
         */
        val email: String,
        /**
         * 이름
         */
        val name: String,
        /**
         * 친구 리스트
         */
        val friends: List<FriendView>? = emptyList()
) {

    private constructor(builder: Builder) :
            this(builder.id, builder.email, builder.name, builder.friends)

    companion object {
        inline fun profileView(block: Builder.() -> Unit) = Builder().apply(block).build()
        inline fun profileViewBuilder(block: Builder.() -> Unit) = Builder().apply(block)

    }

    class Builder {
        var id: String = ""
        var email: String = ""
        var name: String = ""
        var friends: List<FriendView>? = emptyList()
        fun build(): ProfileView {
            return ProfileView(this)
        }

        fun profile(profile: Profile): Builder {
            this.id = profile.id!!
            this.email = profile.email
            this.name = profile.name
            return this
        }

        fun friends(friends: List<Friend>?): Builder {
            this.friends = friends?.map(Friend::toView)
            return this
        }
    }
}