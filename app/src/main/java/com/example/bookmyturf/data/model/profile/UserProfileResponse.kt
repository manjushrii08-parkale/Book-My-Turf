package com.example.bookmyturf.data.model.profile

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val success: Boolean,
    val message: String,
    val data: UserProfileData?
)

data class UserProfileData(
    val user: UserProfile?
)

data class UserProfile(

    val id: Int,

    val name: String?,

    val phone: String?,

    @SerializedName("date_of_birth")
    val dateOfBirth: String?,

    val email: String,

    @SerializedName("phone_verified_at")
    val phoneVerifiedAt: String?,

    val role: String,

    val status: String,

    @SerializedName("trial_used")
    val trialUsed: Boolean,

    @SerializedName("subscription_plan")
    val subscriptionPlan: String?,

    @SerializedName("subscription_status")
    val subscriptionStatus: String?,

    @SerializedName("subscription_start_at")
    val subscriptionStartAt: String?,

    @SerializedName("subscription_end_at")
    val subscriptionEndAt: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)