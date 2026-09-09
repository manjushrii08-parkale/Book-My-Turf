package com.example.bookmyturf.data.remote

// =========================================================
// AUTH MODELS
// =========================================================

import com.example.bookmyturf.data.model.AdminDashboardResponse
import com.example.bookmyturf.data.model.AdminSubscriptionResponse
import com.example.bookmyturf.data.model.LoginResponse
import com.example.bookmyturf.data.model.MeResponse
import com.example.bookmyturf.data.model.OtpResponse
import com.example.bookmyturf.data.model.SendOtpRequest
import com.example.bookmyturf.data.model.SuperAdminDashboardResponse
import com.example.bookmyturf.data.model.VerifyOtpRequest
import okhttp3.RequestBody
// =========================================================
// TURF MODELS
// =========================================================

import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.model.turf.ImageUploadResponse
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest

// =========================================================
// SLOT MODELS
// =========================================================

import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest

// =========================================================
// FAVORITE MODELS
// =========================================================

import com.example.bookmyturf.data.model.favorite.FavoriteCheckResponse
import com.example.bookmyturf.data.model.favorite.FavoriteListResponse
import com.example.bookmyturf.data.model.favorite.FavoriteResponse

// =========================================================
// BOOKING MODELS
// =========================================================

import com.example.bookmyturf.data.model.booking.BookingListResponse
import com.example.bookmyturf.data.model.booking.BookingResponse
import com.example.bookmyturf.data.model.booking.CreateBookingRequest
import com.example.bookmyturf.data.model.booking.CancelBookingRequest
// =========================================================
// RETROFIT
// =========================================================

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


interface ApiService {

    // =========================================================
    // AUTHENTICATION
    // =========================================================

    @POST("api/v1/auth/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): OtpResponse


    @POST("api/v1/auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): LoginResponse


    @GET("api/v1/auth/me")
    suspend fun getMe(
        @Header("Authorization") authorization: String
    ): MeResponse


    @POST("api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String
    )


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
    // SUPER ADMIN
    // =========================================================

    @GET("api/v1/super-admin/dashboard")
    suspend fun getSuperAdminDashboard(
        @Header("Authorization") authorization: String
    ): SuperAdminDashboardResponse


    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GET("api/v1/admin/dashboard")
    suspend fun getAdminDashboard(
        @Header("Authorization") authorization: String
    ): AdminDashboardResponse


    // =========================================================
    // USER FAVORITES
    // =========================================================

    // GET ALL FAVORITE TURFS

    @GET("api/v1/user/favorites")
    suspend fun getFavorites(
        @Header("Authorization") authorization: String
    ): FavoriteListResponse


    // ADD TURF TO FAVORITES

    @POST("api/v1/user/favorites/{turfId}")
    suspend fun addFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteResponse


    // REMOVE TURF FROM FAVORITES

    @DELETE("api/v1/user/favorites/{turfId}")
    suspend fun removeFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteResponse


    // CHECK FAVORITE STATUS

    @GET("api/v1/user/favorites/{turfId}/check")
    suspend fun checkFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteCheckResponse

// =========================================================
// USER BOOKINGS
// =========================================================

// CREATE BOOKING

    @POST("api/v1/user/bookings")
    suspend fun createBooking(
        @Header("Authorization") authorization: String,
        @Body request: CreateBookingRequest
    ): BookingResponse


// GET MY BOOKINGS

    @GET("api/v1/user/bookings")
    suspend fun getMyBookings(
        @Header("Authorization") authorization: String
    ): BookingListResponse


    // CANCEL BOOKING
    @POST("api/v1/user/bookings/{bookingId}/cancel")
    suspend fun cancelBooking(
        @Header("Authorization")
        authorization: String, @Path("bookingId")
        bookingId: Int, @Body request: CancelBookingRequest
    ): BookingResponse




    // =========================================================
    // ADMIN TURF MANAGEMENT
    // =========================================================

    // GET ALL TURFS

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


    // =========================================================
// UPLOAD TURF IMAGES
// =========================================================

    @Multipart
    @POST("api/v1/admin/turfs/upload-images")
    suspend fun uploadTurfImages(
        @Header("Authorization") authorization: String,
        @Part("turf_id") turfId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): ImageUploadResponse


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
    // ADMIN SLOT MANAGEMENT
    // =========================================================

    // GET ALL SLOTS FOR A TURF

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

