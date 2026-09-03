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

import com.example.bookmyturf.viewmodel.AdminViewModel


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
                backStackEntry.arguments
                    ?.getString("email")
                    ?: ""


            val role =
                backStackEntry.arguments
                    ?.getString("role")
                    ?: "USER"


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
                        role = loggedInRole
                    )


                    Log.d(
                        "AUTO_LOGIN",
                        "Session saved: $loggedInRole"
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
                            Routes.turfDetails(turfId)
                        )
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
                    }
                )

            } else {

                Text(
                    text = "Invalid turf."
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