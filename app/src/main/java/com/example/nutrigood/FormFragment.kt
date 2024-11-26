package com.example.nutrigood

import android.content.Context
import android.widget.Toast
import com.data.response.Product
import com.data.retrofit.ApiConfig
import kotlin.text.isEmpty
import kotlin.text.toDoubleOrNull
import kotlin.text.trim

class FormFragment : androidx.fragment.app.Fragment() {

    private lateinit var etProductName: android.widget.EditText
    private lateinit var etSugarContent: android.widget.EditText

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        val binding = inflater.inflate(com.example.nutrigood.R.layout.fragment_form, container, false)

        etProductName = binding.findViewById(com.example.nutrigood.R.id.et_product_name)
        etSugarContent = binding.findViewById(com.example.nutrigood.R.id.et_sugar_content)

        val btnSaveProduct = binding.findViewById<android.view.View>(com.example.nutrigood.R.id.btn_save_product)
        btnSaveProduct.setOnClickListener {
            saveProduct()
        }

        return binding
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString().trim()
        val sugarContentText = etSugarContent.text.toString().trim()

        if (productName.isEmpty() || sugarContentText.isEmpty()) {
            android.widget.Toast.makeText(requireContext(), "Harap lengkapi semua kolom", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val sugarContent = sugarContentText.toDoubleOrNull()
        if (sugarContent == null) {
            android.widget.Toast.makeText(requireContext(), "Kandungan gula harus berupa angka", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val product =
            com.data.response.Product(namaProduct = productName, valueProduct = sugarContent)

        val sharedPreferences = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        val apiService = ApiConfig.getApiService()
        apiService.addProduct("Bearer $token", product).enqueue(object :
            retrofit2.Callback<com.data.response.Product> {
            override fun onResponse(call: retrofit2.Call<com.data.response.Product>, response: retrofit2.Response<com.data.response.Product>) {
                if (response.isSuccessful) {
                    android.widget.Toast.makeText(requireContext(), "Product saved successfully", android.widget.Toast.LENGTH_SHORT).show()
                    // Reset input setelah berhasil
                    etProductName.text.clear()
                    etSugarContent.text.clear()
                } else {
                    android.widget.Toast.makeText(requireContext(), "Failed to save product: ${response.message()}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<com.data.response.Product>, t: Throwable) {
                android.widget.Toast.makeText(requireContext(), "Network error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        })
    }
}
