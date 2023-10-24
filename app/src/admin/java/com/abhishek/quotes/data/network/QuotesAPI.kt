package com.abhishek.quotes.data.network

import com.abhishek.quotes.domain.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesAPI {

    @GET("fetch")
    suspend fun getQuotes(
        @Query("max_quotes") maxCount : Int = 20
    ) : Response<List<Quote>>
}