package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.payment.RazorpayOrderData
import com.example.bookmyturf.data.model.payment.RazorpayVerifyData
import com.example.bookmyturf.data.model.payment.RazorpayVerifyRequest
import com.example.bookmyturf.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    // =========================================================
    // RAZORPAY ORDER
    // =========================================================

    private val _order =
        MutableStateFlow<RazorpayOrderData?>(null)

    val order: StateFlow<RazorpayOrderData?> =
        _order


    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading


    // =========================================================
    // ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error


    // =========================================================
    // PAYMENT VERIFICATION
    // =========================================================

    private val _verificationResult =
        MutableStateFlow<RazorpayVerifyData?>(null)

    val verificationResult: StateFlow<RazorpayVerifyData?> =
        _verificationResult


    private val _isVerifying =
        MutableStateFlow(false)

    val isVerifying: StateFlow<Boolean> =
        _isVerifying


    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    fun createRazorpayOrder(
        token: String,
        bookingId: Int
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _order.value = null

            val result =
                paymentRepository.createRazorpayOrder(
                    token = token,
                    bookingId = bookingId
                )

            result
                .onSuccess { orderData ->

                    _order.value = orderData

                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to create payment order."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    // =========================================================

    fun verifyRazorpayPayment(
        token: String,
        bookingId: Int,
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String
    ) {

        viewModelScope.launch {

            _isVerifying.value = true
            _error.value = null
            _verificationResult.value = null

            val request =
                RazorpayVerifyRequest(
                    razorpay_order_id =
                        razorpayOrderId,

                    razorpay_payment_id =
                        razorpayPaymentId,

                    razorpay_signature =
                        razorpaySignature
                )

            val result =
                paymentRepository.verifyRazorpayPayment(
                    token = token,
                    bookingId = bookingId,
                    request = request
                )

            result
                .onSuccess { verificationData ->

                    _verificationResult.value =
                        verificationData
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Payment verification failed."
                }

            _isVerifying.value = false
        }
    }


    // =========================================================
    // CLEAR ORDER
    // =========================================================

    fun clearOrder() {

        _order.value = null
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }


    // =========================================================
    // CLEAR VERIFICATION RESULT
    // =========================================================

    fun clearVerificationResult() {

        _verificationResult.value = null
    }
}