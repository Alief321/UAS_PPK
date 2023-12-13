package com.example.uasppk

import com.example.uasppk.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object ApiClient {
//    private const val BASE_URL = "http://192.168.1.4:8080"
    private const val BASE_URL = "http://192.168.213.69:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}