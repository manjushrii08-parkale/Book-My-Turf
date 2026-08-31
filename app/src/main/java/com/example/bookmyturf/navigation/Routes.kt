package com.example.bookmyturf.navigation

object Routes {

    const val ROLE = "role"

    const val LOGIN = "login/{role}"

    const val OTP = "otp/{email}/{role}"

    const val USER_HOME = "user_home"

    const val TURF_DETAILS = "turf_details/{turfId}"

    fun turfDetails(turfId: Int): String {
        return "turf_details/$turfId"
    }

    const val ADMIN_HOME = "admin_home"

    const val ADMIN_ENTRY = "admin_entry"

    const val ADMIN_SUBSCRIPTION = "admin_subscription"

    const val ADMIN_PAID_PLANS = "admin_paid_plans"

    const val SUPER_ADMIN_HOME = "super_admin_home"
}