package com.wish.bunny.wish.domain

/**
작성자: 황수연
처리 내용: 위시 데이터 등록 VO
 */
data class WishVo(
    val content : String, // 내용
    val deadlineDt: String, // 마감일
    val category: String, // 카테고리(하고 싶어요, 먹고 싶어요, 갖고 싶어요)
    val notifyYn: String, // 알림 여부
    val tagNo: String, // 해시태그 번호
    val emoji: String? // 이모지
)
