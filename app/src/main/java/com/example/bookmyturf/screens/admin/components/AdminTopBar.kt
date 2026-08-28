
package com.example.bookmyturf.screens.admin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

private val TurfGreen = Color(0xFF14532D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {

    TopAppBar(

        title = {
            Text(
                text = "BookMyTurf",
                fontWeight = FontWeight.Bold,
            )
        },


        actions = {

            IconButton(
                onClick = onRefresh
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }

            IconButton(
                onClick = onLogout
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout"
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        )
    )
}

