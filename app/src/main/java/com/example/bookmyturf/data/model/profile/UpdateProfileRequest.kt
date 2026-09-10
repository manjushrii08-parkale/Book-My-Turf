package com.example.bookmyturf.data.model.profile


import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    val name: String,
    val phone: String?,

    @SerializedName("date_of_birth")
    val dateOfBirth: String?
)