package com.example.bookmyturf.screens.user

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.viewmodel.BookingViewModel


// ============================================================
// COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val PendingOrange = Color(0xFFF59E0B)
private val ConfirmedGreen = Color(0xFF2E7D32)
private val CancelledRed = Color(0xFFD32F2F)


// ============================================================
// USER BOOKINGS SCREEN
// ============================================================

@Composable
fun UserBookingsScreen(
    onBackClick: () -> Unit,
    bookingViewModel: BookingViewModel
) {

    // =========================================================
    // STATE
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
    // MAIN SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 14.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,

                    contentDescription = "Back",

                    tint = DarkGreen
                )
            }


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "My Bookings",

                    fontSize = 22.sp,

                    fontWeight = FontWeight.Bold,

                    color = Charcoal
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Track your turf bookings in one place.",

                    fontSize = 12.sp,

                    color = Gray
                )
            }
        }


        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color = ForestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading your bookings...",

                        fontSize = 13.sp,

                        color = Gray
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // ERROR
        // =====================================================

        if (error != null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                contentAlignment = Alignment.Center
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(20.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = White
                    )
                ) {

                    Column(
                        modifier =
                            Modifier
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
                                Modifier.size(42.dp),

                            tint = ForestGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(
                            text =
                                "Unable to load bookings",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color = Charcoal
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                error
                                    ?: "Something went wrong.",

                            fontSize = 12.sp,

                            color = Gray
                        )
                    }
                }
            }

            return@Column
        }


        // =====================================================
        // EMPTY
        // =====================================================

        if (bookings.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.SportsSoccer,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(58.dp),

                        tint =
                            LightGreen.copy(
                                alpha = 0.55f
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        text = "No Bookings Yet",

                        fontSize = 21.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color = Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(7.dp)
                    )

                    Text(
                        text =
                            "Your confirmed and pending bookings will appear here.",

                        fontSize = 13.sp,

                        color = Gray
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // BOOKINGS LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

            contentPadding =
                PaddingValues(
                    top = 4.dp,
                    bottom = 28.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            items(
                items = bookings,

                key = { booking ->
                    booking.id
                }
            ) { booking ->

                BookingCard(
                    booking = booking
                )
            }
        }
    }
}


// ============================================================
// BOOKING CARD
// ============================================================

@Composable
private fun BookingCard(
    booking: Booking
) {

    val turf = booking.turf
    val slot = booking.slot


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
                containerColor = White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // =================================================
            // TURF NAME + BOOKING STATUS
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

                        fontSize = 18.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color = Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            turf?.city
                                ?: "",

                        fontSize = 12.sp,

                        color = Gray
                    )
                }


                // =============================================
                // BOOKING STATUS
                // =============================================

                BookingStatusBadge(
                    status =
                        booking.booking_status
                )
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================
            // DATE
            // =================================================

            BookingInfoRow(
                icon = Icons.Default.CalendarToday,

                title = "Booking Date",

                value =
                    booking.booking_date
                        .substringBefore("T")
            )


            Spacer(
                modifier =
                    Modifier.height(9.dp)
            )


            // =================================================
            // TIME
            // =================================================

            BookingInfoRow(
                icon = Icons.Default.Schedule,

                title = "Time",

                value =
                    if (slot != null) {

                        "${slot.startTime} - ${slot.endTime}"

                    } else {

                        "Time unavailable"
                    }
            )


            Spacer(
                modifier =
                    Modifier.height(9.dp)
            )


            // =================================================
            // PAYMENT STATUS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Payment",

                    fontSize = 11.sp,

                    color = Gray
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        booking.payment_status,

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        when (
                            booking.payment_status
                                .uppercase()
                        ) {

                            "PAID" ->
                                ConfirmedGreen

                            "FAILED" ->
                                CancelledRed

                            else ->
                                PendingOrange
                        }
                )
            }


            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )


            // =================================================
            // DIVIDER
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Color(0xFFE8EBE5)
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
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

                        fontSize = 10.sp,

                        color = Gray
                    )

                    Text(
                        text =
                            "₹${booking.total_amount}",

                        fontSize = 20.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color = DarkGreen
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

                        fontSize = 10.sp,

                        color = Gray
                    )

                    Text(
                        text =
                            "#${booking.id}",

                        fontSize = 13.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color = Charcoal
                    )
                }
            }
        }
    }
}


// ============================================================
// BOOKING INFO ROW
// ============================================================

@Composable
private fun BookingInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,

            contentDescription = null,

            modifier =
                Modifier.size(17.dp),

            tint = ForestGreen
        )

        Column(
            modifier =
                Modifier.padding(
                    start = 9.dp
                )
        ) {

            Text(
                text = title,

                fontSize = 10.sp,

                color = Gray
            )

            Text(
                text = value,

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color = Charcoal
            )
        }
    }
}


// ============================================================
// BOOKING STATUS BADGE
// ============================================================

@Composable
private fun BookingStatusBadge(
    status: String
) {

    val normalizedStatus =
        status.uppercase()


    val badgeColor =
        when (normalizedStatus) {

            "CONFIRMED" ->
                ConfirmedGreen

            "CANCELLED" ->
                CancelledRed

            "COMPLETED" ->
                ForestGreen

            else ->
                PendingOrange
        }


    Card(
        shape =
            RoundedCornerShape(50.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    badgeColor.copy(
                        alpha = 0.12f
                    )
            )
    ) {

        Text(
            text =
                normalizedStatus,

            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),

            fontSize = 9.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                badgeColor
        )
    }
}

