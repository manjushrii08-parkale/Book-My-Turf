package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// =============================================================
// PREMIUM BOOKMYTURF COLORS
// =============================================================

private val Background =
    Color(0xFF020907)

private val SurfaceDark =
    Color(0xFF06110D)

private val PrimaryGreen =
    Color(0xFF7DBB4A)

private val LightGreen =
    Color(0xFFA8D86E)

private val PrimaryText =
    Color(0xFFF5F8F6)

private val MutedText =
    Color(0xFF687871)


// =============================================================
// ADMIN BOTTOM BAR
// =============================================================

@Composable
fun AdminBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    NavigationBar(

        containerColor =
            SurfaceDark,

        tonalElevation =
            0.dp
    ) {


        // =====================================================
        // DASHBOARD
        // =====================================================

        NavigationBarItem(

            selected =
                selectedTab == 0,

            onClick = {
                onTabSelected(0)
            },

            icon = {

                Icon(
                    imageVector =
                        Icons.Default.Dashboard,

                    contentDescription =
                        "Dashboard",

                    modifier =
                        Modifier.size(22.dp)
                )
            },

            label = {

                Text(
                    text =
                        "Dashboard",

                    fontSize =
                        10.sp,

                    fontWeight =
                        if (selectedTab == 0) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                )
            },

            alwaysShowLabel =
                true,

            colors =
                NavigationBarItemDefaults.colors(

                    selectedIconColor =
                        LightGreen,

                    selectedTextColor =
                        PrimaryText,

                    unselectedIconColor =
                        MutedText,

                    unselectedTextColor =
                        MutedText,

                    indicatorColor =
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
                )
        )


        // =====================================================
        // TURFS
        // =====================================================

        NavigationBarItem(

            selected =
                selectedTab == 1,

            onClick = {
                onTabSelected(1)
            },

            icon = {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription =
                        "Turfs",

                    modifier =
                        Modifier.size(22.dp)
                )
            },

            label = {

                Text(
                    text =
                        "Turfs",

                    fontSize =
                        10.sp,

                    fontWeight =
                        if (selectedTab == 1) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                )
            },

            alwaysShowLabel =
                true,

            colors =
                NavigationBarItemDefaults.colors(

                    selectedIconColor =
                        LightGreen,

                    selectedTextColor =
                        PrimaryText,

                    unselectedIconColor =
                        MutedText,

                    unselectedTextColor =
                        MutedText,

                    indicatorColor =
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
                )
        )


        // =====================================================
        // BOOKINGS
        // =====================================================

        NavigationBarItem(

            selected =
                selectedTab == 2,

            onClick = {
                onTabSelected(2)
            },

            icon = {

                Icon(
                    imageVector =
                        Icons.Default.CalendarMonth,

                    contentDescription =
                        "Bookings",

                    modifier =
                        Modifier.size(22.dp)
                )
            },

            label = {

                Text(
                    text =
                        "Bookings",

                    fontSize =
                        10.sp,

                    fontWeight =
                        if (selectedTab == 2) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                )
            },

            alwaysShowLabel =
                true,

            colors =
                NavigationBarItemDefaults.colors(

                    selectedIconColor =
                        LightGreen,

                    selectedTextColor =
                        PrimaryText,

                    unselectedIconColor =
                        MutedText,

                    unselectedTextColor =
                        MutedText,

                    indicatorColor =
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
                )
        )


        // =====================================================
        // PROFILE
        // =====================================================

        NavigationBarItem(

            selected =
                selectedTab == 3,

            onClick = {
                onTabSelected(3)
            },

            icon = {

                Icon(
                    imageVector =
                        Icons.Default.AccountCircle,

                    contentDescription =
                        "Profile",

                    modifier =
                        Modifier.size(22.dp)
                )
            },

            label = {

                Text(
                    text =
                        "Profile",

                    fontSize =
                        10.sp,

                    fontWeight =
                        if (selectedTab == 3) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                )
            },

            alwaysShowLabel =
                true,

            colors =
                NavigationBarItemDefaults.colors(

                    selectedIconColor =
                        LightGreen,

                    selectedTextColor =
                        PrimaryText,

                    unselectedIconColor =
                        MutedText,

                    unselectedTextColor =
                        MutedText,

                    indicatorColor =
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
                )
        )
    }
}