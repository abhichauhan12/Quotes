package com.abhishek.quotes.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class QuotesService {

    companion object {
        private val BASE_URL = "http://127.0.0.1:8080/Gradle___com_fiore___Quotes_1_0_SNAPSHOT_war/"

        fun getRetrofitInstance(okHttpClient: OkHttpClient = OkHttpClient()) : Retrofit {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
    }

}