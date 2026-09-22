package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
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
// PREMIUM BOOK MY TURF THEME
// ============================================================

private val Background = Color(0xFF020C09)
private val CardBackground = Color(0xFF071713)
private val SecondarySurface = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFB7E77A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFF9EAEA6)
private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)

// Available slot
private val AvailableBackground = Color(0xFF102A20)
private val AvailableBorder = Color(0xFF315A45)
private val AvailablePrice = Color(0xFFB7E77A)

// Selected slot
private val SelectedBackground = Color(0xFF263D20)
private val SelectedBorder = Color(0xFF9FD765)

// Booked slot
private val BookedBackground = Color(0xFF261719)
private val BookedBorder = Color(0xFF4C292D)
private val BookedText = Color(0xFFFF7479)

// Closed slot
private val ClosedBackground = Color(0xFF111A17)
private val ClosedBorder = Color(0xFF26322D)


// ============================================================
// USER SLOT SELECTION SCREEN
// ============================================================

@Composable
fun UserSlotSelectionScreen(
    turfId: Int,
    onBackClick: () -> Unit,
    onContinueClick: (
        turfId: Int,
        slotId: Int,
        bookingDate: String
    ) -> Unit
) {

    // ========================================================
    // VIEW MODEL
    // ========================================================

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

    // ========================================================
    // STATE
    // ========================================================

    val turf by viewModel.turf.collectAsState()
    val slots by viewModel.slots.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingSlots by viewModel.isLoadingSlots.collectAsState()

    val error by viewModel.error.collectAsState()
    val slotError by viewModel.slotError.collectAsState()

    // ========================================================
    // DATE FORMATTERS
    // ========================================================

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

    // ========================================================
    // AVAILABLE DATES
    // ========================================================

    val availableDates = remember {

        val result = mutableListOf<Calendar>()

        val today = Calendar.getInstance()

        val year =
            today.get(Calendar.YEAR)

        val month =
            today.get(Calendar.MONTH)

        val totalDays =
            today.getActualMaximum(
                Calendar.DAY_OF_MONTH
            )

        for (
        day in today.get(Calendar.DAY_OF_MONTH)
                ..totalDays
        ) {

            val calendar =
                Calendar.getInstance()

            calendar.set(
                year,
                month,
                day,
                0,
                0,
                0
            )

            calendar.set(
                Calendar.MILLISECOND,
                0
            )

            result.add(
                calendar
            )
        }

        result
    }

    // ========================================================
    // SELECTED DATE
    // ========================================================

    var selectedDate by remember {

        mutableStateOf(
            availableDates.first()
        )
    }

    // ========================================================
    // SELECTED SLOT
    // ========================================================

    var selectedSlotId by remember {
        mutableStateOf<Int?>(null)
    }

    // ========================================================
    // BOOKING DATE
    // ========================================================

    val bookingDate =
        apiDateFormat.format(
            selectedDate.time
        )

    // ========================================================
    // LOAD DATA
    // ========================================================

    LaunchedEffect(
        turfId,
        bookingDate
    ) {

        selectedSlotId = null

        viewModel.loadTurf(
            turfId
        )

        viewModel.loadSlots(
            turfId = turfId,
            bookingDate = bookingDate
        )
    }

    // ========================================================
    // SCREEN
    // ========================================================

    Scaffold(
        containerColor = Background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // =================================================
            // TOP SPACE
            // =================================================
            // This moves the COMPLETE top bar downward.
            // It is intentionally outside Scaffold's topBar.
            // =================================================

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // PREMIUM TOP BAR
            // =================================================

            PremiumTopBar(
                turfName = turf?.name,
                onBackClick = onBackClick
            )

            // =================================================
            // SCROLLABLE CONTENT
            // =================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    )
            ) {

                // =================================================
                // LOADING TURF
                // =================================================

                if (isLoading) {

                    PremiumLoadingBox(
                        message = "Loading turf..."
                    )

                    return@Column
                }

                // =================================================
                // ERROR
                // =================================================

                if (error != null) {

                    PremiumMessageBox(
                        message =
                            error
                                ?: "Unable to load turf.",

                        isError = true
                    )

                    return@Column
                }

                // =================================================
                // DATE SECTION
                // =================================================

                SectionHeader(
                    icon = {

                        Icon(
                            imageVector =
                                Icons.Default.CalendarMonth,

                            contentDescription = null,

                            modifier =
                                Modifier.size(18.dp),

                            tint =
                                PrimaryGreen
                        )
                    },

                    title =
                        "Choose Date",

                    subtitle =
                        "Select the date you want to play"
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                // =================================================
                // DATE SELECTOR
                // =================================================

                LazyRow(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp),

                    contentPadding =
                        PaddingValues(
                            horizontal = 2.dp
                        )
                ) {

                    items(
                        items = availableDates
                    ) { date ->

                        val dateValue =
                            apiDateFormat.format(
                                date.time
                            )

                        val selectedValue =
                            apiDateFormat.format(
                                selectedDate.time
                            )

                        val isSelected =
                            dateValue ==
                                    selectedValue

                        PremiumDateBox(
                            date = date,

                            isSelected =
                                isSelected,

                            weekdayFormat =
                                weekdayFormat,

                            dayFormat =
                                dayFormat,

                            monthFormat =
                                monthFormat,

                            onClick = {

                                selectedDate =
                                    date

                                selectedSlotId =
                                    null
                            }
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )

                // =================================================
                // SLOT SECTION HEADER
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
                                "Choose Your Slot",

                            fontSize =
                                19.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                PrimaryText
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Available timings for $bookingDate",

                            fontSize =
                                11.sp,

                            color =
                                SecondaryText
                        )
                    }

                    // =================================================
                    // SLOT COUNT
                    // =================================================

                    Box(
                        modifier = Modifier
                            .background(
                                color =
                                    SecondarySurface,

                                shape =
                                    RoundedCornerShape(
                                        12.dp
                                    )
                            )
                            .border(
                                width = 1.dp,

                                color =
                                    BorderColor,

                                shape =
                                    RoundedCornerShape(
                                        12.dp
                                    )
                            )
                            .padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            )
                    ) {

                        Text(
                            text =
                                "${slots.size} SLOTS",

                            fontSize =
                                8.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                0.7.sp,

                            color =
                                PrimaryGreen
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                // =================================================
                // LEGEND
                // =================================================

                SlotLegend()

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================================
                // SLOT LOADING
                // =================================================

                if (isLoadingSlots) {

                    PremiumLoadingBox(
                        message =
                            "Loading available slots..."
                    )
                }

                // =================================================
                // SLOT ERROR
                // =================================================

                else if (slotError != null) {

                    PremiumMessageBox(
                        message =
                            slotError
                                ?: "Unable to load slots.",

                        isError = true
                    )
                }

                // =================================================
                // EMPTY
                // =================================================

                else if (slots.isEmpty()) {

                    PremiumEmptySlotsBox()
                }

                // =================================================
                // SLOT GRID
                // =================================================

                else {

                    PremiumSlotGrid(
                        slots = slots,

                        selectedSlotId =
                            selectedSlotId,

                        onSlotClick = { slot ->

                            val isAvailable =
                                slot.status == "ACTIVE" &&
                                        !slot.isBooked

                            if (isAvailable) {

                                selectedSlotId =
                                    if (
                                        selectedSlotId ==
                                        slot.id
                                    ) {

                                        null

                                    } else {

                                        slot.id
                                    }
                            }
                        }
                    )
                }

                // =================================================
                // SELECTED SLOT
                // =================================================

                val selectedSlot =
                    slots.firstOrNull {
                        it.id ==
                                selectedSlotId
                    }

                if (selectedSlot != null) {

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )

                    PremiumSelectedSlotCard(
                        selectedSlot =
                            selectedSlot,

                        bookingDate =
                            bookingDate
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    // =================================================
                    // CONTINUE BUTTON
                    // =================================================

                    Button(
                        onClick = {

                            onContinueClick(
                                turfId,
                                selectedSlot.id,
                                bookingDate
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    PrimaryGreen,

                                contentColor =
                                    Background
                            ),

                        contentPadding =
                            PaddingValues(
                                vertical = 16.dp
                            )
                    ) {

                        Text(
                            text =
                                "Continue to Booking",

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )
            }
        }
    }
}


// ============================================================
// PREMIUM TOP BAR
// ============================================================

@Composable
private fun PremiumTopBar(
    turfName: String?,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Back button
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = SecondarySurface,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(
                    onClick = onBackClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(21.dp),
                tint = PrimaryText
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Select Date & Slot",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            if (!turfName.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = turfName,
                    fontSize = 11.sp,
                    color = SecondaryText,
                    maxLines = 1
                )
            }
        }
    }
}

// ============================================================
// DATE BOX
// ============================================================

@Composable
private fun PremiumDateBox(
    date: Calendar,
    isSelected: Boolean,
    weekdayFormat: SimpleDateFormat,
    dayFormat: SimpleDateFormat,
    monthFormat: SimpleDateFormat,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .width(70.dp)
            .background(
                color =
                    if (isSelected) {
                        SecondarySurface
                    } else {
                        CardBackground
                    },

                shape =
                    RoundedCornerShape(
                        17.dp
                    )
            )
            .border(
                width =
                    if (isSelected) {
                        1.5.dp
                    } else {
                        1.dp
                    },

                color =
                    if (isSelected) {
                        PrimaryGreen
                    } else {
                        BorderColor
                    },

                shape =
                    RoundedCornerShape(
                        17.dp
                    )
            )
            .clickable(
                onClick =
                    onClick
            )
            .padding(
                vertical = 11.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                weekdayFormat
                    .format(
                        date.time
                    )
                    .uppercase(),

            fontSize =
                8.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                0.7.sp,

            color =
                if (isSelected) {
                    BrightGreen
                } else {
                    SecondaryText
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

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                PrimaryText
        )

        Spacer(
            modifier =
                Modifier.height(1.dp)
        )

        Text(
            text =
                monthFormat
                    .format(
                        date.time
                    )
                    .uppercase(),

            fontSize =
                8.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                if (isSelected) {
                    PrimaryGreen
                } else {
                    MutedText
                }
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Box(
            modifier = Modifier
                .size(5.dp)
                .background(
                    color =
                        if (isSelected) {
                            PrimaryGreen
                        } else {
                            Color.Transparent
                        },

                    shape =
                        CircleShape
                )
        )
    }
}


// ============================================================
// SLOT LEGEND
// ============================================================

@Composable
private fun SlotLegend() {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(14.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        LegendItem(
            color =
                PrimaryGreen,

            text =
                "Available"
        )

        LegendItem(
            color =
                BrightGreen,

            text =
                "Selected"
        )

        LegendItem(
            color =
                BookedText,

            text =
                "Booked"
        )

        LegendItem(
            color =
                MutedText,

            text =
                "Closed"
        )
    }
}


// ============================================================
// LEGEND ITEM
// ============================================================

@Composable
private fun LegendItem(
    color: Color,
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(7.dp)
                .background(
                    color =
                        color,

                    shape =
                        CircleShape
                )
        )

        Spacer(
            modifier =
                Modifier.width(5.dp)
        )

        Text(
            text =
                text,

            fontSize =
                8.sp,

            fontWeight =
                FontWeight.Medium,

            color =
                SecondaryText
        )
    }
}


// ============================================================
// PREMIUM SLOT GRID
// ============================================================

@Composable
private fun PremiumSlotGrid(
    slots: List<Slot>,
    selectedSlotId: Int?,
    onSlotClick: (Slot) -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(11.dp)
    ) {

        slots
            .chunked(3)
            .forEach { rowSlots ->

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(11.dp)
                ) {

                    rowSlots.forEach { slot ->

                        PremiumSlotBox(
                            slot =
                                slot,

                            isSelected =
                                selectedSlotId ==
                                        slot.id,

                            onClick = {

                                onSlotClick(
                                    slot
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )
                    }

                    repeat(
                        3 - rowSlots.size
                    ) {

                        Spacer(
                            modifier =
                                Modifier.weight(1f)
                        )
                    }
                }
            }
    }
}


// ============================================================
// PREMIUM SLOT BOX
// ============================================================

@Composable
private fun PremiumSlotBox(
    slot: Slot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    // ========================================================
    // IMPORTANT AVAILABILITY LOGIC
    // ========================================================

    val isAvailable =
        slot.status == "ACTIVE" &&
                !slot.isBooked

    // ========================================================
    // COLORS
    // ========================================================

    val backgroundColor =
        when {

            isSelected ->
                SelectedBackground

            slot.isBooked ->
                BookedBackground

            !isAvailable ->
                ClosedBackground

            else ->
                AvailableBackground
        }

    val borderColor =
        when {

            isSelected ->
                SelectedBorder

            slot.isBooked ->
                BookedBorder

            !isAvailable ->
                ClosedBorder

            else ->
                AvailableBorder
        }

    val priceColor =
        when {

            isSelected ->
                BrightGreen

            slot.isBooked ->
                BookedText

            !isAvailable ->
                MutedText

            else ->
                AvailablePrice
        }

    // ========================================================
    // SLOT
    // ========================================================

    Column(
        modifier = modifier
            .aspectRatio(
                0.92f
            )
            .background(
                color =
                    backgroundColor,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .border(
                width =
                    if (isSelected) {
                        1.8.dp
                    } else {
                        1.dp
                    },

                color =
                    borderColor,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .clickable(
                enabled =
                    isAvailable,

                onClick =
                    onClick
            )
            .padding(
                horizontal = 7.dp,
                vertical = 10.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        // =====================================================
        // START TIME
        // =====================================================

        Text(
            text =
                formatTime(
                    slot.startTime
                ),

            fontSize =
                11.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                if (
                    isAvailable ||
                    isSelected
                ) {
                    PrimaryText
                } else {
                    SecondaryText
                },

            textAlign =
                TextAlign.Center,

            maxLines = 1
        )

        // =====================================================
        // END TIME
        // =====================================================

        Text(
            text =
                formatTime(
                    slot.endTime
                ),

            fontSize =
                8.sp,

            fontWeight =
                FontWeight.Medium,

            color =
                MutedText,

            textAlign =
                TextAlign.Center,

            maxLines = 1
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // =====================================================
        // PRICE
        // =====================================================

        Text(
            text =
                "₹${formatPrice(slot.price)}",

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                priceColor,

            textAlign =
                TextAlign.Center,

            maxLines = 1
        )

        Text(
            text =
                "PER SLOT",

            fontSize =
                6.5.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                0.7.sp,

            color =
                MutedText,

            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // =====================================================
        // STATUS
        // =====================================================

        Box(
            modifier = Modifier
                .background(
                    color =
                        when {

                            isSelected ->
                                Color(0xFF45632F)

                            slot.isBooked ->
                                Color(0xFF3A2226)

                            !isAvailable ->
                                Color(0xFF242D29)

                            else ->
                                Color(0xFF1A4232)
                        },

                    shape =
                        RoundedCornerShape(
                            50
                        )
                )
                .padding(
                    horizontal = 7.dp,
                    vertical = 4.dp
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    when {

                        isSelected ->
                            "SELECTED"

                        slot.isBooked ->
                            "BOOKED"

                        isAvailable ->
                            "AVAILABLE"

                        else ->
                            "CLOSED"
                    },

                fontSize =
                    6.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    0.4.sp,

                color =
                    when {

                        isSelected ->
                            BrightGreen

                        slot.isBooked ->
                            BookedText

                        isAvailable ->
                            PrimaryGreen

                        else ->
                            MutedText
                    },

                maxLines = 1
            )
        }

        // =====================================================
        // SELECTED CHECK
        // =====================================================

        if (isSelected) {

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Box(
                modifier = Modifier
                    .size(19.dp)
                    .background(
                        color =
                            PrimaryGreen,

                        shape =
                            CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Check,

                    contentDescription =
                        "Selected",

                    modifier =
                        Modifier.size(
                            12.dp
                        ),

                    tint =
                        Background
                )
            }
        }
    }
}


// ============================================================
// SELECTED SLOT SUMMARY
// ============================================================

@Composable
private fun PremiumSelectedSlotCard(
    selectedSlot: Slot,
    bookingDate: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    SecondarySurface,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .border(
                width = 1.dp,

                color =
                    Color(0xFF365641),

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .padding(16.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // ICON
            // =================================================

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color =
                            PrimaryGreen,

                        shape =
                            RoundedCornerShape(
                                13.dp
                            )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Schedule,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(
                            21.dp
                        ),

                    tint =
                        Background
                )
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
                        "SELECTED SLOT",

                    fontSize =
                        8.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        1.sp,

                    color =
                        PrimaryGreen
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "${formatTime(selectedSlot.startTime)} - ${
                            formatTime(
                                selectedSlot.endTime
                            )
                        }",

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        PrimaryText
                )
            }

            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text =
                        "₹${formatPrice(selectedSlot.price)}",

                    fontSize =
                        19.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        BrightGreen
                )

                Text(
                    text =
                        "TOTAL",

                    fontSize =
                        7.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        0.8.sp,

                    color =
                        MutedText
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Color(0xFF294537)
                )
        )

        Spacer(
            modifier =
                Modifier.height(11.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "BOOKING DATE",

                fontSize =
                    8.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    0.7.sp,

                color =
                    MutedText
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            Text(
                text =
                    bookingDate,

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    PrimaryText
            )
        }
    }
}


// ============================================================
// LOADING BOX
// ============================================================

@Composable
private fun PremiumLoadingBox(
    message: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    CardBackground,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .border(
                width = 1.dp,

                color =
                    BorderColor,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .padding(
                vertical = 28.dp
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            CircularProgressIndicator(
                modifier =
                    Modifier.size(
                        22.dp
                    ),

                color =
                    PrimaryGreen,

                strokeWidth =
                    2.dp
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Text(
                text =
                    message,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Medium,

                color =
                    SecondaryText
            )
        }
    }
}


// ============================================================
// EMPTY SLOTS
// ============================================================

@Composable
private fun PremiumEmptySlotsBox() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    CardBackground,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .border(
                width = 1.dp,

                color =
                    BorderColor,

                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )
            .padding(
                horizontal = 20.dp,
                vertical = 30.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color =
                        SecondarySurface,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.Schedule,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(
                        25.dp
                    ),

                tint =
                    PrimaryGreen
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                "No slots available",

            fontSize =
                15.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                PrimaryText
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                "Please select another date.",

            fontSize =
                11.sp,

            color =
                SecondaryText
        )
    }
}


// ============================================================
// MESSAGE BOX
// ============================================================

@Composable
private fun PremiumMessageBox(
    message: String,
    isError: Boolean
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    CardBackground,

                shape =
                    RoundedCornerShape(
                        16.dp
                    )
            )
            .border(
                width = 1.dp,

                color =
                    if (isError) {
                        Color(0xFF542B2F)
                    } else {
                        BorderColor
                    },

                shape =
                    RoundedCornerShape(
                        16.dp
                    )
            )
            .padding(18.dp)
    ) {

        Text(
            text =
                message,

            fontSize =
                13.sp,

            color =
                if (isError) {
                    BookedText
                } else {
                    SecondaryText
                }
        )
    }
}


// ============================================================
// SECTION HEADER
// ============================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: (@Composable () -> Unit)? = null
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        if (icon != null) {

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color =
                            SecondarySurface,

                        shape =
                            RoundedCornerShape(
                                12.dp
                            )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                icon()
            }

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )
        }

        Column {

            Text(
                text =
                    title,

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    PrimaryText
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    subtitle,

                fontSize =
                    10.sp,

                color =
                    SecondaryText
            )
        }
    }
}


// ============================================================
// PRICE FORMATTER
// ============================================================

private fun formatPrice(
    price: Double
): String {

    return if (
        price % 1.0 == 0.0
    ) {

        price
            .toInt()
            .toString()

    } else {

        String.format(
            Locale.getDefault(),
            "%.2f",
            price
        )
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

        val date =
            inputFormat.parse(time)

        if (date != null) {

            outputFormat.format(
                date
            )

        } else {

            time
        }

    } catch (
        e: Exception
    ) {

        time
    }
}