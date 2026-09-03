package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// BOOKMYTURF PREMIUM COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val White = Color(0xFFFFFFFF)
private val SoftGreen = Color(0xFFF1F5EC)
private val BorderGreen = Color(0xFFDCE6D7)
private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)


// ============================================================
// SPORT MODEL
// ============================================================

private data class SportItem(
    val name: String,
    val emoji: String
)


// ============================================================
// SPORT FILTER ROW
// ============================================================

@Composable
fun SportFilterRow(
    selectedSport: String,
    onSportSelected: (String) -> Unit
) {

    val sports = listOf(

        SportItem(
            name = "Football",
            emoji = "⚽"
        ),

        SportItem(
            name = "Cricket",
            emoji = "🏏"
        ),

        SportItem(
            name = "Badminton",
            emoji = "🏸"
        ),

        SportItem(
            name = "Basketball",
            emoji = "🏀"
        ),

        SportItem(
            name = "Tennis",
            emoji = "🎾"
        )
    )


    // =========================================================
    // HORIZONTAL SPORT LIST
    // =========================================================

    Row(
        modifier = Modifier
            .horizontalScroll(
                rememberScrollState()
            )
            .padding(
                start = 18.dp,
                end = 18.dp,
                top = 7.dp,
                bottom = 7.dp
            ),

        horizontalArrangement =
            Arrangement.spacedBy(10.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // =====================================================
        // ALL
        // =====================================================

        SportItemView(
            name = "All",
            emoji = "✦",
            selected =
                selectedSport.equals(
                    "All",
                    ignoreCase = true
                ),
            onClick = {
                onSportSelected("All")
            }
        )


        // =====================================================
        // SPORTS
        // =====================================================

        sports.forEach { sport ->

            SportItemView(
                name = sport.name,
                emoji = sport.emoji,
                selected =
                    selectedSport.equals(
                        sport.name,
                        ignoreCase = true
                    ),
                onClick = {
                    onSportSelected(
                        sport.name
                    )
                }
            )
        }
    }
}


// ============================================================
// SPORT ITEM
// ============================================================

@Composable
private fun SportItemView(
    name: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .clip(
                RoundedCornerShape(18.dp)
            )
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color =
                    if (selected) {
                        LightGreen
                    } else {
                        BorderGreen
                    },
                shape =
                    RoundedCornerShape(18.dp)
            ),

        shape =
            RoundedCornerShape(18.dp),

        color =
            if (selected) {
                DarkGreen
            } else {
                White
            },

        shadowElevation =
            if (selected) {
                3.dp
            } else {
                0.dp
            },

        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // =================================================
            // EMOJI CIRCLE
            // =================================================

            Row(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) {
                            ForestGreen
                        } else {
                            SoftGreen
                        }
                    ),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = emoji,
                    fontSize = 20.sp
                )
            }


            // =================================================
            // SPORT NAME
            // =================================================

            Text(
                text = name,

                modifier =
                    Modifier.padding(
                        top = 6.dp
                    ),

                fontSize = 11.sp,

                fontWeight =
                    if (selected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },

                color =
                    if (selected) {
                        White
                    } else {
                        Charcoal
                    }
            )
        }
    }
}

