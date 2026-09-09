package com.example.bookmyturf.data.model.payment

data class RazorpayOrderResponse(
    val success: Boolean,
    val message: String,
    val data: RazorpayOrderData?
)

data class RazorpayOrderData(
    val booking_id: Int,
    val order_id: String,
    val amount: Int,
    val currency: String,
    val key_id: String
)