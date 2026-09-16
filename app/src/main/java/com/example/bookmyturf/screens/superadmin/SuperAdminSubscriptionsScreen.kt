package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subscriptions
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
import com.example.bookmyturf.data.model.SuperAdminSubscription
import com.example.bookmyturf.viewmodel.SuperAdminSubscriptionsViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import androidx.compose.foundation.layout.ColumnScope

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val ScreenBackground = Color(0xFFF7FAF7)
private val CardWhite = Color.White
private val TextGray = Color(0xFF78847B)

private val ActiveBackground = Color(0xFFE8F5E9)
private val ActiveText = Color(0xFF2E7D32)

private val InactiveBackground = Color(0xFFFFEBEE)
private val InactiveText = Color(0xFFB3261E)

private val PendingBackground = Color(0xFFFFF4E5)
private val PendingText = Color(0xFFB26A00)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminSubscriptionsScreen(
    onBackClick: () -> Unit,
    onSubscriptionClick: (Int) -> Unit = {},
    viewModel: SuperAdminSubscriptionsViewModel = viewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var searchQuery by remember {
        mutableStateOf("")
    }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.loadSubscriptions()
    }

    val filteredSubscriptions = subscriptions.filter { subscription ->
        val query = searchQuery.trim()

        query.isBlank() ||
                subscription.id.toString().contains(
                    query,
                    ignoreCase = true
                ) ||
                subscription.user_id
                    ?.toString()
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                subscription.user_name
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                subscription.user_email
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                subscription.plan
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                subscription.status
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                subscription.payment_status
                    .orEmpty()
                    .contains(query, ignoreCase = true)
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subscriptions",
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
                        onClick = viewModel::loadSubscriptions
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
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                )
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
                        contentDescription = "Search subscriptions",
                        tint = ForestGreen
                    )
                },
                placeholder = {
                    Text(
                        text = "Search subscriptions",
                        color = TextGray
                    )
                },
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            when {
                isLoading && subscriptions.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = ForestGreen
                        )
                    }
                }

                error != null && subscriptions.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = error ?: "Something went wrong.",
                            color = MaterialTheme.colorScheme.error
                        )

                        Button(
                            onClick = viewModel::loadSubscriptions,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }

                filteredSubscriptions.isEmpty() -> {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "No subscriptions found."
                        } else {
                            "No matching subscriptions found."
                        },
                        color = TextGray
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            items = filteredSubscriptions,
                            key = { subscription ->
                                subscription.id
                            }
                        ) { subscription ->

                            SubscriptionCard(
                                subscription = subscription,
                                onClick = {
                                    onSubscriptionClick(subscription.id)
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
private fun SubscriptionCard(
    subscription: SuperAdminSubscription,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .width(58.dp)
                            .height(58.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Subscriptions,
                            contentDescription = "Subscription",
                            tint = ForestGreen
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = subscription.user_name
                                .orEmpty()
                                .ifBlank { "User Name not available" },
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = subscription.user_email
                                .orEmpty()
                                .ifBlank { "Email not available" },
                            color = TextGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = "ID: ${subscription.id}",
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            InfoContainer {
                InfoRow(
                    label = "User ID",
                    value = subscription.user_id?.toString()
                        ?: "Not available"
                )

                InfoRow(
                    label = "Plan",
                    value = subscription.plan
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                InfoRow(
                    label = "Status",
                    value = subscription.status
                        .orEmpty()
                        .ifBlank { "UNKNOWN" },
                    valueColor = getStatusTextColor(subscription.status)
                )

                InfoRow(
                    label = "Start Date",
                    value = formatSubscriptionDate(
                        subscription.start_date
                    )
                )

                InfoRow(
                    label = "End Date",
                    value = formatSubscriptionDate(
                        subscription.end_date
                    )
                )

                InfoRow(
                    label = "Amount",
                    value = subscription.amount?.let { amount ->
                        "₹%.2f".format(
                            Locale.getDefault(),
                            amount
                        )
                    } ?: "Not available",
                    valueColor = ForestGreen
                )

                InfoRow(
                    label = "Payment",
                    value = subscription.payment_status
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                InfoRow(
                    label = "Trial",
                    value = if (subscription.is_trial == true) {
                        "Yes"
                    } else {
                        "No"
                    }
                )

                InfoRow(
                    label = "Created At",
                    value = formatSubscriptionDate(
                        subscription.created_at
                    )
                )
            }
        }
    }
}

@Composable
private fun InfoContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.width(100.dp),
            color = TextGray,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun getStatusTextColor(status: String?): Color {
    return when (status?.uppercase(Locale.getDefault())) {
        "ACTIVE" -> ActiveText
        "INACTIVE", "EXPIRED", "CANCELLED" -> InactiveText
        "PENDING" -> PendingText
        else -> Color.Black
    }
}

private fun formatSubscriptionDate(
    date: String?
): String {
    if (date.isNullOrBlank()) {
        return "Not available"
    }

    return try {
        val inputFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss"
        )

        val parsedDate = inputFormats.firstNotNullOfOrNull { pattern ->
            try {
                SimpleDateFormat(
                    pattern,
                    Locale.getDefault()
                ).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(date)
            } catch (_: Exception) {
                null
            }
        }

        if (parsedDate != null) {
            val outputFormat = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

            outputFormat.format(parsedDate)
        } else {
            date
        }
    } catch (_: Exception) {
        date
    }
}