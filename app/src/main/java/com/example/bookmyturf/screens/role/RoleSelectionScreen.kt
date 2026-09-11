package com.example.bookmyturf.screens.role

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.R
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminWhite

@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSuperAdminClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // BACKGROUND
        // =====================================================

        Image(
            painter = painterResource(
                id = R.drawable.bg_turf
            ),

            contentDescription = null,

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop
        )

        // =====================================================
        // DARK GRADIENT
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AdminDarkGreen.copy(alpha = 0.20f),
                            Color.Black.copy(alpha = 0.45f),
                            Color.Black.copy(alpha = 0.78f)
                        )
                    )
                )
        )

        // =====================================================
        // CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .navigationBarsPadding()
                .padding(
                    horizontal = 24.dp,
                    vertical = 34.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // =================================================
            // LOGO
            // =================================================

            Surface(
                modifier = Modifier.size(108.dp),

                shape = CircleShape,

                color =
                    AdminWhite.copy(
                        alpha = 0.95f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.logo
                        ),

                        contentDescription =
                            "Book My Turf logo",

                        modifier =
                            Modifier.size(92.dp),

                        contentScale =
                            ContentScale.Fit
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            // =================================================
            // WELCOME
            // =================================================

            Text(
                text = "Welcome",

                color =
                    AdminWhite,

                fontSize =
                    28.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    "Choose how you want to continue",

                color =
                    AdminWhite.copy(
                        alpha = 0.86f
                    ),

                fontSize =
                    14.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // USER
            // =================================================

            RoleCard(
                title = "User",

                icon =
                    Icons.Default.Person,

                description =
                    "Discover and book your favourite turf",

                onClick =
                    onUserClick
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // TURF OWNER
            // =================================================

            RoleCard(
                title = "Turf Owner",

                icon =
                    Icons.Default.Stadium,

                description =
                    "Manage your turf, slots and bookings",

                onClick =
                    onAdminClick
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // SUPER ADMIN
            // =================================================

            RoleCard(
                title = "Super Admin",

                icon =
                    Icons.Default.Shield,

                description =
                    "Manage users, turf owners and the platform",

                badge =
                    "ADMIN",

                onClick =
                    onSuperAdminClick
            )
        }
    }
}


// =============================================================
// ROLE CARD
// =============================================================

@Composable
fun RoleCard(
    title: String,
    icon: ImageVector,
    description: String,
    badge: String? = null,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .clickable {
                onClick()
            },

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite.copy(
                        alpha = 0.96f
                    )
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 7.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // ICON
            // =================================================

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        AdminLightGreen.copy(
                            alpha = 0.18f
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
                        Modifier.size(28.dp),

                    tint =
                        AdminForestGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(15.dp)
            )

            // =================================================
            // TEXT
            // =================================================

            Column(
                modifier =
                    Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            title,

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    if (badge != null) {

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Surface(
                            shape =
                                RoundedCornerShape(6.dp),

                            color =
                                AdminDarkGreen
                        ) {

                            Text(
                                text =
                                    badge,

                                color =
                                    AdminWhite,

                                fontSize =
                                    9.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                modifier =
                                    Modifier.padding(
                                        horizontal = 7.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        description,

                    color =
                        AdminGray,

                    fontSize =
                        12.sp,

                    lineHeight =
                        17.sp
                )
            }

            // =================================================
            // ARROW
            // =================================================

            Icon(
                imageVector =
                    Icons.Default.ArrowForwardIos,

                contentDescription =
                    "Continue",

                modifier =
                    Modifier.size(17.dp),

                tint =
                    AdminForestGreen
            )
        }
    }
}

