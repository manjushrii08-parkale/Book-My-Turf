package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ------------------------------------------------------------
// PREMIUM BOOK MY TURF THEME
// ------------------------------------------------------------

private val Background = Color(0xFF020907)
private val Surface = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)

private val SuccessDark = Color(0xFF172C1D)


// ------------------------------------------------------------
// MAIN SCREEN
// ------------------------------------------------------------

@Composable
fun BookingSuccessScreen(
    bookingId: Int,
    onHomeClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // Very subtle background glow
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopCenter)
                .alpha(0.12f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PrimaryGreen,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ------------------------------------------------
            // SUCCESS ICON
            // ------------------------------------------------

            PremiumSuccessIcon()

            Spacer(modifier = Modifier.height(30.dp))

            // ------------------------------------------------
            // SMALL LABEL
            // ------------------------------------------------

            Text(
                text = "BOOKING CONFIRMED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = LightGreen,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ------------------------------------------------
            // TITLE
            // ------------------------------------------------

            Text(
                text = "You're all set!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.7).sp
            )

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = "Your turf booking has been confirmed.\nGet ready to play.",
                fontSize = 14.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ------------------------------------------------
            // BOOKING REFERENCE
            // ------------------------------------------------

            BookingReferenceCard(
                bookingId = bookingId
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ------------------------------------------------
            // HOME BUTTON
            // ------------------------------------------------

            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color(0xFF0B170A)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Back to Home",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ------------------------------------------------
            // FOOTER
            // ------------------------------------------------

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MutedText
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Book. Play. Enjoy.",
                    fontSize = 11.sp,
                    color = MutedText,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


// ------------------------------------------------------------
// PREMIUM SUCCESS ICON
// ------------------------------------------------------------

@Composable
private fun PremiumSuccessIcon() {

    Box(
        modifier = Modifier.size(116.dp),
        contentAlignment = Alignment.Center
    ) {

        // Outer glow
        Box(
            modifier = Modifier
                .size(116.dp)
                .background(
                    color = PrimaryGreen.copy(alpha = 0.07f),
                    shape = CircleShape
                )
        )

        // Outer ring
        Box(
            modifier = Modifier
                .size(94.dp)
                .border(
                    width = 1.dp,
                    color = PrimaryGreen.copy(alpha = 0.30f),
                    shape = CircleShape
                )
                .background(
                    color = SurfaceHighlight,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            // Inner circle
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                LightGreen,
                                PrimaryGreen
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Booking successful",
                    modifier = Modifier.size(36.dp),
                    tint = Color(0xFF10200D)
                )
            }
        }
    }
}


// ------------------------------------------------------------
// BOOKING REFERENCE CARD
// ------------------------------------------------------------

@Composable
private fun BookingReferenceCard(
    bookingId: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceElevated,
                        Surface
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(22.dp)
            )
    ) {

        // ----------------------------------------------------
        // HEADER
        // ----------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = SurfaceHighlight,
                        shape = RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    modifier = Modifier.size(21.dp),
                    tint = LightGreen
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Booking details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Your reservation is confirmed",
                    fontSize = 11.sp,
                    color = MutedText
                )
            }

            // Confirmed badge
            Box(
                modifier = Modifier
                    .background(
                        color = PrimaryGreen.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = PrimaryGreen.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
            ) {

                Text(
                    text = "CONFIRMED",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGreen,
                    letterSpacing = 0.7.sp
                )
            }
        }

        // ----------------------------------------------------
        // DIVIDER
        // ----------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )

        // ----------------------------------------------------
        // BOOKING ID
        // ----------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "BOOKING REFERENCE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MutedText,
                letterSpacing = 1.4.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "#$bookingId",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BrightGreen,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Small bottom info row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = SuccessDark,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = LightGreen
                    )
                }

                Spacer(modifier = Modifier.width(9.dp))

                Column {

                    Text(
                        text = "Turf reservation",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SecondaryText
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Payment & booking completed",
                        fontSize = 10.sp,
                        color = MutedText
                    )
                }
            }
        }
    }
}