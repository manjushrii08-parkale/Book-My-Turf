
package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.screens.user.components.UserSecondaryTopBar

// ============================================================
// COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)
private val DividerColor = Color(0xFFE5E5E0)

// ============================================================
// USER SETTINGS SCREEN
// ============================================================

@Composable
fun UserSettingsScreen(
    onBackClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onHelpSupportClick: () -> Unit,
    onContactUsClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        UserSecondaryTopBar(
            title = "Settings",
            subtitle = "Manage your preferences and support.",
            onBackClick = onBackClick
        )



        // =====================================================
        // CONTENT
        // =====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            contentPadding = PaddingValues(
                top = 18.dp,
                start = 18.dp,
                end = 18.dp,
                bottom = 30.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // =================================================
            // SETTINGS SUMMARY CARD
            // =================================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                White
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation =
                                2.dp
                        )
                ) {

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(18.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // =====================================
                        // SETTINGS ICON
                        // =====================================

                        Box(
                            modifier =
                                Modifier
                                    .size(72.dp)
                                    .background(
                                        LightGreen.copy(
                                            alpha = 0.15f
                                        ),
                                        CircleShape
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Box(
                                modifier =
                                    Modifier
                                        .size(58.dp)
                                        .background(
                                            ForestGreen,
                                            CircleShape
                                        ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Settings,

                                    contentDescription =
                                        "Settings",

                                    modifier =
                                        Modifier.size(31.dp),

                                    tint =
                                        White
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.width(14.dp)
                        )

                        // =====================================
                        // SETTINGS DETAILS
                        // =====================================

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    "App Settings",

                                fontSize =
                                    19.sp,

                                fontWeight =
                                    FontWeight.ExtraBold,

                                color =
                                    Charcoal
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "Manage privacy, legal information and support.",

                                fontSize =
                                    12.sp,

                                color =
                                    Gray,

                                lineHeight =
                                    17.sp
                            )
                        }
                    }
                }
            }

            // =================================================
            // LEGAL SECTION
            // =================================================

            item {

                SettingsSectionTitle(
                    title =
                        "Legal",

                    subtitle =
                        "Review our policies and terms"
                )
            }

            // =================================================
            // PRIVACY POLICY
            // =================================================

            item {

                SettingsActionCard(
                    icon =
                        Icons.Default.PrivacyTip,

                    title =
                        "Privacy Policy",

                    subtitle =
                        "Learn how BookMyTurf handles your information",

                    onClick =
                        onPrivacyPolicyClick
                )
            }

            // =================================================
            // TERMS & CONDITIONS
            // =================================================

            item {

                SettingsActionCard(
                    icon =
                        Icons.Default.Description,

                    title =
                        "Terms & Conditions",

                    subtitle =
                        "Read the terms for using BookMyTurf",

                    onClick =
                        onTermsClick
                )
            }

            // =================================================
            // SUPPORT SECTION
            // =================================================

            item {

                SettingsSectionTitle(
                    title =
                        "Support",

                    subtitle =
                        "We're here to help"
                )
            }

            // =================================================
            // HELP & SUPPORT
            // =================================================

            item {

                SettingsActionCard(
                    icon =
                        Icons.Default.HelpOutline,

                    title =
                        "Help & Support",

                    subtitle =
                        "Get help with bookings and your account",

                    onClick =
                        onHelpSupportClick
                )
            }

            // =================================================
            // CONTACT US
            // =================================================

            item {

                SettingsActionCard(
                    icon =
                        Icons.Default.Email,

                    title =
                        "Contact Us",

                    subtitle =
                        "Get in touch with the BookMyTurf team",

                    onClick =
                        onContactUsClick
                )
            }

            // =================================================
            // FOOTER
            // =================================================

            item {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 18.dp,
                                bottom = 8.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    // =========================================
                    // SOCCER ICON
                    // =========================================

                    Box(
                        modifier =
                            Modifier
                                .size(48.dp)
                                .background(
                                    LightGreen.copy(
                                        alpha = 0.15f
                                    ),
                                    CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.SportsSoccer,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(26.dp),

                            tint =
                                ForestGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    // =========================================
                    // APP NAME
                    // =========================================

                    Text(
                        text =
                            "BookMyTurf",

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            DarkGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    // =========================================
                    // TAGLINE
                    // =========================================

                    Text(
                        text =
                            "Book your game. Play your game.",

                        fontSize =
                            11.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            Gray
                    )
                }
            }
        }
    }
}

// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun SettingsSectionTitle(
    title: String,
    subtitle: String
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text =
                title,

            fontSize =
                18.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                DarkGreen
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text =
                subtitle,

            fontSize =
                11.sp,

            fontWeight =
                FontWeight.Medium,

            color =
                Gray
        )
    }
}

// ============================================================
// SETTINGS ACTION CARD
// ============================================================

@Composable
private fun SettingsActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick =
                        onClick
                ),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    1.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // ICON CONTAINER
            // =================================================

            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .background(
                            LightGreen.copy(
                                alpha = 0.10f
                            ),
                            RoundedCornerShape(12.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(22.dp),

                    tint =
                        ForestGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            // =================================================
            // TEXT
            // =================================================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        title,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        subtitle,

                    fontSize =
                        11.sp,

                    color =
                        Gray,

                    lineHeight =
                        16.sp
                )
            }

            // =================================================
            // CHEVRON
            // =================================================

            Icon(
                imageVector =
                    Icons.Default.ChevronRight,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(20.dp),

                tint =
                    Gray
            )
        }
    }
}
