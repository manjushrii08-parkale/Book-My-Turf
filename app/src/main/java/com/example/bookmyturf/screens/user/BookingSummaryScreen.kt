package com.example.bookmyturf.screens.user

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

// ============================================================
// SAME THEME AS USER HOME
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val DividerColor = Color(0xFFE5E5E0)
private val ErrorRed = Color(0xFFD32F2F)

// ============================================================
// BOOKING SUMMARY SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    turfId: Int,
    slotId: Int,
    bookingDate: String,
    onBackClick: () -> Unit,
    onConfirmBookingClick: (
        turfId: Int,
        slotId: Int,
        bookingDate: String
    ) -> Unit
) {

    // =========================================================
    // VIEW MODEL
    // =========================================================

    val repository = remember {
        TurfRepository()
    }

    val factory = remember {
        TurfDetailsViewModelFactory(
            repository = repository
        )
    }

    val viewModel: TurfDetailsViewModel =
        viewModel(
            factory = factory
        )

    // =========================================================
    // STATE
    // =========================================================

    val turf by viewModel.turf.collectAsState()

    val slots by viewModel.slots.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val isLoadingSlots by viewModel.isLoadingSlots.collectAsState()

    val error by viewModel.error.collectAsState()

    val slotError by viewModel.slotError.collectAsState()

    // =========================================================
    // LOAD TURF + DATE-WISE SLOTS
    // =========================================================

    LaunchedEffect(
        turfId,
        bookingDate
    ) {

        viewModel.loadTurf(
            turfId = turfId
        )

        viewModel.loadSlots(
            turfId = turfId,
            bookingDate = bookingDate
        )
    }

    // =========================================================
    // SELECTED SLOT
    // =========================================================

    val selectedSlot =
        slots.firstOrNull {
            it.id == slotId
        }

    // =========================================================
    // MAIN UI
    // =========================================================

    Scaffold(

        containerColor = OffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Booking Summary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )

                        Text(
                            text = "Review your booking details",
                            fontSize = 11.sp,
                            color = Gray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription =
                                "Back",
                            tint = Charcoal
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = Charcoal,
                        navigationIconContentColor = Charcoal
                    )
            )
        }

    ) { innerPadding ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    )
        ) {

            // =================================================
            // LOADING
            // =================================================

            if (
                isLoading ||
                isLoadingSlots
            ) {

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp),

                    contentAlignment =
                        Alignment.Center
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
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "Preparing booking summary...",
                            fontSize = 13.sp,
                            color = Gray
                        )
                    }
                }

                return@Column
            }

            // =================================================
            // ERROR
            // =================================================

            if (error != null) {

                SummaryMessageCard(
                    message =
                        error
                            ?: "Unable to load turf details."
                )

                return@Column
            }

            // =================================================
            // SLOT ERROR
            // =================================================

            if (slotError != null) {

                SummaryMessageCard(
                    message =
                        slotError
                            ?: "Unable to load slot details."
                )

                return@Column
            }

            // =================================================
            // SLOT NOT FOUND
            // =================================================

            if (selectedSlot == null) {

                SummaryMessageCard(
                    message =
                        "Selected slot is no longer available."
                )

                return@Column
            }

            // =================================================
            // REVIEW HEADER
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Box(

                        modifier =
                            Modifier
                                .size(56.dp)
                                .background(
                                    LightGreen.copy(
                                        alpha = 0.15f
                                    ),
                                    RoundedCornerShape(16.dp)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.CheckCircle,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(30.dp),

                            tint =
                                ForestGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "Review Your Booking",

                        fontSize = 20.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "Make sure everything looks correct before confirming.",

                        fontSize = 11.sp,

                        color =
                            Gray
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =================================================
            // TURF DETAILS TITLE
            // =================================================

            Text(
                text =
                    "Turf Details",

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // =================================================
            // TURF CARD
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    // =================================================
                    // TURF IMAGE
                    // =================================================

                    val turfImage =
                        turf?.imageUrls?.firstOrNull()

                    if (!turfImage.isNullOrBlank()) {

                        AsyncImage(

                            model =
                                turfImage,

                            contentDescription =
                                turf?.name
                                    ?: "Turf Image",

                            contentScale =
                                ContentScale.Crop,

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp
                                        )
                                    )
                        )

                    } else {

                        Box(

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .background(
                                        LightGreen.copy(
                                            alpha = 0.12f
                                        )
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.SportsSoccer,

                                contentDescription =
                                    "Turf",

                                modifier =
                                    Modifier.size(55.dp),

                                tint =
                                    ForestGreen
                            )
                        }
                    }

                    // =================================================
                    // TURF INFORMATION
                    // =================================================

                    Column(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(17.dp)
                    ) {

                        Text(
                            text =
                                turf?.name
                                    ?: "Turf",

                            fontSize = 19.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                Charcoal
                        )

                        Spacer(
                            modifier =
                                Modifier.height(7.dp)
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocationOn,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(17.dp),

                                tint =
                                    ForestGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(5.dp)
                            )

                            Text(
                                text =
                                    turf?.location
                                        ?: "Location unavailable",

                                fontSize = 12.sp,

                                color =
                                    Gray
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =================================================
            // BOOKING DETAILS
            // =================================================

            Text(
                text =
                    "Booking Details",

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
            ) {

                Column {

                    SummaryDetailRow(
                        icon =
                            Icons.Default.CalendarMonth,

                        title =
                            "Booking Date",

                        value =
                            formatBookingDate(
                                bookingDate
                            )
                    )

                    SummaryDivider()

                    SummaryDetailRow(
                        icon =
                            Icons.Default.AccessTime,

                        title =
                            "Time Slot",

                        value =
                            "${formatTime(
                                selectedSlot.startTime
                            )} - ${
                                formatTime(
                                    selectedSlot.endTime
                                )
                            }"
                    )

                    SummaryDivider()

                    SummaryDetailRow(
                        icon =
                            Icons.Default.SportsSoccer,

                        title =
                            "Booking Type",

                        value =
                            "Turf Booking"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =================================================
            // PRICE SUMMARY
            // =================================================

            Text(
                text =
                    "Price Summary",

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                ) {

                    SummaryPriceRow(
                        title =
                            "Slot Price",

                        amount =
                            selectedSlot.price
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    SummaryPriceRow(
                        title =
                            "Booking Fee",

                        amount =
                            0.0
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    DividerColor
                                )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                "Total Amount",

                            fontSize = 16.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                Charcoal
                        )

                        Text(
                            text =
                                "₹${selectedSlot.price.toInt()}",

                            fontSize = 20.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                DarkGreen
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // CONFIRM BOOKING BUTTON
            // =================================================

            Button(

                onClick = {

                    onConfirmBookingClick(
                        turfId,
                        slotId,
                        bookingDate
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            DarkGreen
                    )
            ) {

                Text(
                    text =
                        "Confirm Booking",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier.padding(
                            vertical = 5.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "Please review the date, time and amount before confirming your booking.",

                modifier =
                    Modifier.fillMaxWidth(),

                fontSize = 10.sp,

                color =
                    Gray
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )
        }
    }
}

// ============================================================
// DETAIL ROW
// ============================================================

@Composable
private fun SummaryDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(

            modifier =
                Modifier
                    .size(42.dp)
                    .background(
                        LightGreen.copy(
                            alpha = 0.12f
                        ),
                        RoundedCornerShape(11.dp)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    icon,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(20.dp),

                tint =
                    ForestGreen
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    title,

                fontSize =
                    11.sp,

                color =
                    Gray
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    value,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Charcoal
            )
        }
    }
}

// ============================================================
// PRICE ROW
// ============================================================

@Composable
private fun SummaryPriceRow(
    title: String,
    amount: Double
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                title,

            fontSize =
                13.sp,

            color =
                Gray
        )

        Text(
            text =
                "₹${amount.toInt()}",

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Medium,

            color =
                Charcoal
        )
    }
}

// ============================================================
// DIVIDER
// ============================================================

@Composable
private fun SummaryDivider() {

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(
                    start = 70.dp
                )
                .background(
                    DividerColor
                )
    )
}

// ============================================================
// MESSAGE CARD
// ============================================================

@Composable
private fun SummaryMessageCard(
    message: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector =
                    Icons.Default.SportsSoccer,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(30.dp),

                tint =
                    ErrorRed
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    message,

                fontSize =
                    14.sp,

                color =
                    ErrorRed
            )
        }
    }
}

// ============================================================
// DATE FORMATTER
// ============================================================

private fun formatBookingDate(
    date: String
): String {

    return try {

        val inputFormat =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

        val outputFormat =
            SimpleDateFormat(
                "EEE, dd MMM yyyy",
                Locale.getDefault()
            )

        val parsed =
            inputFormat.parse(date)

        if (parsed != null) {

            outputFormat.format(parsed)

        } else {

            date
        }

    } catch (e: Exception) {

        date
    }
}

// ============================================================
// TIME FORMATTER
// ============================================================

private fun formatTime(
    time: String
): String {

    return try {

        val inputFormat =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            )

        val outputFormat =
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )

        val parsed =
            inputFormat.parse(time)

        if (parsed != null) {

            outputFormat.format(parsed)

        } else {

            time
        }

    } catch (e: Exception) {

        time
    }
}