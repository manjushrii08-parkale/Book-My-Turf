package com.example.bookmyturf.screens.admin

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
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminBookingRepository
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AdminBookingViewModel
import com.example.bookmyturf.viewmodel.AdminBookingViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

// =============================================================
// LOCAL STATUS COLORS
// =============================================================

private val AdminRed = Color(0xFFB91C1C)
private val AdminRedBackground = Color(0xFFFFF1F2)

private val AdminOrange = Color(0xFFB45309)
private val AdminOrangeBackground = Color(0xFFFFF7ED)

private val AdminBlue = Color(0xFF1D4ED8)
private val AdminBlueBackground = Color(0xFFEFF6FF)


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
            .background(AdminOffWhite)
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
                    message =
                        error
                            ?: "Something went wrong."
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
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 30.dp
        ),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        item {

            Column {

                Text(
                    text = "Bookings",
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = AdminLightGreen.copy(
                        alpha = 0.18f
                    )
                ) {

                    Text(
                        text = "$bookingCount total bookings",

                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),

                        color = AdminForestGreen,
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

        CircularProgressIndicator(
            color = AdminForestGreen
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Loading bookings...",
            color = AdminGray,
            fontSize = 14.sp
        )
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun BookingErrorState(
    message: String
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
            modifier = Modifier.size(76.dp),
            shape = CircleShape,
            color = AdminRedBackground
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Warning,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(34.dp),

                    tint =
                        AdminRed
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            text =
                "Unable to load bookings",

            style =
                MaterialTheme.typography.titleMedium,

            fontWeight =
                FontWeight.Bold,

            color =
                AdminDarkCharcoal
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                message,

            color =
                AdminGray,

            fontSize =
                13.sp
        )
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
            modifier =
                Modifier.size(84.dp),

            shape =
                CircleShape,

            color =
                AdminLightGreen.copy(
                    alpha = 0.20f
                )
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
                        Modifier.size(40.dp),

                    tint =
                        AdminForestGreen
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Text(
            text =
                "No Bookings Yet",

            style =
                MaterialTheme.typography.titleLarge,

            fontWeight =
                FontWeight.Bold,

            color =
                AdminDarkCharcoal
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "Bookings for your turfs will appear here.",

            color =
                AdminGray,

            fontSize =
                14.sp
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

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
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
                        AdminLightGreen.copy(
                            alpha = 0.20f
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
                                AdminForestGreen
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
                            AdminDarkCharcoal,

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
                                AdminGray,

                            fontSize =
                                12.sp,

                            maxLines =
                                1
                        )
                    }
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
                    "Turf",

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
                    "Booking Date",

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
                    "Time Slot",

                value =
                    slotTime
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            HorizontalDivider(
                color =
                    AdminOffWhite
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
                            "Total Amount",

                        color =
                            AdminGray,

                        fontSize =
                            11.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            "₹${formatAmount(
                                booking.total_amount
                            )}",

                        color =
                            AdminDarkGreen,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Surface(
                    shape =
                        RoundedCornerShape(8.dp),

                    color =
                        AdminOffWhite
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
                            AdminGray,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.SemiBold
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

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(
            modifier =
                Modifier.size(36.dp),

            shape =
                RoundedCornerShape(10.dp),

            color =
                AdminLightGreen.copy(
                    alpha = 0.18f
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
                        AdminForestGreen
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
                    AdminGray,

                fontSize =
                    11.sp
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    value,

                color =
                    AdminDarkCharcoal,

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                maxLines =
                    2
            )
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
                AdminLightGreen.copy(
                    alpha = 0.18f
                )

            contentColor =
                AdminForestGreen

            icon =
                Icons.Default.CheckCircle
        }

        normalizedStatus.contains("CANCELLED") ||
                normalizedStatus.contains("FAILED") ||
                normalizedStatus.contains("REFUNDED") -> {

            backgroundColor =
                AdminRedBackground

            contentColor =
                AdminRed

            icon =
                Icons.Default.Warning
        }

        normalizedStatus.contains("PENDING") ||
                normalizedStatus.contains("ELIGIBLE") -> {

            backgroundColor =
                AdminOrangeBackground

            contentColor =
                AdminOrange

            icon =
                Icons.Default.Warning
        }

        else -> {

            backgroundColor =
                AdminBlueBackground

            contentColor =
                AdminBlue

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
                        AdminGray,

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

