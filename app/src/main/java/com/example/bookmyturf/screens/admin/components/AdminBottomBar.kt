package com.example.bookmyturf.screens.admin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AdminBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    NavigationBar {

        // =====================================================
        // DASHBOARD
        // =====================================================

        NavigationBarItem(

            selected = selectedTab == 0,

            onClick = {
                onTabSelected(0)
            },

            icon = {

                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Dashboard"
                )
            },

            label = {
                Text("Dashboard")
            }
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

                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = "Turfs"
                )
            },

            label = {
                Text("Turfs")
            }
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

                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Bookings"
                )
            },

            label = {
                Text("Bookings")
            }
        )
    }
}

