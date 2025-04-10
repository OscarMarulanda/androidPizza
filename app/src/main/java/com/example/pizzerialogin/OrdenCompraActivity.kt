package com.example.pizzerialogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.graphics.Color
import android.util.Log
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdenCompraActivity : AppCompatActivity() {
    private lateinit var buttonRegistrarCompra: Button
    private lateinit var buttonVerReservas: Button
    private lateinit var spinnerContainer: LinearLayout

    private val items = listOf(
        SpinnerOrden("Aceitunas"),
        SpinnerOrden("Cebolla"),
        SpinnerOrden("Carne de Res"),
        SpinnerOrden("Champiñones"),
        SpinnerOrden("Chorizo"),
        SpinnerOrden("Cereza Almíbar"),
        SpinnerOrden("Duraznos Almíbar"),
        SpinnerOrden("Jalapeños"),
        SpinnerOrden("Jamón"),
        SpinnerOrden("Mango"),
        SpinnerOrden("Masa"),
        SpinnerOrden("Maíz"),
        SpinnerOrden("Pollo"),
        SpinnerOrden("Plátano"),
        SpinnerOrden("Pimiento"),
        SpinnerOrden("Piña"),
        SpinnerOrden("Pepperoni"),
        SpinnerOrden("Pasta de Tomate"),
        SpinnerOrden("Queso"),
        SpinnerOrden("Salsa BBQ"),
        SpinnerOrden("Salchicha"),
        SpinnerOrden("Tocino"),
        SpinnerOrden("Uvas Pasas")
    )
    private val adapter by lazy { SpinnerAdapterOrden(this, items) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_orden_compra)

        val btnLogout = findViewById<Button>(R.id.btnLogOut)
        btnLogout.setOnClickListener {
            SessionManager.logout(this)
        }

        spinnerContainer = findViewById(R.id.spinnersContainer)
        buttonRegistrarCompra = findViewById(R.id.buttonOrder)
        buttonVerReservas = findViewById(R.id.btnViewReservas)

        setupAddPizzaButton()
        setupReservaButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnViewIngredientes: Button = findViewById(R.id.btnViewIngredientes)

        btnViewIngredientes.setOnClickListener {
            val intent = Intent(this, IngredienteActivity::class.java)
            startActivity(intent)
        }

        buttonVerReservas.setOnClickListener {
            val intent = Intent(this, MostrarReservas::class.java)
            startActivity(intent)
        }
    }

    private fun setupAddPizzaButton() {
        val buttonAddSpinner = findViewById<Button>(R.id.buttonAddSpinner)
        buttonAddSpinner.setOnClickListener {
            val spinnerRow = LinearLayout(this)
            spinnerRow.orientation = LinearLayout.HORIZONTAL

            val newSpinner = Spinner(this)
            newSpinner.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            newSpinner.adapter = adapter

            val editTextSlices = EditText(this)
            editTextSlices.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f)
            editTextSlices.hint = "Cantidad KG"
            editTextSlices.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            editTextSlices.setTextColor(Color.parseColor("#A8E06E"))

            val deleteButton = Button(this)
            deleteButton.text = "❌"
            deleteButton.setBackgroundColor(Color.BLACK)
            deleteButton.setOnClickListener { spinnerContainer.removeView(spinnerRow) }

            spinnerRow.addView(newSpinner)
            spinnerRow.addView(editTextSlices)
            spinnerRow.addView(deleteButton)
            spinnerContainer.addView(spinnerRow)
        }
    }

    private fun showConfirmationDialog(totalSlices: Double) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Registro")

        // Build the message with flavors, slices, date, time, and price
        val message = StringBuilder()

        message.append("Ingredientes Agregados:\n")

        for (i in 0 until spinnerContainer.childCount) {
            val row = spinnerContainer.getChildAt(i) as LinearLayout
            val spinner = row.getChildAt(0) as Spinner
            val slicesInput = row.getChildAt(1) as EditText

            val selectedItem = spinner.selectedItem as SpinnerOrden
            val saborName = selectedItem.nombre
            val numSlices = slicesInput.text.toString().toDoubleOrNull() ?: 0.0

            if (numSlices > 0) {
                message.append("- $saborName: $numSlices Kg\n")
            }
        }

        builder.setMessage(message.toString())

        // Buttons for confirmation or cancellation
        builder.setPositiveButton("Confirmar") { _, _ ->
            val usuarioDocumento = SessionManager.userId ?: ""

            val reservaRequest = OrdenCompraRequest(
                UsuarioDocumento = usuarioDocumento
            )

            lifecycleScope.launch {
                sendReservaRequest(reservaRequest)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss() // Close dialog if canceled
        }

        // Show the dialog
        builder.create().show()
    }

    private fun setupReservaButton() {
        buttonRegistrarCompra.setOnClickListener {
            val usuarioDocumento = SessionManager.userId ?: ""


            val totalSlices = getTotalSlices()
            if (totalSlices == 0.0) {
                Toast.makeText(this, "Seleccione al menos 1 porción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Show confirmation dialog before making the reservation
            showConfirmationDialog(totalSlices)
        }
    }

    private fun getTotalSlices(): Double {
        var totalSlices = 0.0
        for (i in 0 until spinnerContainer.childCount) {
            val row = spinnerContainer.getChildAt(i) as LinearLayout
            val slicesInput = row.getChildAt(1) as EditText
            val slices = slicesInput.text.toString().toDoubleOrNull() ?: 0.0
            totalSlices += slices
        }
        return totalSlices
    }

    private suspend fun sendReservaRequest(ordenCompraRequest: OrdenCompraRequest) {
        val apiService = RetrofitClient.getInstance(this)

        try {
            val response = apiService.createOrdenCompra(ordenCompraRequest) // suspend function

            if (response.isSuccessful) {
                postOrdenIngrediente() // wait for this to finish
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OrdenCompraActivity, "Reserva creada exitosamente", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@OrdenCompraActivity, IngredienteActivity::class.java))
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OrdenCompraActivity, "Error al crear reserva", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@OrdenCompraActivity, "Error de conexión", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun postOrdenIngrediente() {
        val apiService = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            for (i in 0 until spinnerContainer.childCount) {
                val row = spinnerContainer.getChildAt(i) as LinearLayout
                val spinner = row.getChildAt(0) as Spinner
                val slicesInput = row.getChildAt(1) as EditText

                val selectedItem = spinner.selectedItem as SpinnerOrden
                val idSabor = getSaborIdFromName(selectedItem.nombre) // Convert name to ID
                val numPorciones = slicesInput.text.toString().toDoubleOrNull() ?: 0.0

                if (numPorciones > 0) {
                    val lineaRequest = OrdenIngredienteRequest(
                        idIngrediente = idSabor,
                        CantidadComprada = numPorciones
                    )

                    try {
                        // Now it's a suspend function, so call it directly
                        val response = apiService.postOrdenIngrediente(lineaRequest)
                        if (response.isSuccessful) {
                            println("Linea creada: ${response.body()}")
                        } else {
                            println("Error al crear linea: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        println("Error de conexión al enviar Linea: ${e.message}")
                    }
                }
            }
        }
    }

    private fun getSaborIdFromName(saborName: String): String {
        return when (saborName) {
            "Aceitunas" -> "Act"
            "Cebolla" -> "Cbll"
            "Carne de Res" -> "Cdr"
            "Champiñones" -> "Chpm"
            "Chorizo" -> "Chz"
            "Cereza Almíbar" -> "Cra"
            "Duraznos Almíbar" -> "Drl"
            "Jalapeños" -> "jlp"
            "Jamón" -> "Jm"
            "Mango" -> "Mng"
            "Masa" -> "Ms"
            "Maíz" -> "Mz"
            "Pollo" -> "Pll"
            "Plátano" -> "Plt"
            "Pimiento" -> "Pmto"
            "Piña" -> "Pna"
            "Pepperoni" -> "Pprn"
            "Pasta de Tomate" -> "Pst"
            "Queso" -> "Qs"
            "Salsa BBQ" -> "sBBQ"
            "Salchicha" -> "Slch"
            "Tocino" -> "Tcn"
            "Uvas Pasas" -> "Uvp"
            else -> "" // Default case, return empty string or handle error
        }
    }

}