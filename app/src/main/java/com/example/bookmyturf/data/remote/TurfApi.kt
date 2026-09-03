package com.example.bookmyturf.data.remote

import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

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
// GET ALL SLOTS FOR USER TURF
// =========================================================

    @GET("api/v1/user/turfs/{turfId}/slots")
    suspend fun getSlots(
        @Path("turfId") turfId: Int
    ): Response<SlotsResponse>

}