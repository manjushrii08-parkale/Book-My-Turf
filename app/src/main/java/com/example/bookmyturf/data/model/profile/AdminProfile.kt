package com.example.bookmyturf.data.model

data class AdminProfileResponse(
    val success: Boolean,
    val message: String,
    val data: AdminProfileData? = null
)

data class AdminProfileData(
    val admin: AdminProfile? = null
)

data class AdminProfile(
    val id: Int,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String? = null,
    val status: String? = null
)

data class AdminProfileUpdateRequest(
    val name: String,
    val phone: String?
)

