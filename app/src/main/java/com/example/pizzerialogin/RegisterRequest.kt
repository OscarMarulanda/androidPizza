package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("UsuarioDocumento") val documento: String,
    @SerializedName("UsuarioTelefono") val telefono: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("UsuarioPrimerNombre") val primerNombre: String,
    @SerializedName("UsuarioApellido") val apellido: String,
    @SerializedName("idTipoDocumento") val tipoDocumento: Int,
    @SerializedName("idTipoUsuario") val tipoUsuario: Int
)
