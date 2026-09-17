package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.review.ReviewResponse
import com.example.bookmyturf.data.model.review.SubmitReviewRequest
import com.example.bookmyturf.data.model.review.SubmitReviewResponse
import com.example.bookmyturf.data.remote.ApiService

class ReviewRepository(
    private val apiService: ApiService
) {

    // =====================================================
    // SUBMIT REVIEW
    // =====================================================

    suspend fun submitReview(
        token: String,
        bookingId: Int,
        rating: Int,
        comment: String?
    ): Result<SubmitReviewResponse> {

        return try {

            val response = apiService.submitReview(
                authorization = "Bearer $token",
                request = SubmitReviewRequest(
                    bookingId = bookingId,
                    rating = rating,
                    comment = comment
                )
            )

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(
                    Exception(
                        response.message
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message
                        ?: "Unable to submit review."
                )
            )
        }
    }


    // =====================================================
    // GET TURF REVIEWS
    // =====================================================

    suspend fun getTurfReviews(
        token: String,
        turfId: Int
    ): Result<ReviewResponse> {

        return try {

            val response = apiService.getTurfReviews(
                authorization = "Bearer $token",
                turfId = turfId
            )

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(
                    Exception(
                        response.message
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message
                        ?: "Unable to load reviews."
                )
            )
        }
    }
}

