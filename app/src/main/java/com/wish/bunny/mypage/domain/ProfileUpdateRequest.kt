package com.wish.bunny.mypage.domain

data class ProfileUpdateRequest(
    val email: String,
    var imgUrl: String,
    val memberId: String,
    val nickname: String
)