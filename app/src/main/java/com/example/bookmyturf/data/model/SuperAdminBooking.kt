package com.example.bookmyturf.data.model

import com.google.gson.annotations.SerializedName

// =========================================================
// SUPER ADMIN BOOKINGS RESPONSE
// =========================================================

data class SuperAdminBookingsResponse(
    val success: Boolean,
    val message: String,
    val data: List<SuperAdminBooking>
)

// =========================================================
// SUPER ADMIN BOOKING
// =========================================================

data class SuperAdminBooking(
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("turf_id")
    val turfId: Int,

    @SerializedName("slot_id")
    val slotId: Int,

    @SerializedName("booking_date")
    val bookingDate: String,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("payment_id")
    val paymentId: String?,

    @SerializedName("payment_status")
    val paymentStatus: String,

    @SerializedName("booking_status")
    val bookingStatus: String,

    @SerializedName("cancelled_at")
    val cancelledAt: String?,

    @SerializedName("cancellation_reason")
    val cancellationReason: String?,

    @SerializedName("refund_status")
    val refundStatus: String,

    @SerializedName("refunded_at")
    val refundedAt: String?,

    @SerializedName("booked_at")
    val bookedAt: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    val user: SuperAdminBookingUser?,
    val turf: SuperAdminBookingTurf?,
    val slot: SuperAdminBookingSlot?
)

// =========================================================
// USER DETAILS
// =========================================================

data class SuperAdminBookingUser(
    val id: Int,
    val name: String?,
    val email: String?
)

// =========================================================
// TURF DETAILS
// =========================================================

data class SuperAdminBookingTurf(
    val id: Int,
    val name: String?,
    val city: String?
)

// =========================================================
// SLOT DETAILS
// =========================================================

data class SuperAdminBookingSlot(
    val id: Int,

    @SerializedName("turf_id")
    val turfId: Int,

    @SerializedName("admin_id")
    val adminId: Int,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    val price: Double,

    val status: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)