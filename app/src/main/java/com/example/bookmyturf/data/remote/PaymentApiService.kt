package com.example.bookmyturf.data.remote

import com.example.bookmyturf.data.model.payment.RazorpayOrderResponse
import com.example.bookmyturf.data.model.payment.RazorpayVerifyRequest
import com.example.bookmyturf.data.model.payment.RazorpayVerifyResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {

    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    @POST("api/v1/user/bookings/{bookingId}/payment/order")
    suspend fun createRazorpayOrder(
        @Header("Authorization")
        authorization: String,

        @Path("bookingId")
        bookingId: Int
    ): RazorpayOrderResponse


    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    // =========================================================

    @POST("api/v1/user/bookings/{bookingId}/payment/verify")
    suspend fun verifyRazorpayPayment(
        @Header("Authorization")
        authorization: String,

        @Path("bookingId")
        bookingId: Int,

        @Body
        request: RazorpayVerifyRequest
    ): RazorpayVerifyResponse
}