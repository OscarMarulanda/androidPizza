package com.example.pizzerialogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MostrarReservas: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservasAdapter: ReservasAdapter
    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_reservas)

        recyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obtenerReservas()

        val btnViewIngredientes: Button = findViewById(R.id.btnViewIngredientes)
        val btnAddIngrediente: Button = findViewById(R.id.btnAddIngredientes)

        btnViewIngredientes.setOnClickListener {
            val intent = Intent(this, IngredienteActivity::class.java)
            startActivity(intent)
        }

        btnAddIngrediente.setOnClickListener {
            val intent = Intent(this, OrdenCompraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtenerReservas() {
        lifecycleScope.launch {
            try {
                val response = apiService.getReservas()
                if (response.isSuccessful && response.body() != null) {
                    val reservas = response.body()!!
                    reservasAdapter = ReservasAdapter(reservas, apiService)
                    recyclerView.adapter = reservasAdapter
                } else {
                    Toast.makeText(this@MostrarReservas, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MostrarReservas, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}