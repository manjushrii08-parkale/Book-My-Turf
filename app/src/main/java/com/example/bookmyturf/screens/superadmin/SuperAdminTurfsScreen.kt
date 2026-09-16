package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.SuperAdminTurf
import com.example.bookmyturf.viewmodel.SuperAdminTurfsViewModel

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)
private val ScreenBackground = Color(0xFFF7FAF7)
private val CardWhite = Color.White
private val TextGray = Color(0xFF78847B)
private val BlockedRed = Color(0xFFB3261E)
private val ActiveBackground = Color(0xFFE8F5E9)
private val InactiveBackground = Color(0xFFFFEBEE)
private val ActiveText = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminTurfsScreen(
    token: String,
    onBackClick: () -> Unit,
    onTurfClick: (Int) -> Unit = {},
    viewModel: SuperAdminTurfsViewModel = viewModel()
) {
    val turfs by viewModel.turfs.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val actionMessage by viewModel.actionMessage.collectAsStateWithLifecycle()

    var searchQuery by remember {
        mutableStateOf("")
    }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.loadTurfs(token)
    }

    val filteredTurfs = turfs.filter { turf ->
        val query = searchQuery.trim()

        query.isBlank() ||
                turf.id.toString().contains(query, ignoreCase = true) ||
                turf.admin_id
                    ?.toString()
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.admin_name
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.admin_email
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.name
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.location
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.city
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                turf.status
                    .orEmpty()
                    .contains(query, ignoreCase = true)
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Turfs",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkGreen
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.loadTurfs(token)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = DarkGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkGreen,
                    navigationIconContentColor = DarkGreen,
                    actionIconContentColor = DarkGreen
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ForestGreen
                    )
                },
                placeholder = {
                    Text(
                        text = "Search turfs",
                        color = TextGray
                    )
                },
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (!actionMessage.isNullOrBlank()) {
                Text(
                    text = actionMessage.orEmpty(),
                    color = ForestGreen,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            when {
                isLoading && turfs.isEmpty() -> {
                    LoadingTurfsState()
                }

                error != null && turfs.isEmpty() -> {
                    ErrorTurfsState(
                        message = error.orEmpty(),
                        onRetry = {
                            viewModel.loadTurfs(token)
                        }
                    )
                }

                filteredTurfs.isEmpty() -> {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "No turfs found."
                        } else {
                            "No matching turfs found."
                        },
                        color = TextGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            items = filteredTurfs,
                            key = { turf ->
                                turf.id
                            }
                        ) { turf ->

                            TurfCard(
                                turf = turf,
                                onClick = {
                                    onTurfClick(turf.id)
                                },
                                onStatusChange = {
                                    if (
                                        turf.status.equals(
                                            "ACTIVE",
                                            ignoreCase = true
                                        )
                                    ) {
                                        viewModel.blockTurf(
                                            turfId = turf.id,
                                            token = token
                                        )
                                    } else {
                                        viewModel.activateTurf(
                                            turfId = turf.id,
                                            token = token
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TurfCard(
    turf: SuperAdminTurf,
    onClick: () -> Unit,
    onStatusChange: () -> Unit
) {
    val isActive = turf.status.equals(
        "ACTIVE",
        ignoreCase = true
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(
                            color = ForestGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(17.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = turf.name
                            .orEmpty()
                            .ifBlank { "T" }
                            .take(1)
                            .uppercase(),
                        color = ForestGreen,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = turf.name
                            .orEmpty()
                            .ifBlank { "Turf name unavailable" },
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Managed by ${
                            turf.admin_name
                                .orEmpty()
                                .ifBlank { "Unknown admin" }
                        }",
                        color = TextGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "#${turf.id}",
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isActive) {
                                ActiveBackground
                            } else {
                                InactiveBackground
                            },
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                ) {
                    Text(
                        text = turf.status
                            .orEmpty()
                            .ifBlank { "UNKNOWN" }
                            .uppercase(),
                        color = if (isActive) {
                            ActiveText
                        } else {
                            BlockedRed
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Text(
                    text = turf.price?.let {
                        "₹$it"
                    } ?: "Price unavailable",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ScreenBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TurfInfoRow(
                    label = "Admin ID",
                    value = turf.admin_id?.toString()
                        ?: "Not available"
                )

                TurfInfoRow(
                    label = "Email",
                    value = turf.admin_email
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                TurfInfoRow(
                    label = "Location",
                    value = turf.location
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                TurfInfoRow(
                    label = "City",
                    value = turf.city
                        .orEmpty()
                        .ifBlank { "Not available" }
                )
            }

            Text(
                text = "Tap the card to view turf details",
                color = TextGray,
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = onStatusChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) {
                        BlockedRed
                    } else {
                        ForestGreen
                    }
                )
            ) {
                Text(
                    text = if (isActive) {
                        "Block Turf"
                    } else {
                        "Activate Turf"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TurfInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.width(82.dp),
            color = TextGray,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = ":",
            modifier = Modifier.width(12.dp),
            color = TextGray,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = DarkGreen,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LoadingTurfsState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            color = ForestGreen
        )

        Text(
            text = "Loading turfs...",
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorTurfsState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message.ifBlank {
                "Something went wrong."
            },
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen
            )
        ) {
            Text(
                text = "Retry",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}