package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// ============================================================
// SAME THEME AS USER HOME SCREEN
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

// ============================================================
// TURF DETAILS SCREEN
// ============================================================

@Composable
fun TurfDetailsScreen(
    turfId: Int,
    onBackClick: () -> Unit,
    onSlotSelected: (
        turfId: Int,
        slotId: Int,
        bookingDate: String
    ) -> Unit = { _, _, _ -> }
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

    val viewModel: TurfDetailsViewModel = viewModel(
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
    // DATE FORMATTERS
    // =========================================================

    val apiDateFormat = remember {
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )
    }

    val weekdayFormat = remember {
        SimpleDateFormat(
            "EEE",
            Locale.getDefault()
        )
    }

    val dayFormat = remember {
        SimpleDateFormat(
            "dd",
            Locale.getDefault()
        )
    }

    val monthFormat = remember {
        SimpleDateFormat(
            "MMM",
            Locale.getDefault()
        )
    }

    // =========================================================
    // NEXT 7 DAYS
    // =========================================================

    val availableDates = remember {

        val result = mutableListOf<Calendar>()

        val calendar = Calendar.getInstance()

        repeat(7) {

            result.add(
                calendar.clone() as Calendar
            )

            calendar.add(
                Calendar.DAY_OF_MONTH,
                1
            )
        }

        result
    }

    var selectedDate by remember {
        mutableStateOf(
            availableDates.first()
        )
    }

    var selectedSlotId by remember {
        mutableStateOf<Int?>(null)
    }

    // =========================================================
    // LOAD DATA
    // =========================================================

    LaunchedEffect(turfId) {

        viewModel.loadTurf(turfId)

        viewModel.loadSlots(turfId)
    }

    // =========================================================
    // MAIN
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        when {

            // =================================================
            // LOADING
            // =================================================

            isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator(
                        color = ForestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading turf details...",
                        fontSize = 13.sp,
                        color = Gray
                    )
                }
            }

            // =================================================
            // ERROR
            // =================================================

            error != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                LightGreen.copy(
                                    alpha = 0.14f
                                ),
                                RoundedCornerShape(18.dp)
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.SportsSoccer,
                            contentDescription = null,
                            modifier =
                                Modifier.size(32.dp),
                            tint = ForestGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Text(
                        text = "Unable to load turf",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            error ?: "Something went wrong.",
                        fontSize = 13.sp,
                        color = Gray
                    )
                }
            }

            // =================================================
            // TURF
            // =================================================

            turf != null -> {

                val currentTurf = turf!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {

                    // =================================================
                    // TOP BAR
                    // =================================================

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White)
                            .padding(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick =
                                onBackClick
                        ) {

                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription =
                                    "Back",
                                tint = Charcoal
                            )
                        }

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    currentTurf.name,
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color = Charcoal
                            )

                            Text(
                                text = "Turf Details",
                                fontSize = 11.sp,
                                color = Gray
                            )
                        }
                    }

                    // =================================================
                    // MAIN INFORMATION
                    // =================================================

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp,
                                vertical = 16.dp
                            )
                    ) {

                        // =================================================
                        // TURF TITLE
                        // =================================================

                        Text(
                            text =
                                currentTurf.name,
                            fontSize = 27.sp,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color = Charcoal
                        )

                        Spacer(
                            modifier =
                                Modifier.height(7.dp)
                        )

                        // =================================================
                        // LOCATION
                        // =================================================

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocationOn,
                                contentDescription =
                                    "Location",
                                modifier =
                                    Modifier.size(19.dp),
                                tint = ForestGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(5.dp)
                            )

                            Text(
                                text =
                                    "${currentTurf.location}, ${currentTurf.city}",
                                fontSize = 13.sp,
                                color = Gray
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        // =================================================
                        // RATING + PRICE
                        // =================================================

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Star,
                                    contentDescription =
                                        "Rating",
                                    modifier =
                                        Modifier.size(18.dp),
                                    tint = LightGreen
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(4.dp)
                                )

                                Text(
                                    text =
                                        "${currentTurf.rating}",
                                    fontSize = 14.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color = Charcoal
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(4.dp)
                                )

                                Text(
                                    text =
                                        "(${currentTurf.reviewCount} reviews)",
                                    fontSize = 12.sp,
                                    color = Gray
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.weight(1f)
                            )

                            Text(
                                text =
                                    "₹${currentTurf.price.toInt()} / hour",
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.ExtraBold,
                                color = DarkGreen
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(24.dp)
                        )

                        // =================================================
                        // DATE CARD
                        // =================================================

                        SectionTitle(
                            icon = {
                                Icon(
                                    imageVector =
                                        Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    modifier =
                                        Modifier.size(19.dp),
                                    tint = ForestGreen
                                )
                            },
                            title = "Select Date",
                            subtitle =
                                "Choose your preferred booking date"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(14.dp)
                        )

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(7.dp)
                        ) {

                            availableDates.forEach { date ->

                                val dateValue =
                                    apiDateFormat.format(
                                        date.time
                                    )

                                val selectedValue =
                                    apiDateFormat.format(
                                        selectedDate.time
                                    )

                                val isSelected =
                                    dateValue == selectedValue

                                Card(
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .clickable {

                                                selectedDate =
                                                    date

                                                selectedSlotId =
                                                    null
                                            },
                                    shape =
                                        RoundedCornerShape(
                                            14.dp
                                        ),
                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor =
                                                if (isSelected) {
                                                    DarkGreen
                                                } else {
                                                    White
                                                }
                                        ),
                                    elevation =
                                        CardDefaults.cardElevation(
                                            defaultElevation =
                                                if (isSelected) {
                                                    3.dp
                                                } else {
                                                    1.dp
                                                }
                                        )
                                ) {

                                    Column(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical = 11.dp,
                                                    horizontal = 2.dp
                                                ),
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally
                                    ) {

                                        Text(
                                            text =
                                                weekdayFormat.format(
                                                    date.time
                                                ),
                                            fontSize = 10.sp,
                                            fontWeight =
                                                FontWeight.Bold,
                                            color =
                                                if (isSelected) {
                                                    White
                                                } else {
                                                    Gray
                                                }
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(4.dp)
                                        )

                                        Text(
                                            text =
                                                dayFormat.format(
                                                    date.time
                                                ),
                                            fontSize = 18.sp,
                                            fontWeight =
                                                FontWeight.ExtraBold,
                                            color =
                                                if (isSelected) {
                                                    White
                                                } else {
                                                    Charcoal
                                                }
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(2.dp)
                                        )

                                        Text(
                                            text =
                                                monthFormat.format(
                                                    date.time
                                                ),
                                            fontSize = 10.sp,
                                            color =
                                                if (isSelected) {
                                                    White.copy(
                                                        alpha =
                                                            0.85f
                                                    )
                                                } else {
                                                    Gray
                                                }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )

                        // =================================================
                        // SLOTS
                        // =================================================

                        SectionTitle(
                            icon = {
                                Icon(
                                    imageVector =
                                        Icons.Default.AccessTime,
                                    contentDescription = null,
                                    modifier =
                                        Modifier.size(19.dp),
                                    tint = ForestGreen
                                )
                            },
                            title = "Available Slots",
                            subtitle =
                                "Choose an available time slot"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(14.dp)
                        )

                        // =================================================
                        // SLOT LOADING
                        // =================================================

                        if (isLoadingSlots) {

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

                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(22.dp),
                                    horizontalArrangement =
                                        Arrangement.Center,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    CircularProgressIndicator(
                                        modifier =
                                            Modifier.size(24.dp),
                                        color =
                                            ForestGreen,
                                        strokeWidth = 2.dp
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(10.dp)
                                    )

                                    Text(
                                        text =
                                            "Loading available slots...",
                                        fontSize = 13.sp,
                                        color = Gray
                                    )
                                }
                            }
                        }

                        // =================================================
                        // SLOT ERROR
                        // =================================================

                        else if (slotError != null) {

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

                                Text(
                                    text =
                                        slotError
                                            ?: "Unable to load slots.",
                                    modifier =
                                        Modifier.padding(
                                            18.dp
                                        ),
                                    fontSize = 13.sp,
                                    color =
                                        Color(0xFFD32F2F)
                                )
                            }
                        }

                        // =================================================
                        // EMPTY SLOTS
                        // =================================================

                        else if (slots.isEmpty()) {

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
                                            .padding(
                                                vertical = 28.dp,
                                                horizontal = 18.dp
                                            ),
                                    horizontalAlignment =
                                        Alignment.CenterHorizontally
                                ) {

                                    Box(
                                        modifier =
                                            Modifier
                                                .size(52.dp)
                                                .background(
                                                    LightGreen.copy(
                                                        alpha =
                                                            0.14f
                                                    ),
                                                    RoundedCornerShape(
                                                        16.dp
                                                    )
                                                ),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.AccessTime,
                                            contentDescription =
                                                null,
                                            modifier =
                                                Modifier.size(
                                                    25.dp
                                                ),
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
                                            "No slots available",
                                        fontSize = 16.sp,
                                        fontWeight =
                                            FontWeight.Bold,
                                        color =
                                            Charcoal
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(4.dp)
                                    )

                                    Text(
                                        text =
                                            "Please choose another date.",
                                        fontSize = 12.sp,
                                        color = Gray
                                    )
                                }
                            }
                        }

                        // =================================================
                        // SLOT LIST
                        // =================================================

                        else {

                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(
                                        10.dp
                                    )
                            ) {

                                slots.forEach { slot ->

                                    TurfTimeSlotCard(
                                        slot = slot,
                                        isSelected =
                                            selectedSlotId ==
                                                    slot.id,
                                        onClick = {

                                            selectedSlotId =
                                                slot.id

                                            onSlotSelected(
                                                turfId,
                                                slot.id,
                                                apiDateFormat.format(
                                                    selectedDate.time
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(30.dp)
                        )

                        // =================================================
                        // ABOUT
                        // =================================================

                        SectionTitle(
                            title = "About Turf",
                            subtitle =
                                "Everything you need to know"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                currentTurf.description
                                    ?: "No description available.",
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Gray
                        )

                        Spacer(
                            modifier =
                                Modifier.height(26.dp)
                        )

                        // =================================================
                        // SPORTS
                        // =================================================

                        SectionTitle(
                            icon = {
                                Icon(
                                    imageVector =
                                        Icons.Default.SportsSoccer,
                                    contentDescription = null,
                                    modifier =
                                        Modifier.size(19.dp),
                                    tint = ForestGreen
                                )
                            },
                            title = "Sports"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                currentTurf.sportsTypes
                                    .joinToString(" • "),
                            fontSize = 14.sp,
                            color = Gray
                        )

                        Spacer(
                            modifier =
                                Modifier.height(26.dp)
                        )

                        // =================================================
                        // AMENITIES
                        // =================================================

                        SectionTitle(
                            title = "Amenities"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                currentTurf.amenities
                                    .joinToString(" • "),
                            fontSize = 14.sp,
                            color = Gray
                        )

                        Spacer(
                            modifier =
                                Modifier.height(26.dp)
                        )

                        // =================================================
                        // OPENING HOURS
                        // =================================================

                        SectionTitle(
                            title = "Opening Hours"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "${currentTurf.openingTime} - ${currentTurf.closingTime}",
                            fontSize = 14.sp,
                            color = Gray
                        )

                        // =================================================
                        // ADDRESS
                        // =================================================

                        currentTurf.address?.let { address ->

                            Spacer(
                                modifier =
                                    Modifier.height(26.dp)
                            )

                            SectionTitle(
                                title = "Address"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            Text(
                                text = address,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                color = Gray
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(40.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String? = null,
    icon: (@Composable () -> Unit)? = null
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            icon?.invoke()

            if (icon != null) {

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                color = Charcoal
            )
        }

        if (subtitle != null) {

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Gray
            )
        }
    }
}

// ============================================================
// TIME SLOT CARD
// ============================================================

@Composable
private fun TurfTimeSlotCard(
    slot: Slot,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    enabled =
                        slot.status == "ACTIVE",
                    onClick = onClick
                )
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = ForestGreen,
                            shape =
                                RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        LightGreen.copy(
                            alpha = 0.16f
                        )
                    } else {
                        White
                    }
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    if (isSelected) 3.dp else 1.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .background(
                            if (isSelected) {
                                DarkGreen
                            } else {
                                LightGreen.copy(
                                    alpha = 0.14f
                                )
                            },
                            RoundedCornerShape(12.dp)
                        ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier =
                        Modifier.size(22.dp),
                    tint =
                        if (isSelected) {
                            White
                        } else {
                            ForestGreen
                        }
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
                        "${formatTime(slot.startTime)} - ${
                            formatTime(slot.endTime)
                        }",
                    fontSize = 15.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color = Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        if (slot.status == "ACTIVE") {
                            "Available"
                        } else {
                            "Unavailable"
                        },
                    fontSize = 11.sp,
                    fontWeight =
                        FontWeight.Medium,
                    color =
                        if (slot.status == "ACTIVE") {
                            ForestGreen
                        } else {
                            Color(0xFFD32F2F)
                        }
                )
            }

            Text(
                text =
                    "₹${slot.price.toInt()}",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    if (isSelected) {
                        DarkGreen
                    } else {
                        Charcoal
                    }
            )
        }
    }
}

// ============================================================
// FORMAT TIME
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

        val date =
            inputFormat.parse(time)

        if (date != null) {

            outputFormat.format(date)

        } else {

            time
        }

    } catch (e: Exception) {

        time
    }
}

