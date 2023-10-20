package com.abhishek.quotes.data.network

import com.abhishek.quotes.domain.model.network.AuthResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AdminLoginAPI {

    @POST("auth")
    suspend fun login(
        @Query("username") username: String,
        @Header("token") token : String,
    ) : Response<AuthResponse>

}