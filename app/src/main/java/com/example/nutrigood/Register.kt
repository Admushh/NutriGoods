package com.example.nutrigood

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Register : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etAge: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var spinnerDiabetes: Spinner
    private lateinit var btnSubmit: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize views
        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etAge = findViewById(R.id.et_age)
        etPassword = findViewById(R.id.et_confirm_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        spinnerDiabetes = findViewById(R.id.spinner_diabetes)
        btnSubmit = findViewById(R.id.btn_submit)

        // Set up spinner options from resources
        val diabetesOptions = resources.getStringArray(R.array.diabetes_options)
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, // Layout for spinner items
            diabetesOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiabetes.adapter = adapter

        // Set up submit button listener
        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val age = etAge.text.toString().trim()
            val diabetesStatus = spinnerDiabetes.selectedItem.toString()

            // Validate inputs
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (password.length < 7) {
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    sendEmailVerification()
                } else {
                    Toast.makeText(this, "Failed to Register", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification() {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        firebaseUser?.let { user ->
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent. Please verify and log in again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    firebaseAuth.signOut()
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}