package com.wish.bunny.wish.domain

import com.wish.bunny.wish.domain.WishItem
import java.util.Objects

data class WishMapResult(
    val result: Objects,
    val list : List<WishItem>,
    val size: Int,
    val udResult: Int
)