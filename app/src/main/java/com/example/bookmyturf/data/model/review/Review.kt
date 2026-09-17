
package com.example.bookmyturf.data.model.review

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("turf_id")
    val turfId: Int,

    @SerializedName("booking_id")
    val bookingId: Int,

    val rating: Int,

    val comment: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    val user: ReviewUser?
)

data class ReviewUser(
    val id: Int,
    val name: String
)
