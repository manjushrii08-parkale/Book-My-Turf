package com.example.bookmyturf.screens.admin

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminSlotRepository
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AdminSlotViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// =============================================================
// LOCAL COLORS
// =============================================================

private val AdminRed = Color(0xFFB91C1C)
private val AdminRedBackground = Color(0xFFFFF1F2)

private val AdminOrange = Color(0xFFB45309)
private val AdminOrangeBackground = Color(0xFFFFF7ED)


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

        containerColor = AdminOffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Manage Slots",
                            color = AdminDarkCharcoal,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Turf #$turfId",
                            fontSize = 12.sp,
                            color = AdminGray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack,
                        enabled = !isLoading
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AdminDarkGreen
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminWhite
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Turf Slots",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AdminDarkCharcoal
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "${slots.size} slots configured",
                        color = AdminGray,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = {

                        editingSlot = null
                        showSlotDialog = true
                    },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
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
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // =================================================
            // ERROR MESSAGE
            // =================================================

            if (!error.isNullOrBlank()) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = AdminRedBackground
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = AdminRed
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = error ?: "",
                            color = AdminRed,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            // =================================================
            // SUCCESS MESSAGE
            // =================================================

            if (!successMessage.isNullOrBlank()) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = AdminLightGreen.copy(alpha = 0.16f)
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = AdminForestGreen
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = successMessage ?: "",
                            color = AdminForestGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

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

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator(
                        color = AdminForestGreen
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading slots...",
                        color = AdminGray
                    )
                }

                return@Column
            }

            // =================================================
            // EMPTY STATE
            // =================================================

            if (
                !isLoading &&
                slots.isEmpty()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Surface(
                        modifier = Modifier.size(82.dp),
                        shape = CircleShape,
                        color = AdminLightGreen.copy(alpha = 0.20f)
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = AdminForestGreen
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "No Slots Added",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AdminDarkCharcoal
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Create time slots to make this turf available for booking.",
                        color = AdminGray,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Button(
                        onClick = {

                            editingSlot = null
                            showSlotDialog = true
                        },
                        shape = RoundedCornerShape(11.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AdminDarkGreen,
                            contentColor = AdminWhite
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Create Slots",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                return@Column
            }

            // =================================================
            // SLOT LIST
            // =================================================

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 30.dp
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

                onSave = {
                        startTime,
                        endTime,
                        price ->

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

            icon = {

                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = AdminRedBackground
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = AdminRed
                        )
                    }
                }
            },

            title = {

                Text(
                    text = "Delete Slot?",
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )
            },

            text = {

                Text(
                    text =
                        "Are you sure you want to delete " +
                                "${formatSlotDisplayTime(slot.startTime)} - " +
                                "${formatSlotDisplayTime(slot.endTime)}?"
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
                        containerColor = AdminRed,
                        contentColor = AdminWhite
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
                        color = AdminGray,
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

        val isActive =
            slot.status.equals(
                "ACTIVE",
                ignoreCase = true
            )

        val newStatus =
            if (isActive) {
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

            icon = {

                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = if (isActive) {
                        AdminOrangeBackground
                    } else {
                        AdminLightGreen.copy(alpha = 0.18f)
                    }
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = if (isActive) {
                                Icons.Default.ToggleOff
                            } else {
                                Icons.Default.ToggleOn
                            },
                            contentDescription = null,
                            modifier = Modifier.size(27.dp),
                            tint = if (isActive) {
                                AdminOrange
                            } else {
                                AdminForestGreen
                            }
                        )
                    }
                }
            },

            title = {

                Text(
                    text =
                        if (isActive) {
                            "Deactivate Slot?"
                        } else {
                            "Activate Slot?"
                        },

                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )
            },

            text = {

                Text(
                    text =
                        if (isActive) {
                            "This slot will no longer be available for new bookings."
                        } else {
                            "This slot will become available for booking again."
                        },

                    color = AdminGray
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
                            AdminOrange
                        } else {
                            AdminDarkGreen
                        },

                        contentColor = AdminWhite
                    )
                ) {

                    Text(
                        text =
                            if (isActive) {
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
                        color = AdminGray,
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

        title = {

            Text(
                text = "Add Multiple Slots",
                fontWeight = FontWeight.Bold,
                color = AdminDarkCharcoal
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "${newSlots.size} slot(s) ready to create",
                    fontSize = 12.sp,
                    color = AdminGray
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // =================================================
                // START TIME
                // =================================================

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
                    modifier = Modifier.height(8.dp)
                )

                // =================================================
                // END TIME
                // =================================================

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
                    modifier = Modifier.height(8.dp)
                )

                // =================================================
                // PRICE
                // =================================================

                OutlinedTextField(

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

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Price per Slot")
                    },

                    placeholder = {
                        Text("500")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                    singleLine = true,

                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // ADD SLOT
                // =================================================

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
                                            startTime =
                                                cleanStart,

                                            endTime =
                                                cleanEnd,

                                            price =
                                                priceValue
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

                    shape = RoundedCornerShape(10.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
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
                        text = "Add This Slot",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // =================================================
                // PENDING SLOTS
                // =================================================

                if (newSlots.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Slots to Create",
                        fontWeight = FontWeight.Bold,
                        color = AdminDarkCharcoal
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    newSlots.forEachIndexed {
                            index,
                            slot ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 3.dp
                                ),
                            shape =
                                RoundedCornerShape(10.dp),

                            color =
                                AdminOffWhite
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

                                    modifier =
                                        Modifier.weight(1f),

                                    fontSize = 12.sp,

                                    fontWeight =
                                        FontWeight.SemiBold,

                                    color =
                                        AdminDarkCharcoal
                                )

                                Text(
                                    text =
                                        "₹${formatSlotPrice(slot.price)}",

                                    fontSize = 12.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        AdminForestGreen
                                )

                                IconButton(
                                    onClick = {

                                        if (!isLoading) {

                                            newSlots.removeAt(
                                                index
                                            )
                                        }
                                    }
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Remove",

                                        modifier =
                                            Modifier.size(18.dp),

                                        tint =
                                            AdminRed
                                    )
                                }
                            }
                        }
                    }
                }

                // =================================================
                // VALIDATION ERROR
                // =================================================

                if (!validationError.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            validationError ?: "",

                        color =
                            AdminRed,

                        fontSize =
                            12.sp
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

                shape =
                    RoundedCornerShape(10.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDarkGreen,

                        contentColor =
                            AdminWhite
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = AdminWhite
                    )

                } else {

                    Text(
                        text = "Create Slots",
                        fontWeight = FontWeight.SemiBold
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
                    color = AdminGray,
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

        title = {

            Text(
                text = "Edit Slot",
                fontWeight = FontWeight.Bold,
                color = AdminDarkCharcoal
            )
        },

        text = {

            Column {

                // =================================================
                // START TIME
                // =================================================

                TimeSelectionButton(
                    label = "Start Time",

                    value =
                        formatSlotDisplayTime(
                            startTime
                        ),

                    onClick = {
                        showStartTimePicker = true
                    },

                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // END TIME
                // =================================================

                TimeSelectionButton(
                    label = "End Time",

                    value =
                        formatSlotDisplayTime(
                            endTime
                        ),

                    onClick = {
                        showEndTimePicker = true
                    },

                    enabled = !isLoading
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // PRICE
                // =================================================

                OutlinedTextField(

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

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Price per Slot"
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true,

                    enabled =
                        !isLoading
                )

                if (
                    !validationError.isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            validationError ?: "",

                        color =
                            AdminRed,

                        fontSize =
                            12.sp
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

                enabled =
                    !isLoading,

                shape =
                    RoundedCornerShape(10.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDarkGreen,

                        contentColor =
                            AdminWhite
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp,

                        color =
                            AdminWhite
                    )

                } else {

                    Text(
                        text =
                            "Update",

                        fontWeight =
                            FontWeight.SemiBold
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
                    color = AdminGray,
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
            parseTimeToPicker(
                startTime
            )

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
            parseTimeToPicker(
                endTime
            )

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
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 10.dp
        )
    ) {

        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = AdminForestGreen
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                fontSize = 11.sp,
                color = AdminGray
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value ?: "Select time",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (value.isNullOrBlank()) {
                    AdminGray
                } else {
                    AdminDarkCharcoal
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

        icon = {

            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = AdminLightGreen.copy(
                    alpha = 0.18f
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.AccessTime,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(25.dp),

                        tint =
                            AdminForestGreen
                    )
                }
            }
        },

        title = {

            Text(
                text = title,
                color = AdminDarkCharcoal,
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
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "12-hour format • AM / PM",
                    color = AdminGray,
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
                    containerColor = AdminDarkGreen,
                    contentColor = AdminWhite
                )
            ) {

                Text(
                    text = "Set Time",
                    fontWeight = FontWeight.SemiBold
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
                    color = AdminGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
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

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            // =================================================
            // TIME + STATUS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    modifier =
                        Modifier.size(42.dp),

                    shape =
                        RoundedCornerShape(12.dp),

                    color =
                        AdminLightGreen.copy(
                            alpha = 0.18f
                        )
                ) {

                    Box(
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
                                AdminForestGreen
                        )
                    }
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
                            "${formatSlotDisplayTime(slot.startTime)} - " +
                                    formatSlotDisplayTime(slot.endTime),

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AdminDarkCharcoal,

                        maxLines =
                            1
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Time slot",

                        fontSize =
                            11.sp,

                        color =
                            AdminGray
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                SlotStatusBadge(
                    isActive =
                        isActive
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            HorizontalDivider(
                color =
                    AdminOffWhite
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =================================================
            // PRICE
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
                            "Price per slot",

                        fontSize =
                            11.sp,

                        color =
                            AdminGray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            "₹${formatSlotPrice(slot.price)}",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AdminDarkGreen
                    )
                }

                Text(
                    text =
                        if (isActive) {
                            "Available for booking"
                        } else {
                            "Not available for booking"
                        },

                    fontSize =
                        11.sp,

                    color =
                        if (isActive) {
                            AdminForestGreen
                        } else {
                            AdminGray
                        },

                    fontWeight =
                        FontWeight.Medium,

                    maxLines =
                        1
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
// ACTIONS
// =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // EDIT
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 4.dp
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Slot",
                        modifier = Modifier.size(16.dp),
                        tint = AdminForestGreen
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = "Edit",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdminForestGreen,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // ACTIVATE / DEACTIVATE
                OutlinedButton(
                    onClick = onToggleStatus,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 4.dp
                    )
                ) {

                    Icon(
                        imageVector = if (isActive) {
                            Icons.Default.ToggleOff
                        } else {
                            Icons.Default.ToggleOn
                        },
                        contentDescription = "Change Status",
                        modifier = Modifier.size(17.dp),
                        tint = if (isActive) {
                            AdminOrange
                        } else {
                            AdminForestGreen
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
                            AdminOrange
                        } else {
                            AdminForestGreen
                        },
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // DELETE
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 4.dp
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AdminRedBackground,
                        contentColor = AdminRed
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Slot",
                        modifier = Modifier.size(16.dp),
                        tint = AdminRed
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = "Delete",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AdminRed,
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
        shape =
            RoundedCornerShape(50.dp),

        color =
            if (isActive) {
                AdminLightGreen.copy(
                    alpha = 0.20f
                )
            } else {
                AdminOffWhite
            }
    ) {

        Row(
            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                AdminForestGreen
                            } else {
                                AdminGray
                            }
                        )
            )

            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )

            Text(
                text =
                    if (isActive) {
                        "ACTIVE"
                    } else {
                        "INACTIVE"
                    },

                color =
                    if (isActive) {
                        AdminForestGreen
                    } else {
                        AdminGray
                    },

                fontSize =
                    10.sp,

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

                    calendar.time =
                        parsed

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

                    calendar.time =
                        parsed

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

                    calendar.time =
                        parsed

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
                input.parse(
                    cleanValue
                )

            if (parsed == null) {
                throw IllegalArgumentException()
            }

            val calendar =
                Calendar.getInstance()

            calendar.time =
                parsed

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
