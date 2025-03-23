package com.example.pizzerialogin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Reservar : AppCompatActivity() {
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var spinnerContainer: LinearLayout
    private lateinit var buttonReservar: Button
    private val items = listOf(
        SpinnerItem("Carne Tradicional", R.drawable.carne),
        SpinnerItem("Carne BBQ", R.drawable.bbq),
        SpinnerItem("Hawaiana", R.drawable.phw),
        SpinnerItem("Pollo Champiñones", R.drawable.pollochampinones),
        SpinnerItem("Maduro", R.drawable.maduro),
        SpinnerItem("Mango Tocineta", R.drawable.mangotocineta),
        SpinnerItem("Mexicana", R.drawable.mexicana),
        SpinnerItem("Paisa", R.drawable.paisa),
        SpinnerItem("Ranchera", R.drawable.ranchera),
        SpinnerItem("Tropical", R.drawable.tropical)
    )
    private val adapter by lazy { SpinnerAdapter(this, items) }
    private val pricePerSlice = 14000 // Precio por porción en pesos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservar)

        editTextDate = findViewById(R.id.editTextDate)
        editTextTime = findViewById(R.id.editTextTime)
        spinnerContainer = findViewById(R.id.spinnersContainer)
        buttonReservar = findViewById(R.id.buttonReservar)

        setupDatePicker()
        setupTimePicker()
        setupAddPizzaButton()
        setupReservaButton()
    }

    private fun setupDatePicker() {
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val today = calendar.timeInMillis

            // Set the max date to tomorrow
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val tomorrow = calendar.timeInMillis

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    editTextDate.setText("$year-${month + 1}-$dayOfMonth")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Restrict date selection
            datePickerDialog.datePicker.minDate = today
            datePickerDialog.datePicker.maxDate = tomorrow

            datePickerDialog.show()
        }
    }

    private fun setupTimePicker() {
        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    val roundedMinute = if (minute < 30) 0 else 30
                    editTextTime.setText(String.format("%02d:%02d:00", hour, roundedMinute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                0,
                true
            )
            timePickerDialog.show()
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
            editTextSlices.hint = "Porciones"
            editTextSlices.inputType = android.text.InputType.TYPE_CLASS_NUMBER

            val deleteButton = Button(this)
            deleteButton.text = "❌"
            deleteButton.setOnClickListener { spinnerContainer.removeView(spinnerRow) }

            spinnerRow.addView(newSpinner)
            spinnerRow.addView(editTextSlices)
            spinnerRow.addView(deleteButton)
            spinnerContainer.addView(spinnerRow)
        }
    }

    private fun setupReservaButton() {
        buttonReservar.setOnClickListener {
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val usuarioDocumento = SessionManager.userId ?: ""

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Seleccione fecha y hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalSlices = getTotalSlices()
            if (totalSlices == 0) {
                Toast.makeText(this, "Seleccione al menos 1 porción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fechaHoraEntrega = "$date $time"
            val precioTotal = totalSlices * pricePerSlice.toDouble()

            val reservaRequest = ReservaRequest(
                FechaHoraEntrega = fechaHoraEntrega,
                PrecioTotal = precioTotal,
                UsuarioDocumento = usuarioDocumento
            )

            sendReservaRequest(reservaRequest)
        }
    }

    private fun getTotalSlices(): Int {
        var totalSlices = 0
        for (i in 0 until spinnerContainer.childCount) {
            val row = spinnerContainer.getChildAt(i) as LinearLayout
            val slicesInput = row.getChildAt(1) as EditText
            val slices = slicesInput.text.toString().toIntOrNull() ?: 0
            totalSlices += slices
        }
        return totalSlices
    }

    private fun sendReservaRequest(reservaRequest: ReservaRequest) {
        val apiService = RetrofitClient.instance
        val call = apiService.createReserva(reservaRequest)

        call.enqueue(object : Callback<ReservaResponse> {
            override fun onResponse(call: Call<ReservaResponse>, response: Response<ReservaResponse>) {
                if (response.isSuccessful) {
                    val reservaResponse = response.body()
                    val idPedido = reservaResponse?.reserva?.idPedido ?: return
                    sendLineaRequests(idPedido)
                    Toast.makeText(this@Reservar, "Reserva creada exitosamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@Reservar, "Error al crear reserva", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
                Toast.makeText(this@Reservar, "Error de conexión", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun sendLineaRequests(idPedido: Int) {
        val apiService = RetrofitClient.instance

        for (i in 0 until spinnerContainer.childCount) {
            val row = spinnerContainer.getChildAt(i) as LinearLayout
            val spinner = row.getChildAt(0) as Spinner
            val slicesInput = row.getChildAt(1) as EditText

            val selectedItem = spinner.selectedItem as SpinnerItem
            val idSabor = getSaborIdFromName(selectedItem.nombre) // Convert name to ID
            val numPorciones = slicesInput.text.toString().toIntOrNull() ?: 0

            if (numPorciones > 0) {
                val lineaRequest = LineaRequest(
                    idSabor = idSabor,
                    NumeroPorciones = numPorciones
                )

                val call = apiService.createLinea(lineaRequest)
                call.enqueue(object : Callback<LineaResponse> {
                    override fun onResponse(call: Call<LineaResponse>, response: Response<LineaResponse>) {
                        if (response.isSuccessful) {
                            println("Linea creada: ${response.body()}")
                        } else {
                            println("Error al crear linea: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<LineaResponse>, t: Throwable) {
                        println("Error de conexión al enviar Linea: ${t.message}")
                    }
                })
            }
        }
    }

    private fun getSaborIdFromName(saborName: String): String {
        return when (saborName) {
            "Carne Tradicional" -> "Crt"
            "Carne BBQ" -> "PcBBQ"
            "Hawaiana" -> "Phw"
            "Pollo Champiñones" -> "Pllc"
            "Maduro" -> "Pm"
            "Mango Tocineta" -> "Pmt"
            "Mexicana" -> "Pmx"
            "Paisa" -> "Pps"
            "Ranchera" -> "Prh"
            "Tropical" -> "Ptrp"
            else -> "" // Default case, return empty string or handle error
        }
    }
}
