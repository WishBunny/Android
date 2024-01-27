package com.wish.bunny.emoji

import com.wish.bunny.emoji.domain.EmojiList
import retrofit2.Call
import retrofit2.http.GET

interface EmojiService {
    @GET("/emojis")
    fun getEmojis(): Call<EmojiList>
}