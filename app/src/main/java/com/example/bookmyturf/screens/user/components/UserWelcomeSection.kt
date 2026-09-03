package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// BOOKMYTURF PREMIUM WELCOME SECTION
// ============================================================

@Composable
fun UserWelcomeSection(
    modifier: Modifier = Modifier
) {

    // =========================================================
    // COLORS
    // =========================================================

    val darkGreen = Color(0xFF173D20)
    val forestGreen = Color(0xFF2E6B35)
    val lightGreen = Color(0xFF7DBB4A)

    val offWhite = Color(0xFFF8F8F5)
    val white = Color(0xFFFFFFFF)

    val charcoal = Color(0xFF1C1C1C)
    val gray = Color(0xFF737373)


    // =========================================================
    // MAIN CONTENT
    // =========================================================

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            )
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp),

            color = white,

            tonalElevation = 2.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    )
            ) {

                // =================================================
                // TOP ROW
                // =================================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // -----------------------------------------
                        // ACCENT DOT
                        // -----------------------------------------

                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(
                                    lightGreen,
                                    CircleShape
                                )
                        )

                        Spacer(
                            modifier =
                                Modifier.size(8.dp)
                        )

                        Text(
                            text = "READY TO PLAY?",

                            fontSize = 11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing = 1.3.sp,

                            color = forestGreen
                        )
                    }


                    // -----------------------------------------
                    // SPORT ICON
                    // -----------------------------------------

                    Surface(
                        shape = CircleShape,

                        color =
                            darkGreen.copy(
                                alpha = 0.08f
                            )
                    ) {

                        Text(
                            text = "⚽",

                            modifier =
                                Modifier.padding(
                                    8.dp
                                ),

                            fontSize = 18.sp
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // MAIN TITLE
                // =================================================

                Text(
                    text = "Find your perfect turf.",

                    fontSize = 26.sp,

                    lineHeight = 31.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color = charcoal
                )


                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )


                // =================================================
                // DESCRIPTION
                // =================================================

                Text(
                    text =
                        "Choose a turf, pick your slot, and get ready to play.",

                    fontSize = 13.sp,

                    lineHeight = 19.sp,

                    fontWeight =
                        FontWeight.Normal,

                    color = gray
                )


                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )


                // =================================================
                // BOTTOM ACCENT
                // =================================================

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Your game starts here",

                        fontSize = 12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color = darkGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.size(5.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.ArrowForward,

                        contentDescription = null,

                        modifier =
                            Modifier.size(15.dp),

                        tint = lightGreen
                    )
                }
            }
        }
    }
}

