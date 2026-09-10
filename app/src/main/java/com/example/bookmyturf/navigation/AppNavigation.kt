package com.example.bookmyturf.navigation

import android.net.Uri
import android.util.Log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookmyturf.screens.user.PaymentScreen
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository

import com.example.bookmyturf.screens.admin.AdminEntryScreen
import com.example.bookmyturf.screens.admin.AdminHomeScreen
import com.example.bookmyturf.screens.admin.AdminPaidPlansScreen
import com.example.bookmyturf.screens.admin.AdminSubscriptionScreen
import com.example.bookmyturf.screens.admin.AdminViewModelFactory

import com.example.bookmyturf.screens.auth.OtpVerificationScreen
import com.example.bookmyturf.screens.auth.PhoneLoginScreen

import com.example.bookmyturf.screens.role.RoleSelectionScreen

import com.example.bookmyturf.screens.superadmin.SuperAdminHomeScreen

import com.example.bookmyturf.screens.user.TurfDetailsScreen
import com.example.bookmyturf.screens.user.UserMainScreen
import com.example.bookmyturf.screens.user.EditProfileScreen
import com.example.bookmyturf.screens.user.UserSlotSelectionScreen
import com.example.bookmyturf.screens.user.BookingSummaryScreen
import com.example.bookmyturf.viewmodel.AdminViewModel
import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.screens.user.BookingSuccessScreen
import androidx.compose.runtime.collectAsState
import com.example.bookmyturf.screens.user.UserBookingsScreen
@Composable
fun AppNavigation(
    navController: NavHostController
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = LocalContext.current


    // =========================================================
    // SESSION MANAGER
    // =========================================================

    val sessionManager = remember {
        SessionManager(context)
    }


    // =========================================================
    // AUTO LOGIN
    // =========================================================

    val savedToken =
        sessionManager.getToken()

    val savedRole =
        sessionManager.getRole()


    val startDestination = remember {

        when {

            // =================================================
            // USER
            // =================================================

            !savedToken.isNullOrBlank() &&
                    savedRole == "USER" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "USER session found"
                )

                Routes.USER_HOME
            }


            // =================================================
            // ADMIN
            // =================================================

            !savedToken.isNullOrBlank() &&
                    savedRole == "ADMIN" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "ADMIN session found"
                )

                Routes.ADMIN_ENTRY
            }


            // =================================================
            // SUPER ADMIN
            // =================================================

            !savedToken.isNullOrBlank() &&
                    savedRole == "SUPER_ADMIN" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "SUPER_ADMIN session found"
                )

                Routes.SUPER_ADMIN_HOME
            }


            // =================================================
            // NO SESSION
            // =================================================

            else -> {

                Log.d(
                    "AUTO_LOGIN",
                    "No valid session found"
                )

                Routes.ROLE
            }
        }
    }


    // =========================================================
    // GLOBAL LOGOUT
    // =========================================================

    fun logout() {

        Log.d(
            "LOGOUT",
            "Logout started"
        )


        // -----------------------------------------------------
        // CLEAR SESSION
        // -----------------------------------------------------

        sessionManager.clearSession()


        Log.d(
            "LOGOUT",
            "Session cleared"
        )


        // -----------------------------------------------------
        // NAVIGATE TO ROLE
        // -----------------------------------------------------

        navController.navigate(
            Routes.ROLE
        ) {

            popUpTo(0) {
                inclusive = true
            }

            launchSingleTop = true
        }


        Log.d(
            "LOGOUT",
            "Logout navigation completed"
        )
    }


    // =========================================================
    // NAVIGATION HOST
    // =========================================================
    fun openAdminDashboard() {

        Log.d(
            "ADMIN_NAV",
            "Opening Admin Dashboard"
        )

        navController.navigate(
            Routes.ADMIN_HOME
        ) {
            popUpTo(
                Routes.ADMIN_SUBSCRIPTION
            ) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        // =====================================================
        // ROLE SELECTION
        // =====================================================

        composable(
            Routes.ROLE
        ) {

            RoleSelectionScreen(

                onUserClick = {

                    navController.navigate(
                        "login/USER"
                    )
                },


                onAdminClick = {

                    navController.navigate(
                        "login/ADMIN"
                    )
                },


                onSuperAdminClick = {

                    navController.navigate(
                        "login/SUPER_ADMIN"
                    )
                },


                onLoginClick = {

                    navController.navigate(
                        "login/USER"
                    )
                }
            )
        }


        // =====================================================
        // LOGIN
        // =====================================================

        composable(
            Routes.LOGIN
        ) { backStackEntry ->

            val role =
                backStackEntry.arguments
                    ?.getString("role")
                    ?: "USER"


            PhoneLoginScreen(

                role = role,

                onOtpSent = { email ->

                    // -------------------------------------------------
                    // ENCODE EMAIL BEFORE NAVIGATION
                    // -------------------------------------------------

                    val encodedEmail =
                        Uri.encode(email)


                    navController.navigate(
                        "otp/$encodedEmail/$role"
                    )
                }
            )
        }


        // =====================================================
        // OTP
        // =====================================================

        composable(
            Routes.OTP
        ) { backStackEntry ->

            // -----------------------------------------------------
            // DECODE EMAIL
            // -----------------------------------------------------

            val email = Uri.decode(
                backStackEntry.arguments
                    ?.getString("email")
                    ?: ""
            )


            val role =
                backStackEntry.arguments
                    ?.getString("role")
                    ?: "USER"


            // -----------------------------------------------------
            // DEBUG LOG
            // -----------------------------------------------------

            Log.d(
                "OTP_EMAIL",
                "Email received = $email"
            )


            OtpVerificationScreen(

                email = email,

                role = role,

                onLoginSuccess = {
                        loggedInRole,
                        token,
                        userId ->


                    // =============================================
                    // SAVE SESSION
                    // =============================================

                    sessionManager.saveSession(

                        token = token,

                        userId = userId,

                        role = loggedInRole,

                        email = email
                    )


                    // =============================================
                    // DEBUG LOG
                    // =============================================

                    Log.d(
                        "AUTO_LOGIN",
                        "Session saved: role=$loggedInRole email=$email"
                    )


                    // =============================================
                    // ROLE BASED NAVIGATION
                    // =============================================

                    when (
                        loggedInRole.uppercase()
                    ) {


                        // =====================================
                        // USER
                        // =====================================

                        "USER" -> {

                            navController.navigate(
                                Routes.USER_HOME
                            ) {

                                popUpTo(
                                    Routes.ROLE
                                ) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }


                        // =====================================
                        // ADMIN
                        // =====================================

                        "ADMIN" -> {

                            Log.d(
                                "ADMIN_ENTRY",
                                "Opening subscription check"
                            )


                            navController.navigate(
                                Routes.ADMIN_ENTRY
                            ) {

                                popUpTo(
                                    Routes.ROLE
                                ) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }


                        // =====================================
                        // SUPER ADMIN
                        // =====================================

                        "SUPER_ADMIN" -> {

                            navController.navigate(
                                Routes.SUPER_ADMIN_HOME
                            ) {

                                popUpTo(
                                    Routes.ROLE
                                ) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }


                        // =====================================
                        // UNKNOWN ROLE
                        // =====================================

                        else -> {

                            Log.e(
                                "AUTO_LOGIN",
                                "Unknown role: $loggedInRole"
                            )

                            logout()
                        }
                    }
                }
            )
        }


        // =====================================================
        // USER MAIN
        // =====================================================

        composable(
            Routes.USER_HOME
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()


            if (
                !token.isNullOrBlank() &&
                role == "USER"
            ) {


                UserMainScreen(

                    // -------------------------------------------------
                    // TURF CARD CLICK
                    // -------------------------------------------------

                    onTurfClick = { turfId ->

                        Log.d(
                            "USER_NAVIGATION",
                            "Opening turf details: $turfId"
                        )


                        navController.navigate(
                            Routes.turfDetails(
                                turfId
                            )
                        )
                    },


                    // -------------------------------------------------
                    // EDIT PROFILE
                    // -------------------------------------------------
                    onEditProfileClick = {

                        Log.d(
                            "USER_PROFILE",
                            "Opening Edit Profile"
                        )

                        navController.navigate(
                            Routes.USER_EDIT_PROFILE
                        )
                    },


                    // -------------------------------------------------
                    // SETTINGS
                    // -------------------------------------------------

                    onSettingsClick = {

                        Log.d(
                            "USER_PROFILE",
                            "Settings clicked"
                        )

                        // Settings navigation
                        // will be connected later.
                    },


                    // -------------------------------------------------
                    // LOGOUT
                    // -------------------------------------------------

                    onLogoutClick = {

                        logout()
                    }
                )


            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "USER",
                        "Invalid USER session"
                    )

                    logout()
                }
            }
        }

        // =====================================================
// USER EDIT PROFILE
// =====================================================

        composable(
            route = Routes.USER_EDIT_PROFILE
        ) {

            val email =
                sessionManager.getEmail()
                    ?: "No email available"

            EditProfileScreen(

                currentName =
                    "BookMyTurf User",

                currentEmail =
                    email,

                currentPhone =
                    "",

                currentDateOfBirth =
                    "",

                onBackClick = {

                    navController.popBackStack()
                },

                onSaveClick = {
                        name,
                        phone,
                        dateOfBirth ->

                    Log.d(
                        "EDIT_PROFILE",
                        "Name = $name"
                    )

                    Log.d(
                        "EDIT_PROFILE",
                        "Phone = $phone"
                    )

                    Log.d(
                        "EDIT_PROFILE",
                        "Date of Birth = $dateOfBirth"
                    )

                    // API update will be connected next.
                }
            )
        }


        // =====================================================
        // USER TURF DETAILS
        // =====================================================

        composable(

            route = Routes.TURF_DETAILS,

            arguments = listOf(

                navArgument(
                    "turfId"
                ) {

                    type =
                        NavType.IntType
                }
            )

        ) { backStackEntry ->


            val turfId =
                backStackEntry.arguments
                    ?.getInt("turfId")
                    ?: -1


            if (turfId > 0) {

                TurfDetailsScreen(

                    turfId = turfId,

                    onBackClick = {

                        navController.popBackStack()
                    },

                    onBookNowClick = { selectedTurfId ->

                        Log.d(
                            "USER_NAVIGATION",
                            "Opening slot selection: $selectedTurfId"
                        )

                        navController.navigate(
                            Routes.slotSelection(
                                selectedTurfId
                            )
                        )
                    }
                )

            } else {

                Text(
                    text = "Invalid turf."
                )
            }
        }


        // =====================================================
        // USER SLOT SELECTION
        // =====================================================

        composable(

            route = Routes.SLOT_SELECTION,

            arguments = listOf(

                navArgument(
                    "turfId"
                ) {

                    type =
                        NavType.IntType
                }
            )

        ) { backStackEntry ->

            val turfId =
                backStackEntry.arguments
                    ?.getInt("turfId")
                    ?: -1


            if (turfId > 0) {

                UserSlotSelectionScreen(

                    turfId = turfId,

                    onBackClick = {

                        navController.popBackStack()
                    },
                    onContinueClick = {
                            selectedTurfId,
                            slotId,
                            bookingDate ->

                        Log.d(
                            "BOOKING_NAVIGATION",
                            "Turf ID: $selectedTurfId"
                        )

                        Log.d(
                            "BOOKING_NAVIGATION",
                            "Slot ID: $slotId"
                        )

                        Log.d(
                            "BOOKING_NAVIGATION",
                            "Booking Date: $bookingDate"
                        )

                        navController.navigate(
                            Routes.bookingSummary(
                                turfId = selectedTurfId,
                                slotId = slotId,
                                bookingDate = bookingDate
                            )
                        )
                    }
                )

            } else {

                Text(
                    text = "Invalid turf."
                )
            }
        }
        // =====================================================
// USER BOOKING SUMMARY
// =====================================================

        composable(

            route = Routes.BOOKING_SUMMARY,

            arguments = listOf(

                navArgument("turfId") {
                    type = NavType.IntType
                },

                navArgument("slotId") {
                    type = NavType.IntType
                },

                navArgument("bookingDate") {
                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            // =====================================================
            // GET NAVIGATION ARGUMENTS
            // =====================================================

            val turfId =
                backStackEntry.arguments
                    ?.getInt("turfId")
                    ?: -1

            val slotId =
                backStackEntry.arguments
                    ?.getInt("slotId")
                    ?: -1

            val bookingDate =
                backStackEntry.arguments
                    ?.getString("bookingDate")
                    ?: ""


            // =====================================================
            // BOOKING VIEWMODEL
            // =====================================================

            val bookingViewModel: BookingViewModel =
                viewModel()

            val createdBooking =
                bookingViewModel.createdBooking.collectAsState().value

            LaunchedEffect(createdBooking) {

                createdBooking?.let { booking ->

                    Log.d(
                        "BOOKING_NAVIGATION",
                        "Booking created successfully"
                    )

                    Log.d(
                        "BOOKING_NAVIGATION",
                        "Booking ID = ${booking.id}"
                    )

                    navController.navigate(
                        Routes.payment(
                            bookingId = booking.id
                        )
                    ) {

                        popUpTo(
                            Routes.BOOKING_SUMMARY
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }

                    bookingViewModel.clearCreatedBooking()
                }
            }


            // =====================================================
            // SESSION TOKEN
            // =====================================================

            val token =
                sessionManager.getToken()


            // =====================================================
            // VALIDATION
            // =====================================================

            if (
                turfId > 0 &&
                slotId > 0 &&
                bookingDate.isNotBlank()
            ) {

                BookingSummaryScreen(

                    turfId = turfId,

                    slotId = slotId,

                    bookingDate = bookingDate,


                    // =================================================
                    // BACK
                    // =================================================

                    onBackClick = {

                        navController.popBackStack()
                    },


                    // =================================================
                    // CONFIRM BOOKING
                    // =================================================

                    onConfirmBookingClick = {
                            selectedTurfId,
                            selectedSlotId,
                            selectedBookingDate ->


                        Log.d(
                            "BOOKING_SUMMARY",
                            "================================"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "CONFIRM BOOKING CLICKED"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "Turf ID = $selectedTurfId"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "Slot ID = $selectedSlotId"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "Booking Date = $selectedBookingDate"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "Token exists = ${!token.isNullOrBlank()}"
                        )

                        Log.d(
                            "BOOKING_SUMMARY",
                            "================================"
                        )


                        // =================================================
                        // CHECK LOGIN SESSION
                        // =================================================

                        if (token.isNullOrBlank()) {

                            Log.e(
                                "BOOKING_SUMMARY",
                                "No authentication token found"
                            )

                            return@BookingSummaryScreen
                        }


                        // =================================================
                        // CREATE BOOKING
                        // =================================================

                        bookingViewModel.createBooking(

                            token = token,

                            slotId = selectedSlotId,

                            bookingDate = selectedBookingDate
                        )
                    }
                )

            } else {

                Text(
                    text = "Invalid booking details."
                )
            }
        }
        composable(
            route = Routes.USER_BOOKINGS
        ) {

            val bookingViewModel: BookingViewModel = viewModel()

            UserBookingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                bookingViewModel = bookingViewModel
            )
        }

        // =====================================================
// USER PAYMENT
// =====================================================

        composable(
            route = Routes.PAYMENT,
            arguments = listOf(
                navArgument("bookingId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            // =================================================
            // GET BOOKING ID
            // =================================================

            val bookingId =
                backStackEntry.arguments
                    ?.getInt("bookingId")
                    ?: -1


            // =================================================
            // VALID BOOKING ID
            // =================================================

            if (bookingId > 0) {

                PaymentScreen(

                    bookingId = bookingId,


                    // =============================================
                    // PAYMENT VERIFIED SUCCESSFULLY
                    // =============================================

                    onPaymentSuccess = {

                        Log.d(
                            "RAZORPAY",
                            "Payment verified successfully in Laravel"
                        )

                        Log.d(
                            "RAZORPAY",
                            "Opening Booking Success screen"
                        )


                        navController.navigate(
                            Routes.bookingSuccess(
                                bookingId = bookingId
                            )
                        ) {

                            popUpTo(
                                Routes.PAYMENT
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },


                    // =============================================
                    // BACK
                    // =============================================

                    onBackClick = {

                        Log.d(
                            "RAZORPAY",
                            "Back from Payment screen"
                        )

                        navController.popBackStack()
                    }
                )

            } else {

                Log.e(
                    "RAZORPAY",
                    "Invalid booking ID: $bookingId"
                )

                Text(
                    text = "Invalid payment details."
                )
            }
        }
        // =====================================================
// USER BOOKING SUCCESS
// =====================================================

        composable(

            route = Routes.BOOKING_SUCCESS,

            arguments = listOf(

                navArgument("bookingId") {
                    type = NavType.IntType
                }
            )

        ) { backStackEntry ->

            val bookingId =
                backStackEntry.arguments
                    ?.getInt("bookingId")
                    ?: -1


            if (bookingId > 0) {

                BookingSuccessScreen(

                    bookingId = bookingId,

                    // =================================================
                    // BACK TO HOME
                    // =================================================

                    onHomeClick = {

                        navController.navigate(
                            Routes.USER_HOME
                        ) {

                            popUpTo(
                                Routes.USER_HOME
                            ) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )

            } else {

                Text(
                    text = "Invalid booking."
                )
            }
        }


        // =====================================================
        // ADMIN ENTRY
        // =====================================================

        composable(
            Routes.ADMIN_ENTRY
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()


            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {


                val repository =
                    remember {

                        AdminRepository(
                            RetrofitClient.api
                        )
                    }


                val factory =
                    remember {

                        AdminViewModelFactory(
                            repository
                        )
                    }


                val adminViewModel: AdminViewModel =
                    viewModel(
                        factory = factory
                    )


                AdminEntryScreen(

                    token = token,

                    viewModel = adminViewModel,


                    // =============================================
                    // ACTIVE
                    // =============================================

                    onSubscriptionActive = {

                        Log.d(
                            "ADMIN_ENTRY",
                            "Subscription ACTIVE"
                        )


                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {

                            popUpTo(
                                Routes.ADMIN_ENTRY
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },


                    // =============================================
                    // SUBSCRIPTION REQUIRED
                    // =============================================

                    onSubscriptionRequired = {

                        Log.d(
                            "ADMIN_ENTRY",
                            "Subscription required"
                        )


                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {

                            popUpTo(
                                Routes.ADMIN_ENTRY
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },


                    // =============================================
                    // ERROR
                    // =============================================

                    onError = { message ->

                        Log.e(
                            "ADMIN_ENTRY",
                            message
                        )


                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {

                            popUpTo(
                                Routes.ADMIN_ENTRY
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )


            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "ADMIN_ENTRY",
                        "Invalid ADMIN session"
                    )

                    logout()
                }
            }
        }


        // =====================================================
        // ADMIN SUBSCRIPTION
        // =====================================================

        composable(
            Routes.ADMIN_SUBSCRIPTION
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()


            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {


                val repository =
                    remember {

                        AdminRepository(
                            RetrofitClient.api
                        )
                    }


                val factory =
                    remember {

                        AdminViewModelFactory(
                            repository
                        )
                    }


                val adminViewModel: AdminViewModel =
                    viewModel(
                        factory = factory
                    )


                                                                                                                                                                                                                                                                                                                                                                                                            AdminSubscriptionScreen(

                                                                                                                                                                                                                                                                                                                                                                                                                token = token,

                                                                                                                                                                                                                                                                                                                                                                                                                viewModel = adminViewModel,


                                                                                                                                                                                                                                                                                                                                                                                                                // =============================================
                                                                                                                                                                                                                                                                                                                                                                                                                // ACTIVE
                                                                                                                                                                                                                                                                                                                                                                                                                // =============================================

                                                                                                                                                                                                                                                                                                                                                                                                                onSubscriptionActive = {

                                                                                                                                                                                                                                                                                                                                                                                                                    navController.navigate(
                                                                                                                                                                                                                                                                                                                                                                                                                        Routes.ADMIN_HOME
                                                                                                                                                                                                                                                                                                                                                                                                                    ) {

                                                                                                                                                                                                                                                                                                                                                                                                                        popUpTo(
                                                                                                                                                                                                                                                                                                                                                                                                                            Routes.ADMIN_SUBSCRIPTION
                                                                                                                                                                                                                                                                                                                                                                                                                        ) {
                                                                                                                                                                                                                                                                                                                                                                                                                            inclusive = true
                                                                                                                                                                                                                                                                                                                                                                                                                        }

                                                                                                                                                                                                                                                                                                                                                                                                                        launchSingleTop = true
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                },


                                                                                                                                                                                                                                                                                                                                                                                                                // =============================================
                                                                                                                                                                                                                                                                                                                                                                                                                // PAID PLAN
                                                                                                                                                                                                                                                                                                                                                                                                                // =============================================

                                                                                                                                                                                                                                                                                                                                                                                                                onPaidPlanClick = {

                                                                                                                                                                                                                                                                                                                                                                                                                    navController.navigate(
                                                                                                                                                                                                                                                                                                                                                                                                                        Routes.ADMIN_PAID_PLANS
                                                                                                                                                                                                                                                                                                                                                                                                                    ) {

                                                                                                                                                                                                                                                                                                                                                                                                                        launchSingleTop = true
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                            )


                                                                                                                                                                                                                                                                                                                                                                                                        } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "SUBSCRIPTION",
                        "Invalid ADMIN session"
                    )

                    logout()
                }
            }
        }


        // =====================================================
        // ADMIN PAID PLANS
        // =====================================================

        composable(
            Routes.ADMIN_PAID_PLANS
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()


            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {


                AdminPaidPlansScreen(

                    token = token,

                    onPlanSelected = {

                        Log.d(
                            "PAID_PLAN",
                            "Plan selected"
                        )


                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {

                            popUpTo(
                                Routes.ADMIN_PAID_PLANS
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )


            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "PAID_PLAN",
                        "Invalid ADMIN session"
                    )

                    logout()
                }
            }
        }


        // =====================================================
        // ADMIN HOME
        // =====================================================

        composable(
            Routes.ADMIN_HOME
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()


            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {


                AdminHomeScreen(

                    token = token,

                    onLogout = {

                        logout()
                    }
                )


            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "ADMIN",
                        "Invalid ADMIN session"
                    )

                    logout()
                }
            }
        }


        // =====================================================
        // SUPER ADMIN HOME
        // =====================================================

        composable(
            Routes.SUPER_ADMIN_HOME
        ) {

            val token =
                sessionManager.getToken()

            val role =
                sessionManager.getRole()

            val userId =
                sessionManager.getUserId()


            Log.d(
                "SUPER_ADMIN",
                "Dashboard opened"
            )


            Log.d(
                "SUPER_ADMIN",
                "Token exists = ${!token.isNullOrBlank()}"
            )


            Log.d(
                "SUPER_ADMIN",
                "Role = $role"
            )


            Log.d(
                "SUPER_ADMIN",
                "User ID = $userId"
            )


            if (
                !token.isNullOrBlank() &&
                role == "SUPER_ADMIN"
            ) {


                SuperAdminHomeScreen(

                    token = token,


                    onLogout = {

                        logout()
                    },


                    onUsersClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Users clicked"
                        )
                    },


                    onOwnersClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Turf Owners clicked"
                        )
                    },


                    onTurfsClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Turfs clicked"
                        )
                    },


                    onBookingsClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Bookings clicked"
                        )
                    },


                    onSubscriptionsClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Subscriptions clicked"
                        )
                    },


                    onSettingsClick = {

                        Log.d(
                            "SUPER_ADMIN",
                            "Settings clicked"
                        )
                    }
                )


            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "SUPER_ADMIN",
                        "Invalid session"
                    )

                    logout()
                }
            }
        }
    }
}