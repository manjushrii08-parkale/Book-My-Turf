package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.viewmodel.BookingViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// =============================================================
// COLORS
// =============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF7F8F5)
private val White = Color.White

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)
private val LightGray = Color(0xFFE5E7E3)

private val PendingOrange = Color(0xFFF59E0B)
private val ConfirmedGreen = Color(0xFF2E7D32)
private val CancelledRed = Color(0xFFD32F2F)
private val RefundBlue = Color(0xFF2563EB)


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
    bookingViewModel: BookingViewModel
) {

    // =========================================================
    // VIEWMODEL STATE
    // =========================================================

    val bookings by
    bookingViewModel.bookings.collectAsState()

    val isLoading by
    bookingViewModel.isLoading.collectAsState()

    val error by
    bookingViewModel.error.collectAsState()

    // =========================================================
    // SESSION
    // =========================================================

    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }

    // =========================================================
    // SEARCH STATE
    // =========================================================

    var searchQuery by remember {
        mutableStateOf("")
    }

    // =========================================================
    // FILTER STATE
    // =========================================================

    var selectedFilter by remember {
        mutableStateOf(
            BookingFilter.ALL
        )
    }

    // =========================================================
    // LOAD BOOKINGS
    // =========================================================

    LaunchedEffect(token) {

        if (!token.isNullOrBlank()) {

            bookingViewModel.loadMyBookings(
                token = token
            )
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

        val query =
            searchQuery.trim()
                .lowercase(Locale.getDefault())

        bookings.filter { booking ->

            // -------------------------------------------------
            // SEARCH
            // -------------------------------------------------

            val turfName =
                booking.turf?.name
                    ?.lowercase(Locale.getDefault())
                    ?: ""

            val city =
                booking.turf?.city
                    ?.lowercase(Locale.getDefault())
                    ?: ""

            val bookingId =
                booking.id.toString()

            val matchesSearch =
                query.isBlank() ||
                        turfName.contains(query) ||
                        city.contains(query) ||
                        bookingId.contains(query)

            // -------------------------------------------------
            // FILTER
            // -------------------------------------------------

            val matchesFilter =
                when (selectedFilter) {

                    BookingFilter.ALL -> {
                        true
                    }

                    BookingFilter.UPCOMING -> {
                        isUpcomingBooking(
                            booking
                        )
                    }

                    BookingFilter.COMPLETED -> {
                        booking.booking_status
                            .uppercase() ==
                                "COMPLETED"
                    }

                    BookingFilter.CANCELLED -> {
                        booking.booking_status
                            .uppercase() ==
                                "CANCELLED"
                    }

                    BookingFilter.REFUND -> {
                        isRefundBooking(
                            booking
                        )
                    }
                }

            matchesSearch && matchesFilter
        }
    }


    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        // =====================================================
        // TOP BAR
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 12.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        DarkGreen
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "My Bookings",

                    fontSize =
                        23.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                Text(
                    text =
                        "Manage your turf bookings",

                    fontSize =
                        12.sp,

                    color =
                        Gray
                )
            }

            IconButton(
                onClick = {

                    if (
                        !token.isNullOrBlank()
                    ) {

                        bookingViewModel
                            .loadMyBookings(
                                token = token
                            )
                    }
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Refresh,

                    contentDescription =
                        "Refresh",

                    tint =
                        ForestGreen
                )
            }
        }


        // =====================================================
        // SEARCH
        // =====================================================

        OutlinedTextField(
            value =
                searchQuery,

            onValueChange = {
                searchQuery = it
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp
                ),

            singleLine = true,

            shape =
                RoundedCornerShape(15.dp),

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Search,

                    contentDescription =
                        "Search",

                    tint =
                        ForestGreen
                )
            },

            placeholder = {

                Text(
                    text =
                        "Search turf, city or booking ID",

                    fontSize =
                        13.sp,

                    color =
                        Gray
                )
            }
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =====================================================
        // HORIZONTAL FILTERS
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            BookingFilterChip(
                title =
                    BookingFilter.ALL.title,

                selected =
                    selectedFilter ==
                            BookingFilter.ALL,

                onClick = {
                    selectedFilter =
                        BookingFilter.ALL
                }
            )

            BookingFilterChip(
                title =
                    BookingFilter.UPCOMING.title,

                selected =
                    selectedFilter ==
                            BookingFilter.UPCOMING,

                onClick = {
                    selectedFilter =
                        BookingFilter.UPCOMING
                }
            )

            BookingFilterChip(
                title =
                    BookingFilter.COMPLETED.title,

                selected =
                    selectedFilter ==
                            BookingFilter.COMPLETED,

                onClick = {
                    selectedFilter =
                        BookingFilter.COMPLETED
                }
            )

            BookingFilterChip(
                title =
                    BookingFilter.CANCELLED.title,

                selected =
                    selectedFilter ==
                            BookingFilter.CANCELLED,

                onClick = {
                    selectedFilter =
                        BookingFilter.CANCELLED
                }
            )

            BookingFilterChip(
                title =
                    BookingFilter.REFUND.title,

                selected =
                    selectedFilter ==
                            BookingFilter.REFUND,

                onClick = {
                    selectedFilter =
                        BookingFilter.REFUND
                }
            )
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =====================================================
        // RESULT COUNT
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "${filteredBookings.size} booking" +
                            if (
                                filteredBookings.size != 1
                            ) {
                                "s"
                            } else {
                                ""
                            },

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    Charcoal
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            if (
                selectedFilter !=
                BookingFilter.ALL
            ) {

                Text(
                    text =
                        selectedFilter.title,

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        ForestGreen
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(6.dp)
        )


        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Box(
                modifier =
                    Modifier.fillMaxSize(),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color =
                            ForestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "Loading your bookings...",

                        fontSize =
                            13.sp,

                        color =
                            Gray
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
                error =
                    error ?: "Something went wrong.",

                onRetry = {

                    bookingViewModel
                        .clearError()

                    if (
                        !token.isNullOrBlank()
                    ) {

                        bookingViewModel
                            .loadMyBookings(
                                token = token
                            )
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
                searchQuery =
                    searchQuery,

                filter =
                    selectedFilter
            )

            return@Column
        }


        // =====================================================
        // BOOKINGS
        // =====================================================

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(
                    top = 5.dp,
                    bottom = 30.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(
                items =
                    filteredBookings,

                key = {
                        booking ->
                    booking.id
                }
            ) { booking ->

                BookingCard(
                    booking =
                        booking,

                    token =
                        token,

                    bookingViewModel =
                        bookingViewModel
                )
            }
        }
    }
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
        onClick =
            onClick,

        shape =
            RoundedCornerShape(50.dp),

        color =
            if (selected) {
                ForestGreen
            } else {
                White
            },

        border =
            if (selected) {
                null
            } else {
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    LightGray
                )
            }
    ) {

        Text(
            text =
                title,

            modifier =
                Modifier.padding(
                    horizontal = 17.dp,
                    vertical = 9.dp
                ),

            fontSize =
                12.sp,

            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.SemiBold
                },

            color =
                if (selected) {
                    White
                } else {
                    Charcoal
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
    bookingViewModel: BookingViewModel
) {

    val turf =
        booking.turf

    val slot =
        booking.slot

    var showCancelDialog by remember(
        booking.id
    ) {
        mutableStateOf(false)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp
            ),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
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
                            turf?.name
                                ?: "Turf",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    if (
                        !turf?.city
                            .isNullOrBlank()
                    ) {

                        Text(
                            text =
                                turf?.city
                                    ?: "",

                            fontSize =
                                12.sp,

                            color =
                                Gray
                        )
                    }
                }


                BookingStatusBadge(
                    status =
                        booking.booking_status
                )
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // DATE
            // =================================================

            BookingInfoRow(
                icon =
                    Icons.Default.CalendarToday,

                title =
                    "Booking Date",

                value =
                    formatBookingDate(
                        booking.booking_date
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(11.dp)
            )


            // =================================================
            // TIME
            // =================================================

            BookingInfoRow(
                icon =
                    Icons.Default.Schedule,

                title =
                    "Time",

                value =
                    if (slot != null) {

                        "${formatTime(slot.startTime)} - " +
                                formatTime(slot.endTime)

                    } else {

                        "Time unavailable"
                    }
            )


            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )


            // =================================================
            // PAYMENT
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Payment Status",

                    fontSize =
                        11.sp,

                    color =
                        Gray
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                PaymentStatusBadge(
                    status =
                        booking.payment_status
                )
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================
            // DIVIDER
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        LightGray
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )


            // =================================================
            // AMOUNT + BOOKING ID
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text =
                            "Total Amount",

                        fontSize =
                            10.sp,

                        color =
                            Gray
                    )

                    Text(
                        text =
                            "₹${formatAmount(booking.total_amount)}",

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            DarkGreen
                    )
                }


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text =
                            "Booking ID",

                        fontSize =
                            10.sp,

                        color =
                            Gray
                    )

                    Text(
                        text =
                            formatBookingId(
                                booking.id
                            ),

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Charcoal
                    )
                }
            }


            // =================================================
            // CANCELLED SECTION
            // =================================================

            if (
                booking.booking_status
                    .uppercase() ==
                "CANCELLED"
            ) {

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                CancelledBookingSection(
                    booking =
                        booking
                )
            }


            // =================================================
            // CANCEL BUTTON
            // =================================================

            if (
                booking.booking_status
                    .uppercase() ==
                "PENDING" ||

                booking.booking_status
                    .uppercase() ==
                "CONFIRMED"
            ) {

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Button(
                    onClick = {
                        showCancelDialog =
                            true
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                CancelledRed
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Cancel,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(7.dp)
                    )

                    Text(
                        text =
                            "Cancel Booking",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }


    // =========================================================
    // CANCEL DIALOG
    // =========================================================

    if (showCancelDialog) {

        CancelBookingDialog(
            booking =
                booking,

            onDismiss = {
                showCancelDialog =
                    false
            },

            onConfirm = { reason ->

                if (
                    !token.isNullOrBlank()
                ) {

                    bookingViewModel
                        .cancelBooking(
                            token =
                                token,

                            bookingId =
                                booking.id,

                            reason =
                                reason
                        )
                }

                showCancelDialog =
                    false
            }
        )
    }
}


// =============================================================
// CANCELLED BOOKING SECTION
// =============================================================

@Composable
private fun CancelledBookingSection(
    booking: Booking
) {

    val refundStatus =
        booking.refund_status
            ?.uppercase()
            ?: "NOT_APPLICABLE"


    val refundText =
        when (refundStatus) {

            "ELIGIBLE" ->
                "Refund Eligible"

            "REQUESTED" ->
                "Refund Requested"

            "PROCESSING" ->
                "Refund Processing"

            "REFUNDED" ->
                "Refund Completed"

            "REJECTED" ->
                "Refund Rejected"

            else ->
                "Refund Not Applicable"
        }


    val refundColor =
        when (refundStatus) {

            "ELIGIBLE",
            "REQUESTED",
            "PROCESSING",
            "REFUNDED" ->
                RefundBlue

            "REJECTED" ->
                CancelledRed

            else ->
                Gray
        }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    CancelledRed.copy(
                        alpha =
                            0.06f
                    ),

                shape =
                    RoundedCornerShape(13.dp)
            )
            .padding(13.dp)
    ) {

        Column {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Cancel,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(18.dp),

                    tint =
                        CancelledRed
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        "Booking Cancelled",

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        CancelledRed
                )
            }


            if (
                !booking
                    .cancellation_reason
                    .isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Reason: ${booking.cancellation_reason}",

                    fontSize =
                        11.sp,

                    color =
                        Gray
                )
            }


            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CheckCircle,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(16.dp),

                    tint =
                        refundColor
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        refundText,

                    fontSize =
                        12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        refundColor
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

    val normalized =
        status.uppercase()


    val badgeColor =
        when (normalized) {

            "PAID" ->
                ConfirmedGreen

            "FAILED" ->
                CancelledRed

            else ->
                PendingOrange
        }


    Surface(
        shape =
            RoundedCornerShape(50.dp),

        color =
            badgeColor.copy(
                alpha =
                    0.10f
            )
    ) {

        Text(
            text =
                normalized,

            modifier =
                Modifier.padding(
                    horizontal = 9.dp,
                    vertical = 5.dp
                ),

            fontSize =
                9.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                badgeColor
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

    val normalized =
        status.uppercase()


    val badgeColor =
        when (normalized) {

            "CONFIRMED" ->
                ConfirmedGreen

            "CANCELLED" ->
                CancelledRed

            "COMPLETED" ->
                ForestGreen

            else ->
                PendingOrange
        }


    Surface(
        shape =
            RoundedCornerShape(50.dp),

        color =
            badgeColor.copy(
                alpha =
                    0.10f
            )
    ) {

        Text(
            text =
                normalized,

            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),

            fontSize =
                9.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                badgeColor
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
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(
            modifier =
                Modifier.size(34.dp),

            shape =
                RoundedCornerShape(10.dp),

            color =
                ForestGreen.copy(
                    alpha =
                        0.08f
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
                        Modifier.size(17.dp),

                    tint =
                        ForestGreen
                )
            }
        }


        Column(
            modifier =
                Modifier.padding(
                    start = 10.dp
                )
        ) {

            Text(
                text =
                    title,

                fontSize =
                    10.sp,

                color =
                    Gray
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    value,

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    Charcoal
            )
        }
    }
}


// =============================================================
// CANCEL DIALOG
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
        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Cancel Booking",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "Are you sure you want to cancel ${formatBookingId(booking.id)}?",

                    fontSize =
                        14.sp,

                    color =
                        Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text =
                        "Cancellation is subject to the 24-hour refund policy.",

                    fontSize =
                        12.sp,

                    color =
                        Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value =
                        reason,

                    onValueChange = {
                        reason = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            text =
                                "Cancellation reason"
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "Optional"
                        )
                    },

                    minLines =
                        2,

                    maxLines =
                        4
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

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            CancelledRed
                    )
            ) {

                Text(
                    text =
                        "Cancel Booking"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    onDismiss
            ) {

                Text(
                    text =
                        "Keep Booking",

                    color =
                        ForestGreen
                )
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

        contentAlignment =
            Alignment.Center
    ) {

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        White
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Refresh,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(44.dp),

                    tint =
                        ForestGreen
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Unable to load bookings",

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(7.dp)
                )

                Text(
                    text =
                        error,

                    fontSize =
                        12.sp,

                    color =
                        Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                OutlinedButton(
                    onClick =
                        onRetry
                ) {

                    Text(
                        text =
                            "Try Again"
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
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                modifier =
                    Modifier.size(78.dp),

                shape =
                    RoundedCornerShape(24.dp),

                color =
                    LightGreen.copy(
                        alpha =
                            0.10f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.SportsSoccer,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(40.dp),

                        tint =
                            ForestGreen
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            val title =
                when {

                    searchQuery.isNotBlank() ->
                        "No Matching Bookings"

                    filter == BookingFilter.ALL ->
                        "No Bookings Yet"

                    filter == BookingFilter.UPCOMING ->
                        "No Upcoming Bookings"

                    filter == BookingFilter.COMPLETED ->
                        "No Completed Bookings"

                    filter == BookingFilter.CANCELLED ->
                        "No Cancelled Bookings"

                    filter == BookingFilter.REFUND ->
                        "No Refund Bookings"

                    else ->
                        "No Bookings"
                }


            Text(
                text =
                    title,

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )


            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )


            Text(
                text =
                    if (
                        searchQuery.isNotBlank()
                    ) {

                        "Try a different turf name, city or booking ID."

                    } else {

                        "Your bookings will appear here."
                    },

                fontSize =
                    13.sp,

                color =
                    Gray
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

    val status =
        booking.booking_status
            .uppercase()

    if (
        status != "PENDING" &&
        status != "CONFIRMED"
    ) {
        return false
    }


    val slot =
        booking.slot
            ?: return true


    return try {

        val date =
            booking.booking_date
                .substringBefore("T")


        val startTime =
            slot.startTime
                .substringBefore(".")
                .substringBefore("+")


        val bookingDateTime =
            LocalDateTime.of(
                java.time.LocalDate.parse(
                    date
                ),
                LocalTime.parse(
                    startTime
                )
            )


        bookingDateTime.isAfter(
            LocalDateTime.now()
        )

    } catch (
        e: Exception
    ) {

        // If date/time parsing fails,
        // keep PENDING/CONFIRMED as upcoming.
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
        booking.refund_status
            ?.uppercase()
    ) {

        "ELIGIBLE",
        "REQUESTED",
        "PROCESSING",
        "REFUNDED" ->
            true

        else ->
            false
    }
}


// =============================================================
// DATE FORMAT
// =============================================================

private fun formatBookingDate(
    dateString: String
): String {

    return try {

        val date =
            java.time.LocalDate.parse(
                dateString.substringBefore("T")
            )

        date.format(
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy",
                Locale.getDefault()
            )
        )

    } catch (
        e: Exception
    ) {

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

        val cleanTime =
            time
                .substringBefore(".")
                .substringBefore("+")


        LocalTime.parse(
            cleanTime
        ).format(
            DateTimeFormatter.ofPattern(
                "hh:mm a",
                Locale.getDefault()
            )
        )

    } catch (
        e: Exception
    ) {

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
// AMOUNT
// =============================================================

private fun formatAmount(
    amount: Double
): String {

    return if (
        amount % 1.0 == 0.0
    ) {

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

