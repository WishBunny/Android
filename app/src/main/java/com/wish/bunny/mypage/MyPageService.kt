package com.wish.bunny.mypage

import com.wish.bunny.mypage.domain.ProfileGetResponse
import com.wish.bunny.mypage.domain.ProfileUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MyPageService {
    @GET("member")
    fun loadMyProfile(@Header("Authorization") accessToken: String): Call<ProfileGetResponse>

    @PATCH("member")
    fun updateMyProfile(@Header("Authorization") accessToken: String, @Body updatedMember: ProfileUpdateRequest): Call<ProfileGetResponse>
}