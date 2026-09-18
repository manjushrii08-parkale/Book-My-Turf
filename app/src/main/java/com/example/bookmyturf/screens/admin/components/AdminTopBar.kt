package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    unreadNotificationCount: Int = 0,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {

    TopAppBar(

        // =====================================================
        // TITLE
        // =====================================================

        title = {

            Column {

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

            // =================================================
            // NOTIFICATION
            // =================================================

            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(42.dp)
            ) {

                // ---------------------------------------------
                // NOTIFICATION CONTAINER
                // ---------------------------------------------

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(12.dp)
                        ),
                    color = AdminOffWhite
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.NotificationsNone,

                                contentDescription =
                                    "Notifications",

                                modifier =
                                    Modifier.size(22.dp),

                                tint =
                                    AdminDarkGreen
                            )
                        }
                    }
                }

                // =================================================
                // UNREAD COUNT BADGE
                // =================================================

                if (unreadNotificationCount > 0) {

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(
                                top = 0.dp,
                                end = 0.dp
                            )
                            .size(19.dp),

                        shape = CircleShape,

                        color = AdminDarkGreen,

                        shadowElevation = 2.dp,

                        border = androidx.compose.foundation.BorderStroke(
                            width = 2.dp,
                            color = AdminWhite
                        )
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    if (
                                        unreadNotificationCount > 9
                                    ) {
                                        "9+"
                                    } else {
                                        unreadNotificationCount.toString()
                                    },

                                color =
                                    AdminWhite,

                                fontSize =
                                    8.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // =================================================
            // LOGOUT
            // =================================================

            Surface(
                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        end = 8.dp
                    )
                    .size(42.dp)
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

