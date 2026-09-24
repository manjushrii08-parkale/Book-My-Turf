package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminSlotRepository
import com.example.bookmyturf.viewmodel.AdminSlotViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// =============================================================
// PREMIUM COLORS
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)
private val WarningOrange = Color(0xFFD6A15C)

// =============================================================
// TEMPORARY NEW SLOT MODEL
// =============================================================

private data class NewSlot(
    val startTime: String,
    val endTime: String,
    val price: Double
)

// =============================================================
// ADMIN SLOT SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSlotScreen(
    token: String,
    turfId: Int,
    onBack: () -> Unit
) {

    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository = remember {
        AdminSlotRepository(
            RetrofitClient.api
        )
    }

    // =========================================================
    // FACTORY
    // =========================================================

    val factory = remember {
        AdminSlotViewModelFactory(
            repository
        )
    }

    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: AdminSlotViewModel = viewModel(
        factory = factory
    )

    // =========================================================
    // STATE
    // =========================================================

    val slots by viewModel.slots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // =========================================================
    // DIALOG STATES
    // =========================================================

    var showSlotDialog by remember {
        mutableStateOf(false)
    }

    var editingSlot by remember {
        mutableStateOf<Slot?>(null)
    }

    var deletingSlot by remember {
        mutableStateOf<Slot?>(null)
    }

    var togglingSlot by remember {
        mutableStateOf<Slot?>(null)
    }

    // =========================================================
    // LOAD SLOTS
    // =========================================================

    LaunchedEffect(
        turfId,
        token
    ) {
        viewModel.loadSlots(
            token = token,
            turfId = turfId
        )
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(
        containerColor = Background,
        topBar = {
            PremiumSlotTopBar(
                onBackClick = onBack,
                enabled = !isLoading
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // =================================================
            // PAGE HEADER
            // =================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 12.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Manage Slots",
                            color = PrimaryText,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.4).sp
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = "Turf #$turfId",
                            color = SecondaryText,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = {
                            editingSlot = null
                            showSlotDialog = true
                        },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 11.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Background
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text = "Add Slot",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "${slots.size} slots configured",
                    color = MutedText,
                    fontSize = 12.sp
                )
            }

            // =================================================
            // ERROR
            // =================================================

            if (!error.isNullOrBlank()) {

                PremiumMessageBanner(
                    message = error ?: "",
                    isError = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            // =================================================
            // SUCCESS
            // =================================================

            if (!successMessage.isNullOrBlank()) {

                PremiumMessageBanner(
                    message = successMessage ?: "",
                    isError = false
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            // =================================================
            // INITIAL LOADING
            // =================================================

            if (
                isLoading &&
                slots.isEmpty()
            ) {

                LoadingSlots()

                return@Column
            }

            // =================================================
            // EMPTY STATE
            // =================================================

            if (
                !isLoading &&
                slots.isEmpty()
            ) {

                EmptySlotsState(
                    onCreateSlots = {
                        editingSlot = null
                        showSlotDialog = true
                    }
                )

                return@Column
            }

            // =================================================
            // SLOT LIST
            // =================================================

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                    bottom = 32.dp
                )
            ) {

                items(
                    items = slots,
                    key = {
                        it.id
                    }
                ) { slot ->

                    SlotCard(
                        slot = slot,

                        onEdit = {
                            editingSlot = slot
                            showSlotDialog = true
                        },

                        onToggleStatus = {
                            togglingSlot = slot
                        },

                        onDelete = {
                            deletingSlot = slot
                        }
                    )
                }
            }
        }
    }

    // =========================================================
    // ADD / EDIT SLOT DIALOG
    // =========================================================

    if (showSlotDialog) {

        val currentEditingSlot = editingSlot

        if (currentEditingSlot == null) {

            MultipleSlotFormDialog(
                isLoading = isLoading,

                onDismiss = {
                    if (!isLoading) {
                        showSlotDialog = false
                    }
                },

                onSave = { newSlots ->

                    showSlotDialog = false

                    createMultipleSlots(
                        viewModel = viewModel,
                        token = token,
                        turfId = turfId,
                        newSlots = newSlots,
                        onComplete = {

                            viewModel.loadSlots(
                                token = token,
                                turfId = turfId
                            )
                        }
                    )
                }
            )

        } else {

            SingleSlotFormDialog(
                slot = currentEditingSlot,
                isLoading = isLoading,

                onDismiss = {

                    if (!isLoading) {
                        showSlotDialog = false
                        editingSlot = null
                    }
                },

                onSave = { startTime, endTime, price ->

                    viewModel.updateSlot(
                        token = token,
                        turfId = turfId,
                        slotId = currentEditingSlot.id,
                        startTime = startTime,
                        endTime = endTime,
                        price = price,

                        onSuccess = {

                            showSlotDialog = false
                            editingSlot = null

                            viewModel.loadSlots(
                                token = token,
                                turfId = turfId
                            )
                        }
                    )
                }
            )
        }
    }

    // =========================================================
    // DELETE CONFIRMATION
    // =========================================================

    deletingSlot?.let { slot ->

        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    deletingSlot = null
                }
            },

            containerColor = SurfaceElevated,

            icon = {

                ConfirmationIcon(
                    icon = Icons.Default.Delete,
                    tint = ErrorRed
                )
            },

            title = {

                Text(
                    text = "Delete Slot?",
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text =
                        "Are you sure you want to delete " +
                                "${formatSlotDisplayTime(slot.startTime)} - " +
                                "${formatSlotDisplayTime(slot.endTime)}?",
                    color = SecondaryText
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel.deleteSlot(
                            token = token,
                            turfId = turfId,
                            slotId = slot.id,

                            onSuccess = {

                                deletingSlot = null

                                viewModel.loadSlots(
                                    token = token,
                                    turfId = turfId
                                )
                            }
                        )
                    },

                    enabled = !isLoading,

                    shape = RoundedCornerShape(10.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed,
                        contentColor = PrimaryText
                    )
                ) {

                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        deletingSlot = null
                    },
                    enabled = !isLoading
                ) {

                    Text(
                        text = "Cancel",
                        color = SecondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    // =========================================================
    // ACTIVATE / DEACTIVATE CONFIRMATION
    // =========================================================

    togglingSlot?.let { slot ->

        val isActive = slot.status.equals(
            "ACTIVE",
            ignoreCase = true
        )

        val newStatus = if (isActive) {
            "INACTIVE"
        } else {
            "ACTIVE"
        }

        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    togglingSlot = null
                }
            },

            containerColor = SurfaceElevated,

            icon = {

                ConfirmationIcon(
                    icon = if (isActive) {
                        Icons.Default.ToggleOff
                    } else {
                        Icons.Default.ToggleOn
                    },

                    tint = if (isActive) {
                        WarningOrange
                    } else {
                        PrimaryGreen
                    }
                )
            },

            title = {

                Text(
                    text = if (isActive) {
                        "Deactivate Slot?"
                    } else {
                        "Activate Slot?"
                    },
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text = if (isActive) {
                        "This slot will no longer be available for new bookings."
                    } else {
                        "This slot will become available for booking again."
                    },
                    color = SecondaryText
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel.updateSlotStatus(
                            token = token,
                            turfId = turfId,
                            slotId = slot.id,
                            status = newStatus,

                            onSuccess = {

                                togglingSlot = null

                                viewModel.loadSlots(
                                    token = token,
                                    turfId = turfId
                                )
                            }
                        )
                    },

                    enabled = !isLoading,

                    shape = RoundedCornerShape(10.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isActive) {
                            WarningOrange
                        } else {
                            PrimaryGreen
                        },
                        contentColor = if (isActive) {
                            PrimaryText
                        } else {
                            Background
                        }
                    )
                ) {

                    Text(
                        text = if (isActive) {
                            "Deactivate"
                        } else {
                            "Activate"
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        togglingSlot = null
                    },
                    enabled = !isLoading
                ) {

                    Text(
                        text = "Cancel",
                        color = SecondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    // =========================================================
    // CLEAR SUCCESS MESSAGE
    // =========================================================

    if (!successMessage.isNullOrBlank()) {

        LaunchedEffect(successMessage) {

            kotlinx.coroutines.delay(1500)

            viewModel.clearSuccessMessage()
        }
    }
}

// =============================================================
// PREMIUM TOP BAR
// =============================================================

@Composable
private fun PremiumSlotTopBar(
    onBackClick: () -> Unit,
    enabled: Boolean
) {

    Surface(
        color = Background,
        shadowElevation = 0.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF020907),
                            Color(0xFF071810),
                            Color(0xFF020907)
                        )
                    )
                )
        ) {

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 36.dp,
                            bottom = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceElevated
                    ) {

                        IconButton(
                            onClick = onBackClick,
                            enabled = enabled
                        ) {

                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,

                                contentDescription = "Back",

                                tint = PrimaryText,

                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Text(
                        text = "BookMyTurf",
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Border,
                                    PrimaryGreen.copy(alpha = 0.18f),
                                    Border,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

// =============================================================
// MESSAGE BANNER
// =============================================================

@Composable
private fun PremiumMessageBanner(
    message: String,
    isError: Boolean
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isError) {
            Color(0xFF21100F)
        } else {
            SurfaceHighlight
        },
        border = BorderStroke(
            1.dp,
            if (isError) {
                ErrorRed.copy(alpha = 0.25f)
            } else {
                PrimaryGreen.copy(alpha = 0.20f)
            }
        )
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (isError) {
                    Icons.Default.Warning
                } else {
                    Icons.Default.CheckCircle
                },
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isError) {
                    ErrorRed
                } else {
                    PrimaryGreen
                }
            )

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Text(
                text = message,
                color = if (isError) {
                    ErrorRed
                } else {
                    LightGreen
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// =============================================================
// LOADING
// =============================================================

@Composable
private fun LoadingSlots() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            color = PrimaryGreen,
            strokeWidth = 3.dp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Loading slots...",
            color = SecondaryText,
            fontSize = 13.sp
        )
    }
}

// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun EmptySlotsState(
    onCreateSlots: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(82.dp),
            shape = CircleShape,
            color = SurfaceHighlight,
            border = BorderStroke(
                1.dp,
                Border
            )
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                    tint = PrimaryGreen
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "No Slots Added",
            color = PrimaryText,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text =
                "Create time slots to make this turf available for booking.",
            color = SecondaryText,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onCreateSlots,
            shape = RoundedCornerShape(11.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Background
            )
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = "Create Slots",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =============================================================
// CREATE MULTIPLE SLOTS
// =============================================================

private fun createMultipleSlots(
    viewModel: AdminSlotViewModel,
    token: String,
    turfId: Int,
    newSlots: List<NewSlot>,
    onComplete: () -> Unit
) {

    if (newSlots.isEmpty()) {
        onComplete()
        return
    }

    createNextSlot(
        viewModel = viewModel,
        token = token,
        turfId = turfId,
        newSlots = newSlots,
        index = 0,
        onComplete = onComplete
    )
}

// =============================================================
// CREATE NEXT SLOT
// =============================================================

private fun createNextSlot(
    viewModel: AdminSlotViewModel,
    token: String,
    turfId: Int,
    newSlots: List<NewSlot>,
    index: Int,
    onComplete: () -> Unit
) {

    if (index >= newSlots.size) {
        onComplete()
        return
    }

    val slot = newSlots[index]

    viewModel.createSlot(
        token = token,
        turfId = turfId,
        startTime = slot.startTime,
        endTime = slot.endTime,
        price = slot.price,

        onSuccess = {

            createNextSlot(
                viewModel = viewModel,
                token = token,
                turfId = turfId,
                newSlots = newSlots,
                index = index + 1,
                onComplete = onComplete
            )
        }
    )
}

// =============================================================
// MULTIPLE SLOT FORM DIALOG
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MultipleSlotFormDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (List<NewSlot>) -> Unit
) {

    val newSlots = remember {
        mutableStateListOf<NewSlot>()
    }

    var startTime by remember {
        mutableStateOf<String?>(null)
    }

    var endTime by remember {
        mutableStateOf<String?>(null)
    }

    var price by remember {
        mutableStateOf("")
    }

    var showStartTimePicker by remember {
        mutableStateOf(false)
    }

    var showEndTimePicker by remember {
        mutableStateOf(false)
    }

    var validationError by remember {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },

        containerColor = SurfaceElevated,

        title = {

            PremiumDialogTitle(
                eyebrow = "NEW SCHEDULE",
                title = "Add Multiple Slots",
                subtitle = "${newSlots.size} slot(s) ready to create"
            )
        },

        text = {

            Column {

                TimeSelectionButton(
                    label = "Start Time",
                    value = startTime?.let {
                        formatSlotDisplayTime(it)
                    },
                    onClick = {
                        showStartTimePicker = true
                    },
                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                TimeSelectionButton(
                    label = "End Time",
                    value = endTime?.let {
                        formatSlotDisplayTime(it)
                    },
                    onClick = {
                        showEndTimePicker = true
                    },
                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                PremiumPriceField(
                    value = price,
                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.matches(
                                Regex("^\\d*(\\.\\d{0,2})?$")
                            )
                        ) {
                            price = value
                            validationError = null
                        }
                    },
                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    onClick = {

                        val cleanStart =
                            startTime?.trim().orEmpty()

                        val cleanEnd =
                            endTime?.trim().orEmpty()

                        val priceValue =
                            price.toDoubleOrNull()

                        when {

                            cleanStart.isBlank() -> {
                                validationError =
                                    "Please select start time."
                            }

                            cleanEnd.isBlank() -> {
                                validationError =
                                    "Please select end time."
                            }

                            priceValue == null ||
                                    priceValue <= 0 -> {
                                validationError =
                                    "Please enter a valid price."
                            }

                            !isEndTimeAfterStartTime(
                                cleanStart,
                                cleanEnd
                            ) -> {
                                validationError =
                                    "End time must be after start time."
                            }

                            else -> {

                                val duplicate =
                                    newSlots.any {
                                        it.startTime == cleanStart &&
                                                it.endTime == cleanEnd
                                    }

                                if (duplicate) {

                                    validationError =
                                        "This slot is already added."

                                } else {

                                    newSlots.add(
                                        NewSlot(
                                            startTime = cleanStart,
                                            endTime = cleanEnd,
                                            price = priceValue
                                        )
                                    )

                                    startTime = null
                                    endTime = null
                                    price = ""
                                    validationError = null
                                }
                            }
                        }
                    },

                    enabled = !isLoading,

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(11.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceHighlight,
                        contentColor = LightGreen
                    ),

                    border = BorderStroke(
                        1.dp,
                        Border
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Add This Slot",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // =================================================
                // PENDING SLOTS
                // =================================================

                if (newSlots.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    HorizontalDivider(
                        color = Border
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Slots to Create",
                        color = PrimaryText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    newSlots.forEachIndexed { index, slot ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceDark,
                            border = BorderStroke(
                                1.dp,
                                Border
                            )
                        ) {

                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 7.dp
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        "${formatSlotDisplayTime(slot.startTime)} - " +
                                                formatSlotDisplayTime(slot.endTime),

                                    modifier = Modifier.weight(1f),

                                    fontSize = 12.sp,

                                    fontWeight =
                                        FontWeight.SemiBold,

                                    color = PrimaryText
                                )

                                Text(
                                    text =
                                        "₹${formatSlotPrice(slot.price)}",

                                    fontSize = 12.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color = LightGreen
                                )

                                IconButton(
                                    onClick = {

                                        if (!isLoading) {
                                            newSlots.removeAt(index)
                                        }
                                    }
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Remove",

                                        modifier =
                                            Modifier.size(17.dp),

                                        tint =
                                            ErrorRed
                                    )
                                }
                            }
                        }
                    }
                }

                if (!validationError.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = validationError ?: "",
                        color = ErrorRed,
                        fontSize = 12.sp
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (newSlots.isEmpty()) {

                        validationError =
                            "Please add at least one slot."

                    } else {

                        onSave(
                            newSlots.toList()
                        )
                    }
                },

                enabled =
                    !isLoading &&
                            newSlots.isNotEmpty(),

                shape = RoundedCornerShape(10.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background
                )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(17.dp),
                        strokeWidth = 2.dp,
                        color = Background
                    )

                } else {

                    Text(
                        text = "Create Slots",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {

                Text(
                    text = "Cancel",
                    color = SecondaryText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )

    // =========================================================
    // START TIME PICKER
    // =========================================================

    if (showStartTimePicker) {

        val initialTime =
            parseTimeToPicker(startTime)

        val timePickerState =
            rememberTimePickerState(
                initialHour = initialTime.first,
                initialMinute = initialTime.second,
                is24Hour = false
            )

        TimePickerDialog(
            title = "Select Start Time",
            state = timePickerState,
            isLoading = isLoading,

            onCancel = {
                showStartTimePicker = false
            },

            onConfirm = {

                startTime =
                    timePickerStateToBackendValue(
                        timePickerState
                    )

                validationError = null

                showStartTimePicker = false
            }
        )
    }

    // =========================================================
    // END TIME PICKER
    // =========================================================

    if (showEndTimePicker) {

        val initialTime =
            parseTimeToPicker(endTime)

        val timePickerState =
            rememberTimePickerState(
                initialHour = initialTime.first,
                initialMinute = initialTime.second,
                is24Hour = false
            )

        TimePickerDialog(
            title = "Select End Time",
            state = timePickerState,
            isLoading = isLoading,

            onCancel = {
                showEndTimePicker = false
            },

            onConfirm = {

                endTime =
                    timePickerStateToBackendValue(
                        timePickerState
                    )

                validationError = null

                showEndTimePicker = false
            }
        )
    }
}

// =============================================================
// SINGLE SLOT FORM DIALOG
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleSlotFormDialog(
    slot: Slot,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        startTime: String,
        endTime: String,
        price: Double
    ) -> Unit
) {

    var startTime by remember(slot.id) {
        mutableStateOf(
            normalizeBackendTime(
                slot.startTime
            )
        )
    }

    var endTime by remember(slot.id) {
        mutableStateOf(
            normalizeBackendTime(
                slot.endTime
            )
        )
    }

    var price by remember(slot.id) {
        mutableStateOf(
            formatSlotPrice(slot.price)
        )
    }

    var showStartTimePicker by remember {
        mutableStateOf(false)
    }

    var showEndTimePicker by remember {
        mutableStateOf(false)
    }

    var validationError by remember {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },

        containerColor = SurfaceElevated,

        title = {

            PremiumDialogTitle(
                eyebrow = "EDIT SCHEDULE",
                title = "Edit Slot",
                subtitle = "Update time and pricing"
            )
        },

        text = {

            Column {

                TimeSelectionButton(
                    label = "Start Time",
                    value = formatSlotDisplayTime(startTime),
                    onClick = {
                        showStartTimePicker = true
                    },
                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                TimeSelectionButton(
                    label = "End Time",
                    value = formatSlotDisplayTime(endTime),
                    onClick = {
                        showEndTimePicker = true
                    },
                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                PremiumPriceField(
                    value = price,
                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.matches(
                                Regex(
                                    "^\\d*(\\.\\d{0,2})?$"
                                )
                            )
                        ) {
                            price = value
                            validationError = null
                        }
                    },
                    enabled = !isLoading
                )

                if (!validationError.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = validationError ?: "",
                        color = ErrorRed,
                        fontSize = 12.sp
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val cleanStart =
                        startTime.trim()

                    val cleanEnd =
                        endTime.trim()

                    val priceValue =
                        price.toDoubleOrNull()

                    when {

                        cleanStart.isBlank() -> {

                            validationError =
                                "Please select start time."
                        }

                        cleanEnd.isBlank() -> {

                            validationError =
                                "Please select end time."
                        }

                        priceValue == null ||
                                priceValue <= 0 -> {

                            validationError =
                                "Please enter a valid price."
                        }

                        !isEndTimeAfterStartTime(
                            cleanStart,
                            cleanEnd
                        ) -> {

                            validationError =
                                "End time must be after start time."
                        }

                        else -> {

                            onSave(
                                cleanStart,
                                cleanEnd,
                                priceValue
                            )
                        }
                    }
                },

                enabled = !isLoading,

                shape = RoundedCornerShape(10.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background
                )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(17.dp),
                        strokeWidth = 2.dp,
                        color = Background
                    )

                } else {

                    Text(
                        text = "Update",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {

                Text(
                    text = "Cancel",
                    color = SecondaryText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )

    // =========================================================
    // START TIME PICKER
    // =========================================================

    if (showStartTimePicker) {

        val initialTime =
            parseTimeToPicker(startTime)

        val timePickerState =
            rememberTimePickerState(
                initialHour = initialTime.first,
                initialMinute = initialTime.second,
                is24Hour = false
            )

        TimePickerDialog(
            title = "Select Start Time",
            state = timePickerState,
            isLoading = isLoading,

            onCancel = {
                showStartTimePicker = false
            },

            onConfirm = {

                startTime =
                    timePickerStateToBackendValue(
                        timePickerState
                    )

                validationError = null

                showStartTimePicker = false
            }
        )
    }

    // =========================================================
    // END TIME PICKER
    // =========================================================

    if (showEndTimePicker) {

        val initialTime =
            parseTimeToPicker(endTime)

        val timePickerState =
            rememberTimePickerState(
                initialHour = initialTime.first,
                initialMinute = initialTime.second,
                is24Hour = false
            )

        TimePickerDialog(
            title = "Select End Time",
            state = timePickerState,
            isLoading = isLoading,

            onCancel = {
                showEndTimePicker = false
            },

            onConfirm = {

                endTime =
                    timePickerStateToBackendValue(
                        timePickerState
                    )

                validationError = null

                showEndTimePicker = false
            }
        )
    }
}

// =============================================================
// DIALOG TITLE
// =============================================================

@Composable
private fun PremiumDialogTitle(
    eyebrow: String,
    title: String,
    subtitle: String
) {

    Column {

        Text(
            text = eyebrow,
            color = PrimaryGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = title,
            color = PrimaryText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            color = SecondaryText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

// =============================================================
// PRICE FIELD
// =============================================================

@Composable
private fun PremiumPriceField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text("Price per Slot")
        },
        placeholder = {
            Text("500")
        },
        prefix = {
            Text(
                text = "₹ ",
                color = LightGreen,
                fontWeight = FontWeight.Bold
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        singleLine = true,
        enabled = enabled,

        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = PrimaryText,
            unfocusedTextColor = PrimaryText,

            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = Border,

            focusedLabelColor = PrimaryGreen,
            unfocusedLabelColor = SecondaryText,

            focusedPlaceholderColor = MutedText,
            unfocusedPlaceholderColor = MutedText,

            cursorColor = PrimaryGreen,

            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,

            disabledContainerColor = SurfaceDark,
            disabledTextColor = MutedText,
            disabledBorderColor = Border,
            disabledLabelColor = MutedText
        ),

        shape = RoundedCornerShape(11.dp)
    )
}

// =============================================================
// TIME SELECTION BUTTON
// =============================================================

@Composable
private fun TimeSelectionButton(
    label: String,
    value: String?,
    onClick: () -> Unit,
    enabled: Boolean
) {

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        contentPadding = PaddingValues(
            horizontal = 13.dp,
            vertical = 10.dp
        ),
        border = BorderStroke(
            1.dp,
            Border
        )
    ) {

        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = PrimaryGreen
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                fontSize = 10.sp,
                color = SecondaryText
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value ?: "Select time",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (value.isNullOrBlank()) {
                    MutedText
                } else {
                    PrimaryText
                }
            )
        }
    }
}

// =============================================================
// TIME PICKER DIALOG
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    title: String,
    state: TimePickerState,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {

            if (!isLoading) {
                onCancel()
            }
        },

        containerColor = SurfaceElevated,

        icon = {

            ConfirmationIcon(
                icon = Icons.Default.AccessTime,
                tint = PrimaryGreen
            )
        },

        title = {

            Text(
                text = title,
                color = PrimaryText,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                TimePicker(
                    state = state
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "12-hour format • AM / PM",
                    color = MutedText,
                    fontSize = 11.sp
                )
            }
        },

        confirmButton = {

            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background
                )
            ) {

                Text(
                    text = "Set Time",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onCancel,
                enabled = !isLoading
            ) {

                Text(
                    text = "Cancel",
                    color = SecondaryText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

// =============================================================
// CONFIRMATION ICON
// =============================================================

@Composable
private fun ConfirmationIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {

    Surface(
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        color = SurfaceHighlight,
        border = BorderStroke(
            1.dp,
            tint.copy(alpha = 0.25f)
        )
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = tint
            )
        }
    }
}

// =============================================================
// SLOT CARD
// =============================================================

@Composable
private fun SlotCard(
    slot: Slot,
    onEdit: () -> Unit,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {

    val isActive =
        slot.status.equals(
            "ACTIVE",
            ignoreCase = true
        )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        color = SurfaceDark,
        border = BorderStroke(
            1.dp,
            Border
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // =================================================
            // TIME + STATUS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceHighlight
                ) {

                    Box(
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
                                PrimaryGreen
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(11.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "${formatSlotDisplayTime(slot.startTime)} - " +
                                    formatSlotDisplayTime(slot.endTime),

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            PrimaryText,

                        maxLines = 1
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Time slot",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                SlotStatusBadge(
                    isActive = isActive
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            HorizontalDivider(
                color = Border
            )

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            // =================================================
            // PRICE
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "PRICE / SLOT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = MutedText
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "₹${formatSlotPrice(slot.price)}",

                        fontSize = 19.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            LightGreen
                    )
                }

                Text(
                    text =
                        if (isActive) {
                            "Available for booking"
                        } else {
                            "Not available"
                        },

                    fontSize = 10.sp,

                    color =
                        if (isActive) {
                            LightGreen
                        } else {
                            MutedText
                        },

                    fontWeight =
                        FontWeight.Medium,

                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // ACTIONS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(7.dp)
            ) {

                // EDIT
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 3.dp
                    ),
                    border = BorderStroke(
                        1.dp,
                        Border
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription =
                            "Edit Slot",
                        modifier = Modifier.size(16.dp),
                        tint = LightGreen
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = "Edit",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LightGreen,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // ACTIVATE / DEACTIVATE
                OutlinedButton(
                    onClick = onToggleStatus,
                    modifier = Modifier
                        .weight(1.35f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 3.dp
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isActive) {
                            WarningOrange.copy(
                                alpha = 0.45f
                            )
                        } else {
                            Border
                        }
                    )
                ) {

                    Icon(
                        imageVector = if (isActive) {
                            Icons.Default.ToggleOff
                        } else {
                            Icons.Default.ToggleOn
                        },
                        contentDescription =
                            "Change Status",
                        modifier = Modifier.size(17.dp),
                        tint = if (isActive) {
                            WarningOrange
                        } else {
                            LightGreen
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = if (isActive) {
                            "Deactivate"
                        } else {
                            "Activate"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isActive) {
                            WarningOrange
                        } else {
                            LightGreen
                        },
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // DELETE
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(0.9f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 3.dp
                    ),
                    border = BorderStroke(
                        1.dp,
                        ErrorRed.copy(alpha = 0.35f)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription =
                            "Delete Slot",
                        modifier = Modifier.size(16.dp),
                        tint = ErrorRed
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = "Delete",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorRed,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

// =============================================================
// SLOT STATUS BADGE
// =============================================================

@Composable
private fun SlotStatusBadge(
    isActive: Boolean
) {

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = if (isActive) {
            PrimaryGreen.copy(alpha = 0.12f)
        } else {
            SurfaceHighlight
        },
        border = BorderStroke(
            1.dp,
            if (isActive) {
                PrimaryGreen.copy(alpha = 0.22f)
            } else {
                Border
            }
        )
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) {
                            PrimaryGreen
                        } else {
                            MutedText
                        }
                    )
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = if (isActive) {
                    "ACTIVE"
                } else {
                    "INACTIVE"
                },

                color = if (isActive) {
                    LightGreen
                } else {
                    MutedText
                },

                fontSize = 9.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

// =============================================================
// BACKEND TIME -> PICKER TIME
// =============================================================

private fun parseTimeToPicker(
    value: String?
): Pair<Int, Int> {

    if (value.isNullOrBlank()) {
        return Pair(9, 0)
    }

    val cleanValue =
        value.trim().uppercase()

    return try {

        when {

            cleanValue.matches(
                Regex(
                    "^\\d{2}:\\d{2}:\\d{2}$"
                )
            ) -> {

                val input =
                    SimpleDateFormat(
                        "HH:mm:ss",
                        Locale.getDefault()
                    )

                val parsed =
                    input.parse(cleanValue)

                if (parsed != null) {

                    val calendar =
                        Calendar.getInstance()

                    calendar.time = parsed

                    Pair(
                        calendar.get(
                            Calendar.HOUR_OF_DAY
                        ),
                        calendar.get(
                            Calendar.MINUTE
                        )
                    )

                } else {

                    Pair(9, 0)
                }
            }

            cleanValue.matches(
                Regex(
                    "^\\d{2}:\\d{2}$"
                )
            ) -> {

                val input =
                    SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    )

                val parsed =
                    input.parse(cleanValue)

                if (parsed != null) {

                    val calendar =
                        Calendar.getInstance()

                    calendar.time = parsed

                    Pair(
                        calendar.get(
                            Calendar.HOUR_OF_DAY
                        ),
                        calendar.get(
                            Calendar.MINUTE
                        )
                    )

                } else {

                    Pair(9, 0)
                }
            }

            cleanValue.matches(
                Regex(
                    "^\\d{1,2}:\\d{2}\\s?(AM|PM)$"
                )
            ) -> {

                val input =
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )

                val parsed =
                    input.parse(cleanValue)

                if (parsed != null) {

                    val calendar =
                        Calendar.getInstance()

                    calendar.time = parsed

                    Pair(
                        calendar.get(
                            Calendar.HOUR_OF_DAY
                        ),
                        calendar.get(
                            Calendar.MINUTE
                        )
                    )

                } else {

                    Pair(9, 0)
                }
            }

            else -> {
                Pair(9, 0)
            }
        }

    } catch (e: Exception) {

        Pair(9, 0)
    }
}

// =============================================================
// PICKER -> BACKEND TIME
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
private fun timePickerStateToBackendValue(
    state: TimePickerState
): String {

    return String.format(
        Locale.getDefault(),
        "%02d:%02d:00",
        state.hour,
        state.minute
    )
}

// =============================================================
// BACKEND TIME -> DISPLAY TIME
// =============================================================

private fun formatSlotDisplayTime(
    value: String
): String {

    val cleanValue =
        value.trim().uppercase()

    if (cleanValue.isBlank()) {
        return "—"
    }

    return try {

        when {

            cleanValue.matches(
                Regex(
                    "^\\d{2}:\\d{2}:\\d{2}$"
                )
            ) -> {

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
                    input.parse(cleanValue)

                if (parsed != null) {
                    output.format(parsed)
                } else {
                    cleanValue
                }
            }

            cleanValue.matches(
                Regex(
                    "^\\d{2}:\\d{2}$"
                )
            ) -> {

                val input =
                    SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    )

                val output =
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )

                val parsed =
                    input.parse(cleanValue)

                if (parsed != null) {
                    output.format(parsed)
                } else {
                    cleanValue
                }
            }

            cleanValue.matches(
                Regex(
                    "^\\d{1,2}:\\d{2}\\s?(AM|PM)$"
                )
            ) -> {

                cleanValue
            }

            else -> {
                cleanValue
            }
        }

    } catch (e: Exception) {

        cleanValue
    }
}

// =============================================================
// NORMALIZE BACKEND TIME
// =============================================================

private fun normalizeBackendTime(
    value: String
): String {

    val cleanValue =
        value.trim()

    return when {

        cleanValue.matches(
            Regex(
                "^\\d{2}:\\d{2}:\\d{2}$"
            )
        ) -> {

            cleanValue
        }

        cleanValue.matches(
            Regex(
                "^\\d{2}:\\d{2}$"
            )
        ) -> {

            "$cleanValue:00"
        }

        cleanValue.matches(
            Regex(
                "^\\d{1,2}:\\d{2}\\s?(AM|PM)$",
                RegexOption.IGNORE_CASE
            )
        ) -> {

            try {

                val input =
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )

                val parsed =
                    input.parse(
                        cleanValue.uppercase()
                    )

                if (parsed != null) {

                    val output =
                        SimpleDateFormat(
                            "HH:mm:ss",
                            Locale.getDefault()
                        )

                    output.format(parsed)

                } else {

                    cleanValue
                }

            } catch (e: Exception) {

                cleanValue
            }
        }

        else -> {
            cleanValue
        }
    }
}

// =============================================================
// VALIDATE TIME RANGE
// =============================================================

private fun isEndTimeAfterStartTime(
    startTime: String,
    endTime: String
): Boolean {

    return try {

        val start =
            parseTimeToMinutes(
                startTime
            )

        val end =
            parseTimeToMinutes(
                endTime
            )

        end > start

    } catch (e: Exception) {

        false
    }
}

// =============================================================
// TIME -> MINUTES
// =============================================================

private fun parseTimeToMinutes(
    value: String
): Int {

    val cleanValue =
        value.trim().uppercase()

    return when {

        cleanValue.matches(
            Regex(
                "^\\d{2}:\\d{2}:\\d{2}$"
            )
        ) -> {

            val parts =
                cleanValue.split(":")

            val hour =
                parts[0].toInt()

            val minute =
                parts[1].toInt()

            hour * 60 + minute
        }

        cleanValue.matches(
            Regex(
                "^\\d{2}:\\d{2}$"
            )
        ) -> {

            val parts =
                cleanValue.split(":")

            val hour =
                parts[0].toInt()

            val minute =
                parts[1].toInt()

            hour * 60 + minute
        }

        cleanValue.matches(
            Regex(
                "^\\d{1,2}:\\d{2}\\s?(AM|PM)$"
            )
        ) -> {

            val input =
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                )

            val parsed =
                input.parse(cleanValue)

            if (parsed == null) {
                throw IllegalArgumentException()
            }

            val calendar =
                Calendar.getInstance()

            calendar.time = parsed

            calendar.get(
                Calendar.HOUR_OF_DAY
            ) * 60 +
                    calendar.get(
                        Calendar.MINUTE
                    )
        }

        else -> {

            throw IllegalArgumentException()
        }
    }
}

// =============================================================
// PRICE FORMAT
// =============================================================

private fun formatSlotPrice(
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