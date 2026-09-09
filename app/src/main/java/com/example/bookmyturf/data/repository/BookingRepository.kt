package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.data.model.booking.BookingResponse
import com.example.bookmyturf.data.model.booking.CancelBookingRequest
import com.example.bookmyturf.data.model.booking.CreateBookingRequest
import com.example.bookmyturf.data.remote.RetrofitClient
import android.util.Log
class BookingRepository {

    // =========================================================
    // RETROFIT API
    // =========================================================

    private val bookingApi =
        RetrofitClient.api


    // =========================================================
    // CREATE BOOKING
    // =========================================================

    suspend fun createBooking(
        token: String,
        slotId: Int,
        bookingDate: String
    ): Result<Booking> {

        return try {

            Log.d("BOOKING_API", "================================")
            Log.d("BOOKING_API", "CREATE BOOKING STARTED")
            Log.d("BOOKING_API", "Slot ID = $slotId")
            Log.d("BOOKING_API", "Booking Date = $bookingDate")
            Log.d("BOOKING_API", "Token exists = ${token.isNotBlank()}")

            val request = CreateBookingRequest(
                slot_id = slotId,
                booking_date = bookingDate
            )

            Log.d("BOOKING_API", "Sending request to Laravel...")

            val response: BookingResponse =
                bookingApi.createBooking(
                    authorization = "Bearer $token",
                    request = request
                )

            Log.d("BOOKING_API", "Response received")
            Log.d("BOOKING_API", "Success = ${response.success}")
            Log.d("BOOKING_API", "Message = ${response.message}")

            if (response.success) {

                val booking =
                    response.data?.booking

                if (booking != null) {

                    Log.d(
                        "BOOKING_API",
                        "Booking created successfully. ID = ${booking.id}"
                    )

                    Log.d("BOOKING_API", "================================")

                    Result.success(booking)

                } else {

                    Log.e(
                        "BOOKING_API",
                        "Booking data is null"
                    )

                    Log.d("BOOKING_API", "================================")

                    Result.failure(
                        Exception("Booking data not found.")
                    )
                }

            } else {

                Log.e(
                    "BOOKING_API",
                    "Laravel returned success=false"
                )

                Log.e(
                    "BOOKING_API",
                    "Message = ${response.message}"
                )

                Log.d("BOOKING_API", "================================")

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to create booking."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "BOOKING_API",
                "================================"
            )

            Log.e(
                "BOOKING_API",
                "CREATE BOOKING FAILED"
            )

            Log.e(
                "BOOKING_API",
                "Exception Type = ${e::class.java.name}"
            )

            Log.e(
                "BOOKING_API",
                "Exception Message = ${e.message}"
            )

            Log.e(
                "BOOKING_API",
                "Cause = ${e.cause}"
            )

            e.printStackTrace()

            Log.e(
                "BOOKING_API",
                "================================"
            )

            Result.failure(e)
        }
    }

    // =========================================================
    // GET MY BOOKINGS
    // =========================================================

    suspend fun getMyBookings(
        token: String
    ): Result<List<Booking>> {

        return try {

            val response =
                bookingApi.getMyBookings(
                    authorization =
                        "Bearer $token"
                )

            if (response.success) {

                Result.success(
                    response.data?.bookings
                        ?: emptyList()
                )

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to load bookings."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    suspend fun cancelBooking(
        token: String,
        bookingId: Int,
        reason: String? = null
    ): Result<Booking> {

        return try {

            // -------------------------------------------------
            // CREATE CANCEL REQUEST
            // -------------------------------------------------

            val request =
                CancelBookingRequest(
                    reason = reason
                )

            // -------------------------------------------------
            // CALL API
            // -------------------------------------------------

            val response: BookingResponse =
                bookingApi.cancelBooking(
                    authorization =
                        "Bearer $token",

                    bookingId =
                        bookingId,

                    request =
                        request
                )

            // -------------------------------------------------
            // CHECK RESPONSE
            // -------------------------------------------------

            if (response.success) {

                val booking =
                    response.data?.booking

                if (booking != null) {

                    Result.success(
                        booking
                    )

                } else {

                    Result.failure(
                        Exception(
                            "Cancelled booking data not found."
                        )
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to cancel booking."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}

