package com.example.pizzerialogin
import com.example.pizzerialogin.User

data class RegisterResponse(
    val usuario: User?,
    val message: String?,
    val status: Int
)
