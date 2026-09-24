package com.example.bookmyturf.screens.role

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.R


// ============================================================
// PREMIUM COLORS
// ============================================================

private val Background = Color(0xFF020907)

private val CardBackground = Color(0xFF0C2017)

private val PrimaryGreen = Color(0xFF8ED653)

private val LightGreen = Color(0xFFB7E77E)

private val White = Color(0xFFFFFFFF)

private val Gold = Color(0xFFE2C65A)


// ============================================================
// ROLE SELECTION SCREEN
// ============================================================

@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSuperAdminClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // ====================================================
        // BACKGROUND IMAGE
        // ====================================================

        Image(
            painter = painterResource(
                id = R.drawable.role_background
            ),

            contentDescription = null,

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop
        )


        // ====================================================
        // DARK OVERLAY
        // ====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.50f)
                )
        )


        // ====================================================
        // TOP GRADIENT
        // ====================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Background.copy(alpha = 0.95f),
                            Background.copy(alpha = 0.60f),
                            Color.Transparent
                        )
                    )
                )
        )


        // ====================================================
        // BOTTOM GRADIENT
        // ====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Background.copy(alpha = 0.95f)
                        )
                    )
                )
        )


        // ====================================================
        // MAIN CONTENT
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(
                    top = 200.dp,
                    bottom = 12.dp
                ),

            horizontalAlignment =
                Alignment.Start,

            verticalArrangement =
                Arrangement.Top
        ) {


            // =================================================
            // WELCOME
            // =================================================

            Text(
                text = "WELCOME",

                color = White.copy(alpha = 0.88f),

                fontSize = 10.sp,

                fontWeight =
                    FontWeight.SemiBold,

                letterSpacing = 3.sp
            )


            Spacer(
                modifier = Modifier.height(5.dp)
            )


            // =================================================
            // MAIN TITLE
            // =================================================

            Text(
                text = "Choose your",

                color = White,

                fontSize = 28.sp,

                lineHeight = 30.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing = (-0.5).sp
            )


            Text(
                text = "account type",

                color = LightGreen,

                fontSize = 28.sp,

                lineHeight = 30.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing = (-0.5).sp
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            // =================================================
            // SUBTITLE
            // =================================================

            Text(
                text =
                    "Choose your account type to continue",

                color =
                    White.copy(alpha = 0.68f),

                fontSize = 11.sp
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // GREEN LINE
            // =================================================

            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(3.dp)
                    .clip(
                        RoundedCornerShape(50)
                    )
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                PrimaryGreen,
                                LightGreen
                            )
                        )
                    )
            )


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            // =================================================
            // USER CARD
            // =================================================

            PremiumRoleCard(
                title = "User",

                label = "PLAYER",

                description =
                    "Find turfs, check availability and book your game.",

                icon =
                    Icons.Default.PersonOutline,

                onClick =
                    onUserClick
            )


            Spacer(
                modifier = Modifier.height(9.dp)
            )


            // =================================================
            // TURF OWNER CARD
            // =================================================

            PremiumRoleCard(
                title = "Turf Owner",

                label = "BUSINESS",

                description =
                    "Manage your turf, slots, bookings and customers.",

                icon =
                    Icons.Default.Stadium,

                onClick =
                    onAdminClick
            )


            Spacer(
                modifier = Modifier.height(9.dp)
            )


            // =================================================
            // SUPER ADMIN CARD
            // =================================================

            PremiumRoleCard(
                title = "Super Admin",

                label = "PLATFORM",

                description =
                    "Manage users, turf owners and platform operations.",

                icon =
                    Icons.Default.Security,

                badge = "ADMIN",

                onClick =
                    onSuperAdminClick
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // BOTTOM TAGLINE
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .width(25.dp)
                        .height(1.dp)
                        .background(
                            PrimaryGreen.copy(alpha = 0.60f)
                        )
                )

                Spacer(
                    modifier = Modifier.width(7.dp)
                )

                Text(
                    text = "YOUR GAME STARTS HERE",

                    color =
                        White.copy(alpha = 0.65f),

                    fontSize = 8.sp,

                    fontWeight =
                        FontWeight.Medium,

                    letterSpacing = 1.4.sp
                )

                Spacer(
                    modifier = Modifier.width(7.dp)
                )

                Box(
                    modifier = Modifier
                        .width(25.dp)
                        .height(1.dp)
                        .background(
                            PrimaryGreen.copy(alpha = 0.60f)
                        )
                )
            }
        }
    }
}


// ============================================================
// PREMIUM ROLE CARD
// ============================================================

@Composable
private fun PremiumRoleCard(
    title: String,
    label: String,
    description: String,
    icon: ImageVector,
    badge: String? = null,
    onClick: () -> Unit
) {

    val isAdmin =
        badge != null


    val accentColor =
        if (isAdmin) {
            Gold
        } else {
            PrimaryGreen
        }


    // ========================================================
    // CARD
    // ========================================================

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable {
                onClick()
            },

        shape =
            RoundedCornerShape(20.dp),

        color =
            CardBackground.copy(alpha = 0.94f),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    accentColor.copy(
                        alpha = 0.60f
                    )
            ),

        shadowElevation = 7.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // =================================================
            // ICON
            // =================================================

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(
                                    alpha = 0.26f
                                ),

                                Color(0xFF07140F)
                            )
                        )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        title,

                    modifier =
                        Modifier.size(24.dp),

                    tint =
                        accentColor
                )
            }


            Spacer(
                modifier = Modifier.width(12.dp)
            )


            // =================================================
            // CARD TEXT
            // =================================================

            Column(
                modifier =
                    Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.Center
            ) {


                // =============================================
                // TITLE
                // =============================================

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            title,

                        color =
                            White,

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    if (badge != null) {

                        Spacer(
                            modifier =
                                Modifier.width(6.dp)
                        )

                        Surface(
                            shape =
                                RoundedCornerShape(5.dp),

                            color =
                                Gold.copy(
                                    alpha = 0.14f
                                ),

                            border =
                                BorderStroke(
                                    1.dp,

                                    Gold.copy(
                                        alpha = 0.40f
                                    )
                                )
                        ) {

                            Text(
                                text =
                                    badge,

                                color =
                                    Gold,

                                fontSize =
                                    6.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                letterSpacing =
                                    0.8.sp,

                                modifier =
                                    Modifier.padding(
                                        horizontal = 6.dp,
                                        vertical = 3.dp
                                    )
                            )
                        }
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )


                // =============================================
                // LABEL
                // =============================================

                Text(
                    text =
                        label,

                    color =
                        accentColor,

                    fontSize =
                        7.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        1.3.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )


                // =============================================
                // DESCRIPTION
                // =============================================

                Text(
                    text =
                        description,

                    color =
                        White.copy(
                            alpha = 0.65f
                        ),

                    fontSize =
                        9.sp,

                    lineHeight =
                        12.sp,

                    maxLines =
                        2
                )
            }


            Spacer(
                modifier =
                    Modifier.width(7.dp)
            )


            // =================================================
            // ARROW
            // =================================================

            Box(
                modifier =
                    Modifier
                        .size(34.dp)
                        .clip(
                            CircleShape
                        )
                        .background(
                            accentColor.copy(
                                alpha = 0.10f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ArrowForward,

                    contentDescription =
                        "Continue",

                    modifier =
                        Modifier.size(17.dp),

                    tint =
                        accentColor
                )
            }
        }
    }
}