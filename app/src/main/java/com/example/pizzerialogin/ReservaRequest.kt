package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class ReservaRequest(
    val FechaHoraEntrega: String,
    val PrecioTotal: Double,
    val UsuarioDocumento: String
)
