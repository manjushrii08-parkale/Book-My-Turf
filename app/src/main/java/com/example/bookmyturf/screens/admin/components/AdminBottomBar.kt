package com.example.bookmyturf.screens.admin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminWhite

@Composable
fun AdminBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    NavigationBar(
        containerColor = AdminWhite
    ) {

        // =====================================================
        // DASHBOARD
        // =====================================================

        NavigationBarItem(
            selected = selectedTab == 0,

            onClick = {
                onTabSelected(0)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Dashboard"
                )
            },

            label = {
                Text(
                    text = "Dashboard"
                )
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AdminDarkGreen,
                selectedTextColor = AdminDarkGreen,
                unselectedIconColor = AdminGray,
                unselectedTextColor = AdminGray,
                indicatorColor = AdminWhite
            )
        )


        // =====================================================
        // TURFS
        // =====================================================

        NavigationBarItem(
            selected = selectedTab == 1,

            onClick = {
                onTabSelected(1)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = "Turfs"
                )
            },

            label = {
                Text(
                    text = "Turfs"
                )
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AdminDarkGreen,
                selectedTextColor = AdminDarkGreen,
                unselectedIconColor = AdminGray,
                unselectedTextColor = AdminGray,
                indicatorColor = AdminWhite
            )
        )


        // =====================================================
        // BOOKINGS
        // =====================================================

        NavigationBarItem(
            selected = selectedTab == 2,

            onClick = {
                onTabSelected(2)
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Bookings"
                )
            },

            label = {
                Text(
                    text = "Bookings"
                )
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AdminDarkGreen,
                selectedTextColor = AdminDarkGreen,
                unselectedIconColor = AdminGray,
                unselectedTextColor = AdminGray,
                indicatorColor = AdminWhite
            )
        )
    }
}

