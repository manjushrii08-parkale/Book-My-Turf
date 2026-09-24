package com.example.bookmyturf.screens.admin
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// =============================================================
// PREMIUM ADMIN COLORS
// Same design system as:
// Subscription / Dashboard / Turfs / Bookings
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)


// =============================================================
// ADMIN PROFILE SCREEN
// =============================================================
//
// NOTE:
// This screen does NOT contain Scaffold.
//
// AdminHomeScreen already provides:
// - AdminTopBar
// - AdminBottomBar
//
// modifier receives Scaffold paddingValues so the content
// remains between TopBar and BottomBar.
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
                .background(Background)
                .padding(
                    horizontal = 20.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
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

            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                color =
                    SurfaceDark
            ) {

                Box {

                    // -----------------------------------------
                    // SUBTLE TOP GRADIENT
                    // -----------------------------------------

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors =
                                            listOf(
                                                Color(0xFF071810),
                                                Color(0xFF0D2017),
                                                Color(0xFF06110D)
                                            )
                                    )
                                )
                    )


                    Column(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 30.dp,
                                    bottom = 24.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally

                    ) {

                        // -------------------------------------
                        // AVATAR
                        // -------------------------------------

                        Box(

                            modifier =
                                Modifier
                                    .size(84.dp)
                                    .clip(CircleShape)
                                    .background(
                                        PrimaryGreen.copy(
                                            alpha = 0.12f
                                        )
                                    ),

                            contentAlignment =
                                Alignment.Center

                        ) {

                            Box(

                                modifier =
                                    Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(
                                            SurfaceHighlight
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
                                        LightGreen,

                                    fontSize =
                                        28.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }


                        Spacer(
                            modifier =
                                Modifier.height(16.dp)
                        )


                        // -------------------------------------
                        // ADMIN NAME
                        // -------------------------------------

                        Text(

                            text =
                                adminName.ifBlank {
                                    "Admin"
                                },

                            color =
                                PrimaryText,

                            fontSize =
                                22.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                (-0.3).sp
                        )


                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )


                        // -------------------------------------
                        // EMAIL
                        // -------------------------------------

                        Text(

                            text =
                                adminEmail.ifBlank {
                                    "No email available"
                                },

                            color =
                                SecondaryText,

                            fontSize =
                                12.sp
                        )


                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )


                        // -------------------------------------
                        // ROLE BADGE
                        // -------------------------------------

                        Surface(

                            shape =
                                RoundedCornerShape(50.dp),

                            color =
                                PrimaryGreen.copy(
                                    alpha = 0.10f
                                )
                        ) {

                            Text(

                                text =
                                    "TURF OWNER",

                                modifier =
                                    Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 7.dp
                                    ),

                                color =
                                    LightGreen,

                                fontSize =
                                    10.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                letterSpacing =
                                    0.9.sp
                            )
                        }
                    }
                }
            }
        }


        // =====================================================
        // PROFILE INFORMATION TITLE
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

            Surface(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEditProfileClick()
                        },

                shape =
                    RoundedCornerShape(16.dp),

                color =
                    SurfaceDark
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
                    // EDIT ICON
                    // -----------------------------------------

                    Surface(

                        modifier =
                            Modifier.size(44.dp),

                        shape =
                            RoundedCornerShape(12.dp),

                        color =
                            PrimaryGreen.copy(
                                alpha = 0.10f
                            )
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Edit,

                                contentDescription =
                                    "Edit Profile",

                                tint =
                                    LightGreen,

                                modifier =
                                    Modifier.size(21.dp)
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
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
                                PrimaryText,

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
                                SecondaryText,

                            fontSize =
                                11.sp
                        )
                    }


                    // -----------------------------------------
                    // SMALL ARROW / ACCENT
                    // -----------------------------------------

                    Text(

                        text =
                            "›",

                        color =
                            MutedText,

                        fontSize =
                            25.sp,

                        fontWeight =
                            FontWeight.Light
                    )
                }
            }
        }


        // =====================================================
        // FOOTER
        // =====================================================

        item {

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            HorizontalDivider(

                color =
                    Border
            )


            Spacer(
                modifier =
                    Modifier.height(18.dp)
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
                        LightGreen,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )


                Text(

                    text =
                        "Manage your turf business with ease",

                    color =
                        MutedText,

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
            PrimaryText,

        fontSize =
            14.sp,

        fontWeight =
            FontWeight.Bold,

        letterSpacing =
            0.2.sp,

        modifier =
            Modifier.padding(
                start = 2.dp
            )
    )
}


// =============================================================
// PROFILE INFORMATION ROW
// =============================================================

@Composable
private fun AdminProfileInfoRow(

    icon: ImageVector,

    title: String,

    value: String

) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        color =
            SurfaceDark
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),

            verticalAlignment =
                Alignment.CenterVertically

        ) {

            // =================================================
            // ICON
            // =================================================

            Surface(

                modifier =
                    Modifier.size(44.dp),

                shape =
                    RoundedCornerShape(12.dp),

                color =
                    PrimaryGreen.copy(
                        alpha = 0.10f
                    )
            ) {

                Box(

                    contentAlignment =
                        Alignment.Center

                ) {

                    Icon(

                        imageVector =
                            icon,

                        contentDescription =
                            title,

                        tint =
                            LightGreen,

                        modifier =
                            Modifier.size(21.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.width(14.dp)
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
                        title.uppercase(),

                    color =
                        MutedText,

                    fontSize =
                        9.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        0.8.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )


                Text(

                    text =
                        value,

                    color =
                        PrimaryText,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Medium
                )
            }
        }
    }
}