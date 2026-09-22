package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// BOOK MY TURF - USER BOTTOM NAVIGATION
// ============================================================


// ============================================================
// PREMIUM GREEN COLORS
// ============================================================

// Bright BookMyTurf green
private val BottomBarBackground =
    Color(0xFF7DBB4A)

// Dark green for selected icon and text
private val SelectedGreen =
    Color(0xFF020C09)

// Light green selected indicator
private val SelectedIndicator =
    Color(0xFFA8D86E)

// Soft white for unselected items
private val UnselectedColor =
    Color(0xFFE8F0EC)


// ============================================================
// BOTTOM NAVIGATION ITEM
// ============================================================

private data class UserBottomItem(
    val title: String,
    val icon: ImageVector
)


// ============================================================
// USER BOTTOM NAVIGATION
// ============================================================

@Composable
fun UserBottomNavigation(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {

    // =========================================================
    // NAVIGATION ITEMS
    // =========================================================

    val items = listOf(

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


    // =========================================================
    // BOTTOM BAR
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                BottomBarBackground
            )
    ) {

        NavigationBar(

            modifier = Modifier
                .fillMaxWidth(),

            // Bright green bottom navigation background
            containerColor =
                BottomBarBackground,

            // Remove Material elevation
            tonalElevation =
                0.dp,

            // Keep Android system navigation inset
            windowInsets =
                NavigationBarDefaults.windowInsets

        ) {

            // =================================================
            // NAVIGATION ITEMS
            // =================================================

            items.forEachIndexed { index, item ->

                val isSelected =
                    selectedItem == index


                NavigationBarItem(

                    selected =
                        isSelected,

                    onClick = {
                        onItemSelected(index)
                    },


                    // =========================================
                    // ICON
                    // =========================================

                    icon = {

                        Icon(

                            imageVector =
                                item.icon,

                            contentDescription =
                                item.title,

                            tint =
                                if (isSelected) {

                                    SelectedGreen

                                } else {

                                    UnselectedColor
                                }
                        )
                    },


                    // =========================================
                    // LABEL
                    // =========================================

                    label = {

                        Text(

                            text =
                                item.title,

                            fontSize =
                                11.sp,

                            fontWeight =
                                if (isSelected) {

                                    FontWeight.Bold

                                } else {

                                    FontWeight.Medium
                                },

                            color =
                                if (isSelected) {

                                    SelectedGreen

                                } else {

                                    UnselectedColor
                                }
                        )
                    },


                    // =========================================
                    // NAVIGATION COLORS
                    // =========================================

                    colors =
                        NavigationBarItemDefaults.colors(

                            // ---------------------------------
                            // SELECTED
                            // ---------------------------------

                            selectedIconColor =
                                SelectedGreen,

                            selectedTextColor =
                                SelectedGreen,

                            // Light green selected pill
                            indicatorColor =
                                SelectedIndicator,


                            // ---------------------------------
                            // UNSELECTED
                            // ---------------------------------

                            unselectedIconColor =
                                UnselectedColor,

                            unselectedTextColor =
                                UnselectedColor,


                            // ---------------------------------
                            // DISABLED
                            // ---------------------------------

                            disabledIconColor =
                                SelectedGreen.copy(
                                    alpha = 0.4f
                                ),

                            disabledTextColor =
                                SelectedGreen.copy(
                                    alpha = 0.4f
                                )
                        )
                )
            }
        }
    }
}