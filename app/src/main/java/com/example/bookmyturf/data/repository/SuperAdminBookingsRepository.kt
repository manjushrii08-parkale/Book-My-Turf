package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.SuperAdminBookingsResponse
import com.example.bookmyturf.data.remote.ApiService

class SuperAdminBookingsRepository(
    private val apiService: ApiService
) {

    suspend fun getSuperAdminBookings(
        authorization: String
    ): SuperAdminBookingsResponse {
        return apiService.getSuperAdminBookings(authorization)
    }
}