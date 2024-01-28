package com.wish.bunny.mypage.domain

data class WishCountResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val countAchievedDo: Int,
        val countAchievedEat: Int,
        val countAchievedGet: Int,
        val countDo: Int,
        val countEat: Int,
        val countGet: Int
    )
}