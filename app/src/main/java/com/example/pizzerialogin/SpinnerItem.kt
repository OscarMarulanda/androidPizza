package com.example.pizzerialogin


data class SpinnerItem(
    val nombre: String,       // el nombre del sabor de pizza
    val imageResId: Int     // el resource ID para la imagen correspondiente
)

data class SpinnerOrden(
    val nombre: String
)