package com.abhishek.quotes.di

import com.abhishek.quotes.data.network.QuoteFetchAPI
import com.abhishek.quotes.data.network.QuotesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ClientModule {

    @Singleton
    @Provides
    fun provideQuotesFetchAPI(retrofit: Retrofit): QuoteFetchAPI {
        return retrofit.create(QuoteFetchAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideQuotesService(): Retrofit {
        return QuotesService.getRetrofitInstance()
    }

}