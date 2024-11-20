package com.example.nutrigood

import android.content.Intent
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mBtnLogin: Button
    private lateinit var mRegisterAccount: TextView
    private lateinit var mProgressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)
        mBtnLogin = findViewById(R.id.btn_login)
        mRegisterAccount = findViewById(R.id.register_account)
        mProgressBar = findViewById(R.id.progressbarofmainactivity)  // Assuming you add ProgressBar to XML

        // Check if the user is already logged in
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // If the user is logged in, navigate to the main screen
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Login button click listener
        mBtnLogin.setOnClickListener {
            val email = mEmail.text.toString().trim()
            val password = mPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Show progress bar and attempt login
                mProgressBar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        mProgressBar.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            checkEmailVerification()
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Register account click listener
        mRegisterAccount.setOnClickListener {
            // Navigate to the SignUp activity
            startActivity(Intent(this, Register::class.java))
        }
    }

    // Check if the user's email is verified
    private fun checkEmailVerification() {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser != null && firebaseUser.isEmailVerified) {
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Please verify your email first", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }
    }
}