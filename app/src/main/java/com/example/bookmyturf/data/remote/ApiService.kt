package com.example.bookmyturf.data.remote

// =========================================================
// AUTH MODELS
// =========================================================

import com.example.bookmyturf.data.model.AdminDashboardResponse
import com.example.bookmyturf.data.model.AdminProfileResponse
import com.example.bookmyturf.data.model.AdminProfileUpdateRequest
import com.example.bookmyturf.data.model.AdminSubscriptionResponse
import com.example.bookmyturf.data.model.LoginResponse
import com.example.bookmyturf.data.model.MeResponse
import com.example.bookmyturf.data.model.OtpResponse
import com.example.bookmyturf.data.model.SendOtpRequest
import com.example.bookmyturf.data.model.SuperAdminDashboardResponse
import com.example.bookmyturf.data.model.SuperAdminUsersResponse
import com.example.bookmyturf.data.model.SuperAdminAdminsResponse
import com.example.bookmyturf.data.model.SuperAdminSubscriptionsResponse
import com.example.bookmyturf.data.model.SuperAdminSubscriptionDetailsResponse
import com.example.bookmyturf.data.model.SuperAdminTurfsResponse
import com.example.bookmyturf.data.model.SuperAdminTurfDetailsResponse
import com.example.bookmyturf.data.model.SuperAdminBookingsResponse
import com.example.bookmyturf.data.model.VerifyOtpRequest
import com.example.bookmyturf.data.model.GenericResponse

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

import com.example.bookmyturf.data.model.booking.AdminBookingResponse
import com.example.bookmyturf.data.model.booking.BookingListResponse
import com.example.bookmyturf.data.model.booking.BookingResponse
import com.example.bookmyturf.data.model.booking.CancelBookingRequest
import com.example.bookmyturf.data.model.booking.CreateBookingRequest

// =========================================================
// REVIEW MODELS
// =========================================================

import com.example.bookmyturf.data.model.review.SubmitReviewRequest
import com.example.bookmyturf.data.model.review.ReviewResponse
import com.example.bookmyturf.data.model.review.SubmitReviewResponse

// =========================================================
// NOTIFICATION MODELS
// =========================================================

import com.example.bookmyturf.data.model.notification.NotificationListResponse
import com.example.bookmyturf.data.model.notification.UnreadCountResponse

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

// =========================================================
// API SERVICE
// =========================================================

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


    // =========================================================
    // RAZORPAY - CREATE ADMIN SUBSCRIPTION ORDER
    // =========================================================

    @POST("api/v1/subscription/create-order")
    suspend fun createSubscriptionOrder(
        @Header("Authorization") authorization: String,
        @Body request: CreateSubscriptionOrderRequest
    ): RazorpaySubscriptionOrderResponse


    // =========================================================
    // RAZORPAY - VERIFY ADMIN SUBSCRIPTION PAYMENT
    // =========================================================

    @POST("api/v1/subscription/verify-payment")
    suspend fun verifySubscriptionPayment(
        @Header("Authorization") authorization: String,
        @Body request: VerifySubscriptionPaymentRequest
    ): AdminSubscriptionResponse


    // =========================================================
    // SUPER ADMIN
    // =========================================================

    @GET("api/v1/super-admin/dashboard")
    suspend fun getSuperAdminDashboard(
        @Header("Authorization") authorization: String
    ): SuperAdminDashboardResponse


    @GET("api/v1/super-admin/users")
    suspend fun getSuperAdminUsers(
        @Header("Authorization") authorization: String
    ): SuperAdminUsersResponse


    @PATCH("api/v1/super-admin/users/{id}/block")
    suspend fun blockSuperAdminUser(
        @Path("id") userId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    @PATCH("api/v1/super-admin/users/{id}/activate")
    suspend fun activateSuperAdminUser(
        @Path("id") userId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    @GET("api/v1/super-admin/admins")
    suspend fun getSuperAdminAdmins(
        @Header("Authorization") authorization: String
    ): SuperAdminAdminsResponse


    @PATCH("api/v1/super-admin/admins/{id}/block")
    suspend fun blockSuperAdminAdmin(
        @Path("id") adminId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    @PATCH("api/v1/super-admin/admins/{id}/activate")
    suspend fun activateSuperAdminAdmin(
        @Path("id") adminId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    @GET("api/v1/super-admin/subscriptions")
    suspend fun getSuperAdminSubscriptions(
        @Header("Authorization") authorization: String
    ): SuperAdminSubscriptionsResponse


    @GET("api/v1/super-admin/subscriptions/{id}")
    suspend fun getSuperAdminSubscriptionDetails(
        @Path("id") subscriptionId: Int,
        @Header("Authorization") authorization: String
    ): SuperAdminSubscriptionDetailsResponse


    // =========================================================
    // SUPER ADMIN TURF MANAGEMENT
    // =========================================================

    @GET("api/v1/super-admin/turfs")
    suspend fun getSuperAdminTurfs(
        @Header("Authorization") authorization: String
    ): SuperAdminTurfsResponse


    @GET("api/v1/super-admin/turfs/{id}")
    suspend fun getSuperAdminTurfDetails(
        @Path("id") turfId: Int,
        @Header("Authorization") authorization: String
    ): SuperAdminTurfDetailsResponse


    @PATCH("api/v1/super-admin/turfs/{id}/block")
    suspend fun blockSuperAdminTurf(
        @Path("id") turfId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    @PATCH("api/v1/super-admin/turfs/{id}/activate")
    suspend fun activateSuperAdminTurf(
        @Path("id") turfId: Int,
        @Header("Authorization") authorization: String
    ): GenericResponse


    // =========================================================
    // SUPER ADMIN BOOKINGS MANAGEMENT
    // =========================================================

    @GET("api/v1/super-admin/bookings")
    suspend fun getSuperAdminBookings(
        @Header("Authorization") authorization: String
    ): SuperAdminBookingsResponse


    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GET("api/v1/admin/dashboard")
    suspend fun getAdminDashboard(
        @Header("Authorization") authorization: String
    ): AdminDashboardResponse


    // =========================================================
    // ADMIN PROFILE
    // =========================================================

    // Get logged-in admin profile
    @GET("api/v1/admin/profile")
    suspend fun getAdminProfile(
        @Header("Authorization") authorization: String
    ): AdminProfileResponse


    // Update logged-in admin profile
    @PUT("api/v1/admin/profile")
    suspend fun updateAdminProfile(
        @Header("Authorization") authorization: String,
        @Body request: AdminProfileUpdateRequest
    ): AdminProfileResponse


    // =========================================================
    // USER FAVORITES
    // =========================================================

    @GET("api/v1/user/favorites")
    suspend fun getFavorites(
        @Header("Authorization") authorization: String
    ): FavoriteListResponse


    @POST("api/v1/user/favorites/{turfId}")
    suspend fun addFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteResponse


    @DELETE("api/v1/user/favorites/{turfId}")
    suspend fun removeFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteResponse


    @GET("api/v1/user/favorites/{turfId}/check")
    suspend fun checkFavorite(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): FavoriteCheckResponse


    // =========================================================
    // USER BOOKINGS
    // =========================================================

    @POST("api/v1/user/bookings")
    suspend fun createBooking(
        @Header("Authorization") authorization: String,
        @Body request: CreateBookingRequest
    ): BookingResponse


    @GET("api/v1/user/bookings")
    suspend fun getMyBookings(
        @Header("Authorization") authorization: String
    ): BookingListResponse


    @POST("api/v1/user/bookings/{bookingId}/cancel")
    suspend fun cancelBooking(
        @Header("Authorization") authorization: String,
        @Path("bookingId") bookingId: Int,
        @Body request: CancelBookingRequest
    ): BookingResponse


    // =========================================================
    // ADMIN TURF MANAGEMENT
    // =========================================================

    @GET("api/v1/admin/turfs")
    suspend fun getAdminTurfs(
        @Header("Authorization") authorization: String
    ): TurfListResponse


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


    @GET("api/v1/admin/turfs/{id}")
    suspend fun getAdminTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): TurfResponse


    @PUT("api/v1/admin/turfs/{id}")
    suspend fun updateTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body request: UpdateTurfRequest
    ): TurfResponse


    @DELETE("api/v1/admin/turfs/{id}")
    suspend fun deleteTurf(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    )


    // =========================================================
    // ADMIN SLOT MANAGEMENT
    // =========================================================

    @GET("api/v1/admin/turfs/{turfId}/slots")
    suspend fun getSlots(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): SlotsResponse


    @POST("api/v1/admin/turfs/{turfId}/slots")
    suspend fun createSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Body request: CreateSlotRequest
    ): SlotResponse


    @PUT("api/v1/admin/turfs/{turfId}/slots/{slotId}")
    suspend fun updateSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int,
        @Body request: UpdateSlotRequest
    ): SlotResponse


    @DELETE("api/v1/admin/turfs/{turfId}/slots/{slotId}")
    suspend fun deleteSlot(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int
    )


    @PATCH("api/v1/admin/turfs/{turfId}/slots/{slotId}/status")
    suspend fun updateSlotStatus(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int,
        @Path("slotId") slotId: Int,
        @Body request: UpdateSlotStatusRequest
    ): SlotResponse


    // =========================================================
    // ADMIN BOOKINGS
    // =========================================================

    @GET("api/v1/admin/bookings")
    suspend fun getAdminBookings(
        @Header("Authorization") authorization: String
    ): AdminBookingResponse


    // =========================================================
    // USER NOTIFICATIONS
    // =========================================================

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Header("Authorization") authorization: String
    ): NotificationListResponse


    @GET("api/v1/notifications/unread-count")
    suspend fun getUnreadNotificationCount(
        @Header("Authorization") authorization: String
    ): UnreadCountResponse


    @PATCH("api/v1/notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") authorization: String,
        @Path("id") notificationId: String
    ): GenericResponse


    @PATCH("api/v1/notifications/read-all")
    suspend fun markAllNotificationsAsRead(
        @Header("Authorization") authorization: String
    ): GenericResponse


    // =========================================================
    // USER REVIEWS
    // =========================================================

    // Submit review for a completed booking
    @POST("api/v1/user/reviews")
    suspend fun submitReview(
        @Header("Authorization") authorization: String,
        @Body request: SubmitReviewRequest
    ): SubmitReviewResponse


    // Get reviews and rating summary for a turf
    @GET("api/v1/user/turfs/{turfId}/reviews")
    suspend fun getTurfReviews(
        @Header("Authorization") authorization: String,
        @Path("turfId") turfId: Int
    ): ReviewResponse
}


/*
|--------------------------------------------------------------------------
| RAZORPAY SUBSCRIPTION REQUEST MODELS
|--------------------------------------------------------------------------
*/

data class CreateSubscriptionOrderRequest(
    val plan: String
)


data class VerifySubscriptionPaymentRequest(
    val razorpay_order_id: String,
    val razorpay_payment_id: String,
    val razorpay_signature: String
)


/*
|--------------------------------------------------------------------------
| RAZORPAY SUBSCRIPTION ORDER RESPONSE
|--------------------------------------------------------------------------
*/

data class RazorpaySubscriptionOrderResponse(
    val success: Boolean,
    val message: String,
    val data: RazorpaySubscriptionOrderData?
)


data class RazorpaySubscriptionOrderData(
    val subscription_id: Int,
    val order_id: String,
    val amount: Double,
    val amount_paise: Int,
    val currency: String,
    val plan: String,
    val days: Int,
    val razorpay_key: String
)

