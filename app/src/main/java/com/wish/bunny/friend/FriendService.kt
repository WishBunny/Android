package com.wish.bunny.friend

import com.wish.bunny.friend.domain.FriendDeleteResponse
import com.wish.bunny.friend.domain.FriendListResponse
import com.wish.bunny.friend.domain.FriendResponse
import com.wish.bunny.friend.domain.MatchingRequest
import com.wish.bunny.friend.domain.MatchingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendService {
    /**
    작성자:  이혜연
    처리 내용: 초대 코드 발급 API 통신
     */
    @GET("friend/match")
    fun createInviteCode(@Header("Authorization") accessToken: String): Call<FriendResponse>

    /**
    작성자:  이혜연
    처리 내용: 친구 매칭 API 통신
     */
    @POST("friend/match")
    fun matchFriend(@Header("Authorization") accessToken: String, @Body matchingRequest: MatchingRequest): Call<MatchingResponse>

    /**
    작성자:  이혜연
    처리 내용: 친구 리스트 조회 API 통신
     */
    @GET("friend")
    fun getFriendList(@Header("Authorization") accessToken: String): Call<FriendListResponse>

    /**
    작성자:  이혜연
    처리 내용: 친구 매칭 끊기 API 통신
     */
    @DELETE("friend/{friendId}")
    fun deleteFriend(@Path("friendId") friendId: String): Call<FriendDeleteResponse>
}