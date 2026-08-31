package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory

@Composable
fun TurfDetailsScreen(
    turfId: Int,
    onBackClick: () -> Unit,
    viewModel: TurfDetailsViewModel = viewModel(
        factory = TurfDetailsViewModelFactory(
            TurfRepository()
        )
    )
) {

    val turf by viewModel.turf.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // =========================================================
    // LOAD TURF
    // =========================================================

    LaunchedEffect(turfId) {

        viewModel.loadTurf(turfId)
    }


    // =========================================================
    // LOADING
    // =========================================================

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }

        return
    }


    // =========================================================
    // ERROR
    // =========================================================

    if (error != null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = error
                    ?: "Unable to load turf.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        return
    }


    // =========================================================
    // TURF
    // =========================================================

    val currentTurf = turf

    if (currentTurf == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Turf not found.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        return
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        // =====================================================
        // TURF NAME
        // =====================================================

        Text(
            text = currentTurf.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // =====================================================
        // LOCATION
        // =====================================================

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Default.LocationOn,
                contentDescription = "Location"
            )

            Text(
                text = "${currentTurf.location}, ${currentTurf.city}",
                modifier = Modifier.padding(
                    start = 6.dp
                )
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // RATING
        // =====================================================

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Default.Star,
                contentDescription = "Rating"
            )

            Text(
                text =
                    "${currentTurf.rating} (${currentTurf.reviewCount} reviews)",
                modifier = Modifier.padding(
                    start = 6.dp
                )
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // PRICE
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(16.dp)
        ) {

            Column {

                Text(
                    text = "Starting Price",
                    style =
                        MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "₹${currentTurf.price} / slot",
                    style =
                        MaterialTheme.typography.titleLarge
                )
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // SPORTS
        // =====================================================

        Text(
            text = "Sports",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                imageVector =
                    Icons.Default.SportsSoccer,
                contentDescription = "Sports"
            )

            Text(
                text =
                    currentTurf.sportsTypes.joinToString(", ")
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // DESCRIPTION
        // =====================================================

        Text(
            text = "About this turf",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                currentTurf.description
                    ?: "No description available."
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // AMENITIES
        // =====================================================

        Text(
            text = "Amenities",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                if (currentTurf.amenities.isNotEmpty()) {

                    currentTurf.amenities.joinToString(
                        separator = " • "
                    )

                } else {

                    "No amenities listed."
                }
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // ADDRESS
        // =====================================================

        Text(
            text = "Address",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                currentTurf.address
                    ?: currentTurf.location
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // OPERATING HOURS
        // =====================================================

        Text(
            text = "Operating Hours",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "${currentTurf.openingTime} - ${currentTurf.closingTime}"
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =====================================================
        // AVAILABLE SLOTS
        // =====================================================

        Text(
            text = "Available Slots",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "${currentTurf.slotsCount ?: 0} slots available"
        )


        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}