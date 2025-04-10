package com.example.pizzerialogin

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    fun getInstance(context: Context): ApiService {
        val sharedPreferences = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        val clientBuilder = OkHttpClient.Builder()

        if (!token.isNullOrEmpty()) {
            clientBuilder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    return chain.proceed(request)
                }
            })
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
