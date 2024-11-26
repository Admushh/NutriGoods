package com.example.nutrigood

import android.util.Log
import android.widget.Toast
import com.data.retrofit.ApiConfig
import com.data.response.LoginRequest
import com.data.response.LoginResponse
import retrofit2.Call
import kotlin.jvm.java
import kotlin.text.isEmpty
import kotlin.text.trim

class LoginActivity : androidx.appcompat.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nutrigood.R.layout.activity_login)

        // Inisialisasi elemen UI
        val btnLogin = findViewById<android.widget.Button>(com.example.nutrigood.R.id.btn_login)
        val etEmail = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.email)
        val etPassword = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.password)
        val tvRegisterAccount = findViewById<android.widget.TextView>(com.example.nutrigood.R.id.register_account)

        // Navigasi ke halaman registrasi
        tvRegisterAccount.setOnClickListener {
            val intent = android.content.Intent(this, com.example.nutrigood.Register::class.java)
            startActivity(intent)
        }

        // Tombol login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            android.util.Log.e("Login", "Login Button Clicked")

            // Validasi input
            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Email and password are required", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                // Membuat objek LoginRequest dan mengirimkan ke server
                val loginRequest = com.data.response.LoginRequest(email, password)
                android.util.Log.d("LoginRequest", "Request: Email=${loginRequest.getEmail()}, Password=*******") // Sembunyikan password di log
                loginUser(loginRequest)
            }
        }
    }

    private fun loginUser(request: com.data.response.LoginRequest) {
        val apiService = ApiConfig.getApiService()

        // Kirim permintaan login ke server
        apiService.loginUser(request).enqueue(object :
            retrofit2.Callback<com.data.response.LoginResponse> {
            override fun onResponse(call: retrofit2.Call<com.data.response.LoginResponse>, response: retrofit2.Response<com.data.response.LoginResponse>) {
                android.util.Log.d("LoginResponse", "Response Code: ${response.code()}")

                val loginResponse = response.body()
                if (loginResponse != null) {
                    val token = loginResponse.data?.token
                    android.util.Log.d("LoginResponse", "Token: $token")
                    if (token != null) {
                        saveToken(token)
                        startActivity(android.content.Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Log.e("LoginResponse", "Token is null")
                        Toast.makeText(this@LoginActivity, "Failed to retrieve token from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginResponse", "Response Body is null")
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoginError", "Error: ${t.message}")
            }
        })
    }

    private fun saveToken(token: String) {
        // Simpan token ke SharedPreferences
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
        Log.d("SaveToken", "Token saved successfully")
    }
}
