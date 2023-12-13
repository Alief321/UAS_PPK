package com.example.uasppk

import com.example.uasppk.service.QuotesAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuotesAPIClient {
    private const val QUOT_URL = "https://zenquotes.io/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(QUOT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: QuotesAPIService = retrofit.create(QuotesAPIService::class.java)
}