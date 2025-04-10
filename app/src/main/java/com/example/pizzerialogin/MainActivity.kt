package com.example.pizzerialogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.pizzerialogin.RetrofitClient
import com.example.pizzerialogin.User

data class TipoDocumento(val id: Int, val nombre: String)

class MainActivity : AppCompatActivity() {

    private lateinit var tiposDocumento: List<TipoDocumento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tipoDocumentoSpinner = findViewById<Spinner>(R.id.spinnerTipoDocumento)

        tiposDocumento = listOf(
            TipoDocumento(1, "Cédula de Ciudadanía"),
            TipoDocumento(2, "Cédula de Extranjería"),
            TipoDocumento(3, "Número de Pasaporte"),
            TipoDocumento(4, "Tarjeta de Identidad"),
            TipoDocumento(5, "Pasaporte")
        )

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_documentos,
            tiposDocumento.map { it.nombre }
        )
        adapter.setDropDownViewResource(R.layout.spinner_item_documentos)
        tipoDocumentoSpinner.adapter = adapter



        fetchUserById("1124217751")

        val continuar = findViewById<Button>(R.id.button)
        val registrar = findViewById<Button>(R.id.buttonResgister)


        continuar.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registrar.setOnClickListener{
            registerUser()
        }
    }


    fun registerUser() {

        val etDocumento = findViewById<EditText>(R.id.etDocumento)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPrimerNombre = findViewById<EditText>(R.id.etPrimerNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val spinnerTipoDocumento = findViewById<Spinner>(R.id.spinnerTipoDocumento)
        val tipoSeleccionado = spinnerTipoDocumento.selectedItemPosition
        val tipoDocumentoId = tiposDocumento[tipoSeleccionado].id

        val user = RegisterRequest(
            documento = etDocumento.text.toString().trim(),
            telefono = etTelefono.text.toString().trim(),
            contrasena = etContrasena.text.toString().trim(),
            correo = etCorreo.text.toString().trim(),
            primerNombre = etPrimerNombre.text.toString().trim(),
            apellido = etApellido.text.toString().trim(),
            tipoDocumento = tipoDocumentoId,
            tipoUsuario = 3
        )

        RetrofitClient.instance.registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Registro exitoso"
                    Toast.makeText(applicationContext, "Registro de usuario exitoso", Toast.LENGTH_LONG).show()
                    Log.d("Register", "Success: $message")
                } else {
                    Log.e("Register", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "Error al conectar con servidor", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Register", "Failure: ${t.message}")
                Toast.makeText(applicationContext, "Error al conectar con servidor", Toast.LENGTH_LONG).show()            }
        })
    }


    private fun fetchUserById(userId: String) {
        RetrofitClient.instance.getUserById(userId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                if (response.isSuccessful) {
                    response.body()?.usuario?.let { user ->
                        Log.d("API", "User's Name: ${user.usuarioPrimerNombre}")
                    }
                } else {
                    Log.e("API", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API", "API Call Failed", t)
            }
        })
    }
}