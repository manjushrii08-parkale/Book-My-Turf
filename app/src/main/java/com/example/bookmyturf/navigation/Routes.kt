package com.example.bookmyturf.navigation

object Routes {

    const val ROLE = "role"

    const val LOGIN = "login/{role}"

    const val OTP = "otp/{email}/{role}"

    // =====================================================
    // USER ROUTES
    // =====================================================

    const val USER_HOME = "user_home"

    const val USER_BOOKINGS = "user_bookings"

    const val USER_FAVORITES = "user_favorites"

    const val USER_PROFILE = "user_profile"

    const val USER_SETTINGS = "user_settings"

    const val USER_EDIT_PROFILE = "user_edit_profile"

    const val TURF_DETAILS = "turf_details/{turfId}"

    fun turfDetails(turfId: Int): String {
        return "turf_details/$turfId"
    }

    // =====================================================
    // USER SLOT SELECTION
    // =====================================================

    const val SLOT_SELECTION = "slot_selection/{turfId}"

    fun slotSelection(turfId: Int): String {
        return "slot_selection/$turfId"
    }

    // =====================================================
    // USER BOOKING
    // =====================================================

    const val BOOKING_SUMMARY =
        "booking_summary/{turfId}/{slotId}/{bookingDate}"

    fun bookingSummary(
        turfId: Int,
        slotId: Int,
        bookingDate: String
    ): String {
        return "booking_summary/$turfId/$slotId/$bookingDate"
    }

    const val PAYMENT = "payment/{bookingId}"

    fun payment(bookingId: Int): String {
        return "payment/$bookingId"
    }

    const val BOOKING_SUCCESS = "booking_success/{bookingId}"

    fun bookingSuccess(bookingId: Int): String {
        return "booking_success/$bookingId"
    }

    // =====================================================
    // ADMIN ROUTES
    // =====================================================

    const val ADMIN_HOME = "admin_home"

    const val ADMIN_ENTRY = "admin_entry"

    const val ADMIN_SUBSCRIPTION = "admin_subscription"

    const val ADMIN_PAID_PLANS = "admin_paid_plans"

    // =====================================================
    // SUPER ADMIN DASHBOARD
    // =====================================================

    const val SUPER_ADMIN_HOME = "super_admin_home"

    // =====================================================
    // SUPER ADMIN USERS
    // =====================================================

    const val SUPER_ADMIN_USERS = "super_admin_users"

    const val SUPER_ADMIN_USER_DETAILS =
        "super_admin_user_details/{userId}"

    fun superAdminUserDetails(userId: Int): String {
        return "super_admin_user_details/$userId"
    }

    // =====================================================
    // SUPER ADMIN ADMINS
    // =====================================================

    const val SUPER_ADMIN_ADMINS = "super_admin_admins"

    const val SUPER_ADMIN_ADMIN_DETAILS =
        "super_admin_admin_details/{adminId}"

    fun superAdminAdminDetails(adminId: Int): String {
        return "super_admin_admin_details/$adminId"
    }

    // =====================================================
    // SUPER ADMIN SUBSCRIPTIONS
    // =====================================================

    const val SUPER_ADMIN_SUBSCRIPTIONS =
        "super_admin_subscriptions"

    const val SUPER_ADMIN_SUBSCRIPTION_DETAILS =
        "super_admin_subscription_details/{subscriptionId}"

    fun superAdminSubscriptionDetails(
        subscriptionId: Int
    ): String {
        return "super_admin_subscription_details/$subscriptionId"
    }

    // =====================================================
    // SUPER ADMIN TURFS
    // =====================================================

    const val SUPER_ADMIN_TURFS = "super_admin_turfs"

    const val SUPER_ADMIN_TURF_DETAILS =
        "super_admin_turf_details/{turfId}"

    fun superAdminTurfDetails(turfId: Int): String {
        return "super_admin_turf_details/$turfId"
    }

    // =====================================================
    // SUPER ADMIN BOOKINGS
    // =====================================================

    const val SUPER_ADMIN_BOOKINGS = "super_admin_bookings"

    const val SUPER_ADMIN_BOOKING_DETAILS = "super_admin_booking_details"

    const val SUPER_ADMIN_REPORTS = "super_admin_reports"

    const val USER_NOTIFICATIONS = "user_notifications"

    const val RATE_REVIEW = "rate_review/{bookingId}/{turfName}"

    fun rateReview(
        bookingId: Int, turfName: String ): String {
        return "rate_review/$bookingId/${android.net.Uri.encode(turfName)}"
    }
}

