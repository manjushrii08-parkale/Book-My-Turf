package com.example.bookmyturf.data.model

data class SuperAdminTurf(
    val id: Int,

    val admin_id: Int?,
    val admin_name: String?,
    val admin_email: String?,

    val name: String?,
    val description: String?,
    val location: String?,
    val city: String?,
    val address: String?,

    val sports_types: List<String>?,
    val amenities: List<String>?,
    val image_urls: List<String>?,

    val price: String?,
    val status: String?,

    val opening_time: String?,
    val closing_time: String?,

    val created_at: String?,
    val updated_at: String?
)

data class SuperAdminTurfsResponse(
    val success: Boolean,
    val message: String,
    val data: List<SuperAdminTurf>?
)

data class SuperAdminTurfDetailsResponse(
    val success: Boolean,
    val message: String,
    val data: SuperAdminTurf?
)