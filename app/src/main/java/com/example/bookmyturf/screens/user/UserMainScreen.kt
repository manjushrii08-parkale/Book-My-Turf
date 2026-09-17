package com.example.bookmyturf.screens.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.ui.theme.UserDarkGreen
import com.example.bookmyturf.ui.theme.UserLightGreen
import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.viewmodel.FavoriteViewModel
import com.example.bookmyturf.viewmodel.UserProfileViewModel
import com.example.bookmyturf.viewmodel.UserProfileViewModelFactory

// =========================================================
// BOTTOM NAVIGATION ITEM
// =========================================================

private data class UserBottomItem(
    val title: String,
    val icon: ImageVector
)

// =========================================================
// USER MAIN SCREEN
// =========================================================

@Composable
fun UserMainScreen(
    onTurfClick: (Int) -> Unit,
    onNotificationsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRateReviewClick: (bookingId: Int, turfName: String) -> Unit
) {

    // =====================================================
    // SELECTED TAB
    // =====================================================

    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    // =====================================================
    // LOGOUT DIALOG STATE
    // =====================================================

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    // =====================================================
    // CONTEXT
    // =====================================================

    val context = LocalContext.current

    // =====================================================
    // SESSION MANAGER
    // =====================================================

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    // =====================================================
    // SAVED EMAIL
    // =====================================================

    val userEmail = remember(sessionManager) {
        sessionManager.getEmail()
    }

    // =====================================================
    // SHARED FAVORITE VIEWMODEL
    // =====================================================

    val favoriteViewModel: FavoriteViewModel =
        viewModel()

    // =====================================================
    // SHARED BOOKING VIEWMODEL
    // =====================================================

    val bookingViewModel: BookingViewModel =
        viewModel()

    // =====================================================
    // PROFILE REPOSITORY
    // =====================================================

    val profileRepository = remember {
        TurfRepository()
    }

    // =====================================================
    // PROFILE VIEWMODEL FACTORY
    // =====================================================

    val profileFactory = remember {
        UserProfileViewModelFactory(
            repository = profileRepository
        )
    }

    // =====================================================
    // PROFILE VIEWMODEL
    // =====================================================

    val profileViewModel: UserProfileViewModel =
        viewModel(
            factory = profileFactory
        )

    // =====================================================
    // PROFILE STATE
    // =====================================================

    val profile by profileViewModel.profile.collectAsState()

    // =====================================================
    // LOAD PROFILE WHEN PROFILE TAB OPENS
    // =====================================================

    LaunchedEffect(selectedItem) {

        if (selectedItem == 3) {
            profileViewModel.loadProfile()
        }
    }

    // =====================================================
    // BOTTOM NAVIGATION ITEMS
    // =====================================================

    val bottomItems = listOf(

        UserBottomItem(
            title = "Home",
            icon = Icons.Default.Home
        ),

        UserBottomItem(
            title = "Favorites",
            icon = Icons.Default.FavoriteBorder
        ),

        UserBottomItem(
            title = "Bookings",
            icon = Icons.AutoMirrored.Filled.ReceiptLong
        ),

        UserBottomItem(
            title = "Profile",
            icon = Icons.Default.Person
        )
    )

    // =====================================================
    // BACK HANDLER
    // =====================================================

    BackHandler {

        if (selectedItem != 0) {

            selectedItem = 0

        } else {

            (context as? android.app.Activity)?.finish()
        }
    }

    // =====================================================
    // LOGOUT CONFIRMATION DIALOG
    // =====================================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {
                Text(
                    text = "Logout?"
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to logout from your account?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showLogoutDialog = false
                        onLogoutClick()
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = UserDarkGreen
                    )
                ) {

                    Text(
                        text = "Logout"
                    )
                }
            },

            dismissButton = {

                OutlinedButton(

                    onClick = {
                        showLogoutDialog = false
                    }
                ) {

                    Text(
                        text = "Cancel"
                    )
                }
            },

            containerColor = Color.White
        )
    }

    // =====================================================
    // SCAFFOLD
    // =====================================================

    Scaffold(

        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {

            NavigationBar(

                containerColor = UserDarkGreen,

                tonalElevation = 8.dp
            ) {

                bottomItems.forEachIndexed { index, item ->

                    NavigationBarItem(

                        selected = selectedItem == index,

                        onClick = {
                            selectedItem = index
                        },

                        icon = {

                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },

                        label = {

                            Text(
                                text = item.title
                            )
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = UserLightGreen,

                            selectedTextColor = UserLightGreen,

                            unselectedIconColor = Color(0xFFB5C2BA),

                            unselectedTextColor = Color(0xFFB5C2BA),

                            indicatorColor = Color(0xFF24542D)
                        )
                    )
                }
            }
        }

    ) { innerPadding ->

        // =================================================
        // SCREEN CONTENT
        // =================================================

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (selectedItem) {

                // =========================================
                // HOME
                // =========================================

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

                        favoriteViewModel = favoriteViewModel
                    )
                }

                // =========================================
                // FAVORITES
                // =========================================

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

                        favoriteViewModel = favoriteViewModel
                    )
                }

                // =========================================
                // BOOKINGS
                // =========================================

                2 -> {

                    UserBookingsScreen(

                        onBackClick = {

                            selectedItem = 0
                        },

                        onRateReviewClick = { bookingId, turfName ->

                            onRateReviewClick(
                                bookingId,
                                turfName
                            )
                        },

                        bookingViewModel = bookingViewModel
                    )
                }

                // =========================================
                // PROFILE
                // =========================================

                3 -> {

                    UserProfileScreen(

                        userName = profile?.name
                            ?.takeIf {
                                it.isNotBlank()
                            }
                            ?: "BookMyTurf User",

                        email = profile?.email
                            ?.takeIf {
                                it.isNotBlank()
                            }
                            ?: userEmail
                            ?: "No email available",

                        phone = profile?.phone
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