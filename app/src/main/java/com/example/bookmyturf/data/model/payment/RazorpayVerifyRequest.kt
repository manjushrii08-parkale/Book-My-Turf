package com.example.bookmyturf.data.model.payment

data class RazorpayVerifyRequest(
    val razorpay_order_id: String,
    val razorpay_payment_id: String,
    val razorpay_signature: String
)