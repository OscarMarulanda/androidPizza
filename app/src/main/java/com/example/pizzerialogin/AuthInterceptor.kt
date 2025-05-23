package com.example.pizzerialogin

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithToken = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(requestWithToken)
    }
}