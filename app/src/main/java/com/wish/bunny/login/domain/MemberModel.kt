package com.wish.bunny.login.domain

data class MemberModel(
    val accessToken: String,
    val email: String,
    val imgUrl: String,
    val memberNo: String,
    val nickname: String,
    val refreshToken: String,
    val regDt: String,
    val useYn: String
)