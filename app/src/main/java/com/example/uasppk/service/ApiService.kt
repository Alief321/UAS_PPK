package com.example.uasppk.service
import com.example.uasppk.model.response.LoginResponse
import com.example.uasppk.model.User
import com.example.uasppk.model.request.LoginRequest
import com.example.uasppk.model.request.RegisterRequest
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @GET("profile")
    fun profile(@Header("Authorization") authHeader: String): Call<ProfileResponse>

}
