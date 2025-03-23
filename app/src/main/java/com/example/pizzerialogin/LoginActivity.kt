package com.example.pizzerialogin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pizzerialogin.LoginRequest
import com.example.pizzerialogin.LoginResponse
import com.example.pizzerialogin.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)

        val editTextUsuario = findViewById<EditText>(R.id.editTextUsuario)
        val editTextCorreo = findViewById<EditText>(R.id.editTextCorreo)
        val editTextContrasena = findViewById<EditText>(R.id.editTextContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuarioDocumento = editTextUsuario.text.toString()
            val correo = editTextCorreo.text.toString()
            val contrasena = editTextContrasena.text.toString()

            if (usuarioDocumento.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                loginUser(usuarioDocumento, correo, contrasena)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(usuarioDocumento: String, correo: String, contrasena: String) {
        val loginRequest = LoginRequest(usuarioDocumento, correo, contrasena)

        RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    SessionManager.userId = usuarioDocumento
                    Log.d("SessionManager", "Stored userId: $usuarioDocumento")
                    response.body()?.let {
                        if (it.status == 200) {
                            val token = it.token

                            // Save token in SharedPreferences
                            sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                            sharedPreferences.edit().putString("TOKEN", token).apply()
                            sharedPreferences.edit().putString("userId", usuarioDocumento).apply()

                            val userId = sharedPreferences.getString("userId", "No ID found")
                            Log.d("UserID", userId ?: "No ID found")

                            Toast.makeText(applicationContext, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()

                            // Navigate to another activity (e.g., HomeActivity)
                            val intent = Intent(this@LoginActivity, Reservar::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Error en la autenticación", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}