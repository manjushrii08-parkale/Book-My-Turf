package com.example.bookmyturf.data.model

data class MeResponse(
    val success: Boolean,
    val data: MeData
)

data class MeData(
    val user: User
)