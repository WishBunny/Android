package com.wish.bunny.friend.domain

/**
작성자:  이혜연
처리 내용: 친구 리스트 응답 객체
 */
data class FriendListResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val matchedToNowDt: String,
        val imgUrl: String,
        val memberId: String,
        val nickname: String,
        val friendId: String
    )
}