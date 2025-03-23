package com.example.pizzerialogin

data class LoginRequest(
    val UsuarioDocumento: String,
    val Correo: String,
    val Contrasena: String
)
