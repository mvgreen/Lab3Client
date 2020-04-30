package com.mvgreen.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val status: String?,
    val id: Int?,

    val currentPlayer: Int?,
    val previousLine: String?,
    val completePoem: String?
)