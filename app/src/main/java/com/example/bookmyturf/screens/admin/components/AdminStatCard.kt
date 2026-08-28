package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TurfGreen = Color(0xFF14532D)
private val TurfGreenLight = Color(0xFFF0FDF4)
private val TurfDark = Color(0xFF0F172A)
private val TurfGray = Color(0xFF64748B)
private val TurfBorder = Color(0xFFE2E8F0)

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        border = BorderStroke(
            width = 1.dp,
            color = TurfBorder
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // =================================================
                // ICON
                // =================================================

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(
                            RoundedCornerShape(11.dp)
                        )
                        .background(TurfGreenLight),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp),
                        tint = TurfGreen
                    )
                }

                Spacer(
                    modifier = Modifier.size(10.dp)
                )

                // =================================================
                // TITLE
                // =================================================

                Text(
                    text = title,

                    modifier = Modifier.weight(1f),

                    fontSize = 12.sp,

                    lineHeight = 15.sp,

                    fontWeight = FontWeight.Medium,

                    color = TurfGray,

                    maxLines = 2,

                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // VALUE
            // =================================================

            Text(
                text = value,

                fontSize = 25.sp,

                lineHeight = 30.sp,

                fontWeight = FontWeight.Bold,

                color = TurfDark,

                maxLines = 1,

                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

