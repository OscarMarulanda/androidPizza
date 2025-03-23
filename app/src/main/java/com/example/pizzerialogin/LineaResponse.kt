package com.example.pizzerialogin

data class LineaResponse(
    val linea: Linea?, // The newly created Linea object
    val status: Int
)

data class Linea(
    val idPedido: Int,
    val idSabor: Int,
    val NumeroPorciones: Int
)
