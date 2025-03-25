package com.example.pizzerialogin

data class LoginResponse(
    val message: String,
    val token: String?, // Nullable in case login fails
    val usuario: User,  // Adjust type if needed (e.g., replace `Any` with a `User` data class)
    val status: Int
)
