package com.wish.bunny.wish.domain

/***
작성자: 김은솔
처리 내용: 위시리스트 data class
 */
data class WishItem (
    val wishNo :String,
    val content : String,
    val deadlineDt: String,
    val regDt: String,
    val achieveYn: String,
    val category: String,
    val useYn : String,
    val notifyYn: String,
    val writerNo: String,
    val tagNo: String,
    val tagContents: String,
    val contents: String,
    val writerYn: String,
    val finishedDt : String,
    val deleteTag : String,
    val emoji: String
)