package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.GenericResponse
import com.example.bookmyturf.data.model.SuperAdminTurf
import com.example.bookmyturf.data.remote.ApiService

class SuperAdminTurfRepository(
    private val apiService: ApiService
) {

    suspend fun getTurfs(
        authorization: String
    ): Result<List<SuperAdminTurf>> {
        return try {
            val response = apiService.getSuperAdminTurfs(authorization)

            if (response.success) {
                Result.success(response.data.orEmpty())
            } else {
                Result.failure(
                    Exception(response.message)
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTurfDetails(
        turfId: Int,
        authorization: String
    ): Result<SuperAdminTurf> {
        return try {
            val response = apiService.getSuperAdminTurfDetails(
                turfId = turfId,
                authorization = authorization
            )

            val turf = response.data

            if (response.success && turf != null) {
                Result.success(turf)
            } else {
                Result.failure(
                    Exception(response.message)
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun blockTurf(
        turfId: Int,
        authorization: String
    ): Result<GenericResponse> {
        return try {
            val response = apiService.blockSuperAdminTurf(
                turfId = turfId,
                authorization = authorization
            )

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(
                    Exception(response.message)
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun activateTurf(
        turfId: Int,
        authorization: String
    ): Result<GenericResponse> {
        return try {
            val response = apiService.activateSuperAdminTurf(
                turfId = turfId,
                authorization = authorization
            )

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(
                    Exception(response.message)
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}