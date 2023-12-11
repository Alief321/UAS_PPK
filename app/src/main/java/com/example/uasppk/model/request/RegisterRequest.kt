package com.example.uasppk.model.request

data class RegisterRequest(
    val nim: String,
    val email: String,
    val name: String,
    val password: String
)
