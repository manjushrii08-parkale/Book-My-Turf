package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
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

            androidx.compose.foundation.layout.Column {

                Text(
                    text = "BookMyTurf",

                    color = AdminDarkCharcoal,

                    fontSize = 19.sp,

                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Admin Panel",

                    color = AdminGray,

                    fontSize = 10.sp,

                    fontWeight = FontWeight.Medium
                )
            }
        },

        // =====================================================
        // ACTIONS
        // =====================================================

        actions = {

            // -------------------------------------------------
            // REFRESH
            // -------------------------------------------------

            Surface(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(40.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),

                color = AdminOffWhite
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    IconButton(
                        onClick = onRefresh
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                "Refresh",

                            modifier =
                                Modifier.size(21.dp),

                            tint =
                                AdminDarkGreen
                        )
                    }
                }
            }

            // -------------------------------------------------
            // LOGOUT
            // -------------------------------------------------

            Surface(
                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        end = 8.dp
                    )
                    .size(40.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),

                color =
                    AdminLightGreen.copy(
                        alpha = 0.18f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    IconButton(
                        onClick = onLogout
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.Logout,

                            contentDescription =
                                "Logout",

                            modifier =
                                Modifier.size(21.dp),

                            tint =
                                AdminDarkGreen
                        )
                    }
                }
            }
        },

        // =====================================================
        // COLORS
        // =====================================================

        colors =
            TopAppBarDefaults.topAppBarColors(

                containerColor =
                    AdminWhite,

                titleContentColor =
                    AdminDarkCharcoal,

                actionIconContentColor =
                    AdminDarkGreen
            )
    )
}
