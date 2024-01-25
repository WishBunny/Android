package com.wish.bunny.mypage.domain

data class ProfileGetResponse(
    val `data`: ProfileData,
    val message: String,
    val success: Boolean
)