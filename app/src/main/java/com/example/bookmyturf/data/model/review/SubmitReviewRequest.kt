package com.example.bookmyturf.data.model.review

import com.google.gson.annotations.SerializedName

data class SubmitReviewRequest(
    @SerializedName("booking_id")
    val bookingId: Int,

    val rating: Int,

    val comment: String?
)

