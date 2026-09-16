package com.example.bookmyturf.data.model

data class SuperAdminAdmin(
    val id: Int,
    val name: String?,
    val email: String?,
    val phone: String?,
    val status: String?,
    val created_at: String?
)

data class SuperAdminAdminsResponse(
    val success: Boolean,
    val message: String,
    val data: List<SuperAdminAdmin>?
)