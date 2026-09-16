package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.data.model.SuperAdminBooking
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val ScreenBackground = Color(0xFFF7FAF7)
private val CardWhite = Color.White
private val TextDark = Color(0xFF1C1C1C)
private val TextGray = Color(0xFF78847B)

private val ActiveBackground = Color(0xFFE8F5E9)
private val ActiveText = Color(0xFF2E7D32)
private val PendingBackground = Color(0xFFFFF4E5)
private val PendingText = Color(0xFFB26A00)
private val InactiveBackground = Color(0xFFFFEBEE)
private val BlockedRed = Color(0xFFB3261E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminBookingDetailsScreen(
    booking: SuperAdminBooking,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Booking Details",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BookingHeaderCard(
                booking = booking
            )

            SectionCard(
                title = "Booking Information",
                icon = Icons.AutoMirrored.Filled.ReceiptLong
            ) {
                DetailRow(
                    label = "Booking ID",
                    value = "#${booking.id}"
                )

                DetailRow(
                    label = "Booking Date",
                    value = formatDate(booking.bookingDate)
                )

                DetailRow(
                    label = "Booked At",
                    value = formatDateTime(booking.bookedAt)
                )

                DetailRow(
                    label = "Created At",
                    value = formatDateTime(booking.createdAt)
                )

                DetailRow(
                    label = "Booking Status",
                    value = booking.bookingStatus
                )
            }

            SectionCard(
                title = "User Information",
                icon = Icons.Default.Person
            ) {
                DetailRow(
                    label = "User ID",
                    value = booking.user?.id?.toString()
                        ?: booking.userId.toString()
                )

                DetailRow(
                    label = "Name",
                    value = booking.user?.name
                        ?: "Not available"
                )

                DetailRow(
                    label = "Email",
                    value = booking.user?.email
                        ?: "Not available"
                )
            }

            SectionCard(
                title = "Turf Information",
                icon = Icons.Default.SportsSoccer
            ) {
                DetailRow(
                    label = "Turf ID",
                    value = booking.turf?.id?.toString()
                        ?: booking.turfId.toString()
                )

                DetailRow(
                    label = "Turf Name",
                    value = booking.turf?.name
                        ?: "Not available"
                )

                DetailRow(
                    label = "City",
                    value = booking.turf?.city
                        ?: "Not available"
                )
            }

            SectionCard(
                title = "Slot Information",
                icon = Icons.Default.Schedule
            ) {
                DetailRow(
                    label = "Slot ID",
                    value = booking.slot?.id?.toString()
                        ?: booking.slotId.toString()
                )

                DetailRow(
                    label = "Start Time",
                    value = formatTime(booking.slot?.startTime)
                )

                DetailRow(
                    label = "End Time",
                    value = formatTime(booking.slot?.endTime)
                )

                DetailRow(
                    label = "Slot Price",
                    value = "₹${booking.slot?.price ?: booking.totalAmount}"
                )

                DetailRow(
                    label = "Slot Status",
                    value = booking.slot?.status
                        ?: "Not available"
                )
            }

            SectionCard(
                title = "Payment Information",
                icon = Icons.Default.CreditCard
            ) {
                DetailRow(
                    label = "Total Amount",
                    value = "₹${booking.totalAmount}",
                    valueColor = ForestGreen,
                    valueBold = true
                )

                DetailRow(
                    label = "Payment Status",
                    value = booking.paymentStatus
                )

                DetailRow(
                    label = "Payment ID",
                    value = booking.paymentId
                        ?: "Not available"
                )

                DetailRow(
                    label = "Refund Status",
                    value = booking.refundStatus
                )

                DetailRow(
                    label = "Refunded At",
                    value = booking.refundedAt?.let {
                        formatDateTime(it)
                    } ?: "Not refunded"
                )
            }

            if (
                !booking.cancelledAt.isNullOrBlank() ||
                !booking.cancellationReason.isNullOrBlank()
            ) {
                SectionCard(
                    title = "Cancellation Information",
                    icon = Icons.Default.CalendarMonth
                ) {
                    DetailRow(
                        label = "Cancelled At",
                        value = booking.cancelledAt?.let {
                            formatDateTime(it)
                        } ?: "Not available"
                    )

                    DetailRow(
                        label = "Reason",
                        value = booking.cancellationReason
                            ?: "No reason provided"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@Composable
private fun BookingHeaderCard(
    booking: SuperAdminBooking
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
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(
                            color = ForestGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(17.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = booking.turf?.name
                            ?: "Turf Booking",
                        color = DarkGreen,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Booking #${booking.id}",
                        color = TextGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE5EAE5)
            )

            Text(
                text = "₹${booking.totalAmount}",
                color = ForestGreen,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusBadge(
                    text = booking.bookingStatus
                )

                StatusBadge(
                    text = booking.paymentStatus
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = ForestGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(10.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            HorizontalDivider(
                color = Color(0xFFE5EAE5)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            content()
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = TextDark,
    valueBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.9f),
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            modifier = Modifier.weight(1.1f),
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (valueBold) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            }
        )
    }
}

@Composable
private fun StatusBadge(
    text: String
) {
    val normalizedStatus = text
        .uppercase(Locale.getDefault())

    val badgeColor = when (normalizedStatus) {
        "PAID",
        "CONFIRMED",
        "COMPLETED",
        "ACTIVE" -> ActiveBackground

        "PENDING" -> PendingBackground

        "CANCELLED",
        "FAILED",
        "REFUNDED",
        "BLOCKED" -> InactiveBackground

        else -> Color(0xFFEFF3EF)
    }

    val textColor = when (normalizedStatus) {
        "PAID",
        "CONFIRMED",
        "COMPLETED",
        "ACTIVE" -> ActiveText

        "PENDING" -> PendingText

        "CANCELLED",
        "FAILED",
        "REFUNDED",
        "BLOCKED" -> BlockedRed

        else -> TextGray
    }

    Surface(
        color = badgeColor,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = normalizedStatus,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDate(
    value: String
): String {
    return try {
        val inputFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd"
        )

        val outputFormat = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        inputFormats.forEach { pattern ->
            try {
                val inputFormat = SimpleDateFormat(
                    pattern,
                    Locale.getDefault()
                )

                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val parsedDate = inputFormat.parse(value)

                if (parsedDate != null) {
                    return outputFormat.format(parsedDate)
                }
            } catch (_: Exception) {
                // Try the next format.
            }
        }

        value.substringBefore("T")
    } catch (_: Exception) {
        value
    }
}

private fun formatDateTime(
    value: String
): String {
    return try {
        val inputFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX"
        )

        val outputFormat = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )

        inputFormats.forEach { pattern ->
            try {
                val inputFormat = SimpleDateFormat(
                    pattern,
                    Locale.getDefault()
                )

                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val parsedDate = inputFormat.parse(value)

                if (parsedDate != null) {
                    return outputFormat.format(parsedDate)
                }
            } catch (_: Exception) {
                // Try the next format.
            }
        }

        value
    } catch (_: Exception) {
        value
    }
}

private fun formatTime(
    value: String?
): String {
    if (value.isNullOrBlank()) {
        return "Not available"
    }

    return try {
        val inputFormats = listOf(
            "HH:mm:ss",
            "HH:mm"
        )

        val outputFormat = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )

        inputFormats.forEach { pattern ->
            try {
                val inputFormat = SimpleDateFormat(
                    pattern,
                    Locale.getDefault()
                )

                val parsedTime = inputFormat.parse(value)

                if (parsedTime != null) {
                    return outputFormat.format(parsedTime)
                }
            } catch (_: Exception) {
                // Try the next format.
            }
        }

        value
    } catch (_: Exception) {
        value
    }
}