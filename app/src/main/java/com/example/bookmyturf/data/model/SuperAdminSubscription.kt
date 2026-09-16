package com.example.bookmyturf.data.model

data class SuperAdminSubscription(
    val id: Int,
    val user_id: Int?,
    val user_name: String?,
    val user_email: String?,
    val plan: String?,
    val status: String?,
    val start_date: String?,
    val end_date: String?,
    val amount: Double?,
    val payment_status: String?,
    val is_trial: Boolean?,
    val created_at: String?
)

data class SuperAdminSubscriptionsResponse(
    val success: Boolean,
    val message: String,
    val data: List<SuperAdminSubscription>?
)

