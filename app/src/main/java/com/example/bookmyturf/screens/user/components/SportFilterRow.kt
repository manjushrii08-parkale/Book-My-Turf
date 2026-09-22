package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

private val SurfaceDark = Color(0xFF071713)
private val SurfaceSelected = Color(0xFF123D24)
private val White = Color.White
private val SecondaryText = Color(0xFF9EAAA4)
private val LightGreen = Color(0xFF7DBB4A)
private val BorderColor = Color(0xFF1D3029)

private data class SportItem(
    val name: String,
    val emoji: String
)

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
            .horizontalScroll(rememberScrollState())
            .padding(
                start = 18.dp,
                end = 18.dp,
                top = 6.dp,
                bottom = 6.dp
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
                modifier = Modifier
                    .size(
                        width = 135.dp,
                        height = 48.dp
                    )
                    .border(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) {
                            LightGreen
                        } else {
                            BorderColor
                        },
                        shape = RoundedCornerShape(14.dp)
                    ),
                shape = RoundedCornerShape(14.dp),
                color = if (selected) {
                    SurfaceSelected
                } else {
                    SurfaceDark
                },
                shadowElevation = if (selected) 3.dp else 0.dp,
                onClick = {
                    onSportSelected(sport.name)
                }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .background(Color.Transparent),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sport.emoji,
                        fontSize = 20.sp
                    )

                    Text(
                        text = sport.name,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 12.sp,
                        fontWeight = if (selected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        },
                        color = if (selected) {
                            White
                        } else {
                            SecondaryText
                        },
                        maxLines = 1
                    )
                }
            }
        }
    }
}






