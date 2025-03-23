package com.example.pizzerialogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.pizzerialogin.RetrofitClient
import com.example.pizzerialogin.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



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
        val etTipoDocumento = findViewById<EditText>(R.id.etTipoDocumento)
        val etTipoUsuario = findViewById<EditText>(R.id.etTipoUsuario)

        val user = RegisterRequest(
            documento = etDocumento.text.toString().trim(),
            telefono = etTelefono.text.toString().trim(),
            contrasena = etContrasena.text.toString().trim(),
            correo = etCorreo.text.toString().trim(),
            primerNombre = etPrimerNombre.text.toString().trim(),
            apellido = etApellido.text.toString().trim(),
            tipoDocumento = etTipoDocumento.text.toString().toIntOrNull() ?: 1,
            tipoUsuario = etTipoUsuario.text.toString().toIntOrNull() ?: 0
        )

        RetrofitClient.instance.registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Log.d("Register", "Success: ${response.body()?.message}")
                } else {
                    Log.e("Register", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Register", "Failure: ${t.message}")
            }
        })
    }


    private fun fetchUserById(userId: String) {
        RetrofitClient.instance.getUserById(userId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        Log.d("API", "User: ${response.body()}")
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