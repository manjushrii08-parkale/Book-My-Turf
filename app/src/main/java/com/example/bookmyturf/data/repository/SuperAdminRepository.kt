package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.SuperAdminDashboardResponse
import com.example.bookmyturf.data.remote.ApiService

class SuperAdminRepository(
    private val apiService: ApiService
) {

    // =========================================================
    // GET SUPER ADMIN DASHBOARD
    // =========================================================

    suspend fun getDashboard(
        token: String
    ): SuperAdminDashboardResponse {

        return apiService.getSuperAdminDashboard(
            authorization = "Bearer $token"
        )
    }
}