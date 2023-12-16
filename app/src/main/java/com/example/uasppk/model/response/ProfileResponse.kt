package com.example.uasppk.model.response

data class ProfileResponse(
    val name: String,
    val email: String,
    val nim: String,
    val kelas: String,
    val nip: String,
    val matkulAmpu: List<String>,

)
