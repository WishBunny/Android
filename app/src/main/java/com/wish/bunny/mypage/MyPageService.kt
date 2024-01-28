package com.wish.bunny.mypage

import com.wish.bunny.mypage.domain.ProfileGetResponse
import com.wish.bunny.mypage.domain.ProfileUpdateRequest
import com.wish.bunny.mypage.domain.WishCountResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MyPageService {
    @GET("member/with-email")
    fun loadMyProfile(@Header("Authorization") accessToken: String): Call<ProfileGetResponse>

    @PATCH("member")
    fun updateMyProfile(@Header("Authorization") accessToken: String, @Body updatedMember: ProfileUpdateRequest): Call<ProfileGetResponse>

    @GET("wish/count/{memberId}")
    fun countWish(@Path("memberId") memberId: String): Call<WishCountResponse>
}