package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.data.model.booking.BookingResponse
import com.example.bookmyturf.data.model.booking.CreateBookingRequest
import com.example.bookmyturf.data.remote.RetrofitClient

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

            val request = CreateBookingRequest(
                slot_id = slotId,
                booking_date = bookingDate
            )

            val response: BookingResponse =
                bookingApi.createBooking(
                    authorization = "Bearer $token",
                    request = request
                )

            if (response.success) {

                val booking =
                    response.data?.booking

                if (booking != null) {

                    Result.success(booking)

                } else {

                    Result.failure(
                        Exception(
                            "Booking data not found."
                        )
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to create booking."
                        }
                    )
                )
            }

        } catch (e: Exception) {

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
}

