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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.R


@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSuperAdminClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ==================================================
        // BACKGROUND IMAGE
        // ==================================================

        Image(
            painter = painterResource(
                id = R.drawable.bg_turf
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        // ==================================================
        // DARK OVERLAY
        // ==================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.45f)
                )
        )


        // ==================================================
        // MAIN CONTENT
        // ==================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 40.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {


            // ==================================================
            // TITLE
            // ==================================================

            Text(
                text = "Welcome to Turf Booking",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ==================================================
            // SUBTITLE
            // ==================================================

            Text(
                text = "Choose your role to continue",
                color = Color.White.copy(
                    alpha = 0.85f
                ),
                fontSize = 16.sp
            )


            Spacer(
                modifier = Modifier.height(35.dp)
            )


            // ==================================================
            // USER
            // ==================================================

            RoleCard(
                title = "User",
                icon = "👤",
                description = "Book your favourite turf",
                badge = null,
                onClick = onUserClick
            )


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ==================================================
            // ADMIN / TURF OWNER
            // ==================================================

            RoleCard(
                title = "Turf Owner",
                icon = "🏟️",
                description = "Manage your turf and bookings",
                badge = null,
                onClick = onAdminClick
            )


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ==================================================
            // SUPER ADMIN
            // ==================================================

            RoleCard(
                title = "Super Admin",
                icon = "🛡️",
                description = "Manage users, admins and turfs",
                onClick = onSuperAdminClick
            )


            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // ==================================================
            // EXISTING LOGIN
            // ==================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Already have an account?",
                    color = Color.White,
                    fontSize = 14.sp
                )


                Spacer(
                    modifier = Modifier.width(6.dp)
                )


                Text(
                    text = "Login",
                    color = Color(0xFF81C784),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline,

                    modifier = Modifier.clickable {
                        onLoginClick()
                    }
                )
            }
        }
    }
}


// ==========================================================
// ROLE CARD
// ==========================================================

@Composable
fun RoleCard(
    title: String,
    icon: String,
    description: String,
    badge: String? = null,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(
                alpha = 0.95f
            )
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {


            // ==================================================
            // ICON CIRCLE
            // ==================================================

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = icon,
                    fontSize = 28.sp
                )
            }


            Spacer(
                modifier = Modifier.width(18.dp)
            )


            // ==================================================
            // TEXT SECTION
            // ==================================================

            Column(
                modifier = Modifier.weight(1f),

                verticalArrangement = Arrangement.Center
            ) {


                // ----------------------------------------------
                // TITLE + BADGE
                // ----------------------------------------------

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )


                    // ------------------------------------------
                    // BADGE
                    // ------------------------------------------

                    if (badge != null) {

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )


                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF2E7D32),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            Text(
                                text = badge,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                // ----------------------------------------------
                // DESCRIPTION
                // ----------------------------------------------

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }


            // ==================================================
            // ARROW
            // ==================================================

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,

                contentDescription = "Next",

                tint = Color(0xFF2E7D32),

                modifier = Modifier.size(30.dp)
            )
        }
    }
}