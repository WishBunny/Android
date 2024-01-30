package com.wish.bunny.friend.domain

/**
작성자:  이혜연
처리 내용: 친구 응답 객체
 */
data class FriendResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val deletedYn: String,
        val fromMemberId: String,
        val id: String,
        val inviteCode: String,
        val matchedDt: Any,
        val matchedYn: String,
        val toMemberId: Any
    )
}