package com.example.bookmyturf.data.model

import com.google.gson.annotations.SerializedName

data class AdminSubscriptionResponse(
    val success: Boolean,
    val message: String,
    val data: AdminSubscriptionStatusData?
)

data class AdminSubscriptionStatusData(
    val subscription: AdminSubscription?,

    @SerializedName("trial_used")
    val trialUsed: Boolean
)

data class SuperAdminSubscriptionDetailsResponse(
    val success: Boolean,
    val message: String,
    val data: SuperAdminSubscription?
)