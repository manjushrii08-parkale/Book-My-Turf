package com.example.bookmyturf.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// PREMIUM THEME
// ============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)

private val LogoutRed = Color(0xFFFF6B6B)

// ============================================================
// USER PROFILE
// ============================================================

@Composable
fun UserProfileScreen(
    userName: String = "BookMyTurf User",
    email: String = "No email available",
    phone: String = "No phone available",
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
    ) {

        ProfileTopBar(
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 20.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --------------------------------------------------
            // PROFILE
            // --------------------------------------------------

            item {

                ProfileHero(
                    userName = userName,
                    email = email,
                    onEditProfileClick = onEditProfileClick
                )
            }

            // --------------------------------------------------
            // PERSONAL INFORMATION
            // --------------------------------------------------

            item {

                ProfileSectionHeader(
                    title = "Personal Information",
                    subtitle = "Your account details"
                )
            }

            item {

                PersonalInformation(
                    userName = userName,
                    email = email,
                    phone = phone
                )
            }

            // --------------------------------------------------
            // ACCOUNT
            // --------------------------------------------------

            item {

                ProfileSectionHeader(
                    title = "Account",
                    subtitle = "Manage your preferences"
                )
            }

            item {

                AccountAction(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    subtitle = "App preferences",
                    onClick = onSettingsClick
                )
            }

            item {

                AccountAction(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    subtitle = "Sign out of your account",
                    iconTint = LogoutRed,
                    titleColor = LogoutRed,
                    onClick = onLogoutClick
                )
            }

            // --------------------------------------------------
            // FOOTER
            // --------------------------------------------------

            item {

                ProfileFooter()
            }
        }
    }
}

// ============================================================
// TOP BAR
// ============================================================

@Composable
private fun ProfileTopBar(
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        onBackClick()
                    },
                shape = RoundedCornerShape(13.dp),
                color = SurfaceElevated,
                border = BorderStroke(
                    1.dp,
                    Border
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(19.dp),
                        tint = PrimaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "My Profile",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Manage your account",
                    fontSize = 11.sp,
                    color = MutedText
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )
    }
}

// ============================================================
// PROFILE HERO
// ============================================================

@Composable
private fun ProfileHero(
    userName: String,
    email: String,
    onEditProfileClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        border = BorderStroke(
            1.dp,
            Border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            color = PrimaryGreen.copy(
                                alpha = 0.10f
                            ),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = PrimaryGreen.copy(
                                alpha = 0.25f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(
                                color = SurfaceHighlight,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(29.dp),
                            tint = LightGreen
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = userName,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryText
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = SecondaryText,
                        maxLines = 1
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // Edit button

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEditProfileClick()
                    },
                shape = RoundedCornerShape(13.dp),
                color = PrimaryGreen.copy(
                    alpha = 0.08f
                ),
                border = BorderStroke(
                    1.dp,
                    PrimaryGreen.copy(
                        alpha = 0.22f
                    )
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(18.dp),
                        tint = LightGreen
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Edit Profile",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MutedText
                    )
                }
            }
        }
    }
}

// ============================================================
// SECTION HEADER
// ============================================================

@Composable
private fun ProfileSectionHeader(
    title: String,
    subtitle: String
) {

    Column {

        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryText
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = MutedText
        )
    }
}

// ============================================================
// PERSONAL INFORMATION
// ============================================================

@Composable
private fun PersonalInformation(
    userName: String,
    email: String,
    phone: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        border = BorderStroke(
            1.dp,
            Border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column {

            ProfileInfoRow(
                icon = Icons.Default.Person,
                title = "Name",
                value = userName
            )

            ProfileDivider()

            ProfileInfoRow(
                icon = Icons.Default.Email,
                title = "Email",
                value = email
            )

            ProfileDivider()

            ProfileInfoRow(
                icon = Icons.Default.Phone,
                title = "Phone",
                value = phone
            )
        }
    }
}

// ============================================================
// INFORMATION ROW
// ============================================================

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(11.dp),
            color = PrimaryGreen.copy(
                alpha = 0.08f
            )
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = PrimaryGreen
                )
            }
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 10.sp,
                color = MutedText
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )
        }
    }
}

// ============================================================
// ACCOUNT ACTION
// ============================================================

@Composable
private fun AccountAction(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconTint: Color = PrimaryGreen,
    titleColor: Color = PrimaryText
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(17.dp),
        color = SurfaceDark,
        border = BorderStroke(
            1.dp,
            if (title == "Logout") {
                LogoutRed.copy(alpha = 0.15f)
            } else {
                Border
            }
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(11.dp),
                color = iconTint.copy(
                    alpha = 0.08f
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = iconTint
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MutedText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(19.dp),
                tint = MutedText
            )
        }
    }
}

// ============================================================
// DIVIDER
// ============================================================

@Composable
private fun ProfileDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 67.dp)
            .height(1.dp)
            .background(Border)
    )
}

// ============================================================
// FOOTER
// ============================================================

@Composable
private fun ProfileFooter() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 4.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = PrimaryGreen.copy(
                        alpha = 0.07f
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                modifier = Modifier.size(23.dp),
                tint = PrimaryGreen
            )
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = "BookMyTurf",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryText
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Book your game. Play your game.",
            fontSize = 10.sp,
            color = MutedText
        )
    }
}
