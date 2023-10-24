package com.abhishek.quotes.data.network.interceptors

import android.content.Context
import com.abhishek.quotes.data.proto.adminDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val adminToken : String? = try {
                context.adminDataStore.data.first().token
            }catch (_: NullPointerException) {
                null
            }

            val originalRequest: Request = chain.request()

            if (!adminToken.isNullOrBlank()) {
                val builder: Request.Builder = originalRequest.newBuilder()
                builder.header("token", adminToken)
                val updatedRequest: Request = builder.method(originalRequest.method(), originalRequest.body()).build()
                chain.proceed(updatedRequest)
            } else {
                chain.proceed(originalRequest)
            }
        }
    }
}