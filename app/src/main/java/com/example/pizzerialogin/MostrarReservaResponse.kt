package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class MostrarReservaResponse(
    @SerializedName("idPedido") val idPedido: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("Entregada") val entregada: Int,
    @SerializedName("FechaHoraEntrega") val fechaHoraEntrega: String,
    @SerializedName("PrecioTotal") val precioTotal: Int,
    @SerializedName("UsuarioDocumento") val usuarioDocumento: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("usuario") val usuario: Usuario?,
    @SerializedName("lineas") val lineas: List<LineaDeReserva>
)

data class Usuario(
    @SerializedName("UsuarioDocumento") val usuarioDocumento: String,
    @SerializedName("UsuarioPrimerNombre") val usuarioPrimerNombre: String,
    @SerializedName("UsuarioApellido") val usuarioApellido: String
)

data class LineaDeReserva(
    @SerializedName("idSabor") val idSabor: String,
    @SerializedName("idPedido") val idPedido: Int,
    @SerializedName("NumeroPorciones") val numeroPorciones: Int
)
