package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Subscriptions
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
import androidx.compose.runtime.getValue
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

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)
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
fun SuperAdminSubscriptionDetailsScreen(
    subscriptionId: Int,
    onBackClick: () -> Unit,
    viewModel: SuperAdminSubscriptionsViewModel = viewModel()
) {
    val subscription by viewModel.selectedSubscription
        .collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading
        .collectAsStateWithLifecycle()

    val error by viewModel.error
        .collectAsStateWithLifecycle()

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(subscriptionId) {
        viewModel.loadSubscriptionDetails(subscriptionId)
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subscription Details",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkGreen,
                    navigationIconContentColor = DarkGreen
                )
            )
        }
    ) { innerPadding ->

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = ForestGreen
                    )
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardWhite
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = error ?: "Something went wrong.",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            subscription != null -> {
                SubscriptionDetailsContent(
                    subscription = subscription!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Subscription details not found.",
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionDetailsContent(
    subscription: SuperAdminSubscription,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    BoxIcon(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Subscriptions,
                                contentDescription = "Subscription",
                                tint = ForestGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    )

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = subscription.user_name
                                .orEmpty()
                                .ifBlank { "User name not available" },
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = subscription.user_email
                                .orEmpty()
                                .ifBlank { "Email not available" },
                            color = TextGray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = "ID: ${subscription.id}",
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                StatusBadge(
                    status = subscription.status
                        .orEmpty()
                        .ifBlank { "UNKNOWN" }
                )
            }
        }

        DetailSectionCard(
            title = "User Information",
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User information",
                    tint = ForestGreen
                )
            }
        ) {
            DetailRow(
                label = "User ID",
                value = subscription.user_id
                    ?.toString()
                    ?: "Not available"
            )

            DetailRow(
                label = "User Name",
                value = subscription.user_name
                    .orEmpty()
                    .ifBlank { "User name not available" }
            )

            DetailRow(
                label = "Email",
                value = subscription.user_email
                    .orEmpty()
                    .ifBlank { "Email not available" }
            )
        }

        DetailSectionCard(
            title = "Subscription Information",
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Subscription information",
                    tint = ForestGreen
                )
            }
        ) {
            DetailRow(
                label = "Plan",
                value = subscription.plan
                    .orEmpty()
                    .ifBlank { "Not available" }
            )

            DetailRow(
                label = "Start Date",
                value = formatSubscriptionDate(
                    subscription.start_date
                )
            )

            DetailRow(
                label = "End Date",
                value = formatSubscriptionDate(
                    subscription.end_date
                )
            )

            DetailRow(
                label = "Created At",
                value = formatSubscriptionDate(
                    subscription.created_at
                )
            )

            DetailRow(
                label = "Trial",
                value = if (subscription.is_trial == true) {
                    "Yes"
                } else {
                    "No"
                }
            )
        }

        DetailSectionCard(
            title = "Payment Information",
            icon = {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Payment information",
                    tint = ForestGreen
                )
            }
        ) {
            DetailRow(
                label = "Amount",
                value = subscription.amount?.let { amount ->
                    "₹%.2f".format(
                        Locale.getDefault(),
                        amount
                    )
                } ?: "Not available",
                valueColor = ForestGreen
            )

            DetailRow(
                label = "Payment Status",
                value = subscription.payment_status
                    .orEmpty()
                    .ifBlank { "Not available" }
            )
        }
    }
}

@Composable
private fun BoxIcon(
    icon: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .size(58.dp)
            .background(
                color = ForestGreen.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BoxIcon(
                    icon = icon
                )

                Text(
                    text = title,
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = TextGray,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StatusBadge(
    status: String
) {
    val normalizedStatus = status.uppercase(Locale.getDefault())

    val backgroundColor = when (normalizedStatus) {
        "ACTIVE" -> ActiveBackground
        "PENDING" -> PendingBackground
        "INACTIVE", "EXPIRED", "CANCELLED" -> InactiveBackground
        else -> ScreenBackground
    }

    val textColor = when (normalizedStatus) {
        "ACTIVE" -> ActiveText
        "PENDING" -> PendingText
        "INACTIVE", "EXPIRED", "CANCELLED" -> InactiveText
        else -> TextGray
    }

    Row(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 7.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = normalizedStatus,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun formatSubscriptionDate(
    date: String?
): String {
    if (date.isNullOrBlank()) {
        return "Not available"
    }

    val inputPatterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd HH:mm:ss"
    )

    for (pattern in inputPatterns) {
        try {
            val inputFormat = SimpleDateFormat(
                pattern,
                Locale.US
            )

            if (pattern.contains("'Z'")) {
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            }

            val parsedDate = inputFormat.parse(date)

            if (parsedDate != null) {
                val outputFormat = SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
                )

                return outputFormat.format(parsedDate)
            }
        } catch (_: Exception) {
            // Try the next supported date format.
        }
    }

    return date
}