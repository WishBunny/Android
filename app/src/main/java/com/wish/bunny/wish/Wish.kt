package com.wish.bunny.wish

import com.google.gson.annotations.SerializedName

data class Wish (
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    val id: String,

    @SerializedName("body")
    val body: String
)
