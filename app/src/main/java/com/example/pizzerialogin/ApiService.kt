package com.example.pizzerialogin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

import com.example.pizzerialogin.User
import retrofit2.Response

interface ApiService {

    @GET("pizzapaisa/{UsuarioDocumento}")
    fun getUserById(@Path("UsuarioDocumento") userId: String): Call<ApiResponse>

    @POST("login") // Adjust this to match your API endpoint
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("pizzapaisa") // Ensure this matches your Laravel route
    fun registerUser(@Body user: RegisterRequest): Call<RegisterResponse>

    @POST("reserva")
    fun createReserva(@Body request: ReservaRequest): Call<ReservaResponse>

    @POST("linea")
    fun createLinea(@Body lineaRequest: LineaRequest): Call<LineaResponse>

    @GET("reserva")
    suspend fun getReservas(): Response<List<MostrarReservaResponse>>

}