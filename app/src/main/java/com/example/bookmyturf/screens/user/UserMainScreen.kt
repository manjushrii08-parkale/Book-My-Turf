package com.example.bookmyturf.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.ui.theme.UserDarkGreen
import com.example.bookmyturf.ui.theme.UserLightGreen
import com.example.bookmyturf.viewmodel.BookingViewModel
import com.example.bookmyturf.viewmodel.FavoriteViewModel


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
    onTurfClick: (Int) -> Unit
) {

    // =====================================================
    // SELECTED TAB
    // =====================================================

    var selectedItem by remember {
        mutableIntStateOf(0)
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
    // SCAFFOLD
    // =====================================================

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background,

        bottomBar = {

            NavigationBar(

                containerColor =
                    UserDarkGreen,

                tonalElevation = 8.dp
            ) {

                bottomItems.forEachIndexed { index, item ->

                    NavigationBarItem(

                        selected =
                            selectedItem == index,

                        onClick = {

                            selectedItem = index
                        },

                        icon = {

                            Icon(
                                imageVector =
                                    item.icon,

                                contentDescription =
                                    item.title
                            )
                        },

                        label = {

                            Text(
                                text =
                                    item.title
                            )
                        },

                        colors =
                            NavigationBarItemDefaults.colors(

                                selectedIconColor =
                                    UserLightGreen,

                                selectedTextColor =
                                    UserLightGreen,

                                unselectedIconColor =
                                    Color(0xFFB5C2BA),

                                unselectedTextColor =
                                    Color(0xFFB5C2BA),

                                indicatorColor =
                                    Color(0xFF24542D)
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

            modifier =
                Modifier
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

                        favoriteViewModel =
                            favoriteViewModel
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

                        favoriteViewModel =
                            favoriteViewModel
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

                        bookingViewModel =
                            bookingViewModel
                    )
                }


                // =========================================
                // PROFILE
                // =========================================

                3 -> {

                    UserPlaceholderScreen(

                        title =
                            "Profile",

                        subtitle =
                            "Manage your profile and account."
                    )
                }
            }
        }
    }
}


// =========================================================
// PLACEHOLDER SCREEN
// =========================================================

@Composable
private fun UserPlaceholderScreen(
    title: String,
    subtitle: String
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(

            text =
                title,

            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(

            text =
                subtitle,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

