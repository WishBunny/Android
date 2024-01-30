package com.wish.bunny.friend.domain

/**
작성자:  이혜연
처리 내용: 친구 삭제 응답 객체
 */
data class FriendDeleteResponse(
    val `data`: Int,
    val message: String,
    val success: Boolean
)