package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.GenericResponse
import com.example.bookmyturf.data.remote.ApiService
import com.example.bookmyturf.data.remote.RetrofitClient

class SuperAdminRepository {

    private val api: ApiService
        get() = RetrofitClient.api

    suspend fun getDashboard(token: String) =
        api.getSuperAdminDashboard("Bearer $token")

    suspend fun getUsers(token: String) =
        api.getSuperAdminUsers("Bearer $token")

    suspend fun getAdmins(token: String) =
        api.getSuperAdminAdmins("Bearer $token")

    suspend fun blockUser(
        token: String,
        userId: Int
    ): GenericResponse {
        return api.blockSuperAdminUser(
            userId = userId,
            authorization = "Bearer $token"
        )
    }

    suspend fun activateUser(
        token: String,
        userId: Int
    ): GenericResponse {
        return api.activateSuperAdminUser(
            userId = userId,
            authorization = "Bearer $token"
        )
    }

    suspend fun blockAdmin(
        token: String,
        adminId: Int
    ): GenericResponse {
        return api.blockSuperAdminAdmin(
            adminId = adminId,
            authorization = "Bearer $token"
        )
    }

    suspend fun activateAdmin(
        token: String,
        adminId: Int
    ): GenericResponse {
        return api.activateSuperAdminAdmin(
            adminId = adminId,
            authorization = "Bearer $token"
        )
    }

    suspend fun getSubscriptions(token: String) =
        api.getSuperAdminSubscriptions("Bearer $token")

    suspend fun getSubscriptionDetails(
        token: String,
        subscriptionId: Int
    ) =
        api.getSuperAdminSubscriptionDetails(
            subscriptionId = subscriptionId,
            authorization = "Bearer $token"
        )
}