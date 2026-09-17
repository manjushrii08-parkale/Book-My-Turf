package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.review.Review
import com.example.bookmyturf.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,

    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val reviews: List<Review> = emptyList(),

    val submitSuccess: Boolean = false,

    val errorMessage: String? = null
)

class ReviewViewModel(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReviewUiState()
    )

    val uiState: StateFlow<ReviewUiState> =
        _uiState.asStateFlow()


    // =====================================================
    // LOAD TURF REVIEWS
    // =====================================================

    fun loadTurfReviews(
        token: String,
        turfId: Int
    ) {

        if (turfId <= 0) {
            _uiState.value = ReviewUiState(
                errorMessage = "Invalid turf."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            repository.getTurfReviews(
                token = token,
                turfId = turfId
            )
                .onSuccess { response ->

                    val data = response.data

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        averageRating =
                            data?.averageRating ?: 0.0,
                        totalReviews =
                            data?.totalReviews ?: 0,
                        reviews =
                            data?.reviews ?: emptyList(),
                        errorMessage = null
                    )
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            exception.message
                                ?: "Unable to load reviews."
                    )
                }
        }
    }


    // =====================================================
    // SUBMIT REVIEW
    // =====================================================

    fun submitReview(
        token: String,
        bookingId: Int,
        rating: Int,
        comment: String?
    ) {

        if (bookingId <= 0) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Invalid booking."
            )
            return
        }

        if (rating !in 1..5) {
            _uiState.value = _uiState.value.copy(
                errorMessage =
                    "Please select a rating from 1 to 5."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                submitSuccess = false,
                errorMessage = null
            )

            repository.submitReview(
                token = token,
                bookingId = bookingId,
                rating = rating,
                comment = comment
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
            )
                .onSuccess {

                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submitSuccess = true,
                        errorMessage = null
                    )
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submitSuccess = false,
                        errorMessage =
                            exception.message
                                ?: "Unable to submit review."
                    )
                }
        }
    }


    // =====================================================
    // CLEAR ERROR
    // =====================================================

    fun clearError() {

        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }


    // =====================================================
    // CLEAR SUBMIT SUCCESS
    // =====================================================

    fun clearSubmitSuccess() {

        _uiState.value = _uiState.value.copy(
            submitSuccess = false
        )
    }
}

