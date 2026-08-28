package com.example.bookmyturf.screens.admin


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.bookmyturf.viewmodel.AdminViewModel

private val TurfGreen = Color(0xFF14532D)

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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = TurfGreen
        )

        Text(
            text = "Checking subscription...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}