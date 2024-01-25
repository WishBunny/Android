package com.wish.bunny.wish

import com.wish.bunny.wish.domain.Message
import com.wish.bunny.wish.domain.WishVo
import com.wish.bunny.wish.domain.WishMapResult
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path


interface WishService {

    // fun getWishList(): Call<List<>>
    @GET("wish/list")
    fun getWishList(): Call<List<WishItem>>

    /**
    작성자: 황수연
    처리 내용: 위시 데이터 등록 서비스
     */
    @POST("wish")
    fun wishInsert(@Body wvo: WishVo): Call<Response<Message>>
    fun getWishList(): Call<WishMapResult>

    @GET("wish/{wishNo}")
    fun getWishDetail(@Path("wishNo") wishNo: String): Call<WishItem>

    @PATCH("wish/{wishNo}")
    fun finishWish(@Path("wishNo") wishNo: String): Call<WishMapResult>
}