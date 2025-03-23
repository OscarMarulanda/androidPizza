package com.example.pizzerialogin

data class ReservaResponse(
    val message: String?,  // Present when there's an error
    val errors: Map<String, List<String>>?, // Validation errors
    val status: Int,
    val reserva: ReservaData? // Only present when successful
)

data class ReservaData(
    val idPedido: Int,
    val Entregada: Int,
    val FechaHoraEntrega: String,
    val PrecioTotal: Double,
    val UsuarioDocumento: String
)