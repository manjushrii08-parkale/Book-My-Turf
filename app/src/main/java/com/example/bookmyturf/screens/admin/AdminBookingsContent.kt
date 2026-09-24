package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminBookingRepository
import com.example.bookmyturf.viewmodel.AdminBookingViewModel
import com.example.bookmyturf.viewmodel.AdminBookingViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

// =============================================================
// PREMIUM ADMIN COLORS
// Same visual system as Subscription + Dashboard + Turfs
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)
private val WarningOrange = Color(0xFFFFB454)
private val InfoBlue = Color(0xFF75A9FF)


// =============================================================
// ADMIN BOOKINGS CONTENT
// =============================================================

@Composable
fun AdminBookingsContent(
    modifier: Modifier = Modifier,
    token: String
) {

    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository = remember {
        AdminBookingRepository(
            RetrofitClient.api
        )
    }

    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: AdminBookingViewModel = viewModel(
        factory = remember {
            AdminBookingViewModelFactory(repository)
        }
    )

    // =========================================================
    // STATE
    // =========================================================

    val bookingsResponse by
    viewModel.adminBookings.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()

    // =========================================================
    // LOAD BOOKINGS
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {

            viewModel.loadAdminBookings(token)
        }
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {

        when {

            // =================================================
            // LOADING
            // =================================================

            isLoading && bookingsResponse == null -> {

                LoadingBookings()
            }

            // =================================================
            // ERROR
            // =================================================

            !error.isNullOrBlank() &&
                    bookingsResponse == null -> {

                BookingErrorState(
                    message = error
                        ?: "Something went wrong.",
                    onRetry = {
                        if (token.isNotBlank()) {
                            viewModel.loadAdminBookings(token)
                        }
                    }
                )
            }

            // =================================================
            // CONTENT
            // =================================================

            else -> {

                val bookings =
                    bookingsResponse
                        ?.data
                        ?.bookings
                        ?: emptyList()

                val bookingCount =
                    bookingsResponse
                        ?.data
                        ?.booking_count
                        ?: bookings.size

                if (bookings.isEmpty()) {

                    EmptyBookingsState()

                } else {

                    BookingList(
                        bookings = bookings,
                        bookingCount = bookingCount
                    )
                }
            }
        }
    }
}


// =============================================================
// BOOKING LIST
// =============================================================

@Composable
private fun BookingList(
    bookings: List<Booking>,
    bookingCount: Int
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 14.dp,
            bottom = 30.dp
        ),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        item {

            Column {

                Text(
                    text = "Bookings",
                    color = PrimaryText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = PrimaryGreen.copy(alpha = 0.10f)
                ) {

                    Text(
                        text = "$bookingCount total bookings",

                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 6.dp
                        ),

                        color = LightGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // =====================================================
        // BOOKINGS
        // =====================================================

        items(
            items = bookings,
            key = { it.id }
        ) { booking ->

            AdminBookingCard(
                booking = booking
            )
        }
    }
}


// =============================================================
// LOADING
// =============================================================

@Composable
private fun LoadingBookings() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(62.dp),
            shape = RoundedCornerShape(18.dp),
            color = SurfaceElevated
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = PrimaryGreen,
                    strokeWidth = 2.5.dp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(13.dp)
        )

        Text(
            text = "Loading bookings...",
            color = SecondaryText,
            fontSize = 12.sp
        )
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun BookingErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(18.dp),
            color = ErrorRed.copy(alpha = 0.08f)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = ErrorRed
                )
            }
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Text(
            text = "Unable to load bookings",
            color = PrimaryText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = message,
            color = SecondaryText,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(11.dp),
            border = BorderStroke(
                1.dp,
                Border
            ),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                containerColor = SurfaceElevated,
                contentColor = PrimaryText
            )
        ) {

            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(17.dp)
            )

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Text(
                text = "Retry",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


// =============================================================
// EMPTY
// =============================================================

@Composable
private fun EmptyBookingsState() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(76.dp),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceElevated
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CalendarMonth,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(34.dp),

                    tint =
                        PrimaryGreen
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(17.dp)
        )

        Text(
            text =
                "No Bookings Yet",

            color =
                PrimaryText,

            style =
                MaterialTheme.typography.titleLarge,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "Bookings for your turfs will appear here.",

            color =
                SecondaryText,

            fontSize =
                12.sp
        )
    }
}


// =============================================================
// BOOKING CARD
// =============================================================

@Composable
private fun AdminBookingCard(
    booking: Booking
) {

    val customerName =
        booking.user?.name
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "Customer"

    val customerEmail =
        booking.user?.email
            ?.takeIf {
                it.isNotBlank()
            }

    val turfName =
        booking.turf?.name
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "Turf"

    val slotStart =
        booking.slot?.startTime

    val slotEnd =
        booking.slot?.endTime

    val slotTime =
        if (
            !slotStart.isNullOrBlank() &&
            !slotEnd.isNullOrBlank()
        ) {

            "${formatSlotTime(slotStart)} - " +
                    formatSlotTime(slotEnd)

        } else {

            "Slot unavailable"
        }

    val bookingStatus =
        booking.booking_status
            .replace("_", " ")
            .uppercase()

    val paymentStatus =
        booking.payment_status
            .replace("_", " ")
            .uppercase()

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            // =================================================
            // CUSTOMER
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    modifier =
                        Modifier.size(46.dp),

                    shape =
                        CircleShape,

                    color =
                        PrimaryGreen.copy(
                            alpha = 0.10f
                        )
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(23.dp),

                            tint =
                                PrimaryGreen
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.width(11.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            customerName,

                        color =
                            PrimaryText,

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        maxLines =
                            1
                    )

                    customerEmail?.let { email ->

                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )

                        Text(
                            text =
                                email,

                            color =
                                SecondaryText,

                            fontSize =
                                11.sp,

                            maxLines =
                                1
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Surface(
                    shape =
                        RoundedCornerShape(8.dp),

                    color =
                        SurfaceElevated
                ) {

                    Text(
                        text =
                            "#${booking.id}",

                        modifier =
                            Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 5.dp
                            ),

                        color =
                            MutedText,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            // =================================================
            // TURF
            // =================================================

            BookingInfoRow(
                icon =
                    Icons.Default.SportsSoccer,

                title =
                    "TURF",

                value =
                    turfName
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // =================================================
            // DATE
            // =================================================

            BookingInfoRow(
                icon =
                    Icons.Default.CalendarMonth,

                title =
                    "BOOKING DATE",

                value =
                    formatBookingDate(
                        booking.booking_date
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // =================================================
            // TIME
            // =================================================

            BookingInfoRow(
                icon =
                    Icons.Default.CalendarMonth,

                title =
                    "TIME SLOT",

                value =
                    slotTime
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            HorizontalDivider(
                color =
                    Border
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // AMOUNT
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "TOTAL AMOUNT",

                        color =
                            MutedText,

                        fontSize =
                            9.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            0.8.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "₹${formatAmount(
                                booking.total_amount
                            )}",

                        color =
                            PrimaryText,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // STATUS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                BookingStatusBadge(
                    modifier =
                        Modifier.weight(1f),

                    label =
                        "Booking",

                    status =
                        bookingStatus
                )

                BookingStatusBadge(
                    modifier =
                        Modifier.weight(1f),

                    label =
                        "Payment",

                    status =
                        paymentStatus
                )
            }
        }
    }
}


// =============================================================
// BOOKING INFO ROW
// =============================================================

@Composable
private fun BookingInfoRow(
    icon: ImageVector,
    title: String,
    value: String
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(12.dp),

        color =
            SurfaceElevated
    ) {

        Row(
            modifier =
                Modifier.padding(11.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                modifier =
                    Modifier.size(36.dp),

                shape =
                    RoundedCornerShape(10.dp),

                color =
                    PrimaryGreen.copy(
                        alpha = 0.10f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            icon,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(18.dp),

                        tint =
                            PrimaryGreen
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        title,

                    color =
                        MutedText,

                    fontSize =
                        9.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        0.7.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        value,

                    color =
                        PrimaryText,

                    fontSize =
                        12.sp,

                    fontWeight =
                        FontWeight.Medium,

                    maxLines =
                        2
                )
            }
        }
    }
}


// =============================================================
// STATUS BADGE
// =============================================================

@Composable
private fun BookingStatusBadge(
    modifier: Modifier = Modifier,
    label: String,
    status: String
) {

    val normalizedStatus =
        status
            .replace("_", " ")
            .uppercase()

    val backgroundColor: Color
    val contentColor: Color
    val icon: ImageVector

    when {

        normalizedStatus.contains("CONFIRMED") ||
                normalizedStatus.contains("PAID") ||
                normalizedStatus.contains("SUCCESS") -> {

            backgroundColor =
                PrimaryGreen.copy(
                    alpha = 0.10f
                )

            contentColor =
                LightGreen

            icon =
                Icons.Default.CheckCircle
        }

        normalizedStatus.contains("CANCELLED") ||
                normalizedStatus.contains("FAILED") ||
                normalizedStatus.contains("REFUNDED") -> {

            backgroundColor =
                ErrorRed.copy(
                    alpha = 0.08f
                )

            contentColor =
                ErrorRed

            icon =
                Icons.Default.Warning
        }

        normalizedStatus.contains("PENDING") ||
                normalizedStatus.contains("ELIGIBLE") -> {

            backgroundColor =
                WarningOrange.copy(
                    alpha = 0.08f
                )

            contentColor =
                WarningOrange

            icon =
                Icons.Default.Warning
        }

        else -> {

            backgroundColor =
                InfoBlue.copy(
                    alpha = 0.08f
                )

            contentColor =
                InfoBlue

            icon =
                Icons.Default.CalendarMonth
        }
    }

    Surface(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(12.dp),

        color =
            backgroundColor
    ) {

        Row(
            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 9.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    icon,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(16.dp),

                tint =
                    contentColor
            )

            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )

            Column {

                Text(
                    text =
                        label,

                    color =
                        MutedText,

                    fontSize =
                        9.sp
                )

                Text(
                    text =
                        normalizedStatus,

                    color =
                        contentColor,

                    fontSize =
                        10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    maxLines =
                        1
                )
            }
        }
    }
}


// =============================================================
// BOOKING DATE FORMAT
// =============================================================

private fun formatBookingDate(
    date: String
): String {

    val cleanDate =
        date.substringBefore("T")

    return try {

        val input =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

        val output =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            )

        val parsed =
            input.parse(cleanDate)

        if (parsed != null) {

            output.format(parsed)

        } else {

            cleanDate.ifBlank {
                "Date unavailable"
            }
        }

    } catch (e: Exception) {

        cleanDate.ifBlank {
            "Date unavailable"
        }
    }
}


// =============================================================
// SLOT TIME FORMAT
// =============================================================

private fun formatSlotTime(
    time: String
): String {

    val cleanTime =
        time.trim()

    return try {

        val input =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            )

        val output =
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )

        val parsed =
            input.parse(cleanTime)

        if (parsed != null) {

            output.format(parsed)

        } else {

            cleanTime
        }

    } catch (e: Exception) {

        cleanTime
    }
}


// =============================================================
// AMOUNT FORMAT
// =============================================================

private fun formatAmount(
    amount: Double
): String {

    return if (amount % 1.0 == 0.0) {

        amount
            .toInt()
            .toString()

    } else {

        String.format(
            Locale.getDefault(),
            "%.2f",
            amount
        )
    }
}