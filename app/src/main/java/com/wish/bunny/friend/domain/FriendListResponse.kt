package com.wish.bunny.friend.domain

data class FriendListResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val matchedToNowDt: String,
        val imgUrl: String,
        val memberId: String,
        val nickname: String,
        val friendId: String
    )
}