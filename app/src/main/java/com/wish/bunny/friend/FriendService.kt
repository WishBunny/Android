package com.wish.bunny.friend

import com.wish.bunny.friend.domain.FriendResponse
import com.wish.bunny.friend.domain.MatchingRequest
import com.wish.bunny.friend.domain.MatchingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FriendService {

    @GET("friend/match")
    fun createInviteCode(@Header("Authorization") accessToken: String): Call<FriendResponse>

    @POST("friend/match")
    fun matchFriend(@Header("Authorization") accessToken: String, @Body matchingRequest: MatchingRequest): Call<MatchingResponse>
}