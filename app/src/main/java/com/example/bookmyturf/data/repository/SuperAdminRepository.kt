package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.remote.ApiService

class SuperAdminRepository {

    private val api: ApiService
        get() = RetrofitClient.api

    suspend fun getDashboard(token: String) =
        api.getSuperAdminDashboard("Bearer $token")

    suspend fun getUsers(token: String) =
        api.getSuperAdminUsers("Bearer $token")
}