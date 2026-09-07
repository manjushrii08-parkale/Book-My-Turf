package com.example.bookmyturf.data.remote


import com.example.bookmyturf.data.model.AdminDashboardResponse
import com.example.bookmyturf.data.model.AdminSubscriptionResponse

import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.model.turf.ImageUploadResponse
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest

import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface AdminApiService {

    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GET("api/v1/admin/dashboard")
    suspend fun getAdminDashboard(
        @Header("Authorization") authorization: String
    ): AdminDashboardResponse


    // =========================================================
    // ADMIN SUBSCRIPTION
    // =========================================================

    @GET("api/v1/subscription/status")
    suspend fun getSubscriptionStatus(
        @Header("Authorization") authorization: String
    ): AdminSubscriptionResponse

    @POST("api/v1/subscription/free-trial")
    suspend fun startFreeTrial(
        @Header("Authorization") authorization: String
    ): AdminSubscriptionResponse

    @POST("api/v1/subscription/paid-plan")
    suspend fun choosePaidPlan(
        @Header("Authorization") authorization: String
    ): AdminSubscriptionResponse


    // =========================================================
    // ADMIN TURF MANAGEMENT
    // =========================================================

    // GET ALL MY TURFS

    @GET("api/v1/admin/turfs")
    suspend fun getAdminTurfs(
        @Header("Authorization") authorization: String
    ): TurfListResponse


    // CREATE TURF

    @POST("api/v1/admin/turfs")
    suspend fun createTurf(
        @Header("Authorization") authorization: String,
        @Body request: CreateTurfRequest
    ): TurfResponse


    // GET SINGLE TURF

    @GET("api/v1/admin/turfs/{id}")
    suspend fun getAdminTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): TurfResponse


    // UPDATE TURF

    @PUT("api/v1/admin/turfs/{id}")
    suspend fun updateTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body request: UpdateTurfRequest
    ): TurfResponse


    // DELETE TURF

    @DELETE("api/v1/admin/turfs/{id}")
    suspend fun deleteTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    )


    // =========================================================
    // TURF IMAGE MANAGEMENT
    // =========================================================

    @Multipart
    @POST("api/v1/admin/turfs/upload-images")
    suspend fun uploadTurfImages(
        @Header("Authorization") authorization: String,
        @Part("turf_id") turfId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): ImageUploadResponse


    // =========================================================
    // ADMIN SLOT MANAGEMENT
    // =========================================================

    // GET ALL SLOTS

    @GET("api/v1/admin/turfs/{turfId}/slots")
    suspend fun getSlots(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): SlotsResponse


    // CREATE SLOT

    @POST("api/v1/admin/turfs/{turfId}/slots")
    suspend fun createSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Body request: CreateSlotRequest
    ): SlotResponse


    // UPDATE SLOT

    @PUT("api/v1/admin/turfs/{turfId}/slots/{slotId}")
    suspend fun updateSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int,
        @Body request: UpdateSlotRequest
    ): SlotResponse


    // DELETE SLOT

    @DELETE("api/v1/admin/turfs/{turfId}/slots/{slotId}")
    suspend fun deleteSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int
    )


    // UPDATE SLOT STATUS

    @PATCH("api/v1/admin/turfs/{turfId}/slots/{slotId}/status")
    suspend fun updateSlotStatus(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int,
        @Body request: UpdateSlotStatusRequest
    ): SlotResponse
}