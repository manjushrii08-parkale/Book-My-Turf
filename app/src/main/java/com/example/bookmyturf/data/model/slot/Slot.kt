package com.example.bookmyturf.data.model.slot

import com.google.gson.annotations.SerializedName

data class Slot(

    val id: Int,

    @SerializedName("turf_id")
    val turfId: Int,

    @SerializedName("admin_id")
    val adminId: Int,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    val price: Double,

    val status: String,

    @SerializedName("booking_id")
    val bookingId: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)