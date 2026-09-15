package com.example.bookmyturf.navigation

import android.net.Uri
import android.util.Log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


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
import com.example.bookmyturf.screens.user.BookingSuccessScreen
import com.example.bookmyturf.screens.user.BookingSummaryScreen
import com.example.bookmyturf.screens.user.EditProfileScreen
import com.example.bookmyturf.screens.user.PaymentScreen
import com.example.bookmyturf.screens.user.TurfDetailsScreen
import com.example.bookmyturf.screens.user.UserBookingsScreen
import com.example.bookmyturf.screens.user.UserMainScreen
import com.example.bookmyturf.screens.user.UserSlotSelectionScreen

import com.example.bookmyturf.viewmodel.AdminViewModel
import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.screens.superadmin.SuperAdminDashboardScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminUsersScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminUserDetailsScreen
@Composable
fun AppNavigation(
    navController: NavHostController
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context =
        LocalContext.current


    // =========================================================
    // SESSION MANAGER
    // =========================================================

    val sessionManager =
        remember {
            SessionManager(context)
        }


    // =========================================================
    // AUTO LOGIN
    // =========================================================

    val savedToken =
        sessionManager.getToken()

    val savedRole =
        sessionManager.getRole()


    val startDestination =
        remember {

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

        sessionManager.clearSession()

        Log.d(
            "LOGOUT",
            "Session cleared"
        )

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

            val email =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("email")
                        ?: ""
                )

            val role =
                backStackEntry.arguments
                    ?.getString("role")
                    ?: "USER"


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

                    // =========================================
                    // SAVE SESSION
                    // =========================================

                    sessionManager.saveSession(

                        token = token,

                        userId = userId,

                        role = loggedInRole,

                        email = email
                    )


                    Log.d(
                        "AUTO_LOGIN",
                        "Session saved: role=$loggedInRole email=$email"
                    )


                    // =========================================
                    // ROLE BASED NAVIGATION
                    // =========================================

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
                        // UNKNOWN
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

                    onEditProfileClick = {

                        Log.d(
                            "USER_PROFILE",
                            "Opening Edit Profile"
                        )

                        navController.navigate(
                            Routes.USER_EDIT_PROFILE
                        )
                    },

                    onSettingsClick = {

                        Log.d(
                            "USER_PROFILE",
                            "Settings clicked"
                        )
                    },

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


            val bookingViewModel: BookingViewModel =
                viewModel()


            val createdBooking =
                bookingViewModel
                    .createdBooking
                    .collectAsState()
                    .value


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


            val token =
                sessionManager.getToken()


            if (
                turfId > 0 &&
                slotId > 0 &&
                bookingDate.isNotBlank()
            ) {

                BookingSummaryScreen(

                    turfId = turfId,

                    slotId = slotId,

                    bookingDate = bookingDate,

                    onBackClick = {

                        navController.popBackStack()
                    },

                    onConfirmBookingClick = {
                            selectedTurfId,
                            selectedSlotId,
                            selectedBookingDate ->

                        if (token.isNullOrBlank()) {

                            Log.e(
                                "BOOKING_SUMMARY",
                                "No authentication token found"
                            )

                            return@BookingSummaryScreen
                        }


                        bookingViewModel.createBooking(

                            token = token,

                            slotId = selectedSlotId,

                            bookingDate = selectedBookingDate
                        )
                    }
                )

            } else {

                Text(
                    text =
                        "Invalid booking details."
                )
            }
        }


        // =====================================================
        // USER BOOKINGS
        // =====================================================

        composable(
            route = Routes.USER_BOOKINGS
        ) {

            val bookingViewModel: BookingViewModel =
                viewModel()


            UserBookingsScreen(

                onBackClick = {

                    navController.popBackStack()
                },

                bookingViewModel =
                    bookingViewModel
            )
        }


        // =====================================================
        // USER PAYMENT
        // =====================================================

        composable(

            route = Routes.PAYMENT,

            arguments = listOf(

                navArgument("bookingId") {

                    type =
                        NavType.IntType
                }
            )

        ) { backStackEntry ->

            val bookingId =
                backStackEntry.arguments
                    ?.getInt("bookingId")
                    ?: -1


            if (bookingId > 0) {

                PaymentScreen(

                    bookingId = bookingId,

                    onPaymentSuccess = {

                        Log.d(
                            "RAZORPAY",
                            "Payment verified successfully in Laravel"
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

                    onBackClick = {

                        navController.popBackStack()
                    }
                )

            } else {

                Text(
                    text =
                        "Invalid payment details."
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

                    type =
                        NavType.IntType
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
                    text =
                        "Invalid booking."
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

                    token =
                        token,

                    viewModel =
                        adminViewModel,


                    // =========================================
                    // ACTIVE
                    // =========================================

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


                    // =========================================
                    // SUBSCRIPTION REQUIRED
                    // =========================================

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


                    // =========================================
                    // ERROR
                    // =========================================

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

                    token =
                        token,

                    viewModel =
                        adminViewModel,


                    // =========================================
                    // ACTIVE
                    // =========================================

                    onSubscriptionActive = {

                        Log.d(
                            "ADMIN_SUBSCRIPTION",
                            "Subscription ACTIVE"
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
                    },


                    // =========================================
                    // PAID PLANS
                    // =========================================

                    onPaidPlanClick = {

                        Log.d(
                            "ADMIN_SUBSCRIPTION",
                            "Opening PRO Plans"
                        )

                        navController.navigate(
                            Routes.ADMIN_PAID_PLANS
                        ) {

                            launchSingleTop = true
                        }
                    },


                    // =========================================
                    // BACK
                    // =========================================

                    onBackClick = {

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
                )

            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "ADMIN_SUBSCRIPTION",
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


                AdminPaidPlansScreen(

                    token =
                        token,

                    viewModel =
                        adminViewModel,


                    // =========================================
                    // PAYMENT SUCCESS
                    // =========================================

                    onPlanActivated = {

                        Log.d(
                            "PAID_PLAN",
                            "PRO subscription activated"
                        )

                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {

                            popUpTo(
                                Routes.ADMIN_PAID_PLANS
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },


                    // =========================================
                    // BACK → SUBSCRIPTION
                    // =========================================

                    onBackClick = {

                        Log.d(
                            "PAID_PLAN",
                            "Back to subscription"
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

                    token =
                        token,

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
                        SuperAdminDashboardScreen(
                            onLogout = {
                                logout()
                            },
                            onUsersClick = {
                                navController.navigate(Routes.SUPER_ADMIN_USERS)
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
        // =====================================================
// SUPER ADMIN USERS
// =====================================================

        composable(
            route = Routes.SUPER_ADMIN_USERS
        ) {
            SuperAdminUsersScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onUserClick = { userId ->

                    navController.navigate(
                        Routes.superAdminUserDetails(userId)
                    )
                }
            )
        }

        // =====================================================
// SUPER ADMIN USER DETAILS
// =====================================================

        composable(
            route = Routes.SUPER_ADMIN_USER_DETAILS,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val userId =
                backStackEntry.arguments?.getInt("userId") ?: -1

            val usersViewModel: com.example.bookmyturf.viewmodel.SuperAdminUsersViewModel =
                viewModel()

            val users by usersViewModel.users.collectAsState()

            val selectedUser =
                users.firstOrNull { it.id == userId }

            if (selectedUser != null) {

                SuperAdminUserDetailsScreen(
                    user = selectedUser,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            } else {

                LaunchedEffect(Unit) {
                    usersViewModel.loadUsers()
                }

                Text(
                    text = "Loading user details..."
                )
            }
        }
            }

        }



