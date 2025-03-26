package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class UpdateEntregaRequest(
    @SerializedName("idPedido") val idPedido: Int,
    @SerializedName("Entregada") val entregada: Int
)