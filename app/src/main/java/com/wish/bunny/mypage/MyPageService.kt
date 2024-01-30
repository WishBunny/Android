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
    /**
        작성자: 엄상은
        처리 내용: 마이페이지 조회
    */
    @GET("member/with-email")
    fun loadMyProfile(@Header("Authorization") accessToken: String): Call<ProfileGetResponse>

    /**
        작성자: 엄상은
        처리 내용: 마이페이지 수정
     */
    @PATCH("member")
    fun updateMyProfile(@Header("Authorization") accessToken: String, @Body updatedMember: ProfileUpdateRequest): Call<ProfileGetResponse>

    /**
    작성자:  이혜연
    처리 내용: 위시리스트 남은 개수 및 달성 개수 반환 API 통신
     */
    @GET("wish/count/{memberId}")
    fun countWish(@Path("memberId") memberId: String): Call<WishCountResponse>
}