package com.abhishek.quotes.domain.model.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val message: String?,
    val token: String?
)