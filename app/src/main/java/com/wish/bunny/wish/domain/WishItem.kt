package com.wish.bunny.wish.domain

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
    val contents: String
)