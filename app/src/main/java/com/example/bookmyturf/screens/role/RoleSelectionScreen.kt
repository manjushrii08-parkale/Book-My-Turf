package com.example.bookmyturf.screens.role

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// BOOKMYTURF PREMIUM COLOR PALETTE
// Same dark background as EditProfileScreen
// ============================================================

// Main screen background
private val Background = Color(0xFF020907)

// White role cards
private val CardWhite = Color(0xFFFFFFFF)

// Green accents
private val PrimaryGreen = Color(0xFF7DBB4A)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFFA8D86E)

// Main card text
private val PrimaryText = Color(0xFF1C1C1C)

// Secondary card text
private val SecondaryText = Color(0xFF737373)

// Light green icon background
private val IconBackground = Color(0xFFF1F7EE)

// Card border
private val Border = Color(0xFF1A3027)

// Screen secondary text
// Same style as EditProfileScreen
private val ScreenSecondaryText = Color(0xFFA1AEA8)


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
        // MAIN CONTENT
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .navigationBarsPadding()
                .padding(
                    horizontal = 22.dp,
                    vertical = 28.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // =================================================
            // BOOKMYTURF LABEL
            // =================================================

            Text(
                text = "BOOKMYTURF",

                color = PrimaryGreen,

                fontSize = 11.sp,

                fontWeight = FontWeight.Bold,

                letterSpacing = 2.8.sp,

                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // MAIN TITLE
            // =================================================

            Text(
                text = "Welcome",

                color = Color.White,

                fontSize = 34.sp,

                fontWeight = FontWeight.Bold,

                letterSpacing = (-0.8).sp,

                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            // =================================================
            // SUBTITLE
            // =================================================

            Text(
                text = "Choose your account type to continue",

                color = ScreenSecondaryText,

                fontSize = 13.sp,

                fontWeight = FontWeight.Normal,

                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            // =================================================
            // CONTINUE AS
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 3.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "CONTINUE AS",

                    color = PrimaryGreen,

                    fontSize = 10.sp,

                    fontWeight = FontWeight.Bold,

                    letterSpacing = 1.5.sp
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Border.copy(
                                alpha = 0.65f
                            )
                        )
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // USER CARD
            // =================================================

            PremiumRoleCard(
                title = "User",

                label = "PLAYER",

                description =
                    "Find turfs, check availability and book your game.",

                icon = Icons.Default.PersonOutline,

                onClick = onUserClick
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // TURF OWNER CARD
            // =================================================

            PremiumRoleCard(
                title = "Turf Owner",

                label = "BUSINESS",

                description =
                    "Manage your turf, slots, bookings and customers.",

                icon = Icons.Default.Stadium,

                onClick = onAdminClick
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // SUPER ADMIN CARD
            // =================================================

            PremiumRoleCard(
                title = "Super Admin",

                label = "PLATFORM",

                description =
                    "Manage users, turf owners and platform operations.",

                icon = Icons.Default.Security,

                badge = "ADMIN",

                onClick = onSuperAdminClick
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            // =================================================
            // BOTTOM TEXT
            // =================================================

            Text(
                text = "Your game starts here.",

                color = Color.White.copy(
                    alpha = 0.70f
                ),

                fontSize = 11.sp,

                fontWeight = FontWeight.Medium,

                letterSpacing = 0.4.sp,

                textAlign = TextAlign.Center
            )
        }
    }
}


// ============================================================
// PREMIUM WHITE ROLE CARD
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

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .clickable {
                onClick()
            },

        // =====================================================
        // CARD SHAPE
        // =====================================================

        shape = RoundedCornerShape(
            20.dp
        ),

        // =====================================================
        // WHITE CARD
        // =====================================================

        color = CardWhite,

        // =====================================================
        // CARD BORDER
        // =====================================================

        border = BorderStroke(
            width = 1.dp,
            color = Border
        ),

        // =====================================================
        // CARD ELEVATION
        // =====================================================

        shadowElevation = 4.dp
    ) {

        Row(

            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 15.dp,
                    vertical = 14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // ICON BOX
            // =================================================

            Box(

                modifier = Modifier
                    .size(54.dp)
                    .clip(
                        RoundedCornerShape(
                            16.dp
                        )
                    )
                    .background(
                        IconBackground
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector = icon,

                    contentDescription = title,

                    modifier = Modifier.size(
                        25.dp
                    ),

                    tint = ForestGreen
                )
            }

            Spacer(
                modifier = Modifier.width(15.dp)
            )

            // =================================================
            // CARD TEXT
            // =================================================

            Column(

                modifier = Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.Center
            ) {

                // =============================================
                // TITLE + BADGE
                // =============================================

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        text = title,

                        color = PrimaryText,

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    // =========================================
                    // ADMIN BADGE
                    // =========================================

                    if (badge != null) {

                        Spacer(
                            modifier = Modifier.width(7.dp)
                        )

                        Surface(

                            shape =
                                RoundedCornerShape(
                                    5.dp
                                ),

                            color =
                                PrimaryGreen.copy(
                                    alpha = 0.16f
                                )
                        ) {

                            Text(

                                text = badge,

                                color = ForestGreen,

                                fontSize = 7.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                letterSpacing = 0.7.sp,

                                modifier = Modifier.padding(
                                    horizontal = 7.dp,
                                    vertical = 4.dp
                                )
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                // =============================================
                // ROLE LABEL
                // =============================================

                Text(

                    text = label,

                    color = ForestGreen,

                    fontSize = 8.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing = 1.2.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // =============================================
                // DESCRIPTION
                // =============================================

                Text(

                    text = description,

                    color = SecondaryText,

                    fontSize = 10.5.sp,

                    lineHeight = 15.sp,

                    maxLines = 2
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            // =================================================
            // ARROW CIRCLE
            // =================================================

            Box(

                modifier = Modifier
                    .size(34.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        IconBackground
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
                        ForestGreen
                )
            }
        }
    }
}