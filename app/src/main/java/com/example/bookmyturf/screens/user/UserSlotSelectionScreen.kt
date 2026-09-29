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
// COLORS
// ============================================================

private val Background = Color(0xFF020C09)
private val CardBackground = Color(0xFF071713)
private val SecondarySurface = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val BrightGreen = Color(0xFFB7E77A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFF9EAEA6)
private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)

// Available
private val AvailableBackground = Color(0xFF102A20)
private val AvailableBorder = Color(0xFF315A45)
private val AvailablePrice = Color(0xFFB7E77A)

// Selected
private val SelectedBackground = Color(0xFF263D20)
private val SelectedBorder = Color(0xFF9FD765)

// Booked
private val BookedBackground = Color(0xFF261719)
private val BookedBorder = Color(0xFF4C292D)
private val BookedText = Color(0xFFFF7479)

// Expired
private val ExpiredBackground = Color(0xFF171B19)
private val ExpiredBorder = Color(0xFF303B35)
private val ExpiredText = Color(0xFF7E8D86)


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

    val isLoadingSlots by
    viewModel.isLoadingSlots.collectAsState()

    val error by viewModel.error.collectAsState()

    val slotError by
    viewModel.slotError.collectAsState()

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

        val year = today.get(
            Calendar.YEAR
        )

        val month = today.get(
            Calendar.MONTH
        )

        val firstDay =
            today.get(
                Calendar.DAY_OF_MONTH
            )

        val lastDay =
            today.getActualMaximum(
                Calendar.DAY_OF_MONTH
            )

        for (
        day in firstDay..lastDay
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
                .padding(
                    innerPadding
                )
        ) {

            Spacer(
                modifier = Modifier.height(
                    20.dp
                )
            )

            // =================================================
            // TOP BAR
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color =
                                SecondarySurface,
                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )
                        )
                        .clickable {
                            onBackClick()
                        },
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription =
                            "Back",
                        modifier =
                            Modifier.size(21.dp),
                        tint =
                            PrimaryText
                    )
                }

                Spacer(
                    modifier = Modifier.width(
                        14.dp
                    )
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Select Date & Slot",
                        fontSize =
                            19.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            PrimaryText
                    )

                    if (
                        !turf?.name.isNullOrBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    2.dp
                                )
                        )

                        Text(
                            text =
                                turf?.name ?: "",
                            fontSize =
                                11.sp,
                            color =
                                SecondaryText,
                            maxLines =
                                1
                        )
                    }
                }
            }

            // =================================================
            // CONTENT
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
                // TURF LOADING
                // =================================================

                if (isLoading) {

                    LoadingBox(
                        text =
                            "Loading turf..."
                    )

                } else if (
                    error != null
                ) {

                    MessageBox(
                        text =
                            error
                                ?: "Unable to load turf."
                    )

                } else {

                    // =================================================
                    // DATE
                    // =================================================

                    SectionHeader(
                        title =
                            "Choose Date",
                        subtitle =
                            "Select the date you want to play"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    LazyRow(
                        modifier =
                            Modifier.fillMaxWidth(),

                        contentPadding =
                            PaddingValues(
                                horizontal =
                                    2.dp
                            ),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                10.dp
                            )
                    ) {

                        items(
                            items =
                                availableDates,

                            key = { date ->
                                apiDateFormat.format(
                                    date.time
                                )
                            }
                        ) { date ->

                            val dateString =
                                apiDateFormat.format(
                                    date.time
                                )

                            val selectedString =
                                apiDateFormat.format(
                                    selectedDate.time
                                )

                            DateBox(
                                date =
                                    date,

                                isSelected =
                                    dateString ==
                                            selectedString,

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
                            Modifier.height(
                                30.dp
                            )
                    )

                    // =================================================
                    // SLOT HEADER
                    // =================================================

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(
                                    1f
                                )
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
                                    Modifier.height(
                                        3.dp
                                    )
                            )

                            Text(
                                text =
                                    "Timings for $bookingDate",
                                fontSize =
                                    11.sp,
                                color =
                                    SecondaryText
                            )
                        }

                        Box(
                            modifier =
                                Modifier
                                    .background(
                                        color =
                                            SecondarySurface,
                                        shape =
                                            RoundedCornerShape(
                                                12.dp
                                            )
                                    )
                                    .border(
                                        1.dp,
                                        BorderColor,
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
                                color =
                                    PrimaryGreen,
                                letterSpacing =
                                    0.6.sp
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    // =================================================
                    // LEGEND
                    // =================================================

                    SlotLegend()

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    // =================================================
                    // SLOT CONTENT
                    // =================================================

                    if (
                        isLoadingSlots
                    ) {

                        LoadingBox(
                            text =
                                "Checking slot availability..."
                        )

                    } else if (
                        slotError != null
                    ) {

                        MessageBox(
                            text =
                                slotError
                                    ?: "Unable to load slots."
                        )

                    } else if (
                        slots.isEmpty()
                    ) {

                        EmptySlotsBox()

                    } else {

                        SlotGrid(
                            slots =
                                slots,

                            selectedSlotId =
                                selectedSlotId,

                            onSlotClick = { slot ->

                                if (
                                    slot.isBookable &&
                                    !slot.isBooked &&
                                    !slot.isExpired
                                ) {

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

                    if (
                        selectedSlot != null
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    24.dp
                                )
                        )

                        SelectedSlotCard(
                            slot =
                                selectedSlot,

                            bookingDate =
                                bookingDate
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    14.dp
                                )
                        )

                        Button(
                            onClick = {

                                if (
                                    selectedSlot.isBookable &&
                                    !selectedSlot.isBooked &&
                                    !selectedSlot.isExpired
                                ) {

                                    onContinueClick(
                                        turfId,
                                        selectedSlot.id,
                                        bookingDate
                                    )
                                }
                            },

                            enabled =
                                selectedSlot.isBookable &&
                                        !selectedSlot.isBooked &&
                                        !selectedSlot.isExpired,

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    15.dp
                                ),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        PrimaryGreen,

                                    contentColor =
                                        Background,

                                    disabledContainerColor =
                                        Color(0xFF24332C),

                                    disabledContentColor =
                                        MutedText
                                ),

                            contentPadding =
                                PaddingValues(
                                    vertical =
                                        15.dp
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
                            Modifier.height(
                                30.dp
                            )
                    )
                }
            }
        }
    }
}


// ============================================================
// SECTION HEADER
// ============================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

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
                Modifier.height(
                    3.dp
                )
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


// ============================================================
// DATE BOX
// ============================================================

@Composable
private fun DateBox(
    date: Calendar,
    isSelected: Boolean,
    weekdayFormat: SimpleDateFormat,
    dayFormat: SimpleDateFormat,
    monthFormat: SimpleDateFormat,
    onClick: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .width(
                    70.dp
                )
                .background(
                    color =
                        if (
                            isSelected
                        ) {
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
                        if (
                            isSelected
                        ) {
                            1.5.dp
                        } else {
                            1.dp
                        },

                    color =
                        if (
                            isSelected
                        ) {
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
                    vertical =
                        11.dp
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

            color =
                if (
                    isSelected
                ) {
                    BrightGreen
                } else {
                    SecondaryText
                }
        )

        Spacer(
            modifier =
                Modifier.height(
                    4.dp
                )
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
                Modifier.height(
                    2.dp
                )
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
                if (
                    isSelected
                ) {
                    PrimaryGreen
                } else {
                    MutedText
                }
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
            Arrangement.spacedBy(
                12.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        LegendDot(
            color =
                PrimaryGreen,

            text =
                "Available"
        )

        LegendDot(
            color =
                BrightGreen,

            text =
                "Selected"
        )

        LegendDot(
            color =
                BookedText,

            text =
                "Booked"
        )

        LegendDot(
            color =
                ExpiredText,

            text =
                "Time Passed"
        )
    }
}


// ============================================================
// LEGEND DOT
// ============================================================

@Composable
private fun LegendDot(
    color: Color,
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier =
                Modifier
                    .size(
                        7.dp
                    )
                    .background(
                        color =
                            color,

                        shape =
                            CircleShape
                    )
        )

        Spacer(
            modifier =
                Modifier.width(
                    5.dp
                )
        )

        Text(
            text =
                text,

            fontSize =
                8.sp,

            color =
                SecondaryText
        )
    }
}


// ============================================================
// SLOT GRID
// ============================================================

@Composable
private fun SlotGrid(
    slots: List<Slot>,
    selectedSlotId: Int?,
    onSlotClick: (Slot) -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        slots
            .chunked(
                3
            )
            .forEach { rowSlots ->

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    rowSlots.forEach { slot ->

                        SlotCard(
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
                                Modifier.weight(
                                    1f
                                )
                        )
                    }

                    repeat(
                        3 -
                                rowSlots.size
                    ) {

                        Spacer(
                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )
                    }
                }
            }
    }
}


// ============================================================
// SLOT CARD
// ============================================================

@Composable
private fun SlotCard(
    slot: Slot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {

    val isAvailable =
        slot.isBookable &&
                !slot.isBooked &&
                !slot.isExpired

    val backgroundColor =
        when {

            isSelected ->
                SelectedBackground

            slot.isBooked ->
                BookedBackground

            slot.isExpired ->
                ExpiredBackground

            !slot.isBookable ->
                ExpiredBackground

            else ->
                AvailableBackground
        }

    val borderColor =
        when {

            isSelected ->
                SelectedBorder

            slot.isBooked ->
                BookedBorder

            slot.isExpired ->
                ExpiredBorder

            !slot.isBookable ->
                ExpiredBorder

            else ->
                AvailableBorder
        }

    val priceColor =
        when {

            isSelected ->
                BrightGreen

            slot.isBooked ->
                BookedText

            slot.isExpired ->
                ExpiredText

            !slot.isBookable ->
                ExpiredText

            else ->
                AvailablePrice
        }

    val statusText =
        when {

            isSelected ->
                "SELECTED"

            slot.isBooked ->
                "BOOKED"

            slot.isExpired ->
                "TIME PASSED"

            !slot.isBookable ->
                "CLOSED"

            else ->
                "AVAILABLE"
        }

    val statusColor =
        when {

            isSelected ->
                BrightGreen

            slot.isBooked ->
                BookedText

            slot.isExpired ->
                ExpiredText

            !slot.isBookable ->
                ExpiredText

            else ->
                PrimaryGreen
        }

    Column(
        modifier =
            modifier
                .height(
                    145.dp
                )
                .background(
                    color =
                        backgroundColor,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                )
                .border(
                    width =
                        if (
                            isSelected
                        ) {
                            2.dp
                        } else {
                            1.dp
                        },

                    color =
                        borderColor,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                )
                .clickable(
                    enabled =
                        isAvailable,

                    onClick =
                        onClick
                )
                .padding(
                    horizontal =
                        6.dp,

                    vertical =
                        9.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        // =====================================================
        // START
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

            maxLines =
                1
        )

        // =====================================================
        // END
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

            maxLines =
                1
        )

        Spacer(
            modifier =
                Modifier.height(
                    7.dp
                )
        )

        // =====================================================
        // PRICE
        // =====================================================

        Text(
            text =
                "₹${formatPrice(slot.price)}",

            fontSize =
                16.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                priceColor,

            textAlign =
                TextAlign.Center,

            maxLines =
                1
        )

        Text(
            text =
                "PER SLOT",

            fontSize =
                6.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                0.5.sp,

            color =
                MutedText
        )

        Spacer(
            modifier =
                Modifier.height(
                    7.dp
                )
        )

        // =====================================================
        // STATUS
        // =====================================================

        Box(
            modifier =
                Modifier
                    .background(
                        color =
                            when {

                                isSelected ->
                                    Color(0xFF45632F)

                                slot.isBooked ->
                                    Color(0xFF3A2226)

                                slot.isExpired ->
                                    Color(0xFF242D29)

                                !slot.isBookable ->
                                    Color(0xFF242D29)

                                else ->
                                    Color(0xFF1A4232)
                            },

                        shape =
                            RoundedCornerShape(
                                50.dp
                            )
                    )
                    .padding(
                        horizontal =
                            8.dp,

                        vertical =
                            4.dp
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    statusText,

                fontSize =
                    6.5.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    0.3.sp,

                color =
                    statusColor,

                maxLines =
                    1,

                textAlign =
                    TextAlign.Center
            )
        }

        // =====================================================
        // CHECK AREA
        // =====================================================

        if (
            isSelected
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Box(
                modifier =
                    Modifier
                        .size(
                            18.dp
                        )
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
                            11.dp
                        ),

                    tint =
                        Background
                )
            }

        } else {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )
        }
    }
}


// ============================================================
// SELECTED SLOT CARD
// ============================================================

@Composable
private fun SelectedSlotCard(
    slot: Slot,
    bookingDate: String
) {

    Column(
        modifier =
            Modifier
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
                    width =
                        1.dp,

                    color =
                        Color(0xFF365641),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                )
                .padding(
                    16.dp
                )
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(
                            42.dp
                        )
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
                    Modifier.width(
                        11.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(
                        1f
                    )
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
                        Modifier.height(
                            3.dp
                        )
                )

                Text(
                    text =
                        "${formatTime(slot.startTime)} - ${
                            formatTime(
                                slot.endTime
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
                        "₹${formatPrice(slot.price)}",

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

                    color =
                        MutedText
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    14.dp
                )
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        1.dp
                    )
                    .background(
                        Color(0xFF294537)
                    )
        )

        Spacer(
            modifier =
                Modifier.height(
                    11.dp
                )
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
                    Modifier.weight(
                        1f
                    )
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
private fun LoadingBox(
    text: String
) {

    Box(
        modifier =
            Modifier
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
                    width =
                        1.dp,

                    color =
                        BorderColor,

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                )
                .padding(
                    vertical =
                        28.dp
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
                    Modifier.width(
                        10.dp
                    )
            )

            Text(
                text =
                    text,

                fontSize =
                    12.sp,

                color =
                    SecondaryText
            )
        }
    }
}


// ============================================================
// MESSAGE BOX
// ============================================================

@Composable
private fun MessageBox(
    text: String
) {

    Box(
        modifier =
            Modifier
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
                    width =
                        1.dp,

                    color =
                        Color(0xFF542B2F),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                )
                .padding(
                    18.dp
                )
    ) {

        Text(
            text =
                text,

            fontSize =
                13.sp,

            color =
                BookedText
        )
    }
}


// ============================================================
// EMPTY SLOTS
// ============================================================

@Composable
private fun EmptySlotsBox() {

    Column(
        modifier =
            Modifier
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
                    width =
                        1.dp,

                    color =
                        BorderColor,

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                )
                .padding(
                    horizontal =
                        20.dp,

                    vertical =
                        30.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier =
                Modifier
                    .size(
                        50.dp
                    )
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
                Modifier.height(
                    12.dp
                )
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
                Modifier.height(
                    4.dp
                )
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
// PRICE FORMAT
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
// TIME FORMAT
// ============================================================

private fun formatTime(
    time: String
): String {

    return try {

        val cleanTime =
            time
                .substringBefore(".")
                .substringBefore("+")
                .trim()

        val normalizedTime =
            when {

                cleanTime.length == 5 ->
                    "$cleanTime:00"

                cleanTime.length >= 8 ->
                    cleanTime.substring(
                        0,
                        8
                    )

                else ->
                    cleanTime
            }

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

        inputFormat.isLenient =
            false

        val date =
            inputFormat.parse(
                normalizedTime
            )

        if (
            date != null
        ) {

            outputFormat.format(
                date
            )

        } else {

            cleanTime
        }

    } catch (
        _: Exception
    ) {

        time
    }
}