package com.example.bookmyturf.data.model.turf

import com.google.gson.annotations.SerializedName

data class Turf(

    val id: Int,

    @SerializedName("admin_id")
    val adminId: Int,

    val name: String,

    val description: String? = null,

    val location: String,

    val city: String,

    val address: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    @SerializedName("sports_types")
    val sportsTypes: List<String> = emptyList(),

    val amenities: List<String> = emptyList(),

    @SerializedName("image_urls")
    val imageUrls: List<String> = emptyList(),

    val price: Double,

    val rating: Double = 0.0,

    @SerializedName("review_count")
    val reviewCount: Int = 0,

    val status: String,

    @SerializedName("opening_time")
    val openingTime: String,

    @SerializedName("closing_time")
    val closingTime: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("slots_count")
    val slotsCount: Int? = null
)