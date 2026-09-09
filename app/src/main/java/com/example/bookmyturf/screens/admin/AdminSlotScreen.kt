package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminSlotRepository
import com.example.bookmyturf.viewmodel.AdminSlotViewModel

private val SlotGreen = Color(0xFF14532D)
private val SlotBackground = Color(0xFFF8FAFC)
private val SlotDark = Color(0xFF0F172A)
private val SlotGray = Color(0xFF64748B)
private val SlotRed = Color(0xFFDC2626)


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

    val viewModel: AdminSlotViewModel =
        viewModel(
            factory = factory
        )


    // =========================================================
    // STATE
    // =========================================================

    val slots by
    viewModel.slots.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()

    val successMessage by
    viewModel.successMessage.collectAsState()


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

        containerColor = SlotBackground,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Manage Slots",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Turf #$turfId",
                            fontSize = 12.sp,
                            color = SlotGray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack,
                        enabled = !isLoading
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,
                            contentDescription =
                                "Back"
                        )
                    }
                },

                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                Color.White
                        )
            )
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
        ) {


            // =================================================
            // HEADER
            // =================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = "Turf Slots",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            SlotDark
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "${slots.size} slots configured",

                        color =
                            SlotGray,

                        fontSize =
                            13.sp
                    )
                }


                Button(

                    onClick = {

                        editingSlot = null
                        showSlotDialog = true
                    },

                    enabled =
                        !isLoading,

                    shape =
                        RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Add,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text = "Add Slots"
                    )
                }
            }


            // =================================================
            // ERROR
            // =================================================

            if (
                !error.isNullOrBlank()
            ) {

                Text(

                    text =
                        error ?: "",

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),

                    color =
                        SlotRed,

                    fontSize =
                        13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }


            // =================================================
            // LOADING
            // =================================================

            if (
                isLoading &&
                slots.isEmpty()
            ) {

                Column(

                    modifier =
                        Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator(
                        color =
                            SlotGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading slots...",
                        color = SlotGray
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

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.AccessTime,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(60.dp),

                        tint =
                            SlotGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(

                        text =
                            "No Slots Added",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            SlotDark
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(

                        text =
                            "Create multiple time slots for this turf.",

                        color =
                            SlotGray,

                        fontSize =
                            14.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(18.dp)
                    )

                    Button(

                        onClick = {

                            editingSlot = null
                            showSlotDialog = true
                        },

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Add,

                            contentDescription =
                                null
                        )

                        Spacer(
                            modifier =
                                Modifier.width(6.dp)
                        )

                        Text(
                            text =
                                "Create Slots"
                        )
                    }
                }

                return@Column
            }


            // =================================================
            // SLOT LIST
            // =================================================

            LazyColumn(

                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp),

                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 30.dp
                    )
            ) {

                items(

                    items =
                        slots,

                    key = {
                        it.id
                    }

                ) { slot ->

                    SlotCard(

                        slot =
                            slot,

                        onEdit = {

                            editingSlot =
                                slot

                            showSlotDialog =
                                true
                        },

                        onToggleStatus = {

                            togglingSlot =
                                slot
                        },

                        onDelete = {

                            deletingSlot =
                                slot
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

        // IMPORTANT:
        // Take delegated state into a local immutable value.
        // This fixes:
        //
        // Smart cast to 'Slot' is impossible
        // because 'editingSlot' is a delegated property.

        val currentEditingSlot =
            editingSlot


        if (
            currentEditingSlot == null
        ) {

            // =================================================
            // ADD MULTIPLE SLOTS
            // =================================================

            MultipleSlotFormDialog(

                isLoading =
                    isLoading,

                onDismiss = {

                    if (!isLoading) {

                        showSlotDialog =
                            false
                    }
                },

                onSave = { newSlots ->

                    showSlotDialog =
                        false

                    createMultipleSlots(

                        viewModel =
                            viewModel,

                        token =
                            token,

                        turfId =
                            turfId,

                        newSlots =
                            newSlots,

                        onComplete = {

                            viewModel.loadSlots(

                                token =
                                    token,

                                turfId =
                                    turfId
                            )
                        }
                    )
                }
            )

        } else {

            // =================================================
            // EDIT SINGLE SLOT
            // =================================================

            SingleSlotFormDialog(

                slot =
                    currentEditingSlot,

                isLoading =
                    isLoading,

                onDismiss = {

                    if (!isLoading) {

                        showSlotDialog =
                            false

                        editingSlot =
                            null
                    }
                },

                onSave = {
                        startTime,
                        endTime,
                        price ->

                    viewModel.updateSlot(

                        token =
                            token,

                        turfId =
                            turfId,

                        slotId =
                            currentEditingSlot.id,

                        startTime =
                            startTime,

                        endTime =
                            endTime,

                        price =
                            price,

                        onSuccess = {

                            showSlotDialog =
                                false

                            editingSlot =
                                null

                            viewModel.loadSlots(

                                token =
                                    token,

                                turfId =
                                    turfId
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

                    deletingSlot =
                        null
                }
            },

            title = {

                Text(
                    text =
                        "Delete Slot"
                )
            },

            text = {

                Text(

                    text =
                        "Are you sure you want to delete " +
                                "${formatSlotTime(slot.startTime)} - " +
                                "${formatSlotTime(slot.endTime)}?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        viewModel.deleteSlot(

                            token =
                                token,

                            turfId =
                                turfId,

                            slotId =
                                slot.id,

                            onSuccess = {

                                deletingSlot =
                                    null

                                viewModel.loadSlots(

                                    token =
                                        token,

                                    turfId =
                                        turfId
                                )
                            }
                        )
                    },

                    enabled =
                        !isLoading
                ) {

                    Text(

                        text =
                            "Delete",

                        color =
                            SlotRed
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        deletingSlot =
                            null
                    },

                    enabled =
                        !isLoading
                ) {

                    Text(
                        text =
                            "Cancel"
                    )
                }
            }
        )
    }


    // =========================================================
    // STATUS CONFIRMATION
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

                    togglingSlot =
                        null
                }
            },

            title = {

                Text(

                    text =
                        if (isActive) {
                            "Deactivate Slot"
                        } else {
                            "Activate Slot"
                        }
                )
            },

            text = {

                Text(

                    text =
                        if (isActive) {

                            "Are you sure you want to deactivate this slot?"

                        } else {

                            "Are you sure you want to activate this slot?"
                        }
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        viewModel.updateSlotStatus(

                            token =
                                token,

                            turfId =
                                turfId,

                            slotId =
                                slot.id,

                            status =
                                newStatus,

                            onSuccess = {

                                togglingSlot =
                                    null

                                viewModel.loadSlots(

                                    token =
                                        token,

                                    turfId =
                                        turfId
                                )
                            }
                        )
                    },

                    enabled =
                        !isLoading
                ) {

                    Text(

                        text =
                            if (isActive) {
                                "Deactivate"
                            } else {
                                "Activate"
                            },

                        color =
                            SlotGreen
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        togglingSlot =
                            null
                    },

                    enabled =
                        !isLoading
                ) {

                    Text(
                        text =
                            "Cancel"
                    )
                }
            }
        )
    }


    // =========================================================
    // SUCCESS MESSAGE
    // =========================================================

    if (
        !successMessage.isNullOrBlank()
    ) {

        LaunchedEffect(
            successMessage
        ) {

            kotlinx.coroutines.delay(
                1500
            )

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

    if (
        newSlots.isEmpty()
    ) {

        onComplete()

        return
    }

    createNextSlot(

        viewModel =
            viewModel,

        token =
            token,

        turfId =
            turfId,

        newSlots =
            newSlots,

        index =
            0,

        onComplete =
            onComplete
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

    if (
        index >= newSlots.size
    ) {

        onComplete()

        return
    }

    val slot =
        newSlots[index]

    viewModel.createSlot(

        token =
            token,

        turfId =
            turfId,

        startTime =
            slot.startTime,

        endTime =
            slot.endTime,

        price =
            slot.price,

        onSuccess = {

            createNextSlot(

                viewModel =
                    viewModel,

                token =
                    token,

                turfId =
                    turfId,

                newSlots =
                    newSlots,

                index =
                    index + 1,

                onComplete =
                    onComplete
            )
        }
    )
}


// =============================================================
// MULTIPLE SLOT FORM DIALOG
// =============================================================

@Composable
private fun MultipleSlotFormDialog(

    isLoading: Boolean,

    onDismiss: () -> Unit,

    onSave: (
        List<NewSlot>
    ) -> Unit

) {

    val newSlots =
        remember {
            mutableStateListOf<NewSlot>()
        }

    var startTime by remember {
        mutableStateOf("")
    }

    var endTime by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
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
                text =
                    "Add Multiple Slots"
            )
        },

        text = {

            Column {

                Text(

                    text =
                        "${newSlots.size} slot(s) ready to create",

                    fontSize =
                        12.sp,

                    color =
                        SlotGray
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // START TIME
                // =================================================

                OutlinedTextField(

                    value =
                        startTime,

                    onValueChange = {

                        startTime =
                            it

                        validationError =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Start Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "09:00 AM"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !isLoading
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                // =================================================
                // END TIME
                // =================================================

                OutlinedTextField(

                    value =
                        endTime,

                    onValueChange = {

                        endTime =
                            it

                        validationError =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "End Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "10:00 AM"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !isLoading
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                // =================================================
                // PRICE
                // =================================================

                OutlinedTextField(

                    value =
                        price,

                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.matches(
                                Regex(
                                    "^\\d*(\\.\\d{0,2})?$"
                                )
                            )
                        ) {

                            price =
                                value

                            validationError =
                                null
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Price per Slot"
                        )
                    },

                    placeholder = {
                        Text(
                            "500"
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


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                // =================================================
                // ADD THIS SLOT
                // =================================================

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
                                    "Please enter start time."
                            }

                            cleanEnd.isBlank() -> {

                                validationError =
                                    "Please enter end time."
                            }

                            priceValue == null ||
                                    priceValue <= 0 -> {

                                validationError =
                                    "Please enter a valid price."
                            }

                            cleanStart.equals(
                                cleanEnd,
                                ignoreCase = true
                            ) -> {

                                validationError =
                                    "Start and end time cannot be same."
                            }

                            else -> {

                                val duplicate =
                                    newSlots.any {

                                        it.startTime.equals(
                                            cleanStart,
                                            ignoreCase = true
                                        ) &&
                                                it.endTime.equals(
                                                    cleanEnd,
                                                    ignoreCase = true
                                                )
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

                                    startTime =
                                        ""

                                    endTime =
                                        ""

                                    price =
                                        ""

                                    validationError =
                                        null
                                }
                            }
                        }
                    },

                    enabled =
                        !isLoading,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(10.dp)
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Add,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        text =
                            "Add This Slot"
                    )
                }


                // =================================================
                // PENDING SLOTS
                // =================================================

                if (
                    newSlots.isNotEmpty()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(

                        text =
                            "Slots to Create",

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )


                    newSlots.forEachIndexed {
                            index,
                            slot ->

                        Row(

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 4.dp
                                    ),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(

                                text =
                                    "${index + 1}. " +
                                            "${slot.startTime} - " +
                                            "${slot.endTime}  " +
                                            "₹${formatSlotPrice(slot.price)}",

                                modifier =
                                    Modifier.weight(1f),

                                fontSize =
                                    13.sp,

                                color =
                                    SlotDark
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

                                    tint =
                                        SlotRed
                                )
                            }
                        }
                    }
                }


                // =================================================
                // VALIDATION ERROR
                // =================================================

                if (
                    !validationError.isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(

                        text =
                            validationError ?: "",

                        color =
                            SlotRed,

                        fontSize =
                            12.sp
                    )
                }
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    if (
                        newSlots.isEmpty()
                    ) {

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
                    RoundedCornerShape(10.dp)
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        text =
                            "Create Slots"
                    )
                }
            }
        },

        dismissButton = {

            TextButton(

                onClick =
                    onDismiss,

                enabled =
                    !isLoading
            ) {

                Text(
                    text =
                        "Cancel"
                )
            }
        }
    )
}


// =============================================================
// SINGLE SLOT FORM DIALOG
// =============================================================

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
            slot.startTime
        )
    }

    var endTime by remember(slot.id) {

        mutableStateOf(
            slot.endTime
        )
    }

    var price by remember(slot.id) {

        mutableStateOf(
            formatSlotPrice(
                slot.price
            )
        )
    }

    var validationError by remember(slot.id) {

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
                text =
                    "Edit Slot"
            )
        },

        text = {

            Column {

                OutlinedTextField(

                    value =
                        startTime,

                    onValueChange = {

                        startTime =
                            it

                        validationError =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Start Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "09:00 AM"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !isLoading
                )


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                OutlinedTextField(

                    value =
                        endTime,

                    onValueChange = {

                        endTime =
                            it

                        validationError =
                            null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "End Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "10:00 AM"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !isLoading
                )


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                OutlinedTextField(

                    value =
                        price,

                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.matches(
                                Regex(
                                    "^\\d*(\\.\\d{0,2})?$"
                                )
                            )
                        ) {

                            price =
                                value

                            validationError =
                                null
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
                            SlotRed,

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
                                "Please enter start time."
                        }

                        cleanEnd.isBlank() -> {

                            validationError =
                                "Please enter end time."
                        }

                        priceValue == null ||
                                priceValue <= 0 -> {

                            validationError =
                                "Please enter a valid price."
                        }

                        cleanStart.equals(
                            cleanEnd,
                            ignoreCase = true
                        ) -> {

                            validationError =
                                "Start and end time cannot be same."
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
                    RoundedCornerShape(10.dp)
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        text =
                            "Update"
                    )
                }
            }
        },

        dismissButton = {

            TextButton(

                onClick =
                    onDismiss,

                enabled =
                    !isLoading
            ) {

                Text(
                    text =
                        "Cancel"
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
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
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
            // TIME
            // =================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        Icons.Default.AccessTime,

                    contentDescription =
                        null,

                    tint =
                        SlotGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(

                    text =
                        "${formatSlotTime(slot.startTime)} - " +
                                formatSlotTime(slot.endTime),

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        SlotDark
                )
            }


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            // =================================================
            // PRICE + STATUS
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
                            SlotGray
                    )

                    Text(

                        text =
                            "₹${formatSlotPrice(slot.price)}",

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            SlotDark
                    )
                }


                Text(

                    text =
                        if (isActive) {
                            "ACTIVE"
                        } else {
                            "INACTIVE"
                        },

                    color =
                        if (isActive) {
                            SlotGreen
                        } else {
                            Color.Gray
                        },

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            // =================================================
            // ACTIONS
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.End
            ) {

                IconButton(
                    onClick =
                        onEdit
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Edit,

                        contentDescription =
                            "Edit Slot",

                        tint =
                            SlotGreen
                    )
                }


                IconButton(
                    onClick =
                        onToggleStatus
                ) {

                    Icon(

                        imageVector =
                            if (isActive) {
                                Icons.Default.ToggleOn
                            } else {
                                Icons.Default.ToggleOff
                            },

                        contentDescription =
                            "Change Status",

                        tint =
                            if (isActive) {
                                SlotGreen
                            } else {
                                Color.Gray
                            }
                    )
                }


                IconButton(
                    onClick =
                        onDelete
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Delete,

                        contentDescription =
                            "Delete Slot",

                        tint =
                            SlotRed
                    )
                }
            }
        }
    }
}


// =============================================================
// TIME FORMAT
// =============================================================

private fun formatSlotTime(
    time: String
): String {

    return time
        .trim()
        .uppercase()
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

        "%.2f".format(
            price
        )
    }
}