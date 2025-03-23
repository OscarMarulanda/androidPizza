package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("UsuarioDocumento") val usuarioDocumento: String,
    @SerializedName("UsuarioTelefono") val usuarioTelefono: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("UsuarioPrimerNombre") val usuarioPrimerNombre: String,
    @SerializedName("UsuarioApellido") val usuarioApellido: String,
    @SerializedName("idTipoDocumento") val idTipoDocumento: Int,
    @SerializedName("idTipoUsuario") val idTipoUsuario: Int
)

data class ApiResponse(
    @SerializedName("usuario") val usuario: User,
    @SerializedName("status") val status: Int
)