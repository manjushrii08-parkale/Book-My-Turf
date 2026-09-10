package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.ui.theme.UserDarkGreen
private val White = Color(0xFFFFFFFF)
private val DividerColor = Color(0xFFE5E5E0)

@Composable
fun UserSecondaryTopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        tonalElevation = 0.dp
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 13.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(23.dp),
                        tint = UserDarkGreen
                    )
                }

                Spacer(
                    modifier = Modifier.width(11.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UserDarkGreen,
                        maxLines = 1
                    )

                    Text(
                        text = subtitle,
                        modifier = Modifier.padding(top = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE5E5E0)
            )
        }
    }
}