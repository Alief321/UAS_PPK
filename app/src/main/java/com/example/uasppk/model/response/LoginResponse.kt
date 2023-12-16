package com.example.uasppk.model.response

data class LoginResponse(
    val token: String,
    val name: String,
    val email: String,
    val id: Int,
    val roles: List<String>,
)
