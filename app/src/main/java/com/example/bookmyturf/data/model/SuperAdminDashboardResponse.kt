package com.example.bookmyturf.data.model

data class SuperAdminDashboardResponse(
    val success: Boolean,
    val message: String,
    val data: SuperAdminDashboardData
)

data class SuperAdminDashboardData(
    val users: UserStatistics,
    val admins: AdminStatistics,
    val super_admins: SuperAdminStatistics,
    val subscriptions: SubscriptionStatistics,
    val turfs: TurfStatistics,
    val bookings: BookingStatistics
)

data class UserStatistics(
    val total: Int,
    val active: Int,
    val blocked: Int
)

data class AdminStatistics(
    val total: Int,
    val active: Int,
    val blocked: Int
)

data class SuperAdminStatistics(
    val total: Int
)

data class SubscriptionStatistics(
    val total: Int,
    val active: Int,
    val inactive: Int,
    val pending_payments: Int,
    val paid: Int,
    val free_trials: Int,
    val total_paid_revenue: Double
)

data class TurfStatistics(
    val total: Int
)

data class BookingStatistics(
    val total: Int
)