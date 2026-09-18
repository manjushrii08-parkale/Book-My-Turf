package com.example.bookmyturf.screens.admin

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite


// =============================================================
// ADMIN PROFILE SCREEN
// =============================================================
//
// NOTE:
// This screen does NOT contain Scaffold.
// AdminHomeScreen already provides:
// - AdminTopBar
// - AdminBottomBar
//
// modifier receives the Scaffold paddingValues so the content
// stays between the TopBar and BottomBar.
// =============================================================

@Composable
fun AdminProfileScreen(

    modifier: Modifier = Modifier,

    adminName: String = "Admin",

    adminEmail: String = "No email available",

    adminPhone: String = "Not added",

    onEditProfileClick: () -> Unit

) {

    LazyColumn(

        modifier =
            modifier
                .fillMaxSize()
                .background(
                    AdminOffWhite
                )
                .padding(
                    horizontal = 16.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(
                16.dp
            )
    ) {

        // =====================================================
        // TOP SPACING
        // =====================================================

        item {

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }


        // =====================================================
        // PROFILE HEADER
        // =====================================================

        item {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AdminWhite
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            3.dp
                    )

            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ) {

                    // -----------------------------------------
                    // AVATAR
                    // -----------------------------------------

                    Box(

                        modifier =
                            Modifier
                                .size(82.dp)
                                .clip(CircleShape)
                                .background(
                                    AdminDarkGreen
                                ),

                        contentAlignment =
                            Alignment.Center

                    ) {

                        Text(

                            text =
                                adminName
                                    .trim()
                                    .take(1)
                                    .uppercase()
                                    .ifBlank {
                                        "A"
                                    },

                            color =
                                Color.White,

                            fontSize =
                                30.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )


                    // -----------------------------------------
                    // ADMIN NAME
                    // -----------------------------------------

                    Text(

                        text =
                            adminName.ifBlank {
                                "Admin"
                            },

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            22.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )


                    // -----------------------------------------
                    // EMAIL
                    // -----------------------------------------

                    Text(

                        text =
                            adminEmail.ifBlank {
                                "No email available"
                            },

                        color =
                            AdminGray,

                        fontSize =
                            13.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    // -----------------------------------------
                    // ROLE BADGE
                    // -----------------------------------------

                    Box(

                        modifier =
                            Modifier
                                .clip(
                                    RoundedCornerShape(50.dp)
                                )
                                .background(
                                    AdminLightGreen.copy(
                                        alpha = 0.18f
                                    )
                                )
                                .padding(
                                    horizontal = 14.dp,
                                    vertical = 7.dp
                                )

                    ) {

                        Text(

                            text =
                                "TURF OWNER",

                            color =
                                AdminForestGreen,

                            fontSize =
                                11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                0.8.sp
                        )
                    }
                }
            }
        }


        // =====================================================
        // PROFILE INFORMATION
        // =====================================================

        item {

            AdminProfileSectionTitle(
                title =
                    "Profile Information"
            )
        }


        // =====================================================
        // NAME
        // =====================================================

        item {

            AdminProfileInfoRow(

                icon =
                    Icons.Default.Person,

                title =
                    "Name",

                value =
                    adminName.ifBlank {
                        "No name available"
                    }
            )
        }


        // =====================================================
        // EMAIL
        // =====================================================

        item {

            AdminProfileInfoRow(

                icon =
                    Icons.Default.Email,

                title =
                    "Email",

                value =
                    adminEmail.ifBlank {
                        "No email available"
                    }
            )
        }


        // =====================================================
        // PHONE
        // =====================================================

        item {

            AdminProfileInfoRow(

                icon =
                    Icons.Default.Phone,

                title =
                    "Phone",

                value =
                    adminPhone.ifBlank {
                        "Not added"
                    }
            )
        }


        // =====================================================
        // ACCOUNT
        // =====================================================

        item {

            AdminProfileSectionTitle(
                title =
                    "Account"
            )
        }


        // =====================================================
        // EDIT PROFILE
        // =====================================================

        item {

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEditProfileClick()
                        },

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AdminWhite
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
                            .padding(16.dp),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    // -----------------------------------------
                    // ICON
                    // -----------------------------------------

                    Box(

                        modifier =
                            Modifier
                                .size(44.dp)
                                .clip(
                                    RoundedCornerShape(12.dp)
                                )
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
                                Icons.Default.Edit,

                            contentDescription =
                                "Edit Profile",

                            tint =
                                AdminForestGreen,

                            modifier =
                                Modifier.size(22.dp)
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.size(14.dp)
                    )


                    // -----------------------------------------
                    // TEXT
                    // -----------------------------------------

                    Column(

                        modifier =
                            Modifier.weight(1f)

                    ) {

                        Text(

                            text =
                                "Edit Profile",

                            color =
                                AdminDarkCharcoal,

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )


                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )


                        Text(

                            text =
                                "Update your profile information",

                            color =
                                AdminGray,

                            fontSize =
                                12.sp
                        )
                    }
                }
            }
        }


        // =====================================================
        // FOOTER
        // =====================================================

        item {

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            HorizontalDivider(

                color =
                    Color.LightGray.copy(
                        alpha = 0.7f
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            Column(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                Text(

                    text =
                        "BookMyTurf",

                    color =
                        AdminDarkGreen,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )


                Text(

                    text =
                        "Manage your turf business with ease",

                    color =
                        AdminGray,

                    fontSize =
                        11.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )
            }
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun AdminProfileSectionTitle(
    title: String
) {

    Text(

        text =
            title,

        color =
            AdminDarkGreen,

        fontSize =
            16.sp,

        fontWeight =
            FontWeight.Bold,

        modifier =
            Modifier.padding(
                start = 4.dp
            )
    )
}


// =============================================================
// INFORMATION ROW
// =============================================================

@Composable
private fun AdminProfileInfoRow(

    icon: ImageVector,

    title: String,

    value: String

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
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
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically

        ) {

            // -----------------------------------------------
            // ICON
            // -----------------------------------------------

            Box(

                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
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

                    tint =
                        AdminForestGreen,

                    modifier =
                        Modifier.size(21.dp)
                )
            }


            Spacer(
                modifier =
                    Modifier.size(14.dp)
            )


            // -----------------------------------------------
            // TEXT
            // -----------------------------------------------

            Column {

                Text(

                    text =
                        title,

                    color =
                        AdminGray,

                    fontSize =
                        12.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )


                Text(

                    text =
                        value,

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Medium
                )
            }
        }
    }
}

