package com.example.bookmyturf.screens.admin.turf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminTurfRepository
import com.example.bookmyturf.viewmodel.AdminTurfViewModel

private val TurfGreen = Color(0xFF14532D)
private val TurfGray = Color(0xFF64748B)

@Composable
fun AdminTurfsScreen(
    token: String,
    onAddTurf: () -> Unit
) {

    // =====================================================
    // REPOSITORY
    // =====================================================

    val repository = remember {
        AdminTurfRepository(
            RetrofitClient.api
        )
    }

    // =====================================================
    // FACTORY
    // =====================================================

    val factory = remember {
        AdminTurfViewModelFactory(
            repository
        )
    }

    // =====================================================
    // VIEWMODEL
    // =====================================================

    val viewModel: AdminTurfViewModel = viewModel(
        factory = factory
    )

    // =====================================================
    // STATE
    // =====================================================

    val turfs by
    viewModel.turfs.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()

    // =====================================================
    // LOAD TURFS
    // =====================================================

    LaunchedEffect(token) {

        viewModel.loadTurfs(token)
    }

    // =====================================================
    // SCREEN
    // =====================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // =================================================
        // HEADER
        // =================================================

        Text(
            text = "Turf Management",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Manage your turf grounds and availability.",
            color = TurfGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =================================================
        // ADD TURF
        // =================================================

        Button(
            onClick = onAddTurf,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Text(
                text = "Add Turf",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =================================================
        // LOADING
        // =================================================

        if (isLoading) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator(
                    color = TurfGreen
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Loading turfs...",
                    color = TurfGray
                )
            }
        }

        // =================================================
        // ERROR
        // =================================================

        if (!error.isNullOrBlank()) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = error ?: "",
                    color = Color.Red
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {
                        viewModel.clearError()
                        viewModel.loadTurfs(token)
                    }
                ) {

                    Text(
                        text = "Retry"
                    )
                }
            }
        }

        // =================================================
        // EMPTY
        // =================================================

        if (
            !isLoading &&
            error.isNullOrBlank() &&
            turfs.isEmpty()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription = null,

                    modifier =
                        Modifier.padding(8.dp),

                    tint = TurfGreen
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "No Turfs Yet",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        "Add your first turf to get started.",

                    color = TurfGray
                )
            }
        }

        // =================================================
        // TURF LIST
        // =================================================

        if (
            !isLoading &&
            error.isNullOrBlank() &&
            turfs.isNotEmpty()
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(turfs) { turf ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = turf.name,

                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text = turf.location,
                                color = TurfGray
                            )

                            Text(
                                text = turf.city,
                                color = TurfGray
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "₹${turf.price} / slot",

                                color = TurfGreen,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "Status: ${turf.status}",

                                fontSize = 13.sp
                            )

                            Text(
                                text =
                                    "Slots: ${turf.slotsCount ?: 0}",

                                color = TurfGray,

                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}