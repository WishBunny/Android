package com.wish.bunny.wish


import com.wish.bunny.wish.domain.Wish
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.http.GET


interface WishService {

    // fun getWishList(): Call<List<>>
    @GET("wish/list")
    fun getWishList(): Call<List<WishItem>>
}