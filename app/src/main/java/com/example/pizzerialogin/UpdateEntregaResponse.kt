package com.example.pizzerialogin

import com.google.gson.annotations.SerializedName

data class UpdateEntregaResponse(
    @SerializedName("message") val message: String,
    @SerializedName("Entrega") val entrega: Int,
    @SerializedName("status") val status: Int
)