package com.example.uasppk.model.request

data class ChangePasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String,

)
