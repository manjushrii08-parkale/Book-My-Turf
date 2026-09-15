package com.example.bookmyturf.screens.superadmin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.viewmodel.SuperAdminViewModel

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val ScreenBackground = Color(0xFFF5F7F5)

private data class StatisticsItem(
    val title: String,
    val value: String,
    val onClick: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminDashboardScreen(
    onLogout: () -> Unit,
    onUsersClick: () -> Unit
) {
    val viewModel: SuperAdminViewModel = viewModel()

    val dashboard by viewModel.dashboard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Super Admin Dashboard",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.loadDashboard()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh dashboard"
                        )
                    }

                    IconButton(
                        onClick = onLogout
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = ScreenBackground
    ) { innerPadding ->

        when {
            isLoading && dashboard == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = ForestGreen
                    )
                }
            }

            error != null && dashboard == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = error ?: "Unable to load dashboard.",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )

                        IconButton(
                            onClick = {
                                viewModel.loadDashboard()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Retry"
                            )
                        }
                    }
                }
            }

            dashboard != null -> {
                val data = dashboard!!

                val statistics = listOf(
                    StatisticsItem(
                        title = "Total Users",
                        value = data.users.total.toString(),
                        onClick = onUsersClick
                    ),
                    StatisticsItem(
                        title = "Active Users",
                        value = data.users.active.toString()
                    ),
                    StatisticsItem(
                        title = "Blocked Users",
                        value = data.users.blocked.toString()
                    ),
                    StatisticsItem(
                        title = "Total Admins",
                        value = data.admins.total.toString()
                    ),
                    StatisticsItem(
                        title = "Active Admins",
                        value = data.admins.active.toString()
                    ),
                    StatisticsItem(
                        title = "Blocked Admins",
                        value = data.admins.blocked.toString()
                    ),
                    StatisticsItem(
                        title = "Super Admins",
                        value = data.super_admins.total.toString()
                    ),
                    StatisticsItem(
                        title = "Total Subscriptions",
                        value = data.subscriptions.total.toString()
                    ),
                    StatisticsItem(
                        title = "Active Subscriptions",
                        value = data.subscriptions.active.toString()
                    ),
                    StatisticsItem(
                        title = "Inactive Subscriptions",
                        value = data.subscriptions.inactive.toString()
                    ),
                    StatisticsItem(
                        title = "Pending Payments",
                        value = data.subscriptions.pending_payments.toString()
                    ),
                    StatisticsItem(
                        title = "Paid Subscriptions",
                        value = data.subscriptions.paid.toString()
                    ),
                    StatisticsItem(
                        title = "Free Trials",
                        value = data.subscriptions.free_trials.toString()
                    ),
                    StatisticsItem(
                        title = "Total Turfs",
                        value = data.turfs.total.toString()
                    ),
                    StatisticsItem(
                        title = "Total Bookings",
                        value = data.bookings.total.toString()
                    ),
                    StatisticsItem(
                        title = "Paid Revenue",
                        value = "₹${data.subscriptions.total_paid_revenue}"
                    )
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = statistics
                    ) { item ->
                        StatisticsCard(
                            title = item.title,
                            value = item.value,
                            onClick = item.onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        onClick()
                    }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = value,
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}