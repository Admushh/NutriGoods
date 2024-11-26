package com.example.nutrigood

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.data.retrofit.ApiConfig
import retrofit2.Call
import kotlin.text.isEmpty
import kotlin.text.toIntOrNull
import kotlin.text.trim

class Register : androidx.appcompat.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nutrigood.R.layout.activity_register)

        // Inisialisasi elemen UI
        val etEmail = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.et_email)
        val etUsername = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.et_username)
        val etAge = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.et_age)
        val etPassword = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.et_password)
        val etConfirmPassword = findViewById<android.widget.EditText>(com.example.nutrigood.R.id.et_confirm_password)
        val spinnerDiabetes = findViewById<android.widget.Spinner>(com.example.nutrigood.R.id.spinner_diabetes)
        val btnSubmit = findViewById<android.widget.Button>(com.example.nutrigood.R.id.btn_submit)

        // Set data untuk spinner
        val diabetesOptions = kotlin.arrayOf("Yes", "No")
        val adapter =
            android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, diabetesOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiabetes.adapter = adapter

        // Klik tombol submit
        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val ageText = etAge.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val diabetesStatus = spinnerDiabetes.selectedItem.toString()

            // Validasi input
            if (email.isEmpty() || username.isEmpty() || ageText.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                android.widget.Toast.makeText(this, "All fields are required", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi umur (harus angka)
            val age = ageText.toIntOrNull()
            if (age == null || age <= 0) {
                android.widget.Toast.makeText(this, "Age must be a valid positive number", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                android.widget.Toast.makeText(this, "Passwords do not match", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi untuk register user
            registerUser(com.data.response.User(email, password, username, age, diabetesStatus))
        }
    }

    private fun registerUser(user: com.data.response.User) {
        val apiService = ApiConfig.getApiService()

        // Log data yang dikirim untuk debugging
        android.util.Log.d("RegisterPayload", "Payload: $user")

        apiService.registerUser(user).enqueue(object : retrofit2.Callback<java.lang.Void> {
            override fun onResponse(call: retrofit2.Call<java.lang.Void>, response: retrofit2.Response<java.lang.Void>) {
                if (response.isSuccessful) {
                    android.widget.Toast.makeText(this@Register, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Register, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@Register,
                        "Failed to register: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Register, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("RegisterError", "Network error: ${t.message}")
            }
        })
    }
}
