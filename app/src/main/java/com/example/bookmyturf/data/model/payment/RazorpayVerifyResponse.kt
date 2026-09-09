package com.example.bookmyturf.data.model.payment

data class RazorpayVerifyResponse(
    val success: Boolean,
    val message: String,
    val data: RazorpayVerifyData?
)

data class RazorpayVerifyData(
    val booking_id: Int,
    val payment_id: String?,
    val payment_status: String,
    val booking_status: String
)