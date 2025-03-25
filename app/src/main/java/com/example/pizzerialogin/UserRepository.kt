package com.example.pizzerialogin

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    fun fetchUserById(userId: String, callback: (User?) -> Unit) {
        RetrofitClient.instance.getUserById(userId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.usuario
                    Log.d("API", "User's Name: ${user?.usuarioPrimerNombre}")
                    callback(user)  // Pass the user object to the callback
                } else {
                    Log.e("API", "Response error: ${response.errorBody()?.string()}")
                    callback(null) // Indicate failure
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API", "API Call Failed", t)
                callback(null) // Indicate failure
            }
        })
    }
}
