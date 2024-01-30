package com.wish.bunny.wish.domain

/**
작성자: 황수연
처리 내용: 위시 데이터 수정 VO
 */
data class WishVo2(
    val wishNo: String? = null, // 위시리스트 번호
    val content: String, // 내용
    val deadlineDt: String, // 마감기한
    val category: String, // 카테고리
    val notifyYn: String, // 알림 여부
    val achieveYn: String, // 위시 달성 여부
    val tagNo: String, // 해시태그 번호
    val deleteTag: String // 삭제할 해시태그 번호
)
