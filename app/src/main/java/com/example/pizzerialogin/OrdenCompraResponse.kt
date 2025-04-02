package com.example.pizzerialogin

data class OrdenCompraResponse(
    val message: String?,  // Present when there's an error
    val errors: Map<String, List<String>>?, // Validation errors
    val status: Int,
    val ordenCompra: OrdenCompra? // Only present when successful
)

data class OrdenCompra(
    val idOrden: Int,
    val UsuarioDocumento: String
)
