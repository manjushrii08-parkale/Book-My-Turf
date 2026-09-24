package com.example.bookmyturf.screens.admin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Storefront

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
        AdminViewModelFactory(
            repository
        )
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

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()


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


    // =========================================================
    // VALIDATION
    // =========================================================

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }


    // =========================================================
    // SELECTED SPORTS
    // =========================================================

    val selectedSports =
        remember {
            mutableStateListOf<String>()
        }


    // =========================================================
    // SELECTED AMENITIES
    // =========================================================

    val selectedAmenities =
        remember {
            mutableStateListOf<String>()
        }


    // =========================================================
    // SELECTED IMAGES
    // =========================================================

    val selectedImages =
        remember {
            mutableStateListOf<Uri>()
        }


    // =========================================================
    // IMAGE PICKER
    // =========================================================

    val imagePicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .PickMultipleVisualMedia(
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

            selectedImages.addAll(
                newImages
            )

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

        containerColor =
            Background,

        topBar = {

            PremiumAddTurfTopBar(
                onBack = onBack,
                enabled = !isLoading
            )
        },

        snackbarHost = {

            SnackbarHost(
                hostState =
                    snackbarHostState
            ) { snackbarData ->

                Snackbar(

                    snackbarData =
                        snackbarData,

                    containerColor =
                        SurfaceElevated,

                    contentColor =
                        PrimaryText,

                    shape =
                        RoundedCornerShape(14.dp)
                )
            }
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
                        start = 18.dp,
                        end = 18.dp,
                        top = 22.dp,
                        bottom = 24.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(22.dp)
        ) {


            // =================================================
            // INTRO
            // =================================================

            AddTurfIntro()


            // =================================================
            // STEP 01
            // =================================================

            PremiumFormSection(
                number = "01",
                title = "Turf Details",
                subtitle =
                    "Give your venue a clear and memorable identity."
            ) {

                PremiumTextField(
                    value = name,
                    onValueChange = {

                        name = it
                        validationMessage = null
                    },
                    label = "Turf Name",
                    placeholder =
                        "e.g. Green Arena Turf",
                    enabled = !isLoading
                )


                PremiumTextField(
                    value = description,
                    onValueChange = {

                        description = it
                    },
                    label = "Description",
                    placeholder =
                        "Describe your turf, facilities and playing experience...",
                    enabled = !isLoading,
                    minLines = 4,
                    maxLines = 6
                )
            }


            // =================================================
            // STEP 02
            // =================================================

            PremiumFormSection(
                number = "02",
                title = "Location",
                subtitle =
                    "Make it easy for customers to find your venue."
            ) {

                PremiumTextField(
                    value = location,
                    onValueChange = {

                        location = it
                        validationMessage = null
                    },
                    label = "Location",
                    placeholder =
                        "e.g. Hinjewadi Phase 1",
                    enabled = !isLoading,
                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.LocationOn,
                            contentDescription =
                                null
                        )
                    }
                )


                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Box(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        PremiumTextField(
                            value = city,
                            onValueChange = {

                                city = it
                                validationMessage = null
                            },
                            label = "City",
                            placeholder =
                                "e.g. Pune",
                            enabled = !isLoading
                        )
                    }
                }


                PremiumTextField(
                    value = address,
                    onValueChange = {

                        address = it
                    },
                    label = "Full Address",
                    placeholder =
                        "Enter the complete venue address",
                    enabled = !isLoading,
                    minLines = 2,
                    maxLines = 4
                )
            }


            // =================================================
            // STEP 03
            // =================================================

            PremiumFormSection(
                number = "03",
                title = "Sports",
                subtitle =
                    "Choose every sport customers can book."
            ) {

                SelectionHeader(
                    selectedCount =
                        selectedSports.size,
                    totalCount =
                        Sports.size,
                    selectedLabel =
                        "sports selected"
                )


                FlowRow(

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Sports.forEach { sport ->

                        val selected =
                            selectedSports.contains(
                                sport
                            )

                        PremiumFilterChip(
                            text = sport,
                            selected = selected,
                            icon =
                                if (selected) {
                                    Icons.Default.SportsSoccer
                                } else {
                                    null
                                },
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
                            }
                        )
                    }
                }
            }


            // =================================================
            // STEP 04
            // =================================================

            PremiumFormSection(
                number = "04",
                title = "Amenities",
                subtitle =
                    "Highlight the facilities available at your venue."
            ) {

                SelectionHeader(
                    selectedCount =
                        selectedAmenities.size,
                    totalCount =
                        Amenities.size,
                    selectedLabel =
                        "amenities selected"
                )


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

                        PremiumFilterChip(
                            text = amenity,
                            selected = selected,
                            icon =
                                if (selected) {
                                    Icons.Default.CheckCircle
                                } else {
                                    null
                                },
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
                            }
                        )
                    }
                }
            }


            // =================================================
            // STEP 05
            // =================================================

            PremiumFormSection(
                number = "05",
                title = "Turf Photos",
                subtitle =
                    "Add high-quality photos to showcase your venue."
            ) {

                PremiumPhotoUploader(
                    selectedImages =
                        selectedImages,

                    isLoading =
                        isLoading,

                    onChoosePhotos = {

                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia
                                    .ImageOnly
                            )
                        )
                    },

                    onRemovePhoto = { uri ->

                        selectedImages.remove(
                            uri
                        )
                    }
                )
            }


            // =================================================
            // STEP 06
            // =================================================

            PremiumFormSection(
                number = "06",
                title = "Pricing",
                subtitle =
                    "Set the standard amount customers pay per slot."
            ) {

                PremiumTextField(
                    value = price,
                    onValueChange = {

                        price = it
                        validationMessage = null
                    },
                    label = "Price per Slot",
                    placeholder = "e.g. 650",
                    enabled = !isLoading,
                    keyboardType =
                        KeyboardType.Decimal,
                    leadingIcon = {

                        Text(
                            text = "₹",
                            color =
                                LightGreen,
                            fontSize =
                                18.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                )


                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )


                Surface(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(12.dp),
                    color =
                        SurfaceHighlight
                ) {

                    Row(
                        modifier =
                            Modifier.padding(
                                horizontal = 13.dp,
                                vertical = 10.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                "Customers will see this as your standard slot price.",
                            color =
                                MutedText,
                            fontSize =
                                11.sp
                        )
                    }
                }
            }


            // =================================================
            // VALIDATION
            // =================================================

            validationMessage?.let { message ->

                ValidationBanner(
                    message = message
                )
            }


            // =================================================
            // CREATE ACTION
            // =================================================

            CreateTurfAction(

                isLoading =
                    isLoading,

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

                        selectedImages.isEmpty() -> {

                            validationMessage =
                                "Please select at least one turf image."
                        }

                        else -> {

                            validationMessage =
                                null

                            scope.launch {

                                // =================================
                                // IMAGE PROCESSING
                                // =================================

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
                                // CREATE TURF
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
                                            selectedSports.toList(),

                                        amenities =
                                            selectedAmenities.toList(),

                                        imageUrls =
                                            emptyList(),

                                        price =
                                            turfPrice,

                                        status =
                                            "ACTIVE"
                                    )


                                viewModel.createTurf(

                                    token =
                                        token,

                                    request =
                                        request,

                                    onSuccess =
                                        { turfResponse ->

                                            // =========================
                                            // TURF ID
                                            // =========================

                                            val turfId =
                                                turfResponse
                                                    .data
                                                    ?.turf
                                                    ?.id

                                            if (
                                                turfId == null
                                            ) {

                                                scope.launch {

                                                    snackbarHostState
                                                        .showSnackbar(
                                                            "Turf created, but Turf ID was not returned."
                                                        )
                                                }

                                            } else {

                                                // =========================
                                                // UPLOAD IMAGES
                                                // =========================

                                                viewModel.uploadTurfImages(

                                                    token =
                                                        token,

                                                    turfId =
                                                        turfId,

                                                    images =
                                                        multipartParts,

                                                    onSuccess = {

                                                        scope.launch {

                                                            snackbarHostState
                                                                .showSnackbar(
                                                                    "Turf created successfully."
                                                                )

                                                            onSuccess()
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            )


            // =================================================
            // FOOTER
            // =================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 2.dp,
                            bottom = 12.dp
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
                        PrimaryGreen,
                    modifier =
                        Modifier.size(15.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        "Your turf will be available after successful creation.",
                    color =
                        MutedText,
                    fontSize =
                        11.sp
                )
            }
        }
    }
}


// =============================================================
// INTRO HEADER
// =============================================================

@Composable
private fun AddTurfIntro() {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                modifier =
                    Modifier.size(42.dp),

                shape =
                    RoundedCornerShape(13.dp),

                color =
                    SurfaceHighlight
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Storefront,

                        contentDescription =
                            null,

                        tint =
                            LightGreen,

                        modifier =
                            Modifier.size(21.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )


            Column {

                Text(
                    text =
                        "NEW VENUE",

                    color =
                        PrimaryGreen,

                    fontSize =
                        10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        1.4.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                Text(
                    text =
                        "Create a new turf",

                    color =
                        PrimaryText,

                    fontSize =
                        24.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        (-0.5).sp
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(
            text =
                "Set up your venue details, facilities, photos and pricing to make it ready for bookings.",

            color =
                SecondaryText,

            fontSize =
                13.sp,

            lineHeight =
                20.sp
        )
    }
}


// =============================================================
// PREMIUM FORM SECTION
// =============================================================

@Composable
private fun PremiumFormSection(
    number: String,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment =
                Alignment.Top
        ) {

            Surface(
                modifier =
                    Modifier.size(34.dp),

                shape =
                    CircleShape,

                color =
                    SurfaceHighlight,

                border =
                    BorderStroke(
                        1.dp,
                        Border
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            number,

                        color =
                            LightGreen,

                        fontSize =
                            11.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
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

                    color =
                        PrimaryText,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        subtitle,

                    color =
                        MutedText,

                    fontSize =
                        11.sp,

                    lineHeight =
                        16.sp
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(13.dp)
        )


        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    Border,
                                    PrimaryGreen.copy(
                                        alpha = 0.12f
                                    ),
                                    Border
                                )
                        )
                    )
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        content()
    }
}


// =============================================================
// SELECTION HEADER
// =============================================================

@Composable
private fun SelectionHeader(
    selectedCount: Int,
    totalCount: Int,
    selectedLabel: String
) {

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
                if (selectedCount == 0) {
                    "Choose from available options"
                } else {
                    "$selectedCount $selectedLabel"
                },

            color =
                if (selectedCount > 0) {
                    PrimaryGreen
                } else {
                    MutedText
                },

            fontSize =
                11.sp,

            fontWeight =
                FontWeight.Medium
        )


        Text(
            text =
                "$totalCount available",

            color =
                MutedText,

            fontSize =
                10.sp
        )
    }


    Spacer(
        modifier =
            Modifier.height(7.dp)
    )
}


// =============================================================
// PREMIUM FILTER CHIP
// =============================================================

@Composable
private fun PremiumFilterChip(
    text: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    onClick: () -> Unit
) {

    FilterChip(

        selected =
            selected,

        onClick =
            onClick,

        label = {

            Text(
                text =
                    text,

                fontSize =
                    11.sp,

                fontWeight =
                    if (selected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
            )
        },

        leadingIcon = {

            if (icon != null) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(15.dp)
                )
            }
        },

        shape =
            RoundedCornerShape(11.dp),

        border =
            FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selected,
                borderColor =
                    Border,
                selectedBorderColor =
                    PrimaryGreen.copy(
                        alpha = 0.50f
                    ),
                borderWidth = 1.dp,
                selectedBorderWidth = 1.dp
            ),

        colors =
            FilterChipDefaults.filterChipColors(

                containerColor =
                    SurfaceElevated,

                labelColor =
                    SecondaryText,

                iconColor =
                    SecondaryText,

                selectedContainerColor =
                    PrimaryGreen.copy(
                        alpha = 0.15f
                    ),

                selectedLabelColor =
                    LightGreen,

                selectedLeadingIconColor =
                    LightGreen
            )
    )
}


// =============================================================
// PREMIUM TEXT FIELD
// =============================================================

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    enabled: Boolean,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardType: KeyboardType =
        KeyboardType.Text,
    leadingIcon:
    (@Composable (() -> Unit))? =
        null
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            Modifier.fillMaxWidth(),

        enabled =
            enabled,

        label = {

            Text(
                text =
                    label
            )
        },

        placeholder = {

            Text(
                text =
                    placeholder
            )
        },

        leadingIcon =
            leadingIcon,

        minLines =
            minLines,

        maxLines =
            maxLines,

        singleLine =
            minLines == 1 &&
                    maxLines == 1,

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            OutlinedTextFieldDefaults.colors(

                focusedTextColor =
                    PrimaryText,

                unfocusedTextColor =
                    PrimaryText,

                disabledTextColor =
                    MutedText,

                focusedContainerColor =
                    SurfaceElevated,

                unfocusedContainerColor =
                    SurfaceElevated,

                disabledContainerColor =
                    SurfaceHighlight,

                focusedBorderColor =
                    PrimaryGreen,

                unfocusedBorderColor =
                    Border,

                disabledBorderColor =
                    Border,

                focusedLabelColor =
                    LightGreen,

                unfocusedLabelColor =
                    SecondaryText,

                disabledLabelColor =
                    MutedText,

                focusedLeadingIconColor =
                    PrimaryGreen,

                unfocusedLeadingIconColor =
                    SecondaryText,

                disabledLeadingIconColor =
                    MutedText,

                focusedPlaceholderColor =
                    MutedText,

                unfocusedPlaceholderColor =
                    MutedText
            )
    )
}


// =============================================================
// PHOTO UPLOADER
// =============================================================

@Composable
private fun PremiumPhotoUploader(
    selectedImages: List<Uri>,
    isLoading: Boolean,
    onChoosePhotos: () -> Unit,
    onRemovePhoto: (Uri) -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Surface(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(16.dp),

            color =
                SurfaceDark,

            border =
                BorderStroke(
                    1.dp,
                    Border
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Surface(
                        modifier =
                            Modifier.size(42.dp),

                        shape =
                            RoundedCornerShape(12.dp),

                        color =
                            SurfaceHighlight
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.AddPhotoAlternate,

                                contentDescription =
                                    null,

                                tint =
                                    LightGreen,

                                modifier =
                                    Modifier.size(21.dp)
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
                                "Venue gallery",

                            color =
                                PrimaryText,

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )

                        Text(
                            text =
                                "${selectedImages.size}/$MAX_IMAGES photos",

                            color =
                                if (
                                    selectedImages.isNotEmpty()
                                ) {
                                    PrimaryGreen
                                } else {
                                    MutedText
                                },

                            fontSize =
                                11.sp
                        )
                    }


                    OutlinedButton(

                        onClick =
                            onChoosePhotos,

                        enabled =
                            !isLoading &&
                                    selectedImages.size <
                                    MAX_IMAGES,

                        shape =
                            RoundedCornerShape(11.dp),

                        border =
                            BorderStroke(
                                1.dp,
                                PrimaryGreen.copy(
                                    alpha = 0.45f
                                )
                            )
                    ) {

                        Text(
                            text =
                                if (
                                    selectedImages.isEmpty()
                                ) {
                                    "Add"
                                } else {
                                    "Add More"
                                },

                            color =
                                LightGreen,

                            fontSize =
                                11.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )


                if (selectedImages.isEmpty()) {

                    EmptyPhotoState(
                        onChoosePhotos =
                            onChoosePhotos,

                        enabled =
                            !isLoading
                    )

                } else {

                    FlowRow(

                        horizontalArrangement =
                            Arrangement.spacedBy(9.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(9.dp)
                    ) {

                        selectedImages.forEachIndexed { index, uri ->

                            TurfImagePreview(

                                uri =
                                    uri,

                                isPrimary =
                                    index == 0,

                                onRemove = {

                                    onRemovePhoto(
                                        uri
                                    )
                                }
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    Text(
                        text =
                            "The first photo will be used as the primary gallery image.",

                        color =
                            MutedText,

                        fontSize =
                            10.sp
                    )
                }
            }
        }
    }
}


// =============================================================
// EMPTY PHOTO STATE
// =============================================================

@Composable
private fun EmptyPhotoState(
    onChoosePhotos: () -> Unit,
    enabled: Boolean
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        color =
            SurfaceElevated
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(22.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "No venue photos yet",

                color =
                    PrimaryText,

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold
            )


            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )


            Text(
                text =
                    "Add clear photos of the playing area and facilities.",

                color =
                    MutedText,

                fontSize =
                    11.sp
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            OutlinedButton(

                onClick =
                    onChoosePhotos,

                enabled =
                    enabled,

                shape =
                    RoundedCornerShape(11.dp),

                border =
                    BorderStroke(
                        1.dp,
                        Border
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.AddPhotoAlternate,

                    contentDescription =
                        null,

                    tint =
                        LightGreen,

                    modifier =
                        Modifier.size(17.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        "Choose Photos",

                    color =
                        LightGreen,

                    fontSize =
                        11.sp
                )
            }
        }
    }
}


// =============================================================
// IMAGE PREVIEW
// =============================================================

@Composable
private fun TurfImagePreview(
    uri: Uri,
    isPrimary: Boolean,
    onRemove: () -> Unit
) {

    val context =
        LocalContext.current

    val bitmap =
        remember(uri) {

            context.contentResolver
                .openInputStream(uri)
                ?.use { stream ->

                    BitmapFactory.decodeStream(
                        stream
                    )
                }
        }


    if (bitmap != null) {

        Box(
            modifier =
                Modifier.size(104.dp)
        ) {

            Image(

                bitmap =
                    bitmap.asImageBitmap(),

                contentDescription =
                    "Turf photo",

                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(13.dp)
                        ),

                contentScale =
                    ContentScale.Crop
            )


            if (isPrimary) {

                Surface(

                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomStart
                            )
                            .padding(6.dp),

                    shape =
                        RoundedCornerShape(7.dp),

                    color =
                        Background.copy(
                            alpha = 0.88f
                        )
                ) {

                    Text(
                        text =
                            "PRIMARY",

                        color =
                            LightGreen,

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            0.6.sp,

                        modifier =
                            Modifier.padding(
                                horizontal = 6.dp,
                                vertical = 3.dp
                            )
                    )
                }
            }


            IconButton(

                onClick =
                    onRemove,

                modifier =
                    Modifier
                        .size(28.dp)
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(3.dp)
                        .background(
                            color =
                                Color.Black.copy(
                                    alpha = 0.72f
                                ),
                            shape =
                                CircleShape
                        )
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Close,

                    contentDescription =
                        "Remove photo",

                    tint =
                        PrimaryText,

                    modifier =
                        Modifier.size(15.dp)
                )
            }
        }
    }
}


// =============================================================
// VALIDATION BANNER
// =============================================================

@Composable
private fun ValidationBanner(
    message: String
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(13.dp),

        color =
            ErrorRed.copy(
                alpha = 0.07f
            ),

        border =
            BorderStroke(
                1.dp,
                ErrorRed.copy(
                    alpha = 0.25f
                )
            )
    ) {

        Row(

            modifier =
                Modifier.padding(13.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(

                modifier =
                    Modifier.size(28.dp),

                shape =
                    CircleShape,

                color =
                    ErrorRed.copy(
                        alpha = 0.12f
                    )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Close,

                        contentDescription =
                            null,

                        tint =
                            ErrorRed,

                        modifier =
                            Modifier.size(15.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )


            Text(
                text =
                    message,

                color =
                    ErrorRed,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}


// =============================================================
// CREATE TURF ACTION
// =============================================================

@Composable
private fun CreateTurfAction(
    isLoading: Boolean,
    onClick: () -> Unit
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Column(

            modifier =
                Modifier.padding(15.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Ready to publish?",

                        color =
                            PrimaryText,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Create the venue and upload its gallery.",

                        color =
                            MutedText,

                        fontSize =
                            10.sp
                    )
                }


                Surface(

                    modifier =
                        Modifier.size(34.dp),

                    shape =
                        CircleShape,

                    color =
                        SurfaceHighlight
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Check,

                            contentDescription =
                                null,

                            tint =
                                PrimaryGreen,

                            modifier =
                                Modifier.size(17.dp)
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )


            Button(

                onClick =
                    onClick,

                enabled =
                    !isLoading,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            PrimaryGreen,

                        contentColor =
                            Background,

                        disabledContainerColor =
                            PrimaryGreen.copy(
                                alpha = 0.35f
                            ),

                        disabledContentColor =
                            Background.copy(
                                alpha = 0.55f
                            )
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(21.dp),

                        color =
                            Background,

                        strokeWidth =
                            2.5.dp
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Text(
                        text =
                            "Creating & Uploading...",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            13.sp
                    )

                } else {

                    Icon(
                        imageVector =
                            Icons.Default.Save,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            "Create Turf",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            14.sp
                    )
                }
            }
        }
    }
}


// =============================================================
// TOP BAR
// =============================================================

@Composable
private fun PremiumAddTurfTopBar(
    onBack: () -> Unit,
    enabled: Boolean
) {

    Surface(
        color =
            Background,

        shadowElevation =
            0.dp
    ) {

        Box(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    Color(0xFF020907),
                                    Color(0xFF071810),
                                    Color(0xFF020907)
                                )
                        )
                    )
        ) {

            Column {

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 14.dp,
                                end = 14.dp,
                                top = 36.dp,
                                bottom = 14.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Surface(

                        modifier =
                            Modifier.size(42.dp),

                        shape =
                            RoundedCornerShape(14.dp),

                        color =
                            SurfaceElevated
                    ) {

                        IconButton(

                            onClick =
                                onBack,

                            enabled =
                                enabled
                        ) {

                            Icon(

                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,

                                contentDescription =
                                    "Back",

                                tint =
                                    PrimaryText,

                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
                    )


                    Column {

                        Text(
                            text =
                                "BookMyTurf",

                            color =
                                PrimaryText,

                            fontSize =
                                20.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                (-0.3).sp
                        )


                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )


                        Text(
                            text =
                                "Add Turf",

                            color =
                                MutedText,

                            fontSize =
                                11.sp
                        )
                    }
                }


                Box(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color.Transparent,
                                            Border,
                                            PrimaryGreen.copy(
                                                alpha = 0.18f
                                            ),
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
// URI → MULTIPART
// =============================================================

private suspend fun uriToMultipart(
    context: Context,
    uri: Uri
): MultipartBody.Part? {

    return withContext(
        Dispatchers.IO
    ) {

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

            var quality =
                85

            resizedBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                outputStream
            )


            while (
                outputStream.size() >
                MAX_IMAGE_SIZE &&
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

        } catch (_: Exception) {

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

    var fileName: String? =
        null

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
                        cursor.getString(
                            index
                        )
                }
            }
        }

    return fileName
}