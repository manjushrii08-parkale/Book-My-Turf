package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.payment.RazorpayOrderData
import com.example.bookmyturf.data.model.payment.RazorpayVerifyData
import com.example.bookmyturf.data.model.payment.RazorpayVerifyRequest
import com.example.bookmyturf.data.remote.PaymentApiService

class PaymentRepository(
    private val paymentApiService: PaymentApiService
) {

    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    suspend fun createRazorpayOrder(
        token: String,
        bookingId: Int
    ): Result<RazorpayOrderData> {

        return try {

            val response =
                paymentApiService.createRazorpayOrder(
                    authorization = "Bearer $token",
                    bookingId = bookingId
                )

            if (
                response.success &&
                response.data != null
            ) {

                Result.success(response.data)

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to create Razorpay order."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    // =========================================================

    suspend fun verifyRazorpayPayment(
        token: String,
        bookingId: Int,
        request: RazorpayVerifyRequest
    ): Result<RazorpayVerifyData> {

        return try {

            val response =
                paymentApiService.verifyRazorpayPayment(
                    authorization = "Bearer $token",
                    bookingId = bookingId,
                    request = request
                )

            if (
                response.success &&
                response.data != null
            ) {

                Result.success(response.data)

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to verify Razorpay payment."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}