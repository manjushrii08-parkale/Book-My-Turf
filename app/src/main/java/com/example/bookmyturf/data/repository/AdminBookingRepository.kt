package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.booking.AdminBookingResponse
import com.example.bookmyturf.data.remote.ApiService

class AdminBookingRepository(
    private val api: ApiService
) {

    // =========================================================
    // GET ADMIN BOOKINGS
    // =========================================================

    suspend fun getAdminBookings(
        token: String
    ): AdminBookingResponse {

        return api.getAdminBookings(
            authorization = "Bearer $token"
        )
    }
}