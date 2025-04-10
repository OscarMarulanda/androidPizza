package com.example.pizzerialogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredienteActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingrediente)

        val btnLogout = findViewById<Button>(R.id.btnLogOut)
        btnLogout.setOnClickListener {
            SessionManager.logout(this)
        }

        recyclerView = findViewById(R.id.recyclerViewIngredientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchIngredientes()

        val btnViewReservas: Button = findViewById(R.id.btnViewReservas)
        val btnAgregarIngredientes: Button = findViewById(R.id.btnAgregarIngredientes)

        btnViewReservas.setOnClickListener {
            val intent = Intent(this, MostrarReservas::class.java)
            startActivity(intent)
        }

        btnAgregarIngredientes.setOnClickListener{
            val intent = Intent(this, OrdenCompraActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchIngredientes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getIngredientes()
                if (response.isSuccessful) {
                    val ingredientes = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        adapter = IngredienteAdapter(ingredientes)
                        recyclerView.adapter = adapter
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Error al obtener ingredientes", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
