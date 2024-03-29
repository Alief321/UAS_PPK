package com.example.uasppk.service

import com.example.uasppk.model.Quotes
import retrofit2.Call
import retrofit2.http.GET

interface QuotesAPIService {

    @GET("quotes")
    fun getQuotes(): Call<Quotes>
}