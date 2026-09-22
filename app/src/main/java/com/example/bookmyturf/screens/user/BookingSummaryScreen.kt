package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
// PREMIUM BOOKMYTURF COLORS
// ============================================================

private val Background = Color(0xFF020C09)
private val Surface = Color(0xFF071713)
private val SurfaceLight = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val BrightGreen = Color(0xFFB7E77A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFF9EAEA6)
private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)
private val ErrorRed = Color(0xFFFF6B6F)

// ============================================================
// BOOKING SUMMARY
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

    // --------------------------------------------------------
    // VIEW MODEL
    // --------------------------------------------------------

    val repository = remember {
        TurfRepository()
    }

    val factory = remember {
        TurfDetailsViewModelFactory(
            repository = repository
        )
    }

    val viewModel: TurfDetailsViewModel =
        viewModel(factory = factory)

    // --------------------------------------------------------
    // STATE
    // --------------------------------------------------------

    val turf by viewModel.turf.collectAsState()
    val slots by viewModel.slots.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingSlots by viewModel.isLoadingSlots.collectAsState()

    val error by viewModel.error.collectAsState()
    val slotError by viewModel.slotError.collectAsState()

    // --------------------------------------------------------
    // LOAD TURF + SLOTS
    // --------------------------------------------------------

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

    // --------------------------------------------------------
    // SELECTED SLOT
    // --------------------------------------------------------

    val selectedSlot =
        slots.firstOrNull {
            it.id == slotId
        }

    // --------------------------------------------------------
    // SCREEN
    // --------------------------------------------------------

    Scaffold(

        containerColor = Background,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Booking Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryText
                    )
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

                            tint =
                                PrimaryText
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                        titleContentColor = PrimaryText,
                        navigationIconContentColor = PrimaryText
                    )
            )
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp
                    )
                    .navigationBarsPadding()
        ) {

            // =================================================
            // LOADING
            // =================================================

            if (
                isLoading ||
                isLoadingSlots
            ) {

                LoadingState()

                return@Column
            }

            // =================================================
            // ERROR
            // =================================================

            if (error != null) {

                ErrorState(
                    message =
                        error ?: "Unable to load turf."
                )

                return@Column
            }

            if (slotError != null) {

                ErrorState(
                    message =
                        slotError ?: "Unable to load slot."
                )

                return@Column
            }

            // =================================================
            // SLOT NOT AVAILABLE
            // =================================================

            if (selectedSlot == null) {

                ErrorState(
                    message =
                        "Selected slot is no longer available."
                )

                return@Column
            }

            // =================================================
            // HERO IMAGE
            // =================================================

            TurfHero(
                imageUrl =
                    turf?.imageUrls?.firstOrNull(),

                turfName =
                    turf?.name ?: "Turf",

                location =
                    turf?.location
                        ?: "Location unavailable"
            )

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            // =================================================
            // BOOKING DETAILS TITLE
            // =================================================

            Text(
                text = "YOUR BOOKING",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.6.sp,
                color = MutedText
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =================================================
            // BOOKING INFORMATION
            // =================================================

            BookingInformation(
                bookingDate =
                    bookingDate,

                startTime =
                    selectedSlot.startTime,

                endTime =
                    selectedSlot.endTime
            )

            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )

            // =================================================
            // PRICE
            // =================================================

            PriceSection(
                price =
                    selectedSlot.price.toInt()
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =================================================
            // CONFIRM BUTTON
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
                    Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PrimaryGreen,

                        contentColor =
                            Background
                    ),

                elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
            ) {

                Text(
                    text = "Confirm Booking",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )
        }
    }
}

// ============================================================
// HERO
// ============================================================

@Composable
private fun TurfHero(
    imageUrl: String?,
    turfName: String,
    location: String
) {

    Box(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(
                    RoundedCornerShape(26.dp)
                )
    ) {

        // ----------------------------------------------------
        // IMAGE
        // ----------------------------------------------------

        if (!imageUrl.isNullOrBlank()) {

            AsyncImage(

                model = imageUrl,

                contentDescription =
                    turfName,

                contentScale =
                    ContentScale.Crop,

                modifier =
                    Modifier.fillMaxSize()
            )

        } else {

            Box(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            SurfaceLight
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(60.dp),

                    tint =
                        PrimaryGreen
                )
            }
        }

        // ----------------------------------------------------
        // PREMIUM GRADIENT
        // ----------------------------------------------------

        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .background(

                        Brush.verticalGradient(

                            colors =
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color(0xE6020C09)
                                )
                        )
                    )
        )

        // ----------------------------------------------------
        // TURF NAME + LOCATION
        // ----------------------------------------------------

        Column(

            modifier =
                Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(20.dp)
        ) {

            Text(
                text =
                    turfName,

                fontSize =
                    25.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Color.White,

                maxLines =
                    1,

                overflow =
                    TextOverflow.Ellipsis
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
                        Modifier.size(16.dp),

                    tint =
                        BrightGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )

                Text(
                    text =
                        location,

                    fontSize =
                        12.sp,

                    color =
                        Color.White.copy(
                            alpha = 0.82f
                        ),

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ============================================================
// BOOKING INFORMATION
// ============================================================

@Composable
private fun BookingInformation(
    bookingDate: String,
    startTime: String,
    endTime: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(
                    Surface
                )
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // ----------------------------------------------------
        // DATE
        // ----------------------------------------------------

        BookingDetail(
            modifier =
                Modifier.weight(1f),

            icon =
                Icons.Default.CalendarMonth,

            title =
                "DATE",

            value =
                formatBookingDate(
                    bookingDate
                )
        )

        // ----------------------------------------------------
        // DIVIDER
        // ----------------------------------------------------

        Box(

            modifier =
                Modifier
                    .width(1.dp)
                    .height(42.dp)
                    .background(
                        BorderColor
                    )
        )

        // ----------------------------------------------------
        // TIME
        // ----------------------------------------------------

        BookingDetail(
            modifier =
                Modifier.weight(1f),

            icon =
                Icons.Default.AccessTime,

            title =
                "TIME",

            value =
                "${formatTime(startTime)} - ${
                    formatTime(endTime)
                }"
        )
    }
}

// ============================================================
// BOOKING DETAIL
// ============================================================

@Composable
private fun BookingDetail(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    value: String
) {

    Row(

        modifier =
            modifier,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(

            modifier =
                Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        SurfaceLight
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
                    Modifier.size(18.dp),

                tint =
                    PrimaryGreen
            )
        }

        Spacer(
            modifier =
                Modifier.width(11.dp)
        )

        Column {

            Text(
                text =
                    title,

                fontSize =
                    9.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    1.2.sp,

                color =
                    MutedText
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    value,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    PrimaryText,

                maxLines =
                    2,

                overflow =
                    TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================
// PRICE SECTION
// ============================================================

@Composable
private fun PriceSection(
    price: Int
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.Bottom,

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text =
                    "TOTAL AMOUNT",

                fontSize =
                    10.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    1.5.sp,

                color =
                    MutedText
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(
                text =
                    "Final booking amount",

                fontSize =
                    12.sp,

                color =
                    SecondaryText
            )
        }

        Row(
            verticalAlignment =
                Alignment.Bottom
        ) {

            Text(
                text =
                    "₹",

                fontSize =
                    19.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    PrimaryGreen
            )

            Spacer(
                modifier =
                    Modifier.width(2.dp)
            )

            Text(
                text =
                    price.toString(),

                fontSize =
                    30.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    BrightGreen
            )
        }
    }
}

// ============================================================
// LOADING STATE
// ============================================================

@Composable
private fun LoadingState() {

    Box(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(500.dp),

        contentAlignment =
            Alignment.Center
    ) {

        CircularProgressIndicator(
            color =
                PrimaryGreen,

            strokeWidth =
                3.dp
        )
    }
}

// ============================================================
// ERROR STATE
// ============================================================

@Composable
private fun ErrorState(
    message: String
) {

    Box(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(500.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(

                modifier =
                    Modifier
                        .size(64.dp)
                        .clip(
                            CircleShape
                        )
                        .background(
                            ErrorRed.copy(
                                alpha = 0.10f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
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
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text =
                    message,

                fontSize =
                    14.sp,

                color =
                    SecondaryText
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
                "EEE, dd MMM",
                Locale.getDefault()
            )

        val parsed =
            inputFormat.parse(date)

        if (parsed != null) {

            outputFormat.format(parsed)

        } else {

            date
        }

    } catch (
        e: Exception
    ) {

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

    } catch (
        e: Exception
    ) {

        time
    }
}