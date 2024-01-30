package com.wish.bunny.wish.domain

/***
작성자: 김은솔
처리 내용: 위시리스트 data class
 */
data class WishItem (

    val wishNo :String, //위시리스트 번호
    val content : String, //내용
    val deadlineDt: String,  //마감일
    val regDt: String,//등록일자
    val achieveYn: String,//완료여부
    val category: String,//카테고리
    val useYn : String,//사용여부
    val notifyYn: String,//알림여부
    val writerNo: String,//글쓴이 번호
    val tagNo: String,//태그 번호
    val tagContents: String,//태그 내용
    val contents: String, //내용
    val writerYn: String,//작성자 여부
    val finishedDt : String, //완료일자
    val deleteTag : String, //삭제 태그
    val emoji: String //이모지
)