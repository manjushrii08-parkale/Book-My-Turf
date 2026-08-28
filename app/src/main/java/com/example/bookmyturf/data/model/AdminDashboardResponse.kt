package com.example.bookmyturf.data.model

import com.google.gson.annotations.SerializedName

data class AdminDashboardResponse(
    val success: Boolean,
    val message: String,
    val data: AdminDashboardData?
)

data class AdminDashboardData(
    val admin: AdminDashboardUser?,
    val subscription: AdminSubscription?,
    val statistics: AdminDashboardStatistics?
)

data class AdminDashboardUser(
    val id: Int,
    val name: String?,
    val email: String?,
    val status: String?
)

data class AdminSubscription(

    val plan: String?,

    val status: String?,

    @SerializedName("started_at")
    val startedAt: String?,

    @SerializedName("expires_at")
    val expiresAt: String?,

    val amount: Double?,

    @SerializedName("is_trial")
    val isTrial: Boolean = false
)
data class AdminDashboardStatistics(

    @SerializedName("total_turfs")
    val totalTurfs: Int = 0,

    @SerializedName("max_turfs")
    val maxTurfs: Int? = null,

    @SerializedName("total_bookings")
    val totalBookings: Int = 0,

    @SerializedName("total_customers")
    val totalCustomers: Int = 0,

    @SerializedName("today_bookings")
    val todayBookings: Int = 0,

    @SerializedName("today_revenue")
    val todayRevenue: Double = 0.0
)