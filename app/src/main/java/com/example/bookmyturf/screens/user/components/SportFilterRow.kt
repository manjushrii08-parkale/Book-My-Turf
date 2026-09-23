package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
// BOOKMYTURF PREMIUM DARK THEME
// ============================================================

private val SurfaceDark = Color(0xFF071410)
private val SurfaceSelected = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)

private val Border = Color(0xFF1A3027)

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
        SportItem("Football", "⚽"),
        SportItem("Cricket", "🏏"),
        SportItem("Box Cricket", "🏏"),
        SportItem("Badminton", "🏸"),
        SportItem("Basketball", "🏀"),
        SportItem("Tennis", "🎾")
    )

    Row(
        modifier = Modifier
            .horizontalScroll(
                rememberScrollState()
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 6.dp,
                bottom = 10.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        sports.forEach { sport ->

            val selected = selectedSport.equals(
                sport.name,
                ignoreCase = true
            )

            Surface(
                onClick = {
                    onSportSelected(sport.name)
                },
                shape = RoundedCornerShape(14.dp),
                color = if (selected) {
                    SurfaceSelected
                } else {
                    SurfaceDark
                },
                border = if (selected) {
                    androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = PrimaryGreen.copy(alpha = 0.65f)
                    )
                } else {
                    androidx.compose.foundation.BorderStroke(
                        width = 0.7.dp,
                        color = Border
                    )
                }
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sport.emoji,
                        fontSize = 16.sp
                    )

                    Text(
                        text = sport.name,
                        fontSize = 12.sp,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        },
                        color = if (selected) {
                            PrimaryText
                        } else {
                            SecondaryText
                        },
                        letterSpacing = 0.1.sp
                    )
                }
            }
        }
    }
}
