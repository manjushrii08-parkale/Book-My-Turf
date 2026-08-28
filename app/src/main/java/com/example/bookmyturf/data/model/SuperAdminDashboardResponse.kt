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
    val bookings: BookingStatistics,
    val revenue: RevenueStatistics
)

data class UserStatistics(
    val total: Int,
    val active: Int,
    val blocked: Int
)

data class AdminStatistics(
    val total: Int,
    val active: Int
)

data class SuperAdminStatistics(
    val total: Int
)

data class SubscriptionStatistics(
    val active: Int,
    val expired: Int,
    val inactive: Int
)

data class TurfStatistics(
    val total: Int
)

data class BookingStatistics(
    val total: Int
)

data class RevenueStatistics(
    val total: Double
)