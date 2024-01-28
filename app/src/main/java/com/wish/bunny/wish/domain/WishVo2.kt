package com.wish.bunny.wish.domain

/**
작성자: 황수연
처리 내용: 위시 데이터 수정 VO
 */
data class WishVo2(
    val wishNo: String? = null,
    val content: String,
    val deadlineDt: String,
    val category: String,
    val notifyYn: String,
    val achieveYn: String,
    val tagNo: String,
    val deleteTag: String
)
