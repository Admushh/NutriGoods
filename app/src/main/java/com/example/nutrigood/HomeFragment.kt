package com.example.nutrigood

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.retrofit.ApiConfig
import com.data.response.Product
import com.data.response.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        productAdapter = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productAdapter

        // Fetch products dari backend
        fetchProducts()

        return view
    }

    private fun fetchProducts() {
        // Ambil token dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            val apiService = ApiConfig.getApiService()
            apiService.getProducts("Bearer $token").enqueue(object : Callback<ProductResponse> {
                override fun onResponse(
                    call: Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {
                    Log.d("HomeFragment", "Response Code: ${response.code()}")
                    if (response.isSuccessful) {
                        val productResponse = response.body()
                        if (productResponse != null && productResponse.data.isNotEmpty()) {
                            productAdapter.setProducts(productResponse.data)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No products available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e("HomeFragment", "Failed to fetch products: ${response.message()}")
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch products",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    Log.e("HomeFragment", "Network error: ${t.message}")
                    Toast.makeText(
                        requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "No token found, please log in", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
