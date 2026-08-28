package com.example.bookmyturf.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)