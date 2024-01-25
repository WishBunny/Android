package com.wish.bunny.wish.domain

/**
작성자: 황수연
처리 내용: 위시 데이터 등록 VO
 */
data class WishVo(
    val content : String,
    val deadlineDt: String,
    val category: String,
    val notifyYn: String,
    val writerNo: String,
    val achieveYn: String,
    val tagNo: String
)
