package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TurfGreen = Color(0xFF14532D)
private val TurfGray = Color(0xFF64748B)

@Composable
fun AdminBookingsContent(
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        // =====================================================
        // ICON
        // =====================================================

        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = TurfGreen
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "Bookings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Text(
            text = "View and manage bookings for your turf.",
            color = TurfGray,
            fontSize = 13.sp
        )
    }
}