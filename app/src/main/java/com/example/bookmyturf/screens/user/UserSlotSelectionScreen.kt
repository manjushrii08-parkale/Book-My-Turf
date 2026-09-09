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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
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
// SAME THEME AS USER HOME
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

// ============================================================
// USER SLOT SELECTION SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
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
// =========================================================
// CURRENT MONTH DATES
// =========================================================

    val availableDates = remember {

        val result = mutableListOf<Calendar>()

        val today = Calendar.getInstance()

        val year = today.get(Calendar.YEAR)

        val month = today.get(Calendar.MONTH)

        val totalDays =
            today.getActualMaximum(
                Calendar.DAY_OF_MONTH
            )

        for (day in today.get(Calendar.DAY_OF_MONTH)..totalDays) {

            val calendar = Calendar.getInstance()

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

            result.add(calendar)
        }

        result
    }

    // =========================================================
    // SELECTED DATE
    // =========================================================

    var selectedDate by remember {
        mutableStateOf(
            availableDates.first()
        )
    }
// =========================================================
// SELECTED SLOT
// =========================================================

    var selectedSlotId by remember {
        mutableStateOf<Int?>(null)
    }

// =========================================================
// BOOKING DATE
// =========================================================

    val bookingDate =
        apiDateFormat.format(
            selectedDate.time
        )

// =========================================================
// LOAD TURF + DATE-WISE SLOTS
// =========================================================

    LaunchedEffect(
        turfId,
        bookingDate
    ) {

        viewModel.loadTurf(
            turfId
        )

        viewModel.loadSlots(
            turfId = turfId,
            bookingDate = bookingDate
        )
    }
    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Scaffold(

        containerColor = OffWhite,

        // =====================================================
        // TOP APP BAR
        // =====================================================

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text =
                                "Select Date & Slot",
                            fontSize = 18.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color = Charcoal
                        )

                        if (turf != null) {

                            Text(
                                text =
                                    turf!!.name,
                                fontSize = 11.sp,
                                color = Gray
                            )
                        }
                    }
                },

                navigationIcon = {

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
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            White,
                        titleContentColor =
                            Charcoal,
                        navigationIconContentColor =
                            Charcoal
                    )
            )
        }

    ) { innerPadding ->

        // =====================================================
        // CONTENT
        // =====================================================

        Column(
            modifier = Modifier
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
            // TURF LOADING
            // =================================================

            if (isLoading) {

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(180.dp),
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
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "Loading turf...",
                            fontSize = 13.sp,
                            color = Gray
                        )
                    }
                }

                return@Column
            }

            // =================================================
            // TURF ERROR
            // =================================================

            if (error != null) {

                Text(
                    text =
                        error
                            ?: "Unable to load turf.",
                    fontSize = 14.sp,
                    color =
                        Color(0xFFD32F2F)
                )

                return@Column
            }

            // =================================================
            // SELECT DATE
            // =================================================

            BookingSectionTitle(
                icon = {

                    Icon(
                        imageVector =
                            Icons.Default.CalendarMonth,
                        contentDescription =
                            null,
                        modifier =
                            Modifier.size(19.dp),
                        tint =
                            ForestGreen
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

            // =================================================
            // HORIZONTAL SCROLLABLE DATE ROW
            // =================================================

            LazyRow(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),

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
                        dateValue == selectedValue

                    Card(
                        modifier =
                            Modifier
                                .width(68.dp)
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
                                        vertical = 11.dp
                                    ),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            // =================================
                            // WEEKDAY
                            // =================================

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

                            // =================================
                            // DAY
                            // =================================

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

                            // =================================
                            // MONTH
                            // =================================

                            Text(
                                text =
                                    monthFormat.format(
                                        date.time
                                    ),
                                fontSize = 10.sp,
                                color =
                                    if (isSelected) {
                                        White.copy(
                                            alpha = 0.85f
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
            // AVAILABLE SLOTS
            // =================================================

            BookingSectionTitle(
                icon = {

                    Icon(
                        imageVector =
                            Icons.Default.AccessTime,
                        contentDescription =
                            null,
                        modifier =
                            Modifier.size(19.dp),
                        tint =
                            ForestGreen
                    )
                },
                title = "Available Slots",
                subtitle =
                    "Choose a time slot for $bookingDate"
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
                        RoundedCornerShape(
                            16.dp
                        ),

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
                        RoundedCornerShape(
                            16.dp
                        ),

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
            // NO SLOTS
            // =================================================

            else if (slots.isEmpty()) {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

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

                        Icon(
                            imageVector =
                                Icons.Default.AccessTime,
                            contentDescription =
                                null,
                            modifier =
                                Modifier.size(28.dp),
                            tint =
                                ForestGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
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

                        BookingSlotCard(
                            slot = slot,

                            isSelected =
                                selectedSlotId ==
                                        slot.id,

                            onClick = {

                                if (
                                    slot.status ==
                                    "ACTIVE"
                                ) {

                                    selectedSlotId =
                                        slot.id
                                }
                            }
                        )
                    }
                }
            }

            // =================================================
            // SELECTED SLOT
            // =================================================

            val selectedSlot =
                slots.firstOrNull {
                    it.id == selectedSlotId
                }

            if (selectedSlot != null) {

                Spacer(
                    modifier =
                        Modifier.height(28.dp)
                )

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                LightGreen.copy(
                                    alpha = 0.12f
                                )
                        )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                    ) {

                        Text(
                            text =
                                "Selected Slot",
                            fontSize = 14.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                DarkGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "${formatTime(
                                    selectedSlot.startTime
                                )} - ${
                                    formatTime(
                                        selectedSlot.endTime
                                    )
                                }",
                            fontSize = 16.sp,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color =
                                Charcoal
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "$bookingDate • ₹${selectedSlot.price.toInt()}",
                            fontSize = 12.sp,
                            color = Gray
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                // =================================================
                // CONTINUE
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
                            13.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                DarkGreen
                        ),

                    contentPadding =
                        PaddingValues(
                            vertical = 15.dp
                        )
                ) {

                    Text(
                        text =
                            "Continue",
                        fontSize = 15.sp,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )
        }
    }
}

// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun BookingSectionTitle(
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
// SLOT CARD
// ============================================================

@Composable
private fun BookingSlotCard(
    slot: Slot,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val isActive =
        slot.status == "ACTIVE"

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = isActive,
                    onClick = onClick
                )
                .then(
                    if (isSelected) {

                        Modifier.border(
                            width = 2.dp,
                            color =
                                ForestGreen,
                            shape =
                                RoundedCornerShape(
                                    16.dp
                                )
                        )

                    } else {

                        Modifier
                    }
                ),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    when {

                        isSelected ->
                            LightGreen.copy(
                                alpha = 0.16f
                            )

                        !isActive ->
                            Color(0xFFF1F1EE)

                        else ->
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

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =====================================================
            // ICON
            // =====================================================

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
                    contentDescription =
                        null,
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

            // =====================================================
            // TIME
            // =====================================================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "${formatTime(
                            slot.startTime
                        )} - ${
                            formatTime(
                                slot.endTime
                            )
                        }",
                    fontSize = 15.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        if (isActive) {
                            Charcoal
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
                        if (isActive) {
                            "Available"
                        } else {
                            "Unavailable"
                        },
                    fontSize = 11.sp,
                    fontWeight =
                        FontWeight.Medium,
                    color =
                        if (isActive) {
                            ForestGreen
                        } else {
                            Gray
                        }
                )
            }

            // =====================================================
            // PRICE
            // =====================================================

            Text(
                text =
                    "₹${slot.price.toInt()}",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    if (isActive) {
                        DarkGreen
                    } else {
                        Gray
                    }
            )
        }
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

            outputFormat.format(date)

        } else {

            time
        }

    } catch (e: Exception) {

        time
    }
}

