package com.wish.bunny.emoji

import com.wish.bunny.emoji.domain.EmojiList
import retrofit2.Call
import retrofit2.http.GET

/**
    작성자: 엄상은
    처리 내용: 이모지 통신
*/
interface EmojiService {
    @GET("/emojis")
    fun getEmojis(): Call<EmojiList>
}