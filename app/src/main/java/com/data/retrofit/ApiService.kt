package com.data.retrofit

import com.data.response.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("products")
    fun addProduct(@Body product: Product): Call<Product>

    @GET("events/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>

}

