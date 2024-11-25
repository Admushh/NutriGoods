package com.example.nutrigood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.data.response.Product
import com.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormFragment : Fragment() {

    private lateinit var etProductName: EditText
    private lateinit var etSugarContent: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_form, container, false)

        etProductName = binding.findViewById(R.id.et_product_name)
        etSugarContent = binding.findViewById(R.id.et_sugar_content)

        val btnSaveProduct = binding.findViewById<View>(R.id.btn_save_product)
        btnSaveProduct.setOnClickListener {
            saveProduct()
        }

        return binding
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString().trim()
        val sugarContentText = etSugarContent.text.toString().trim()

        if (productName.isEmpty() || sugarContentText.isEmpty()) {
            Toast.makeText(requireContext(), "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show()
            return
        }

        val sugarContent = sugarContentText.toDoubleOrNull()
        if (sugarContent == null) {
            Toast.makeText(requireContext(), "Kandungan gula harus berupa angka", Toast.LENGTH_SHORT).show()
            return
        }

        // Siapkan data produk
        val product = Product(namaProduct = productName, valueProduct = sugarContent)

        // Ambil token (ganti dengan implementasi pengambilan token Anda)
        val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjkyODg2OGRjNDRlYTZhOThjODhiMzkzZDM2NDQ1MTM2NWViYjMwZDgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbG9naW4tcmVnaXN0ZXItbnV0cmlnb29kIiwiYXVkIjoibG9naW4tcmVnaXN0ZXItbnV0cmlnb29kIiwiYXV0aF90aW1lIjoxNzMyNTU5ODE4LCJ1c2VyX2lkIjoiZEY2bFZkVU9YaFdLUmU2allLUFFMN2NRa2FmMSIsInN1YiI6ImRGNmxWZFVPWGhXS1JlNmpZS1BRTDdjUWthZjEiLCJpYXQiOjE3MzI1NTk4MTgsImV4cCI6MTczMjU2MzQxOCwiZW1haWwiOiJhZGltYXMuZmFyaGFuLnB1dHJhbnRvLnRpazIyQG1oc3cucG5qLmFjLmlkIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbImFkaW1hcy5mYXJoYW4ucHV0cmFudG8udGlrMjJAbWhzdy5wbmouYWMuaWQiXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.GilKt0aP8z5SBC05PCXFB_2g4XGQA0w54EyVZXhwd_ALdLp3eDAMOCErU56Aq9UIj7ee_wpxIt0W5EshmAZCb-mXTmlE8nldx2TzCKqSkAuBAUwC1ZDO5h4GmcJpQAsJvfWZkjfHSXMBF45-2h9glU8vebQiP8zdfzfaFll-vxdQ8G55fVPwXC9p6L-jK-D8-XMbdR6uR3s4lFsuxE1LOeyc8JARRdIa9B4nl_avbFPRjDXneA9alZ3hwdpWh3edIvCjF97NaScDPbFc8KSwdOLyLVQq1lBS6woGjyGFEEMcEOTPNvrY4NwQGpRxhM7Xjaqpn9pYJJlQEIbQuFlfLg" // Simpan token setelah login


        val apiService = ApiConfig.getApiService(token)
        apiService.addProduct(product).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                    // Reset form setelah berhasil
                    etProductName.text.clear()
                    etSugarContent.text.clear()
                } else {
                    // Tampilkan pesan error dari server jika ada
                    val errorMessage = response.errorBody()?.string() ?: "Gagal menyimpan produk"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(requireContext(), "Terjadi kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
