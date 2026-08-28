package com.example.bookmyturf.screens.admin

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Storefront

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository
import com.example.bookmyturf.viewmodel.AdminViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

import java.io.ByteArrayOutputStream
import java.util.Calendar

// =============================================================
// COLORS
// =============================================================

private val TurfGreen = Color(0xFF14532D)
private val TurfGreenDark = Color(0xFF0F3D21)
private val TurfGreenLight = Color(0xFFF0FDF4)
private val TurfBackground = Color(0xFFF8FAFC)
private val TurfGray = Color(0xFF64748B)
private val TurfRed = Color(0xFFDC2626)

// =============================================================
// CONSTANTS
// =============================================================

private const val MAX_IMAGES = 8
private const val MAX_IMAGE_SIZE = 2 * 1024 * 1024

// =============================================================
// SPORTS
// =============================================================

private val Sports = listOf(
    "Football",
    "Cricket",
    "Box Cricket",
    "Badminton",
    "Basketball",
    "Tennis",
    "Volleyball",
    "Futsal"
)

// =============================================================
// AMENITIES
// =============================================================

private val Amenities = listOf(
    "Parking",
    "Changing Room",
    "Washroom",
    "Drinking Water",
    "Flood Lights",
    "Seating Area",
    "CCTV",
    "First Aid",
    "Wi-Fi",
    "Equipment Rental"
)

// =============================================================
// ADD TURF SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTurfScreen(
    token: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {

    val context = LocalContext.current

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository = remember {
        AdminRepository(
            RetrofitClient.api
        )
    }

    // =========================================================
    // VIEWMODEL FACTORY
    // =========================================================

    val factory = remember {
        AdminViewModelFactory(repository)
    }

    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: AdminViewModel = viewModel(
        factory = factory
    )

    // =========================================================
    // STATE
    // =========================================================

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // =========================================================
    // FORM STATE
    // =========================================================

    var name by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var city by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var openingTime by remember {
        mutableStateOf("")
    }

    var closingTime by remember {
        mutableStateOf("")
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    // =========================================================
    // SPORTS
    // =========================================================

    val selectedSports = remember {
        mutableStateListOf<String>()
    }

    // =========================================================
    // AMENITIES
    // =========================================================

    val selectedAmenities = remember {
        mutableStateListOf<String>()
    }

    // =========================================================
    // IMAGES
    // =========================================================

    val selectedImages = remember {
        mutableStateListOf<Uri>()
    }

    // =========================================================
    // IMAGE PICKER
    // =========================================================

    val imagePicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickMultipleVisualMedia(
                    maxItems = MAX_IMAGES
                )
        ) { uris ->

            if (uris.isEmpty()) {
                return@rememberLauncherForActivityResult
            }

            val remaining =
                MAX_IMAGES - selectedImages.size

            val newImages =
                uris
                    .filter { uri ->
                        !selectedImages.contains(uri)
                    }
                    .take(remaining)

            selectedImages.addAll(newImages)

            if (uris.size > remaining) {

                scope.launch {

                    snackbarHostState.showSnackbar(
                        "Maximum $MAX_IMAGES images allowed."
                    )
                }
            }
        }

    // =========================================================
    // ERROR SNACKBAR
    // =========================================================

    LaunchedEffect(error) {

        if (!error.isNullOrBlank()) {

            snackbarHostState.showSnackbar(
                error ?: "Something went wrong."
            )
        }
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        containerColor = TurfBackground,

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Add Turf",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Create your turf listing",
                            fontSize = 12.sp,
                            color = TurfGray
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
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(18.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            TurfGreen
                    )
            ) {

                Row(

                    modifier =
                        Modifier.padding(18.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Storefront,

                        contentDescription =
                            null,

                        tint =
                            Color.White,

                        modifier =
                            Modifier
                                .size(52.dp)
                                .background(
                                    color =
                                        Color.White.copy(
                                            alpha = 0.15f
                                        ),
                                    shape =
                                        RoundedCornerShape(16.dp)
                                )
                                .padding(13.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
                    )

                    Column {

                        Text(

                            text =
                                "Create Your Turf",

                            color =
                                Color.White,

                            fontSize =
                                19.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(

                            text =
                                "Add complete turf details to attract more bookings.",

                            color =
                                Color.White.copy(
                                    alpha = 0.85f
                                ),

                            fontSize =
                                12.sp
                        )
                    }
                }
            }

            // =================================================
            // BASIC INFORMATION
            // =================================================

            FormSectionCard(
                title = "Basic Information",
                subtitle = "Enter the main details of your turf."
            ) {

                OutlinedTextField(

                    value = name,

                    onValueChange = {
                        name = it
                        validationMessage = null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Turf Name *")
                    },

                    placeholder = {
                        Text("e.g. Green Arena Turf")
                    },

                    singleLine = true
                )

                OutlinedTextField(

                    value = description,

                    onValueChange = {
                        description = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Description")
                    },

                    placeholder = {
                        Text("Tell users about your turf...")
                    },

                    minLines = 3,

                    maxLines = 5
                )
            }

            // =================================================
            // LOCATION
            // =================================================

            FormSectionCard(
                title = "Location",
                subtitle = "Help customers find your turf easily."
            ) {

                OutlinedTextField(

                    value = location,

                    onValueChange = {
                        location = it
                        validationMessage = null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Location *")
                    },

                    placeholder = {
                        Text("e.g. Hinjewadi Phase 1")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.LocationOn,
                            contentDescription =
                                null
                        )
                    },

                    singleLine = true
                )

                OutlinedTextField(

                    value = city,

                    onValueChange = {
                        city = it
                        validationMessage = null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("City *")
                    },

                    placeholder = {
                        Text("e.g. Pune")
                    },

                    singleLine = true
                )

                OutlinedTextField(

                    value = address,

                    onValueChange = {
                        address = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Full Address")
                    },

                    placeholder = {
                        Text("Enter complete address")
                    },

                    minLines = 2,

                    maxLines = 4
                )
            }

            // =================================================
            // SPORTS
            // =================================================

            FormSectionCard(
                title = "Sports",
                subtitle = "Select all sports available at your turf."
            ) {

                FlowRow(

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Sports.forEach { sport ->

                        val selected =
                            selectedSports.contains(sport)

                        FilterChip(

                            selected = selected,

                            onClick = {

                                if (selected) {

                                    selectedSports.remove(
                                        sport
                                    )

                                } else {

                                    selectedSports.add(
                                        sport
                                    )
                                }

                                validationMessage = null
                            },

                            label = {
                                Text(sport)
                            },

                            leadingIcon = {

                                if (selected) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.SportsSoccer,

                                        contentDescription =
                                            null,

                                        modifier =
                                            Modifier.size(17.dp)
                                    )
                                }
                            }
                        )
                    }
                }

                if (selectedSports.isNotEmpty()) {

                    Text(

                        text =
                            "${selectedSports.size} sports selected",

                        color =
                            TurfGreen,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }

            // =================================================
            // AMENITIES
            // =================================================

            FormSectionCard(
                title = "Amenities",
                subtitle = "Select the facilities available."
            ) {

                FlowRow(

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Amenities.forEach { amenity ->

                        val selected =
                            selectedAmenities.contains(
                                amenity
                            )

                        FilterChip(

                            selected = selected,

                            onClick = {

                                if (selected) {

                                    selectedAmenities.remove(
                                        amenity
                                    )

                                } else {

                                    selectedAmenities.add(
                                        amenity
                                    )
                                }
                            },

                            label = {
                                Text(amenity)
                            }
                        )
                    }
                }
            }

            // =================================================
            // IMAGES
            // =================================================

            FormSectionCard(
                title = "Turf Photos",
                subtitle = "Add up to 8 high-quality turf photos."
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                TurfGreenLight
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(16.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.AddPhotoAlternate,

                            contentDescription =
                                null,

                            tint =
                                TurfGreen,

                            modifier =
                                Modifier.size(42.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(

                            text =
                                if (selectedImages.isEmpty())
                                    "No photos selected"
                                else
                                    "${selectedImages.size}/$MAX_IMAGES photos selected",

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                TurfGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(

                            text =
                                "Choose clear photos of your turf, ground and facilities.",

                            color =
                                TurfGray,

                            fontSize =
                                12.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        OutlinedButton(

                            onClick = {

                                imagePicker.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts
                                            .PickVisualMedia
                                            .ImageOnly
                                    )
                                )
                            },

                            enabled =
                                !isLoading &&
                                        selectedImages.size <
                                        MAX_IMAGES
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.AddPhotoAlternate,
                                contentDescription =
                                    null
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(7.dp)
                            )

                            Text(
                                text =
                                    "Choose Photos"
                            )
                        }
                    }
                }

                // =================================================
                // IMAGE PREVIEW
                // =================================================

                if (selectedImages.isNotEmpty()) {

                    Text(

                        text =
                            "Selected Photos",

                        fontWeight =
                            FontWeight.SemiBold,

                        fontSize =
                            14.sp
                    )

                    FlowRow(

                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        selectedImages.forEach { uri ->

                            TurfImagePreview(
                                uri = uri,
                                onRemove = {
                                    selectedImages.remove(
                                        uri
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // =================================================
            // PRICING & TIMING
            // =================================================

            FormSectionCard(
                title = "Pricing & Timing",
                subtitle = "Set your standard slot pricing and hours."
            ) {

                OutlinedTextField(

                    value = price,

                    onValueChange = {

                        price = it
                        validationMessage = null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Price per Slot *")
                    },

                    placeholder = {
                        Text("e.g. 1200")
                    },

                    leadingIcon = {

                        Text(
                            text = "₹",
                            fontWeight =
                                FontWeight.Bold
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )

                // =================================================
                // TIME ROW
                // =================================================

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    // =============================================
                    // OPENING TIME
                    // =============================================

                    OutlinedTextField(

                        value = openingTime,

                        onValueChange = {
                            // Read-only field.
                            // Time is selected from TimePicker.
                        },

                        modifier =
                            Modifier.weight(1f),

                        label = {
                            Text("Opening *")
                        },

                        placeholder = {
                            Text("09:00")
                        },

                        readOnly = true,

                        singleLine = true,

                        trailingIcon = {

                            IconButton(

                                onClick = {

                                    showTimePicker(
                                        context = context,
                                        initialTime = openingTime
                                    ) { selectedTime ->

                                        openingTime =
                                            selectedTime

                                        validationMessage =
                                            null
                                    }
                                },

                                enabled =
                                    !isLoading
                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.AccessTime,

                                    contentDescription =
                                        "Select opening time",

                                    tint =
                                        TurfGreen
                                )
                            }
                        },

                        isError =
                            openingTime.isBlank()
                    )

                    // =============================================
                    // CLOSING TIME
                    // =============================================

                    OutlinedTextField(

                        value = closingTime,

                        onValueChange = {
                            // Read-only field.
                            // Time is selected from TimePicker.
                        },

                        modifier =
                            Modifier.weight(1f),

                        label = {
                            Text("Closing *")
                        },

                        placeholder = {
                            Text("22:00")
                        },

                        readOnly = true,

                        singleLine = true,

                        trailingIcon = {

                            IconButton(

                                onClick = {

                                    showTimePicker(
                                        context = context,
                                        initialTime = closingTime
                                    ) { selectedTime ->

                                        closingTime =
                                            selectedTime

                                        validationMessage =
                                            null
                                    }
                                },

                                enabled =
                                    !isLoading
                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.AccessTime,

                                    contentDescription =
                                        "Select closing time",

                                    tint =
                                        TurfGreen
                                )
                            }
                        },

                        isError =
                            closingTime.isBlank()
                    )
                }

                if (
                    openingTime.isNotBlank() &&
                    closingTime.isNotBlank()
                ) {

                    Text(

                        text =
                            "Operating hours: $openingTime - $closingTime",

                        color =
                            TurfGreen,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }

            // =================================================
            // VALIDATION MESSAGE
            // =================================================

            validationMessage?.let { message ->

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFFEF2F2)
                        ),

                    shape =
                        RoundedCornerShape(12.dp)
                ) {

                    Text(

                        text = message,

                        color =
                            TurfRed,

                        modifier =
                            Modifier.padding(14.dp),

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }

            // =================================================
            // CREATE TURF
            // =================================================

            Button(

                onClick = {

                    val turfPrice =
                        price.toDoubleOrNull()

                    when {

                        name.isBlank() -> {

                            validationMessage =
                                "Please enter the turf name."
                        }

                        location.isBlank() -> {

                            validationMessage =
                                "Please enter the turf location."
                        }

                        city.isBlank() -> {

                            validationMessage =
                                "Please enter the city."
                        }

                        selectedSports.isEmpty() -> {

                            validationMessage =
                                "Please select at least one sport."
                        }

                        turfPrice == null ||
                                turfPrice <= 0 -> {

                            validationMessage =
                                "Please enter a valid slot price."
                        }

                        openingTime.isBlank() -> {

                            validationMessage =
                                "Please select opening time."
                        }

                        !isValidTime(openingTime) -> {

                            validationMessage =
                                "Invalid opening time."
                        }

                        closingTime.isBlank() -> {

                            validationMessage =
                                "Please select closing time."
                        }

                        !isValidTime(closingTime) -> {

                            validationMessage =
                                "Invalid closing time."
                        }

                        selectedImages.isEmpty() -> {

                            validationMessage =
                                "Please select at least one turf image."
                        }

                        else -> {

                            validationMessage = null

                            // =================================
                            // START UPLOAD
                            // =================================

                            scope.launch {

                                val multipartParts =
                                    withContext(
                                        Dispatchers.IO
                                    ) {

                                        selectedImages.mapNotNull { uri ->

                                            uriToMultipart(
                                                context =
                                                    context,

                                                uri =
                                                    uri
                                            )
                                        }
                                    }

                                // =================================
                                // IMAGE PROCESSING FAILED
                                // =================================

                                if (
                                    multipartParts.isEmpty()
                                ) {

                                    validationMessage =
                                        "Unable to process selected images."

                                    return@launch
                                }

                                // =================================
                                // UPLOAD IMAGES
                                // =================================

                                viewModel.uploadTurfImages(

                                    token =
                                        token,

                                    images =
                                        multipartParts,

                                    onSuccess = { uploadedUrls ->

                                        // =================================
                                        // CREATE TURF REQUEST
                                        // =================================

                                        val request =
                                            CreateTurfRequest(

                                                name =
                                                    name.trim(),

                                                description =
                                                    description
                                                        .trim()
                                                        .ifBlank {
                                                            null
                                                        },

                                                location =
                                                    location.trim(),

                                                city =
                                                    city.trim(),

                                                address =
                                                    address
                                                        .trim()
                                                        .ifBlank {
                                                            null
                                                        },

                                                latitude =
                                                    null,

                                                longitude =
                                                    null,

                                                sportsTypes =
                                                    selectedSports
                                                        .toList(),

                                                amenities =
                                                    selectedAmenities
                                                        .toList(),

                                                imageUrls =
                                                    uploadedUrls,

                                                price =
                                                    turfPrice,

                                                // Laravel requires H:i
                                                openingTime =
                                                    openingTime
                                                        .trim(),

                                                // Laravel requires H:i
                                                closingTime =
                                                    closingTime
                                                        .trim(),

                                                status =
                                                    "ACTIVE"
                                            )

                                        // =================================
                                        // CREATE TURF API
                                        // =================================

                                        viewModel.createTurf(

                                            token =
                                                token,

                                            request =
                                                request,

                                            onSuccess =
                                                onSuccess
                                        )
                                    }
                                )
                            }
                        }
                    }
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                enabled =
                    !isLoading,

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            TurfGreen,

                        disabledContainerColor =
                            TurfGreen.copy(
                                alpha = 0.5f
                            )
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(22.dp),

                        color =
                            Color.White,

                        strokeWidth =
                            2.5.dp
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Text(
                        text =
                            "Uploading & Creating..."
                    )

                } else {

                    Icon(

                        imageVector =
                            Icons.Default.Save,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(

                        text =
                            "Create Turf",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // =================================================
            // BOTTOM INFO
            // =================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 20.dp
                        ),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        Icons.Default.CheckCircle,

                    contentDescription =
                        null,

                    tint =
                        TurfGreen,

                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(

                    text =
                        "Your turf will be available after successful creation.",

                    color =
                        TurfGray,

                    fontSize =
                        11.sp
                )
            }
        }
    }
}

// =============================================================
// FORM SECTION CARD
// =============================================================

@Composable
private fun FormSectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(

                text =
                    title,

                color =
                    TurfGreenDark,

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(

                text =
                    subtitle,

                color =
                    TurfGray,

                fontSize =
                    12.sp
            )

            content()
        }
    }
}

// =============================================================
// IMAGE PREVIEW
// =============================================================

@Composable
private fun TurfImagePreview(
    uri: Uri,
    onRemove: () -> Unit
) {

    val context = LocalContext.current

    val bitmap = remember(uri) {

        context.contentResolver
            .openInputStream(uri)
            ?.use { stream ->

                BitmapFactory.decodeStream(
                    stream
                )
            }
    }

    if (bitmap != null) {

        Box {

            Image(

                bitmap =
                    bitmap.asImageBitmap(),

                contentDescription =
                    "Turf photo",

                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(14.dp)
                        ),

                contentScale =
                    ContentScale.Crop
            )

            IconButton(

                onClick =
                    onRemove,

                modifier =
                    Modifier
                        .size(30.dp)
                        .align(
                            Alignment.TopEnd
                        )
                        .background(
                            color =
                                Color.Black.copy(
                                    alpha = 0.65f
                                ),
                            shape =
                                RoundedCornerShape(
                                    50
                                )
                        )
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Close,

                    contentDescription =
                        "Remove photo",

                    tint =
                        Color.White,

                    modifier =
                        Modifier.size(17.dp)
                )
            }
        }
    }
}

// =============================================================
// URI → MULTIPART
// =============================================================

private suspend fun uriToMultipart(
    context: Context,
    uri: Uri
): MultipartBody.Part? {

    return withContext(Dispatchers.IO) {

        try {

            val originalBitmap =
                context.contentResolver
                    .openInputStream(uri)
                    ?.use { input ->

                        BitmapFactory.decodeStream(
                            input
                        )
                    }
                    ?: return@withContext null

            // =============================================
            // RESIZE
            // =============================================

            val resizedBitmap =
                resizeBitmap(
                    bitmap =
                        originalBitmap,

                    maxWidth =
                        1600,

                    maxHeight =
                        1600
                )

            // =============================================
            // COMPRESS
            // =============================================

            val outputStream =
                ByteArrayOutputStream()

            var quality = 85

            resizedBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                outputStream
            )

            while (
                outputStream.size() > MAX_IMAGE_SIZE &&
                quality > 40
            ) {

                outputStream.reset()

                quality -= 10

                resizedBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    quality,
                    outputStream
                )
            }

            val imageBytes =
                outputStream.toByteArray()

            if (imageBytes.isEmpty()) {
                return@withContext null
            }

            // =============================================
            // REQUEST BODY
            // =============================================

            val requestBody =
                imageBytes.toRequestBody(
                    "image/jpeg".toMediaType()
                )

            // =============================================
            // FILE NAME
            // =============================================

            val fileName =
                getFileName(
                    context =
                        context,

                    uri =
                        uri
                )
                    ?.substringBeforeLast(".")
                    ?.ifBlank {
                        "turf_image"
                    }
                    ?: "turf_image"

            // =============================================
            // MULTIPART
            // =============================================

            MultipartBody.Part.createFormData(
                "images[]",
                "${fileName}.jpg",
                requestBody
            )

        } catch (e: Exception) {

            null
        }
    }
}

// =============================================================
// RESIZE BITMAP
// =============================================================

private fun resizeBitmap(
    bitmap: Bitmap,
    maxWidth: Int,
    maxHeight: Int
): Bitmap {

    val width =
        bitmap.width

    val height =
        bitmap.height

    if (
        width <= maxWidth &&
        height <= maxHeight
    ) {

        return bitmap
    }

    val ratio =
        minOf(
            maxWidth.toFloat() / width,
            maxHeight.toFloat() / height
        )

    val newWidth =
        (width * ratio).toInt()

    val newHeight =
        (height * ratio).toInt()

    return Bitmap.createScaledBitmap(
        bitmap,
        newWidth,
        newHeight,
        true
    )
}

// =============================================================
// GET FILE NAME
// =============================================================

private fun getFileName(
    context: Context,
    uri: Uri
): String? {

    var fileName: String? = null

    context.contentResolver
        .query(
            uri,
            arrayOf(
                OpenableColumns.DISPLAY_NAME
            ),
            null,
            null,
            null
        )
        ?.use { cursor ->

            if (cursor.moveToFirst()) {

                val index =
                    cursor.getColumnIndex(
                        OpenableColumns.DISPLAY_NAME
                    )

                if (index >= 0) {

                    fileName =
                        cursor.getString(index)
                }
            }
        }

    return fileName
}

// =============================================================
// VALIDATE TIME
// =============================================================

private fun isValidTime(
    time: String
): Boolean {

    return Regex(
        "^([01]\\d|2[0-3]):[0-5]\\d$"
    ).matches(time)
}

// =============================================================
// TIME PICKER
// =============================================================

private fun showTimePicker(
    context: Context,
    initialTime: String?,
    onTimeSelected: (String) -> Unit
) {

    val calendar =
        Calendar.getInstance()

    var hour =
        calendar.get(
            Calendar.HOUR_OF_DAY
        )

    var minute =
        calendar.get(
            Calendar.MINUTE
        )

    // =========================================================
    // USE PREVIOUSLY SELECTED TIME
    // =========================================================

    if (!initialTime.isNullOrBlank()) {

        val parts =
            initialTime.split(":")

        if (parts.size == 2) {

            hour =
                parts[0]
                    .toIntOrNull()
                    ?: hour

            minute =
                parts[1]
                    .toIntOrNull()
                    ?: minute
        }
    }

    // =========================================================
    // TIME PICKER
    // =========================================================

    TimePickerDialog(

        context,

        { _, selectedHour, selectedMinute ->

            // =============================================
            // IMPORTANT
            // Laravel requires H:i
            // Example: 09:00
            // =============================================

            val formattedTime =
                String.format(
                    "%02d:%02d",
                    selectedHour,
                    selectedMinute
                )

            onTimeSelected(
                formattedTime
            )
        },

        hour,
        minute,

        // 24-hour format
        true

    ).show()
}