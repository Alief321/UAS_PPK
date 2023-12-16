package com.example.uasppk.client

import com.example.uasppk.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // ubah sesuai url api anda (konteksnya disini pakai ip karena webservice masih di local
    private const val BASE_URL = "http://192.168.254.69:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
