package com.example.bookmyturf.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// SAME THEME AS USER HOME / TURF DETAILS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)


// ============================================================
// BOOKING SUCCESS SCREEN
// ============================================================

@Composable
fun BookingSuccessScreen(
    bookingId: Int,
    onHomeClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // =================================================
            // SUCCESS ICON
            // =================================================

            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(
                        color = LightGreen.copy(alpha = 0.16f),
                        shape = CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            color = ForestGreen,
                            shape = CircleShape
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Check,

                        contentDescription =
                            "Booking successful",

                        modifier =
                            Modifier.size(38.dp),

                        tint =
                            White
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )


            // =================================================
            // TITLE
            // =================================================

            Text(
                text =
                    "Booking Successful!",

                fontSize =
                    26.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(9.dp)
            )


            // =================================================
            // MESSAGE
            // =================================================

            Text(
                text =
                    "Your turf has been booked successfully.",

                fontSize =
                    14.sp,

                color =
                    Gray,

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    21.sp
            )


            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )


            // =================================================
            // BOOKING DETAILS CARD
            // =================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

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
                            2.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 18.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            "Booking Confirmed",

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            Gray
                    )


                    Spacer(
                        modifier =
                            Modifier.height(7.dp)
                    )


                    Text(
                        text =
                            "Booking ID",

                        fontSize =
                            12.sp,

                        color =
                            Gray
                    )


                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )


                    Text(
                        text =
                            "#$bookingId",

                        fontSize =
                            21.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            DarkGreen
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )


            // =================================================
            // BACK TO HOME
            // =================================================

            OutlinedButton(
                onClick =
                    onHomeClick,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                shape =
                    RoundedCornerShape(13.dp),

                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor =
                            DarkGreen
                    ),

                border =
                    BorderStroke(
                        width =
                            1.dp,

                        color =
                            ForestGreen
                    )
            ) {

                Text(
                    text =
                        "Back to Home",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            // =================================================
            // FOOTER MESSAGE
            // =================================================

            Text(
                text =
                    "Thank you for booking with Book My Turf.",

                fontSize =
                    11.sp,

                color =
                    Gray,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}