package com.example.pizzerialogin

class OrdenIngredienteResponse(
    val message: String?,
    val OrdenIngrediente: OrdenIngredienteData?,
    val status: Int
)

data class OrdenIngredienteData(
    val idOrden: Int,
    val idIngrediente: String,
    val CantidadSolicitada: Int,
    val idProveedor: Int,
    val CantidadComprada: Int
)