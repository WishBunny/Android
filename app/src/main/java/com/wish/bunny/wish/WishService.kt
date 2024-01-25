package com.wish.bunny.wish
import com.wish.bunny.wish.domain.WishMapResult
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path


interface WishService {

    // fun getWishList(): Call<List<>>
    @GET("wish/list")
    fun getWishList(): Call<WishMapResult>

    @GET("wish/{wishNo}")
    fun getWishDetail(@Path("wishNo") wishNo: String): Call<WishItem>

    @PATCH("wish/{wishNo}")
    fun finishWish(@Path("wishNo") wishNo: String): Call<WishMapResult>

}