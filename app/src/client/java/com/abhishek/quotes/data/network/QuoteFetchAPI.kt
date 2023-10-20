package com.abhishek.quotes.data.network

import com.abhishek.quotes.domain.model.quote.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteFetchAPI {

    @GET("fetch")
    suspend fun getQuotes(
        @Query("max_quotes") maxQuotes : Int = 10,
        @Query("viewed_quotes_ids") viewedQuoteIds : String = "", // "1,2,3,4,5,6"
        @Query("selected_languages") selectedLanguages : String = "", // "hi,en"
    ) : Response<List<Quote>>
}