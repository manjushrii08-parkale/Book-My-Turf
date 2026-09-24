package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// =============================================================
// EXACT COLORS FROM ADMIN SUBSCRIPTION SCREEN
// =============================================================

private val Background = Color(0xFF020907)

private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF183027)


// =============================================================
// ADMIN TOP BAR
// =============================================================

@Composable
fun AdminTopBar(
    unreadNotificationCount: Int = 0,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit
) {

    Surface(
        color = Background,
        shadowElevation = 0.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF020907),
                            Color(0xFF071810),
                            Color(0xFF020907)
                        )
                    )
                )
                .statusBarsPadding()
        ) {

            Column {

                // =================================================
                // MAIN TOP BAR
                // SAME HEIGHT AS SUBSCRIPTION
                // =================================================

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                ) {

                    // =================================================
                    // NOTIFICATION BUTTON
                    // =================================================

                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 14.dp)
                            .size(42.dp),

                        shape =
                            RoundedCornerShape(14.dp),

                        color =
                            SurfaceElevated
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            IconButton(
                                onClick =
                                    onNotificationClick
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.NotificationsNone,

                                    contentDescription =
                                        "Notifications",

                                    tint =
                                        PrimaryText,

                                    modifier =
                                        Modifier.size(20.dp)
                                )
                            }

                            // =================================================
                            // UNREAD BADGE
                            // =================================================

                            if (
                                unreadNotificationCount > 0
                            ) {

                                Surface(
                                    modifier = Modifier
                                        .align(
                                            Alignment.TopEnd
                                        )
                                        .padding(
                                            top = 5.dp,
                                            end = 5.dp
                                        )
                                        .size(17.dp),

                                    shape =
                                        CircleShape,

                                    color =
                                        PrimaryGreen
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
                                                    unreadNotificationCount
                                                        .toString()
                                                },

                                            color =
                                                Color(0xFF061008),

                                            fontSize =
                                                7.sp,

                                            fontWeight =
                                                FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }


                    // =================================================
                    // BOOKMYTURF
                    // EXACT CENTER
                    // =================================================

                    Text(
                        text =
                            "BookMyTurf",

                        modifier =
                            Modifier.align(
                                Alignment.Center
                            ),

                        color =
                            PrimaryText,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            (-0.3).sp
                    )


                    // =================================================
                    // LOGOUT BUTTON
                    // =================================================

                    Surface(
                        modifier = Modifier
                            .align(
                                Alignment.CenterEnd
                            )
                            .padding(
                                end = 14.dp
                            )
                            .size(42.dp),

                        shape =
                            RoundedCornerShape(14.dp),

                        color =
                            SurfaceElevated
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            IconButton(
                                onClick =
                                    onLogout
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.AutoMirrored.Filled.Logout,

                                    contentDescription =
                                        "Logout",

                                    tint =
                                        SecondaryText,

                                    modifier =
                                        Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }


                // =================================================
                // EXACT SAME HORIZONTAL LINE
                // AS PREMIUM SUBSCRIPTION SCREEN
                // =================================================

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Border,
                                    PrimaryGreen.copy(
                                        alpha = 0.18f
                                    ),
                                    Border,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}