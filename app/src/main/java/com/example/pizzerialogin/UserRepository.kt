package com.example.pizzerialogin

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    fun fetchUserById(context: Context, userId: String, callback: (User?) -> Unit) {
        RetrofitClient.getInstance(context).getUserById(userId)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val user = response.body()?.usuario
                        Log.d("API", "User's Name: ${user?.usuarioPrimerNombre}")
                        callback(user)
                    } else {
                        Log.e("API", "Response error: ${response.errorBody()?.string()}")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("API", "API Call Failed", t)
                    callback(null)
                }
            })
    }
}
