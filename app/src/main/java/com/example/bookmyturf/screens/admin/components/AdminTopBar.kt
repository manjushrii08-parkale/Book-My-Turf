package com.example.bookmyturf.screens.admin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {

    TopAppBar(

        // =====================================================
        // TITLE
        // =====================================================

        title = {
            Text(
                text = "BookMyTurf",
                color = AdminDarkCharcoal,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },

        // =====================================================
        // ACTIONS
        // =====================================================

        actions = {

            // -------------------------------------------------
            // REFRESH
            // -------------------------------------------------

            IconButton(
                onClick = onRefresh
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = AdminDarkGreen
                )
            }

            // -------------------------------------------------
            // LOGOUT
            // -------------------------------------------------

            IconButton(
                onClick = onLogout
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = AdminDarkGreen
                )
            }
        },

        // =====================================================
        // COLORS
        // =====================================================

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AdminWhite,
            titleContentColor = AdminDarkCharcoal,
            actionIconContentColor = AdminDarkGreen
        )
    )
}

