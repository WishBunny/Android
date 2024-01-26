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

    @GET("friend/match")
    fun createInviteCode(@Header("Authorization") accessToken: String): Call<FriendResponse>

    @POST("friend/match")
    fun matchFriend(@Header("Authorization") accessToken: String, @Body matchingRequest: MatchingRequest): Call<MatchingResponse>

    @GET("friend")
    fun getFriendList(@Header("Authorization") accessToken: String): Call<FriendListResponse>

    @DELETE("friend/{friendId}")
    fun deleteFriend(@Path("friendId") friendId: String): Call<FriendDeleteResponse>
}