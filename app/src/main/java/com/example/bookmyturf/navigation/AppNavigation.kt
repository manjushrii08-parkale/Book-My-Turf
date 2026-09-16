package com.example.bookmyturf.navigation

import android.net.Uri
import android.util.Log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.SuperAdminBooking
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

import com.example.bookmyturf.screens.superadmin.SuperAdminAdminDetailsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminAdminsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminBookingDetailsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminBookingsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminDashboardScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminSubscriptionDetailsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminSubscriptionsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminTurfDetailsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminTurfsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminUserDetailsScreen
import com.example.bookmyturf.screens.superadmin.SuperAdminUsersScreen

import com.example.bookmyturf.viewmodel.AdminViewModel
import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.viewmodel.SuperAdminAdminsViewModel
import com.example.bookmyturf.viewmodel.SuperAdminUsersViewModel

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
    // SELECTED SUPER ADMIN BOOKING
    // =========================================================

    var selectedSuperAdminBooking by remember {
        mutableStateOf<SuperAdminBooking?>(null)
    }

    // =========================================================
    // AUTO LOGIN
    // =========================================================

    val savedToken = sessionManager.getToken()
    val savedRole = sessionManager.getRole()

    val startDestination = remember {

        when {

            !savedToken.isNullOrBlank() &&
                    savedRole == "USER" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "USER session found"
                )

                Routes.USER_HOME
            }

            !savedToken.isNullOrBlank() &&
                    savedRole == "ADMIN" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "ADMIN session found"
                )

                Routes.ADMIN_ENTRY
            }

            !savedToken.isNullOrBlank() &&
                    savedRole == "SUPER_ADMIN" -> {

                Log.d(
                    "AUTO_LOGIN",
                    "SUPER_ADMIN session found"
                )

                Routes.SUPER_ADMIN_HOME
            }

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
            route = Routes.ROLE
        ) {

            RoleSelectionScreen(

                onUserClick = {
                    navController.navigate("login/USER")
                },

                onAdminClick = {
                    navController.navigate("login/ADMIN")
                },

                onSuperAdminClick = {
                    navController.navigate("login/SUPER_ADMIN")
                }
            )
        }

        // =====================================================
        // LOGIN
        // =====================================================

        composable(
            route = Routes.LOGIN
        ) { backStackEntry ->

            val role =
                backStackEntry.arguments
                    ?.getString("role")
                    ?: "USER"

            PhoneLoginScreen(

                role = role,

                onOtpSent = { email ->

                    val encodedEmail = Uri.encode(email)

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
            route = Routes.OTP
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

                    when (loggedInRole.uppercase()) {

                        "USER" -> {

                            navController.navigate(
                                Routes.USER_HOME
                            ) {
                                popUpTo(Routes.ROLE) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }

                        "ADMIN" -> {

                            Log.d(
                                "ADMIN_ENTRY",
                                "Opening subscription check"
                            )

                            navController.navigate(
                                Routes.ADMIN_ENTRY
                            ) {
                                popUpTo(Routes.ROLE) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }

                        "SUPER_ADMIN" -> {

                            navController.navigate(
                                Routes.SUPER_ADMIN_HOME
                            ) {
                                popUpTo(Routes.ROLE) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }

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
            route = Routes.USER_HOME
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

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
                            Routes.turfDetails(turfId)
                        )
                    },

                    onEditProfileClick = {

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

                currentName = "BookMyTurf User",

                currentEmail = email,

                currentPhone = "",

                currentDateOfBirth = "",

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
                navArgument("turfId") {
                    type = NavType.IntType
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

                        navController.navigate(
                            Routes.slotSelection(selectedTurfId)
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
                navArgument("turfId") {
                    type = NavType.IntType
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

            val bookingViewModel: BookingViewModel = viewModel()

            val createdBooking =
                bookingViewModel.createdBooking
                    .collectAsState()
                    .value

            LaunchedEffect(createdBooking) {

                createdBooking?.let { booking ->

                    navController.navigate(
                        Routes.payment(booking.id)
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

            val token = sessionManager.getToken()

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
                            _,
                            selectedSlotId,
                            selectedBookingDate ->

                        if (token.isNullOrBlank()) {
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
                    text = "Invalid booking details."
                )
            }
        }

        // =====================================================
        // USER BOOKINGS
        // =====================================================

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

            val bookingId =
                backStackEntry.arguments
                    ?.getInt("bookingId")
                    ?: -1

            if (bookingId > 0) {

                PaymentScreen(

                    bookingId = bookingId,

                    onPaymentSuccess = {

                        navController.navigate(
                            Routes.bookingSuccess(bookingId)
                        ) {
                            popUpTo(Routes.PAYMENT) {
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

                    onHomeClick = {

                        navController.navigate(
                            Routes.USER_HOME
                        ) {
                            popUpTo(Routes.USER_HOME) {
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
            route = Routes.ADMIN_ENTRY
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {

                val repository = remember {
                    AdminRepository(RetrofitClient.api)
                }

                val factory = remember {
                    AdminViewModelFactory(repository)
                }

                val adminViewModel: AdminViewModel =
                    viewModel(factory = factory)

                AdminEntryScreen(

                    token = token,

                    viewModel = adminViewModel,

                    onSubscriptionActive = {

                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {
                            popUpTo(Routes.ADMIN_ENTRY) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },

                    onSubscriptionRequired = {

                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {
                            popUpTo(Routes.ADMIN_ENTRY) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },

                    onError = { message ->

                        Log.e(
                            "ADMIN_ENTRY",
                            message
                        )

                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {
                            popUpTo(Routes.ADMIN_ENTRY) {
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
            route = Routes.ADMIN_SUBSCRIPTION
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {

                val repository = remember {
                    AdminRepository(RetrofitClient.api)
                }

                val factory = remember {
                    AdminViewModelFactory(repository)
                }

                val adminViewModel: AdminViewModel =
                    viewModel(factory = factory)

                AdminSubscriptionScreen(

                    token = token,

                    viewModel = adminViewModel,

                    onSubscriptionActive = {

                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {
                            popUpTo(Routes.ADMIN_SUBSCRIPTION) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },

                    onPaidPlanClick = {

                        navController.navigate(
                            Routes.ADMIN_PAID_PLANS
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onBackClick = {

                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {
                            popUpTo(Routes.ADMIN_SUBSCRIPTION) {
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
            route = Routes.ADMIN_PAID_PLANS
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "ADMIN"
            ) {

                val repository = remember {
                    AdminRepository(RetrofitClient.api)
                }

                val factory = remember {
                    AdminViewModelFactory(repository)
                }

                val adminViewModel: AdminViewModel =
                    viewModel(factory = factory)

                AdminPaidPlansScreen(

                    token = token,

                    viewModel = adminViewModel,

                    onPlanActivated = {

                        navController.navigate(
                            Routes.ADMIN_HOME
                        ) {
                            popUpTo(Routes.ADMIN_PAID_PLANS) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },

                    onBackClick = {

                        navController.navigate(
                            Routes.ADMIN_SUBSCRIPTION
                        ) {
                            popUpTo(Routes.ADMIN_PAID_PLANS) {
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
            route = Routes.ADMIN_HOME
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

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
            route = Routes.SUPER_ADMIN_HOME
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "SUPER_ADMIN"
            ) {

                SuperAdminDashboardScreen(

                    onLogout = {
                        logout()
                    },

                    onUsersClick = {
                        navController.navigate(
                            Routes.SUPER_ADMIN_USERS
                        )
                    },

                    onAdminsClick = {
                        navController.navigate(
                            Routes.SUPER_ADMIN_ADMINS
                        )
                    },

                    onSubscriptionsClick = {
                        navController.navigate(
                            Routes.SUPER_ADMIN_SUBSCRIPTIONS
                        )
                    },

                    onTurfsClick = {
                        navController.navigate(
                            Routes.SUPER_ADMIN_TURFS
                        )
                    },

                    onBookingsClick = {
                        navController.navigate(
                            Routes.SUPER_ADMIN_BOOKINGS
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
                backStackEntry.arguments
                    ?.getInt("userId")
                    ?: -1

            val usersViewModel: SuperAdminUsersViewModel =
                viewModel()

            val users by usersViewModel.users.collectAsState()

            val selectedUser =
                users.firstOrNull { user ->
                    user.id == userId
                }

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

        // =====================================================
        // SUPER ADMIN ADMINS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_ADMINS
        ) {

            SuperAdminAdminsScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onAdminClick = { adminId ->

                    navController.navigate(
                        Routes.superAdminAdminDetails(adminId)
                    )
                }
            )
        }

        // =====================================================
        // SUPER ADMIN ADMIN DETAILS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_ADMIN_DETAILS,
            arguments = listOf(
                navArgument("adminId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val adminId =
                backStackEntry.arguments
                    ?.getInt("adminId")
                    ?: -1

            val adminsViewModel: SuperAdminAdminsViewModel =
                viewModel()

            val admins by adminsViewModel.admins.collectAsState()

            val selectedAdmin =
                admins.firstOrNull { admin ->
                    admin.id == adminId
                }

            if (selectedAdmin != null) {

                SuperAdminAdminDetailsScreen(

                    admin = selectedAdmin,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onBlockClick = {
                        adminsViewModel.blockAdmin(adminId)
                    },

                    onActivateClick = {
                        adminsViewModel.activateAdmin(adminId)
                    }
                )

            } else {

                LaunchedEffect(Unit) {
                    adminsViewModel.loadAdmins()
                }

                Text(
                    text = "Loading admin details..."
                )
            }
        }

        // =====================================================
        // SUPER ADMIN TURFS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_TURFS
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "SUPER_ADMIN"
            ) {

                SuperAdminTurfsScreen(

                    token = token,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onTurfClick = { turfId ->

                        navController.navigate(
                            Routes.superAdminTurfDetails(turfId)
                        )
                    }
                )

            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "SUPER_ADMIN_TURFS",
                        "Invalid SUPER_ADMIN session"
                    )

                    logout()
                }
            }
        }

        // =====================================================
        // SUPER ADMIN TURF DETAILS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_TURF_DETAILS,
            arguments = listOf(
                navArgument("turfId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val turfId =
                backStackEntry.arguments
                    ?.getInt("turfId")
                    ?: -1

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                turfId > 0 &&
                !token.isNullOrBlank() &&
                role == "SUPER_ADMIN"
            ) {

                SuperAdminTurfDetailsScreen(

                    turfId = turfId,

                    token = token,

                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            } else {

                Text(
                    text = "Invalid turf details."
                )
            }
        }

        // =====================================================
        // SUPER ADMIN SUBSCRIPTIONS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_SUBSCRIPTIONS
        ) {

            SuperAdminSubscriptionsScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onSubscriptionClick = { subscriptionId ->

                    navController.navigate(
                        Routes.superAdminSubscriptionDetails(
                            subscriptionId
                        )
                    )
                }
            )
        }

        // =====================================================
        // SUPER ADMIN SUBSCRIPTION DETAILS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_SUBSCRIPTION_DETAILS,
            arguments = listOf(
                navArgument("subscriptionId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val subscriptionId =
                backStackEntry.arguments
                    ?.getInt("subscriptionId")
                    ?: -1

            if (subscriptionId > 0) {

                SuperAdminSubscriptionDetailsScreen(

                    subscriptionId = subscriptionId,

                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            } else {

                Text(
                    text = "Invalid subscription."
                )
            }
        }

        // =====================================================
        // SUPER ADMIN BOOKINGS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_BOOKINGS
        ) {

            val token = sessionManager.getToken()
            val role = sessionManager.getRole()

            if (
                !token.isNullOrBlank() &&
                role == "SUPER_ADMIN"
            ) {

                SuperAdminBookingsScreen(

                    token = token,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onBookingClick = { booking ->

                        Log.d(
                            "SUPER_ADMIN_BOOKINGS",
                            "Booking clicked: ${booking.id}"
                        )

                        selectedSuperAdminBooking = booking

                        navController.navigate(
                            Routes.SUPER_ADMIN_BOOKING_DETAILS
                        )
                    }
                )

            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "SUPER_ADMIN_BOOKINGS",
                        "Invalid SUPER_ADMIN session"
                    )

                    logout()
                }
            }
        }

        // =====================================================
        // SUPER ADMIN BOOKING DETAILS
        // =====================================================

        composable(
            route = Routes.SUPER_ADMIN_BOOKING_DETAILS
        ) {

            val booking = selectedSuperAdminBooking

            if (booking != null) {

                SuperAdminBookingDetailsScreen(

                    booking = booking,

                    onBackClick = {
                        navController.popBackStack()
                    }
                )

            } else {

                LaunchedEffect(Unit) {

                    Log.e(
                        "SUPER_ADMIN_BOOKING_DETAILS",
                        "No booking selected"
                    )

                    navController.popBackStack()
                }
            }
        }
    }
}