package com.wish.bunny.wish

import com.wish.bunny.wish.domain.Message
import com.wish.bunny.wish.domain.WishVo
import com.wish.bunny.wish.domain.WishMapResult
import com.wish.bunny.wish.domain.WishItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path


interface WishService {
    /**
    작성자: 황수연
    처리 내용: 위시 데이터 등록 서비스
     */
    @POST("wish")
    fun wishInsert(@Body wvo: WishVo): Call<Response<Message>>

    /**
    작성자: 김은솔
    처리 내용: 작성자에 따라 위시리스트 조회
     */
    @GET("wish/list/{achieveYn}/{writerNo}/{category}")
    fun getWishList(@Path("achieveYn") achieveYn: String, @Path("writerNo") writerNo: String, @Header("Authorization") accessToken: String,@Path("category") category: String): Call<WishMapResult>
    /**
    작성자: 김은솔
    처리 내용: 위시리스트 상세 조회
     */

    @GET("wish/list")
    fun getWishList(): Call<WishMapResult>


    @GET("wish/{wishNo}")
    fun getWishDetail(@Path("wishNo") wishNo: String): Call<WishItem>
    /**
    작성자: 김은솔
    처리 내용: 위시리스트 완료처리
     */
    @PATCH("wish/{wishNo}")
    fun finishWish(@Path("wishNo") wishNo: String): Call<WishMapResult>

    /**
    작성자: 김은솔
    처리 내용: 완료된 위시리스트 가져오기
     */
    @GET("wish/doneListSize/{writerNo}")
    fun doneListSize(@Path("writerNo") writerNo: String): Call<WishMapResult>

}