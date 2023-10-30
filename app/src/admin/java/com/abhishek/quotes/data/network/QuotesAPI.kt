package com.abhishek.quotes.data.network

import com.abhishek.quotes.domain.model.Quote
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface QuotesAPI {

    @GET("admin/fetch")
    suspend fun getQuotes(
        @Query("max_quotes") maxCount: Int = 20,
        @Query("offset") offset: Int = 0,
    ): Response<List<Quote>>

    @Multipart
    @POST("add-quote")
    suspend fun addQuote(
        @Query("message") message: String,
        @Query("author") author: String,
        @Query("language") language: String,
        @Part imageFile: MultipartBody.Part,
    ): Response<Any>

    @Multipart
    @PUT("edit-quote")
    suspend fun updateQuote(
        @Query("id") quoteId: Int,
        @Query("message") message: String,
        @Query("author") author: String,
        @Query("language") language: String,
        @Part imageFile: MultipartBody.Part?,
    ): Response<Any>

}