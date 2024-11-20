package com.example.nutrigood

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Mendapatkan referensi ke Spinner setelah setContentView
        val spinnerDiabetes: Spinner = findViewById(R.id.spinner_diabetes)

        // Mengambil array pilihan dari resources
        val diabetesOptions = resources.getStringArray(R.array.diabetes_options)

        // Membuat ArrayAdapter untuk Spinner
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, // Layout untuk item spinner
            diabetesOptions
        )

        // Mengatur layout dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Menetapkan adapter ke spinner
        spinnerDiabetes.adapter = adapter
    }
}
