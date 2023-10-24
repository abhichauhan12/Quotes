package com.abhishek.quotes.di

import android.content.Context
import com.abhishek.quotes.data.network.AdminLoginAPI
import com.abhishek.quotes.data.network.QuotesAPI
import com.abhishek.quotes.data.network.QuotesService
import com.abhishek.quotes.data.network.interceptors.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAdminLoginAPI(retrofit: Retrofit): AdminLoginAPI {
        return retrofit.create(AdminLoginAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(@ApplicationContext context: Context): HeaderInterceptor {
        return HeaderInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientWithInterceptors(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(headerInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideQuotesService(okHttpClient: OkHttpClient): Retrofit {
        return QuotesService.getRetrofitInstance(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideQuotesAPI(retrofit: Retrofit): QuotesAPI {
        return retrofit.create(QuotesAPI::class.java)
    }
}
