package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.R

// ============================================================
// BOOKMYTURF HORIZONTAL HERO SECTION
// ============================================================

@Composable
fun UserWelcomeSection(
    modifier: Modifier = Modifier
) {

    // =========================================================
    // COLORS
    // =========================================================

    val darkGreen = Color(0xFF06130F)
    val forestGreen = Color(0xFF123D24)
    val lightGreen = Color(0xFF7DBB4A)

    val white = Color.White
    val secondaryWhite = Color(0xFFD9E3DD)

    val inactiveIndicator = Color(0xFF294238)

    // =========================================================
    // PAGER
    // =========================================================

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 }
    )

    // =========================================================
    // MAIN
    // =========================================================

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 6.dp
            )
    ) {

        // =====================================================
        // HORIZONTAL HERO
        // =====================================================

        HorizontalPager(
            state = pagerState,

            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp),

            contentPadding = PaddingValues(
                horizontal = 8.dp
            ),

            pageSpacing = 10.dp
        ) { page ->

            // =================================================
            // HERO CARD
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
            ) {

                // =================================================
                // IMAGE
                // =================================================

                androidx.compose.foundation.Image(
                    painter = painterResource(
                        id = R.drawable.bookmyturf_hero
                    ),

                    contentDescription = null,

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop
                )

                // =================================================
                // LEFT OVERLAY
                // =================================================

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    darkGreen.copy(
                                        alpha = 0.94f
                                    ),

                                    forestGreen.copy(
                                        alpha = 0.72f
                                    ),

                                    Color.Transparent
                                )
                            )
                        )
                )

                // =================================================
                // BOTTOM OVERLAY
                // =================================================

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    darkGreen.copy(
                                        alpha = 0.68f
                                    )
                                )
                            )
                        )
                )

                // =================================================
                // CONTENT
                // =================================================

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                ) {

                    // =================================================
                    // LABEL
                    // =================================================

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    lightGreen,
                                    CircleShape
                                )
                        )

                        Spacer(
                            modifier = Modifier.size(6.dp)
                        )

                        Text(
                            text = when (page) {
                                0 -> "READY TO PLAY?"
                                1 -> "DISCOVER YOUR GAME"
                                else -> "BOOK YOUR SLOT"
                            },

                            fontSize = 9.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing = 1.1.sp,

                            color = lightGreen
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // =================================================
                    // TITLE
                    // =================================================

                    Text(
                        text = when (page) {
                            0 -> "Book Your\nPerfect Turf"
                            1 -> "Find Your\nPerfect Turf"
                            else -> "Play More.\nWorry Less."
                        },

                        fontSize = 22.sp,

                        lineHeight = 24.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color = white
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    // =================================================
                    // DESCRIPTION
                    // =================================================

                    Text(
                        text = when (page) {
                            0 ->
                                "Find your game. Pick your slot.\nGet ready to play."

                            1 ->
                                "Explore turfs near you.\nChoose the perfect place to play."

                            else ->
                                "Select your preferred time.\nConfirm your booking instantly."
                        },

                        fontSize = 10.sp,

                        lineHeight = 14.sp,

                        color = secondaryWhite
                    )
                }
            }
        }

        // =====================================================
        // PAGE INDICATORS
        // =====================================================

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.Center,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            repeat(3) { index ->

                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = 3.dp
                        )
                        .size(
                            width =
                                if (
                                    pagerState.currentPage == index
                                ) {
                                    18.dp
                                } else {
                                    6.dp
                                },

                            height = 6.dp
                        )
                        .clip(CircleShape)
                        .background(
                            if (
                                pagerState.currentPage == index
                            ) {
                                lightGreen
                            } else {
                                inactiveIndicator
                            }
                        )
                )
            }
        }
    }
}