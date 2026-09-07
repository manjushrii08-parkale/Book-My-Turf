package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// SAME THEME AS USER HOME SCREEN
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val DividerColor = Color(0xFFE5E5E0)
private val LogoutRed = Color(0xFFD32F2F)

// ============================================================
// USER PROFILE SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(

        containerColor = OffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Profile",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                Charcoal
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = Charcoal,
                        navigationIconContentColor = Charcoal
                    )
            )
        }

    ) { innerPadding ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    )
        ) {

            // =================================================
            // PROFILE HEADER
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor = White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(22.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    // =========================================
                    // AVATAR
                    // =========================================

                    Box(

                        modifier =
                            Modifier
                                .size(86.dp)
                                .background(
                                    LightGreen.copy(
                                        alpha = 0.16f
                                    ),
                                    CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "Profile",

                            modifier =
                                Modifier.size(44.dp),

                            tint =
                                ForestGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    // =========================================
                    // USER NAME
                    // =========================================

                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // =========================================
                    // EDIT PROFILE BUTTON
                    // =========================================

                    Button(

                        onClick =
                            onEditProfileClick,

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    DarkGreen
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Edit,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(7.dp)
                        )

                        Text(
                            text = "Edit Profile",
                            fontSize = 13.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // PERSONAL INFORMATION
            // =================================================

            ProfileSectionTitle(
                title =
                    "Personal Information",

                subtitle =
                    "Your account details"
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(17.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor = White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
            ) {

                Column {

                    ProfileInfoRow(
                        icon =
                            Icons.Default.Person,

                        title =
                            "Name",

                        value =
                            userName
                    )

                    ProfileDivider()

                    ProfileInfoRow(
                        icon =
                            Icons.Default.Email,

                        title =
                            "Email",

                        value =
                            email
                    )

                    ProfileDivider()

                    ProfileInfoRow(
                        icon =
                            Icons.Default.Phone,

                        title =
                            "Phone",

                        value =
                            phone
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // ACCOUNT
            // =================================================

            ProfileSectionTitle(
                title =
                    "Account",

                subtitle =
                    "Manage your app preferences"
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(17.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor = White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
            ) {

                ProfileActionRow(

                    icon =
                        Icons.Default.Settings,

                    title =
                        "Settings",

                    subtitle =
                        "Manage app preferences",

                    onClick =
                        onSettingsClick
                )
            }

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // LOGOUT
            // =================================================

            Button(

                onClick =
                    onLogoutClick,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = LogoutRed
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.Logout,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(19.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text = "Logout",
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(32.dp)
            )

            // =================================================
            // FOOTER
            // =================================================

            Column(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(22.dp),

                    tint =
                        LightGreen
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "BookMyTurf",

                    fontSize = 13.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        DarkGreen
                )

                Text(
                    text =
                        "Book your game. Play your game.",

                    fontSize = 10.sp,

                    color =
                        Gray
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }
    }
}

// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun ProfileSectionTitle(
    title: String,
    subtitle: String
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight =
                FontWeight.ExtraBold,
            color = Charcoal
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = Gray
        )
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

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(15.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(

            modifier =
                Modifier
                    .size(42.dp)
                    .background(
                        LightGreen.copy(
                            alpha = 0.12f
                        ),
                        RoundedCornerShape(11.dp)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier =
                    Modifier.size(20.dp),
                tint =
                    ForestGreen
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 11.sp,
                color = Gray
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight =
                    FontWeight.Medium,
                color = Charcoal
            )
        }
    }
}

// ============================================================
// ACTION ROW
// ============================================================

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick
                )
                .padding(15.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(

            modifier =
                Modifier
                    .size(44.dp)
                    .background(
                        LightGreen.copy(
                            alpha = 0.12f
                        ),
                        RoundedCornerShape(12.dp)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
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

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Charcoal
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Gray
            )
        }

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

// ============================================================
// DIVIDER
// ============================================================

@Composable
private fun ProfileDivider() {

    Box(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(
                    start = 69.dp
                )
                .background(
                    DividerColor
                )
    )
}

