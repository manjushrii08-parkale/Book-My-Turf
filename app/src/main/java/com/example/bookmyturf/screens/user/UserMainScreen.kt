package com.example.bookmyturf.screens.user

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.repository.TurfRepository

import com.example.bookmyturf.screens.user.components.UserBottomNavigation

import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.viewmodel.FavoriteViewModel
import com.example.bookmyturf.viewmodel.NotificationViewModel
import com.example.bookmyturf.viewmodel.UserProfileViewModel
import com.example.bookmyturf.viewmodel.UserProfileViewModelFactory


// ============================================================
// BOOK MY TURF PREMIUM COLORS
// ============================================================

private val ScreenBackground =
    Color(0xFF020C09)

private val SelectedGreen =
    Color(0xFF7DBB4A)

private val White =
    Color(0xFFF5F8F6)

private val DialogBackground =
    Color(0xFF071713)

private val DialogText =
    Color(0xFFE8F0EC)


// ============================================================
// USER MAIN SCREEN
// ============================================================

@Composable
fun UserMainScreen(
    onTurfClick: (Int) -> Unit,
    onNotificationsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRateReviewClick: (
        bookingId: Int,
        turfName: String
    ) -> Unit
) {

    // ========================================================
    // SELECTED BOTTOM TAB
    // ========================================================

    var selectedItem by remember {
        mutableIntStateOf(0)
    }


    // ========================================================
    // LOGOUT DIALOG
    // ========================================================

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }


    // ========================================================
    // CONTEXT
    // ========================================================

    val context =
        LocalContext.current


    // ========================================================
    // SESSION MANAGER
    // ========================================================

    val sessionManager =
        remember(context) {
            SessionManager(context)
        }


    // ========================================================
    // USER EMAIL
    // ========================================================

    val userEmail =
        remember(sessionManager) {
            sessionManager.getEmail()
        }


    // ========================================================
    // FAVORITE VIEWMODEL
    // ========================================================

    val favoriteViewModel: FavoriteViewModel =
        viewModel()


    // ========================================================
    // BOOKING VIEWMODEL
    // ========================================================

    val bookingViewModel: BookingViewModel =
        viewModel()


    // ========================================================
    // NOTIFICATION VIEWMODEL
    // ========================================================

    val notificationViewModel: NotificationViewModel =
        viewModel()


    // ========================================================
    // LOAD NOTIFICATION COUNT
    // ========================================================

    LaunchedEffect(Unit) {

        val token =
            sessionManager.getToken()

        if (!token.isNullOrBlank()) {

            notificationViewModel.loadUnreadCount(
                token = token
            )
        }
    }


    // ========================================================
    // PROFILE REPOSITORY
    // ========================================================

    val profileRepository =
        remember {
            TurfRepository()
        }


    // ========================================================
    // PROFILE VIEWMODEL FACTORY
    // ========================================================

    val profileFactory =
        remember {

            UserProfileViewModelFactory(
                repository =
                    profileRepository
            )
        }


    // ========================================================
    // PROFILE VIEWMODEL
    // ========================================================

    val profileViewModel: UserProfileViewModel =
        viewModel(
            factory =
                profileFactory
        )


    // ========================================================
    // PROFILE STATE
    // ========================================================

    val profile by
    profileViewModel
        .profile
        .collectAsState()


    // ========================================================
    // LOAD PROFILE WHEN PROFILE TAB OPENS
    // ========================================================

    LaunchedEffect(selectedItem) {

        if (selectedItem == 3) {

            profileViewModel.loadProfile()
        }
    }


    // ========================================================
    // BACK HANDLER
    // ========================================================

    BackHandler {

        if (selectedItem != 0) {

            selectedItem = 0

        } else {

            (context as? android.app.Activity)
                ?.finish()
        }
    }


    // ========================================================
    // LOGOUT CONFIRMATION DIALOG
    // ========================================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {

                showLogoutDialog = false
            },

            title = {

                Text(
                    text = "Logout?",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text =
                        "Are you sure you want to logout from your account?",

                    color =
                        DialogText,

                    fontSize =
                        14.sp
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showLogoutDialog = false

                        onLogoutClick()
                    },

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                SelectedGreen,

                            contentColor =
                                Color(0xFF06130F)
                        ),

                    shape =
                        RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Logout",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                OutlinedButton(

                    onClick = {

                        showLogoutDialog = false
                    },

                    shape =
                        RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Cancel",
                        color = DialogText
                    )
                }
            },

            containerColor =
                DialogBackground
        )
    }


    // ========================================================
    // MAIN SCAFFOLD
    // ========================================================

    Scaffold(

        containerColor =
            ScreenBackground,

        // ====================================================
        // FIXED BOTTOM NAVIGATION
        // ====================================================

        bottomBar = {

            UserBottomNavigation(

                selectedItem =
                    selectedItem,

                onItemSelected = { index ->

                    selectedItem =
                        index
                }
            )
        }

    ) { innerPadding ->


        // ====================================================
        // MAIN CONTENT
        // ====================================================

        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        ScreenBackground
                    )
                    .padding(
                        innerPadding
                    ),

            contentAlignment =
                Alignment.TopCenter
        ) {

            when (selectedItem) {


                // =================================================
                // HOME
                // =================================================

                0 -> {

                    UserHomeScreen(

                        onTurfClick = { turfId ->

                            onTurfClick(
                                turfId
                            )
                        },

                        onNotificationsClick = {

                            onNotificationsClick()
                        },

                        onProfileClick = {

                            selectedItem = 3
                        },

                        favoriteViewModel =
                            favoriteViewModel,

                        notificationViewModel =
                            notificationViewModel
                    )
                }


                // =================================================
                // FAVORITES
                // =================================================

                1 -> {

                    UserFavoritesScreen(

                        onBackClick = {

                            selectedItem = 0
                        },

                        onTurfClick = { turfId ->

                            onTurfClick(
                                turfId
                            )
                        },

                        favoriteViewModel =
                            favoriteViewModel
                    )
                }


                // =================================================
                // BOOKINGS
                // =================================================

                2 -> {

                    UserBookingsScreen(

                        onBackClick = {

                            selectedItem = 0
                        },

                        onRateReviewClick = {

                                bookingId,
                                turfName ->

                            onRateReviewClick(
                                bookingId,
                                turfName
                            )
                        },

                        bookingViewModel =
                            bookingViewModel
                    )
                }


                // =================================================
                // PROFILE
                // =================================================

                3 -> {

                    UserProfileScreen(

                        userName =
                            profile?.name
                                ?.takeIf {
                                    it.isNotBlank()
                                }
                                ?: "BookMyTurf User",

                        email =
                            profile?.email
                                ?.takeIf {
                                    it.isNotBlank()
                                }
                                ?: userEmail
                                ?: "No email available",

                        phone =
                            profile?.phone
                                ?.takeIf {
                                    it.isNotBlank()
                                }
                                ?: "No phone available",

                        onBackClick = {

                            selectedItem = 0
                        },

                        onEditProfileClick =
                            onEditProfileClick,

                        onSettingsClick =
                            onSettingsClick,

                        onLogoutClick = {

                            showLogoutDialog = true
                        }
                    )
                }
            }
        }
    }
}