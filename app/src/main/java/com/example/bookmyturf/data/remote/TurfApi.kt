package com.example.bookmyturf.data.remote

import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.profile.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.bookmyturf.data.model.profile.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.PUT
interface TurfApi {

    // =========================================================
    // GET ALL ACTIVE TURFS
    // =========================================================

    @GET("api/v1/user/turfs")
    suspend fun getTurfs(): Response<TurfListResponse>


    // =========================================================
    // GET SINGLE TURF BY ID
    // =========================================================

    @GET("api/v1/user/turfs/{id}")
    suspend fun getTurfById(
        @Path("id") turfId: Int
    ): Response<TurfResponse>


    // =========================================================
    // GET DATE-WISE SLOTS FOR USER TURF
    // =========================================================

    @GET("api/v1/user/turfs/{turfId}/slots")
    suspend fun getSlots(
        @Path("turfId") turfId: Int,
        @Query("date") bookingDate: String
    ): Response<SlotsResponse>


    // =========================================================
// GET LOGGED-IN USER PROFILE
// =========================================================

    @GET("api/v1/user/profile")
    suspend fun getUserProfile(): Response<UserProfileResponse>

    // =========================================================
// UPDATE LOGGED-IN USER PROFILE
// =========================================================

    @PUT("api/v1/user/profile")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequest
    ): Response<UserProfileResponse>
}