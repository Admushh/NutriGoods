package com.example.nutrigood

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrigood.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logic untuk tombol login
        binding.btnLogin.setOnClickListener {
            // Navigasi ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Logic untuk teks register account
        binding.registerAccount.setOnClickListener {
            // Navigasi ke RegisterActivity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
