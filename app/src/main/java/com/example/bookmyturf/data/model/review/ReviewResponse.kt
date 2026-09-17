package com.example.bookmyturf.data.model.review

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val success: Boolean,
    val message: String,
    val data: ReviewData?
)

data class ReviewData(
    @SerializedName("turf_id")
    val turfId: Int,

    @SerializedName("average_rating")
    val averageRating: Double,

    @SerializedName("total_reviews")
    val totalReviews: Int,

    val reviews: List<Review>
)

data class SubmitReviewResponse(
    val success: Boolean,
    val message: String,
    val data: SubmitReviewData?
)

data class SubmitReviewData(
    val review: Review?
)
