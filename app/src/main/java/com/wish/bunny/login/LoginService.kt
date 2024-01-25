package com.wish.bunny.login

import com.wish.bunny.login.domain.AccessToken
import com.wish.bunny.login.domain.MemberModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
    작성자: 엄상은
    처리 내용: Login 서비스
 */
interface LoginService {
    @POST("kakao/login")
    fun loginByAccessToken(@Body accessToken: AccessToken): Call<MemberModel>
}