package com.example.bookmyturf.data.remote

import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
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
    // GET SINGLE TURF
    // =========================================================

    @GET("api/v1/user/turfs/{id}")
    suspend fun getTurfById(
        @Path("id") id: Int
    ): Response<TurfResponse>
}