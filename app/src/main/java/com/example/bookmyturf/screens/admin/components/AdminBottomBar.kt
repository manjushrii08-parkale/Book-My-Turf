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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminWhite


@Composable
fun AdminBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    NavigationBar(

        containerColor =
            AdminWhite,

        tonalElevation =
            4.dp
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
                        11.sp,

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
                        AdminDarkGreen,

                    selectedTextColor =
                        AdminDarkGreen,

                    unselectedIconColor =
                        AdminGray,

                    unselectedTextColor =
                        AdminGray,

                    indicatorColor =
                        AdminLightGreen.copy(
                            alpha = 0.20f
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
                        11.sp,

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
                        AdminDarkGreen,

                    selectedTextColor =
                        AdminDarkGreen,

                    unselectedIconColor =
                        AdminGray,

                    unselectedTextColor =
                        AdminGray,

                    indicatorColor =
                        AdminLightGreen.copy(
                            alpha = 0.20f
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
                        11.sp,

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
                        AdminDarkGreen,

                    selectedTextColor =
                        AdminDarkGreen,

                    unselectedIconColor =
                        AdminGray,

                    unselectedTextColor =
                        AdminGray,

                    indicatorColor =
                        AdminLightGreen.copy(
                            alpha = 0.20f
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
                        11.sp,

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
                        AdminDarkGreen,

                    selectedTextColor =
                        AdminDarkGreen,

                    unselectedIconColor =
                        AdminGray,

                    unselectedTextColor =
                        AdminGray,

                    indicatorColor =
                        AdminLightGreen.copy(
                            alpha = 0.20f
                        )
                )
        )
    }
}
