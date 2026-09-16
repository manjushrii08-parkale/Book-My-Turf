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
import com.example.bookmyturf.data.model.SuperAdminBooking
import com.example.bookmyturf.viewmodel.SuperAdminBookingsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val ScreenBackground = Color(0xFFF7FAF7)
private val CardWhite = Color.White
private val TextGray = Color(0xFF78847B)

private val ActiveBackground = Color(0xFFE8F5E9)
private val InactiveBackground = Color(0xFFFFEBEE)
private val ActiveText = Color(0xFF2E7D32)
private val BlockedRed = Color(0xFFB3261E)

private val PendingBackground = Color(0xFFFFF4E5)
private val PendingText = Color(0xFFB26A00)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminBookingsScreen(
    token: String,
    onBackClick: () -> Unit,
    onBookingClick: (SuperAdminBooking) -> Unit = {},
    viewModel: SuperAdminBookingsViewModel = viewModel()
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()

    var searchQuery by remember {
        mutableStateOf("")
    }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(token) {
        viewModel.loadBookings(token)
    }

    val filteredBookings = bookings.filter { booking ->
        val query = searchQuery.trim()

        query.isBlank() ||
                booking.id.toString().contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.userId.toString().contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.turfId.toString().contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.slotId.toString().contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.user?.name
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                booking.user?.email
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                booking.turf?.name
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                booking.turf?.city
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                booking.bookingStatus.contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.paymentStatus.contains(
                    query,
                    ignoreCase = true
                ) ||
                booking.refundStatus.contains(
                    query,
                    ignoreCase = true
                )
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bookings",
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
                            viewModel.loadBookings(token)
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
                        contentDescription = "Search",
                        tint = ForestGreen
                    )
                },
                placeholder = {
                    Text(
                        text = "Search bookings",
                        color = TextGray
                    )
                },
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            when {
                isLoading && bookings.isEmpty() -> {
                    LoadingBookingsState()
                }

                error != null && bookings.isEmpty() -> {
                    ErrorBookingsState(
                        message = error.orEmpty(),
                        onRetry = {
                            viewModel.loadBookings(token)
                        }
                    )
                }

                filteredBookings.isEmpty() -> {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "No bookings found."
                        } else {
                            "No matching bookings found."
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
                            items = filteredBookings,
                            key = { booking ->
                                booking.id
                            }
                        ) { booking ->

                            BookingCard(
                                booking = booking,
                                onClick = {
                                    onBookingClick(booking)
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
private fun BookingCard(
    booking: SuperAdminBooking,
    onClick: () -> Unit
) {
    val bookingStatus = booking.bookingStatus
        .ifBlank { "UNKNOWN" }
        .uppercase(Locale.getDefault())

    val paymentStatus = booking.paymentStatus
        .ifBlank { "UNKNOWN" }
        .uppercase(Locale.getDefault())

    val refundStatus = booking.refundStatus
        .ifBlank { "NOT AVAILABLE" }
        .uppercase(Locale.getDefault())

    val statusBackground = when (bookingStatus) {
        "CONFIRMED",
        "COMPLETED" -> ActiveBackground

        "PENDING" -> PendingBackground

        "CANCELLED",
        "REJECTED" -> InactiveBackground

        else -> ScreenBackground
    }

    val statusTextColor = when (bookingStatus) {
        "CONFIRMED",
        "COMPLETED" -> ActiveText

        "PENDING" -> PendingText

        "CANCELLED",
        "REJECTED" -> BlockedRed

        else -> TextGray
    }

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
                        text = booking.turf?.name
                            .orEmpty()
                            .ifBlank { "B" }
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
                        text = booking.turf?.name
                            .orEmpty()
                            .ifBlank { "Turf unavailable" },
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Booked by ${
                            booking.user?.name
                                .orEmpty()
                                .ifBlank { "Unknown user" }
                        }",
                        color = TextGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "#${booking.id}",
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
                            color = statusBackground,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                ) {
                    Text(
                        text = bookingStatus,
                        color = statusTextColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Text(
                    text = "₹${booking.totalAmount}",
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
                BookingInfoRow(
                    label = "User ID",
                    value = booking.userId.toString()
                )

                BookingInfoRow(
                    label = "User",
                    value = booking.user?.name
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                BookingInfoRow(
                    label = "Email",
                    value = booking.user?.email
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                BookingInfoRow(
                    label = "Turf ID",
                    value = booking.turfId.toString()
                )

                BookingInfoRow(
                    label = "City",
                    value = booking.turf?.city
                        .orEmpty()
                        .ifBlank { "Not available" }
                )

                BookingInfoRow(
                    label = "Slot ID",
                    value = booking.slotId.toString()
                )

                BookingInfoRow(
                    label = "Booking Date",
                    value = formatDate(booking.bookingDate)
                )

                BookingInfoRow(
                    label = "Slot",
                    value = formatTime(booking.slot?.startTime) +
                            " - " +
                            formatTime(booking.slot?.endTime)
                )

                BookingInfoRow(
                    label = "Payment",
                    value = paymentStatus
                )

                BookingInfoRow(
                    label = "Refund",
                    value = refundStatus
                )
            }

            Text(
                text = "Tap the card to view booking details",
                color = TextGray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun BookingInfoRow(
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
private fun LoadingBookingsState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            color = ForestGreen
        )

        Text(
            text = "Loading bookings...",
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorBookingsState(
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

private fun formatDate(
    value: String?
): String {
    if (value.isNullOrBlank()) {
        return "Not available"
    }

    return try {
        val inputFormats = listOf(
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                Locale.US
            ),
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.US
            ),
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )
        )

        val parsedDate = inputFormats.firstNotNullOfOrNull { format ->
            try {
                format.timeZone = TimeZone.getTimeZone("UTC")
                format.parse(value)
            } catch (_: Exception) {
                null
            }
        }

        val outputFormat = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        parsedDate?.let {
            outputFormat.format(it)
        } ?: value.substringBefore("T")
    } catch (_: Exception) {
        value.substringBefore("T")
    }
}

private fun formatTime(
    value: String?
): String {
    if (value.isNullOrBlank()) {
        return "N/A"
    }

    return try {
        val inputFormat = SimpleDateFormat(
            "HH:mm:ss",
            Locale.US
        )

        val outputFormat = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )

        val date: Date = inputFormat.parse(value)
            ?: return value

        outputFormat.format(date)
    } catch (_: Exception) {
        value
    }
}