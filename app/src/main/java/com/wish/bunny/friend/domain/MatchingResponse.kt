package com.wish.bunny.friend.domain

/**
작성자:  이혜연
처리 내용: 초대 코드 발급 응답 객체
 */
data class MatchingResponse(
    val `data`: Int,
    val message: String,
    val success: Boolean
)