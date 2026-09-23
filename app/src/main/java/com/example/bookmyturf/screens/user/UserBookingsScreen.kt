package com.example.bookmyturf.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// =============================================================
// PREMIUM COLORS
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)

private val PendingOrange = Color(0xFFF59E0B)
private val ConfirmedGreen = Color(0xFF6FBF73)
private val CancelledRed = Color(0xFFFF6B6B)
private val RefundBlue = Color(0xFF6FA8FF)

private val ErrorSurface = Color(0xFF21100F)

// =============================================================
// BOOKING FILTER
// =============================================================

private enum class BookingFilter(
    val title: String
) {
    ALL("All"),
    UPCOMING("Upcoming"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REFUND("Refund")
}

// =============================================================
// MAIN SCREEN
// =============================================================

@Composable
fun UserBookingsScreen(
    onBackClick: () -> Unit,
    onRateReviewClick: (bookingId: Int, turfName: String) -> Unit,
    bookingViewModel: BookingViewModel
) {
    val bookings by bookingViewModel.bookings.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()
    val error by bookingViewModel.error.collectAsState()

    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(BookingFilter.ALL)
    }

    // =========================================================
    // LOAD BOOKINGS
    // =========================================================

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            bookingViewModel.loadMyBookings(token)
        }
    }

    // =========================================================
    // FILTER BOOKINGS
    // =========================================================

    val filteredBookings = remember(
        bookings,
        searchQuery,
        selectedFilter
    ) {
        val query = searchQuery
            .trim()
            .lowercase(Locale.getDefault())

        bookings.filter { booking ->

            val turfName = booking.turf?.name
                ?.lowercase(Locale.getDefault())
                ?: ""

            val city = booking.turf?.city
                ?.lowercase(Locale.getDefault())
                ?: ""

            val bookingId = booking.id.toString()

            val matchesSearch =
                query.isBlank() ||
                        turfName.contains(query) ||
                        city.contains(query) ||
                        bookingId.contains(query)

            val matchesFilter = when (selectedFilter) {

                BookingFilter.ALL -> true

                BookingFilter.UPCOMING -> {
                    isUpcomingBooking(booking)
                }

                BookingFilter.COMPLETED -> {
                    booking.booking_status.uppercase() == "COMPLETED"
                }

                BookingFilter.CANCELLED -> {
                    booking.booking_status.uppercase() == "CANCELLED"
                }

                BookingFilter.REFUND -> {
                    isRefundBooking(booking)
                }
            }

            matchesSearch && matchesFilter
        }
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
    ) {

        // =====================================================
        // PREMIUM TOP BAR
        // =====================================================

        PremiumBookingsTopBar(
            bookingCount = bookings.size,
            onBackClick = onBackClick
        )

        // =====================================================
        // SEARCH AREA
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
        ) {

            Text(
                text = "Your Activity",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Track and manage all your bookings",
                fontSize = 12.sp,
                color = SecondaryText
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            PremiumSearchField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                }
            )
        }

        // =====================================================
        // FILTERS
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            BookingFilterChip(
                title = BookingFilter.ALL.title,
                selected = selectedFilter == BookingFilter.ALL,
                onClick = {
                    selectedFilter = BookingFilter.ALL
                }
            )

            BookingFilterChip(
                title = BookingFilter.UPCOMING.title,
                selected = selectedFilter == BookingFilter.UPCOMING,
                onClick = {
                    selectedFilter = BookingFilter.UPCOMING
                }
            )

            BookingFilterChip(
                title = BookingFilter.COMPLETED.title,
                selected = selectedFilter == BookingFilter.COMPLETED,
                onClick = {
                    selectedFilter = BookingFilter.COMPLETED
                }
            )

            BookingFilterChip(
                title = BookingFilter.CANCELLED.title,
                selected = selectedFilter == BookingFilter.CANCELLED,
                onClick = {
                    selectedFilter = BookingFilter.CANCELLED
                }
            )

            BookingFilterChip(
                title = BookingFilter.REFUND.title,
                selected = selectedFilter == BookingFilter.REFUND,
                onClick = {
                    selectedFilter = BookingFilter.REFUND
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // =====================================================
        // RESULT SUMMARY
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${filteredBookings.size} " +
                        if (filteredBookings.size == 1) {
                            "booking"
                        } else {
                            "bookings"
                        },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryText
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            if (selectedFilter != BookingFilter.ALL) {

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = PrimaryGreen.copy(alpha = 0.12f),
                    border = BorderStroke(
                        1.dp,
                        Border.copy(alpha = 0.8f)
                    )
                ) {

                    Text(
                        text = selectedFilter.title,
                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 5.dp
                        ),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(34.dp),
                        strokeWidth = 3.dp,
                        color = PrimaryGreen
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "Loading your bookings...",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
            }

            return@Column
        }

        // =====================================================
        // ERROR
        // =====================================================

        if (error != null) {

            BookingErrorState(
                error = error ?: "Something went wrong.",
                onRetry = {

                    bookingViewModel.clearError()

                    if (!token.isNullOrBlank()) {
                        bookingViewModel.loadMyBookings(token)
                    }
                }
            )

            return@Column
        }

        // =====================================================
        // EMPTY
        // =====================================================

        if (filteredBookings.isEmpty()) {

            BookingEmptyState(
                searchQuery = searchQuery,
                filter = selectedFilter
            )

            return@Column
        }

        // =====================================================
        // BOOKINGS LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 4.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {

            items(
                items = filteredBookings,
                key = { booking ->
                    booking.id
                }
            ) { booking ->

                BookingCard(
                    booking = booking,
                    token = token,
                    bookingViewModel = bookingViewModel,
                    onRateReviewClick = onRateReviewClick
                )
            }
        }
    }
}

// =============================================================
// PREMIUM TOP BAR
// =============================================================

@Composable
private fun PremiumBookingsTopBar(
    bookingCount: Int,
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier
                    .size(43.dp)
                    .clickable {
                        onBackClick()
                    },
                shape = RoundedCornerShape(14.dp),
                color = SurfaceElevated,
                border = BorderStroke(
                    1.dp,
                    Border
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        tint = PrimaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "My Bookings",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Manage your turf bookings",
                    fontSize = 11.sp,
                    color = MutedText
                )
            }

            Surface(
                shape = RoundedCornerShape(13.dp),
                color = PrimaryGreen.copy(alpha = 0.10f),
                border = BorderStroke(
                    1.dp,
                    Border.copy(alpha = 0.9f)
                )
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = LightGreen
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = bookingCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )
    }
}

// =============================================================
// SEARCH FIELD
// =============================================================

@Composable
private fun PremiumSearchField(
    value: String,
    onValueChange: (String) -> Unit
) {

    val shape = RoundedCornerShape(15.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(
            color = PrimaryText,
            fontSize = 13.sp
        ),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(
            PrimaryGreen
        ),
        decorationBox = { innerTextField ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SurfaceDark,
                        shape = shape
                    )
                    .border(
                        width = 1.dp,
                        color = Border,
                        shape = shape
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search bookings",
                    modifier = Modifier.size(19.dp),
                    tint = PrimaryGreen
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    if (value.isBlank()) {

                        Text(
                            text = "Search turf, city or booking ID",
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    }

                    innerTextField()
                }
            }
        }
    )
}

// =============================================================
// FILTER CHIP
// =============================================================

@Composable
private fun BookingFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier.clickable {
            onClick()
        },
        shape = RoundedCornerShape(50.dp),
        color = if (selected) {
            PrimaryGreen
        } else {
            SurfaceDark
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                PrimaryGreen
            } else {
                Border
            }
        )
    ) {

        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = 17.dp,
                vertical = 9.dp
            ),
            fontSize = 12.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.SemiBold
            },
            color = if (selected) {
                Color(0xFF07100A)
            } else {
                SecondaryText
            }
        )
    }
}

// =============================================================
// BOOKING CARD
// =============================================================

@Composable
private fun BookingCard(
    booking: Booking,
    token: String?,
    bookingViewModel: BookingViewModel,
    onRateReviewClick: (bookingId: Int, turfName: String) -> Unit
) {

    val turf = booking.turf
    val slot = booking.slot

    var showCancelDialog by remember(booking.id) {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = RoundedCornerShape(21.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = turf?.name ?: "Turf",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryText
                    )

                    if (!turf?.city.isNullOrBlank()) {

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = turf?.city ?: "",
                            fontSize = 12.sp,
                            color = MutedText
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                BookingStatusBadge(
                    status = booking.booking_status
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // DATE
            // =================================================

            BookingInfoRow(
                icon = Icons.Default.CalendarToday,
                title = "Booking Date",
                value = formatBookingDate(
                    booking.booking_date
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // TIME
            // =================================================

            BookingInfoRow(
                icon = Icons.Default.Schedule,
                title = "Time",
                value = if (slot != null) {
                    "${formatTime(slot.startTime)} - ${
                        formatTime(slot.endTime)
                    }"
                } else {
                    "Time unavailable"
                }
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            // =================================================
            // PAYMENT STATUS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Payment Status",
                    fontSize = 11.sp,
                    color = MutedText
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                PaymentStatusBadge(
                    status = booking.payment_status
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Border)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // AMOUNT + BOOKING ID
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Total Amount",
                        fontSize = 10.sp,
                        color = MutedText
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "₹${formatAmount(booking.total_amount)}",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrightGreen
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Booking ID",
                        fontSize = 10.sp,
                        color = MutedText
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = formatBookingId(booking.id),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                }
            }

            // =================================================
            // RATE & REVIEW
            // =================================================

            if (
                booking.booking_status.uppercase() == "COMPLETED" &&
                booking.payment_status.uppercase() == "PAID" &&
                !booking.reviewExists
            ) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        onRateReviewClick(
                            booking.id,
                            turf?.name ?: "Turf"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color(0xFF07100A)
                    )
                ) {

                    Text(
                        text = "Rate & Review",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // =================================================
            // CANCELLED INFORMATION
            // =================================================

            if (
                booking.booking_status.uppercase() == "CANCELLED"
            ) {

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                CancelledBookingSection(
                    booking = booking
                )
            }

            // =================================================
            // CANCEL BUTTON
            // =================================================

            if (
                booking.booking_status.uppercase() == "PENDING" ||
                booking.booking_status.uppercase() == "CONFIRMED"
            ) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                PremiumCancelButton(
                    onClick = {
                        showCancelDialog = true
                    }
                )
            }
        }
    }

    // =========================================================
    // CANCEL DIALOG
    // =========================================================

    if (showCancelDialog) {

        CancelBookingDialog(
            booking = booking,
            onDismiss = {
                showCancelDialog = false
            },
            onConfirm = { reason ->

                if (!token.isNullOrBlank()) {

                    bookingViewModel.cancelBooking(
                        token = token,
                        bookingId = booking.id,
                        reason = reason
                    )
                }

                showCancelDialog = false
            }
        )
    }
}

// =============================================================
// PREMIUM CANCEL BUTTON
// =============================================================

@Composable
private fun PremiumCancelButton(
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(13.dp),
        color = CancelledRed.copy(alpha = 0.07f),
        border = BorderStroke(
            width = 1.dp,
            color = CancelledRed.copy(alpha = 0.35f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = CancelledRed
            )

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Text(
                text = "Cancel Booking",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CancelledRed
            )
        }
    }
}

// =============================================================
// CANCELLED BOOKING SECTION
// =============================================================

@Composable
private fun CancelledBookingSection(
    booking: Booking
) {

    val refundStatus = booking.refund_status
        ?.uppercase()
        ?: "NOT_APPLICABLE"

    val refundText = when (refundStatus) {

        "ELIGIBLE" -> "Refund Eligible"
        "REQUESTED" -> "Refund Requested"
        "PROCESSING" -> "Refund Processing"
        "REFUNDED" -> "Refund Completed"
        "REJECTED" -> "Refund Rejected"

        else -> "Refund Not Applicable"
    }

    val refundColor = when (refundStatus) {

        "ELIGIBLE",
        "REQUESTED",
        "PROCESSING",
        "REFUNDED" -> RefundBlue

        "REJECTED" -> CancelledRed

        else -> MutedText
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CancelledRed.copy(alpha = 0.06f),
                shape = RoundedCornerShape(13.dp)
            )
            .border(
                width = 1.dp,
                color = CancelledRed.copy(alpha = 0.12f),
                shape = RoundedCornerShape(13.dp)
            )
            .padding(13.dp)
    ) {

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = CancelledRed
                )

                Spacer(
                    modifier = Modifier.width(7.dp)
                )

                Text(
                    text = "Booking Cancelled",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CancelledRed
                )
            }

            if (!booking.cancellation_reason.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Reason: ${booking.cancellation_reason}",
                    fontSize = 11.sp,
                    color = SecondaryText
                )
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = refundColor
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = refundText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = refundColor
                )
            }
        }
    }
}

// =============================================================
// PAYMENT STATUS BADGE
// =============================================================

@Composable
private fun PaymentStatusBadge(
    status: String
) {

    val normalized = status.uppercase()

    val badgeColor = when (normalized) {

        "PAID" -> ConfirmedGreen
        "FAILED" -> CancelledRed

        else -> PendingOrange
    }

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = badgeColor.copy(alpha = 0.10f),
        border = BorderStroke(
            1.dp,
            badgeColor.copy(alpha = 0.18f)
        )
    ) {

        Text(
            text = normalized,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = badgeColor
        )
    }
}

// =============================================================
// BOOKING STATUS BADGE
// =============================================================

@Composable
private fun BookingStatusBadge(
    status: String
) {

    val normalized = status.uppercase()

    val badgeColor = when (normalized) {

        "CONFIRMED" -> ConfirmedGreen
        "CANCELLED" -> CancelledRed
        "COMPLETED" -> PrimaryGreen

        else -> PendingOrange
    }

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = badgeColor.copy(alpha = 0.10f),
        border = BorderStroke(
            1.dp,
            badgeColor.copy(alpha = 0.18f)
        )
    ) {

        Text(
            text = normalized,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = badgeColor
        )
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(35.dp),
            shape = RoundedCornerShape(11.dp),
            color = PrimaryGreen.copy(alpha = 0.08f),
            border = BorderStroke(
                1.dp,
                Border
            )
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = PrimaryGreen
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 11.dp)
        ) {

            Text(
                text = title,
                fontSize = 10.sp,
                color = MutedText
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )
        }
    }
}

// =============================================================
// CANCEL BOOKING DIALOG
// =============================================================

@Composable
private fun CancelBookingDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {

    var reason by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,

        title = {

            Text(
                text = "Cancel Booking",
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
        },

        text = {

            Column {

                Text(
                    text = "Are you sure you want to cancel " +
                            "${formatBookingId(booking.id)}?",
                    fontSize = 14.sp,
                    color = SecondaryText
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Cancellation is subject to the 24-hour refund policy.",
                    fontSize = 12.sp,
                    color = MutedText
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                PremiumReasonField(
                    value = reason,
                    onValueChange = {
                        reason = it
                    }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    onConfirm(
                        reason
                            .trim()
                            .ifBlank {
                                null
                            }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CancelledRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {

                Text(
                    text = "Cancel Booking",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Keep Booking",
                    color = LightGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

// =============================================================
// CANCEL REASON FIELD
// =============================================================

@Composable
private fun PremiumReasonField(
    value: String,
    onValueChange: (String) -> Unit
) {

    val shape = RoundedCornerShape(12.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp),
        textStyle = TextStyle(
            color = PrimaryText,
            fontSize = 13.sp
        ),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(
            PrimaryGreen
        ),
        decorationBox = { innerTextField ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = SurfaceDark,
                        shape = shape
                    )
                    .border(
                        width = 1.dp,
                        color = Border,
                        shape = shape
                    )
                    .padding(
                        horizontal = 13.dp,
                        vertical = 12.dp
                    )
            ) {

                if (value.isBlank()) {

                    Text(
                        text = "Cancellation reason (optional)",
                        fontSize = 13.sp,
                        color = MutedText
                    )
                }

                innerTextField()
            }
        }
    )
}

// =============================================================
// ERROR STATE
// =============================================================

@Composable
private fun BookingErrorState(
    error: String,
    onRetry: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(21.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceDark
            ),
            border = BorderStroke(
                1.dp,
                Border
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = ErrorSurface,
                    border = BorderStroke(
                        1.dp,
                        CancelledRed.copy(alpha = 0.15f)
                    )
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(34.dp),
                            tint = CancelledRed
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "Unable to load bookings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = error,
                    fontSize = 12.sp,
                    color = SecondaryText
                )

                Spacer(
                    modifier = Modifier.height(17.dp)
                )

                OutlinedButton(
                    onClick = onRetry,
                    shape = RoundedCornerShape(11.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = PrimaryGreen
                    )
                ) {

                    Text(
                        text = "Try Again",
                        color = LightGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun BookingEmptyState(
    searchQuery: String,
    filter: BookingFilter
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                modifier = Modifier.size(82.dp),
                shape = RoundedCornerShape(25.dp),
                color = PrimaryGreen.copy(alpha = 0.08f),
                border = BorderStroke(
                    1.dp,
                    Border
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        modifier = Modifier.size(42.dp),
                        tint = PrimaryGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            val title = when {

                searchQuery.isNotBlank() -> {
                    "No Matching Bookings"
                }

                filter == BookingFilter.ALL -> {
                    "No Bookings Yet"
                }

                filter == BookingFilter.UPCOMING -> {
                    "No Upcoming Bookings"
                }

                filter == BookingFilter.COMPLETED -> {
                    "No Completed Bookings"
                }

                filter == BookingFilter.CANCELLED -> {
                    "No Cancelled Bookings"
                }

                filter == BookingFilter.REFUND -> {
                    "No Refund Bookings"
                }

                else -> {
                    "No Bookings"
                }
            }

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = if (searchQuery.isNotBlank()) {
                    "Try a different turf name, city or booking ID."
                } else {
                    "Your bookings will appear here."
                },
                fontSize = 13.sp,
                color = SecondaryText
            )
        }
    }
}

// =============================================================
// UPCOMING BOOKING CHECK
// =============================================================

private fun isUpcomingBooking(
    booking: Booking
): Boolean {

    val status = booking.booking_status.uppercase()

    if (
        status != "PENDING" &&
        status != "CONFIRMED"
    ) {
        return false
    }

    val slot = booking.slot ?: return true

    return try {

        val date = booking.booking_date
            .substringBefore("T")

        val startTime = slot.startTime
            .substringBefore(".")
            .substringBefore("+")

        val normalizedTime = when {
            startTime.length == 5 -> {
                "$startTime:00"
            }

            startTime.length >= 8 -> {
                startTime.substring(0, 8)
            }

            else -> {
                startTime
            }
        }

        val parser = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        )

        parser.isLenient = false

        val bookingDateTime = parser.parse(
            "$date $normalizedTime"
        )

        bookingDateTime?.after(Date()) ?: true

    } catch (_: Exception) {

        true
    }
}

// =============================================================
// REFUND BOOKING CHECK
// =============================================================

private fun isRefundBooking(
    booking: Booking
): Boolean {

    return when (
        booking.refund_status?.uppercase()
    ) {

        "ELIGIBLE",
        "REQUESTED",
        "PROCESSING",
        "REFUNDED" -> true

        else -> false
    }
}

// =============================================================
// DATE FORMAT
// =============================================================

private fun formatBookingDate(
    dateString: String
): String {

    return try {

        val cleanDate = dateString
            .substringBefore("T")

        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )

        val outputFormat = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        inputFormat.isLenient = false

        val date = inputFormat.parse(
            cleanDate
        )

        if (date != null) {
            outputFormat.format(date)
        } else {
            cleanDate
        }

    } catch (_: Exception) {

        dateString.substringBefore("T")
    }
}

// =============================================================
// TIME FORMAT
// =============================================================

private fun formatTime(
    time: String
): String {

    return try {

        val cleanTime = time
            .substringBefore(".")
            .substringBefore("+")
            .trim()

        val normalizedTime = when {
            cleanTime.length == 5 -> {
                "$cleanTime:00"
            }

            cleanTime.length >= 8 -> {
                cleanTime.substring(0, 8)
            }

            else -> {
                cleanTime
            }
        }

        val inputFormat = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        )

        val outputFormat = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )

        inputFormat.isLenient = false

        val date = inputFormat.parse(
            normalizedTime
        )

        if (date != null) {
            outputFormat.format(date)
        } else {
            cleanTime
        }

    } catch (_: Exception) {

        time
    }
}

// =============================================================
// BOOKING ID
// =============================================================

private fun formatBookingId(
    id: Int
): String {

    return "#BK%06d".format(
        Locale.getDefault(),
        id
    )
}

// =============================================================
// AMOUNT FORMAT
// =============================================================

private fun formatAmount(
    amount: Double
): String {

    return if (amount % 1.0 == 0.0) {

        amount
            .toLong()
            .toString()

    } else {

        String.format(
            Locale.getDefault(),
            "%.2f",
            amount
        )
    }
}