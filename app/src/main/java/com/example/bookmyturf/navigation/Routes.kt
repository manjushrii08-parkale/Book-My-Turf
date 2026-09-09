package com.example.bookmyturf.navigation

object Routes {

    const val ROLE = "role"

    const val LOGIN = "login/{role}"

    const val OTP = "otp/{email}/{role}"

    const val USER_HOME = "user_home"

    const val USER_BOOKINGS = "user_bookings"

    const val USER_FAVORITES = "user_favorites"

    const val USER_PROFILE = "user_profile"

    const val TURF_DETAILS = "turf_details/{turfId}"

    fun turfDetails(turfId: Int): String {
        return "turf_details/$turfId"
    }

    // =========================================================
// USER SLOT SELECTION
// =========================================================

    const val SLOT_SELECTION = "slot_selection/{turfId}"

    fun slotSelection(turfId: Int): String {
        return "slot_selection/$turfId"
    }

    const val BOOKING_SUMMARY =
        "booking_summary/{turfId}/{slotId}/{bookingDate}"

    fun bookingSummary(
        turfId: Int,
        slotId: Int,
        bookingDate: String
    ): String =
        "booking_summary/$turfId/$slotId/$bookingDate"

    const val PAYMENT = "payment/{bookingId}"

    fun payment(bookingId: Int) =
        "payment/$bookingId"


    const val BOOKING_SUCCESS = "booking_success/{bookingId}"

    fun bookingSuccess(bookingId: Int): String {
        return "booking_success/$bookingId"
    }
    const val ADMIN_HOME = "admin_home"

    const val ADMIN_ENTRY = "admin_entry"

    const val ADMIN_SUBSCRIPTION = "admin_subscription"

    const val ADMIN_PAID_PLANS = "admin_paid_plans"

    const val SUPER_ADMIN_HOME = "super_admin_home"
}