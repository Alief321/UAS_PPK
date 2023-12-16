package com.example.uasppk.service

import com.example.uasppk.model.IPS
import com.example.uasppk.model.Matkul
import com.example.uasppk.model.Nilai
import com.example.uasppk.model.User
import com.example.uasppk.model.request.ChangePasswordRequest
import com.example.uasppk.model.request.LoginRequest
import com.example.uasppk.model.request.NilaiRequest
import com.example.uasppk.model.request.NilaiUpdateRequest
import com.example.uasppk.model.request.RegisterRequest
import com.example.uasppk.model.response.ChangePasswordResponse
import com.example.uasppk.model.response.LoginResponse
import com.example.uasppk.model.response.NilaiIDResponse
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @GET("profile")
    fun profile(@Header("Authorization") authHeader: String): Call<ProfileResponse>

    @DELETE("profile")
    fun deleteAkun(@Header("Authorization") authHeader: String): Call<ProfileResponse>

    @PATCH("changePassword")
    fun changePassword(
        @Header("Authorization") authHeader: String,
        @Body requestBody: ChangePasswordRequest,
    ): Call<ChangePasswordResponse>

    @GET("matkul")
    fun getAllmatkul(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String?,
        @Query("periode") periode: Long?,
    ): Call<List<Matkul>>

    @GET("nilai/mahasiswa/{id}")
    fun getNilaiMahasiswa(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("periode") periode: Long?,
    ): Call<List<Nilai>>

    @GET("ips/mahasiswa")
    fun getIPS(
        @Header("Authorization") authHeader: String,
    ): Call<List<IPS>>

    @GET("user/mahasiswa")
    fun getAllMahasiswa(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String?,
    ): Call<List<User>>

    @POST("nilai")
    fun createNilai(
        @Header("Authorization") authHeader: String,
        @Body requestBody: NilaiRequest,
    ): Call<Nilai>

    @GET("nilai/{id}")
    fun getNilai(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
    ): Call<NilaiIDResponse>

    @PATCH("nilai/edit/{id}")
    fun updateNilai(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Body requestBody: NilaiUpdateRequest,
    ): Call<Nilai>

    @DELETE("nilai/{id}")
    fun deleteNilai(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
    ): Call<Nilai>
}
