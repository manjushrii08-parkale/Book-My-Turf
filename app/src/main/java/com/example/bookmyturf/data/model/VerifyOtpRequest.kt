package com.example.bookmyturf.data.model

data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val role: String
)