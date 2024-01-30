package com.wish.bunny.wish.domain

import com.wish.bunny.wish.domain.WishItem
import java.util.Objects

/***
작성자: 김은솔
처리 내용: 위시리스트 mapResult
 */
data class WishMapResult(
    val result: Objects, //결과
    val list : List<WishItem>, //리스트
    val size: Int, //크기
    val udResult: Int, //업데이트 여부
    var writerYn: String //글쓴이 여부
)