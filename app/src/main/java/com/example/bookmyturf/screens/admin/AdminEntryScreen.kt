package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.viewmodel.AdminViewModel

@Composable
fun AdminEntryScreen(
    token: String,
    viewModel: AdminViewModel,
    onSubscriptionActive: () -> Unit,
    onSubscriptionRequired: () -> Unit,
    onError: (String) -> Unit
) {

    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(token) {

        viewModel.checkSubscription(

            token = token,

            onActive = {
                onSubscriptionActive()
            },

            onNotActive = {
                onSubscriptionRequired()
            },

            onError = { message ->
                onError(message)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminOffWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = AdminForestGreen
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = if (isLoading) {
                "Checking subscription..."
            } else {
                "Loading admin account..."
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = AdminDarkCharcoal
        )
    }
}
