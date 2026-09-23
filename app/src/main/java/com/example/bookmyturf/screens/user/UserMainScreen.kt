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
// BOOKMYTURF PREMIUM DARK THEME
// ============================================================

private val ScreenBackground = Color(0xFF020907)

private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)

private val ErrorRed = Color(0xFFFF6B6B)


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
    // CONTEXT
    // ========================================================

    val context = LocalContext.current


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
    // SESSION
    // ========================================================

    val sessionManager = remember(context) {
        SessionManager(context)
    }


    // ========================================================
    // USER EMAIL
    // ========================================================

    val userEmail = remember(sessionManager) {
        sessionManager.getEmail()
    }


    // ========================================================
    // FAVORITE VIEWMODEL
    // ========================================================

    val favoriteViewModel: FavoriteViewModel = viewModel()


    // ========================================================
    // BOOKING VIEWMODEL
    // ========================================================

    val bookingViewModel: BookingViewModel = viewModel()


    // ========================================================
    // NOTIFICATION VIEWMODEL
    // ========================================================

    val notificationViewModel: NotificationViewModel = viewModel()


    // ========================================================
    // PROFILE REPOSITORY
    // ========================================================

    val profileRepository = remember {
        TurfRepository()
    }


    // ========================================================
    // PROFILE VIEWMODEL FACTORY
    // ========================================================

    val profileFactory = remember {
        UserProfileViewModelFactory(
            repository = profileRepository
        )
    }


    // ========================================================
    // PROFILE VIEWMODEL
    // ========================================================

    val profileViewModel: UserProfileViewModel = viewModel(
        factory = profileFactory
    )


    // ========================================================
    // PROFILE STATE
    // ========================================================

    val profile by profileViewModel
        .profile
        .collectAsState()


    // ========================================================
    // LOAD NOTIFICATION COUNT
    // ========================================================

    LaunchedEffect(Unit) {

        val token = sessionManager.getToken()

        if (!token.isNullOrBlank()) {

            notificationViewModel.loadUnreadCount(
                token = token
            )
        }
    }


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
    // LOGOUT CONFIRMATION
    // ========================================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {

                Text(
                    text = "Logout",
                    color = PrimaryText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text = "Are you sure you want to logout from your BookMyTurf account?",
                    color = SecondaryText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showLogoutDialog = false
                        onLogoutClick()
                    },

                    shape = RoundedCornerShape(12.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = ScreenBackground
                    )
                ) {

                    Text(
                        text = "Logout",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                OutlinedButton(

                    onClick = {
                        showLogoutDialog = false
                    },

                    shape = RoundedCornerShape(12.dp),

                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SecondaryText
                    ),

                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Border
                    )
                ) {

                    Text(
                        text = "Cancel",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },

            shape = RoundedCornerShape(22.dp),

            containerColor = SurfaceElevated
        )
    }


    // ========================================================
    // MAIN SCAFFOLD
    // ========================================================

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        containerColor = ScreenBackground,

        bottomBar = {

            UserBottomNavigation(

                selectedItem = selectedItem,

                onItemSelected = { index ->

                    selectedItem = index
                }
            )
        }

    ) { innerPadding ->


        // ====================================================
        // CONTENT CONTAINER
        // ====================================================

        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(innerPadding),

            contentAlignment = Alignment.TopCenter
        ) {

            when (selectedItem) {

                // =================================================
                // HOME
                // =================================================

                0 -> {

                    UserHomeScreen(

                        onTurfClick = { turfId ->

                            onTurfClick(turfId)
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

                            onTurfClick(turfId)
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
