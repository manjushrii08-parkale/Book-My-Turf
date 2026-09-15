package com.example.bookmyturf.data.model

data class SuperAdminUser(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val status: String,
    val date_of_birth: String?,
    val created_at: String
)

data class SuperAdminUsersResponse(
    val success: Boolean,
    val message: String,
    val data: List<SuperAdminUser>
)