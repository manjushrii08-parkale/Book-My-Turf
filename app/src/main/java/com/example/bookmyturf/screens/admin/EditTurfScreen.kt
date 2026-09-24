package com.example.bookmyturf.screens.admin

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

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

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Storefront

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage

import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository
import com.example.bookmyturf.viewmodel.AdminViewModel


// =============================================================
// PREMIUM ADMIN COLORS
// Same design system as:
// Subscription / Dashboard / Turfs / Bookings / Profile
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)
private val WarningOrange = Color(0xFFFFB454)


// =============================================================
// EDIT TURF SCREEN
// =============================================================

@Composable
fun EditTurfScreen(
    token: String,
    turfId: Int,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }


    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository =
        remember {

            AdminRepository(
                RetrofitClient.api
            )
        }


    // =========================================================
    // VIEWMODEL FACTORY
    // =========================================================

    val factory =
        remember {

            AdminViewModelFactory(
                repository
            )
        }


    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: AdminViewModel =
        viewModel(
            factory = factory
        )


    // =========================================================
    // RESPONSE STATE
    // =========================================================

    val turfResponse by
    viewModel.turf.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()

    val successMessage by
    viewModel.successMessage.collectAsState()


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

    var latitude by remember {
        mutableStateOf("")
    }

    var longitude by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var status by remember {
        mutableStateOf("ACTIVE")
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    var validationError by remember {
        mutableStateOf<String?>(null)
    }


    // =========================================================
    // SPORTS
    // =========================================================

    val availableSports =
        listOf(
            "Football",
            "Cricket",
            "Badminton",
            "Tennis",
            "Basketball",
            "Volleyball"
        )

    val selectedSports =
        remember {
            mutableStateListOf<String>()
        }


    // =========================================================
    // AMENITIES
    // =========================================================

    val availableAmenities =
        listOf(
            "Parking",
            "Changing Room",
            "Washroom",
            "Drinking Water",
            "Flood Lights",
            "Cafeteria",
            "Wi-Fi",
            "First Aid"
        )

    val selectedAmenities =
        remember {
            mutableStateListOf<String>()
        }


    // =========================================================
    // EXISTING IMAGES
    // =========================================================

    val existingImages =
        remember {
            mutableStateListOf<String>()
        }


    // =========================================================
    // NEW IMAGES
    // =========================================================

    val newImageUris =
        remember {
            mutableStateListOf<Uri>()
        }


    // =========================================================
    // FORM LOADED
    // =========================================================

    var formLoaded by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // IMAGE PICKER
    // =========================================================

    val imagePicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .PickMultipleVisualMedia()
        ) { uris ->

            newImageUris.clear()

            newImageUris.addAll(
                uris
            )
        }


    // =========================================================
    // LOAD TURF
    // =========================================================

    LaunchedEffect(
        token,
        turfId
    ) {

        viewModel.loadTurf(
            token = token,
            turfId = turfId
        )
    }


    // =========================================================
    // FILL FORM
    // =========================================================

    LaunchedEffect(turfResponse) {

        val turf: Turf =
            turfResponse
                ?.data
                ?.turf
                ?: return@LaunchedEffect

        if (!formLoaded) {

            name =
                turf.name

            description =
                turf.description ?: ""

            location =
                turf.location

            city =
                turf.city

            address =
                turf.address ?: ""

            latitude =
                turf.latitude
                    ?.toString()
                    ?: ""

            longitude =
                turf.longitude
                    ?.toString()
                    ?: ""

            price =
                turf.price.toString()

            status =
                turf.status.uppercase()

            selectedSports.clear()

            selectedSports.addAll(
                turf.sportsTypes
            )

            selectedAmenities.clear()

            selectedAmenities.addAll(
                turf.amenities
            )

            existingImages.clear()

            existingImages.addAll(
                turf.imageUrls
            )

            formLoaded = true
        }
    }


    // =========================================================
    // ERROR / SUCCESS SNACKBAR
    // =========================================================

    LaunchedEffect(error) {

        if (!error.isNullOrBlank()) {

            snackbarHostState.showSnackbar(
                error ?: "Something went wrong."
            )
        }
    }


    LaunchedEffect(successMessage) {

        if (!successMessage.isNullOrBlank()) {

            snackbarHostState.showSnackbar(
                successMessage ?: "Success"
            )
        }
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        containerColor =
            Background,

        snackbarHost = {

            SnackbarHost(
                hostState =
                    snackbarHostState
            )
        },

        topBar = {

            EditTurfTopBar(
                onBackClick = onBack,
                enabled = !isLoading
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
                        horizontal = 20.dp,
                        vertical = 14.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {


            // =================================================
            // HEADER
            // =================================================

            EditTurfHeroCard()


            // =================================================
            // TURF PHOTOS
            // =================================================

            EditSectionCard(
                title = "Turf Photos",
                subtitle =
                    "Manage your current photos or add new ones."
            ) {

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
                                "${existingImages.size + newImageUris.size} image(s)",

                            color =
                                PrimaryText,

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Current + new photos",

                            color =
                                MutedText,

                            fontSize =
                                11.sp
                        )
                    }


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
                            !isLoading,

                        shape =
                            RoundedCornerShape(11.dp),

                        border =
                            BorderStroke(
                                1.dp,
                                Border
                            ),

                        colors =
                            androidx.compose.material3.ButtonDefaults
                                .outlinedButtonColors(
                                    containerColor =
                                        SurfaceElevated,
                                    contentColor =
                                        PrimaryText
                                )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Add,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(5.dp)
                        )

                        Text(
                            text = "Add",
                            fontSize = 12.sp
                        )
                    }
                }


                if (existingImages.isNotEmpty()) {

                    Text(
                        text =
                            "CURRENT IMAGES",

                        color =
                            MutedText,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            9.sp,

                        letterSpacing =
                            0.8.sp
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items =
                                existingImages,

                            key = {
                                it
                            }
                        ) { imageUrl ->

                            TurfImageItem(
                                imageModel =
                                    buildImageUrl(
                                        imageUrl
                                    ),

                                onRemove = {

                                    existingImages.remove(
                                        imageUrl
                                    )
                                }
                            )
                        }
                    }
                }


                if (newImageUris.isNotEmpty()) {

                    Text(
                        text =
                            "NEW IMAGES",

                        color =
                            MutedText,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            9.sp,

                        letterSpacing =
                            0.8.sp
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items =
                                newImageUris,

                            key = {
                                it.toString()
                            }
                        ) { uri ->

                            TurfImageItem(
                                imageModel =
                                    uri,

                                onRemove = {

                                    newImageUris.remove(
                                        uri
                                    )
                                }
                            )
                        }
                    }
                }
            }


            // =================================================
            // BASIC INFORMATION
            // =================================================

            EditSectionCard(
                title =
                    "Basic Information",

                subtitle =
                    "Update the main information of your turf."
            ) {

                PremiumTextField(
                    value =
                        name,

                    onValueChange = {

                        name = it
                        validationError = null
                    },

                    label =
                        "Turf Name",

                    placeholder =
                        "Enter turf name"
                )


                PremiumTextField(
                    value =
                        description,

                    onValueChange = {

                        description = it
                        validationError = null
                    },

                    label =
                        "Description",

                    placeholder =
                        "Describe your turf",

                    minLines =
                        3,

                    maxLines =
                        5
                )
            }


            // =================================================
            // LOCATION
            // =================================================

            EditSectionCard(
                title =
                    "Location",

                subtitle =
                    "Keep your turf location information accurate."
            ) {

                PremiumTextField(
                    value =
                        location,

                    onValueChange = {

                        location = it
                        validationError = null
                    },

                    label =
                        "Location",

                    placeholder =
                        "Area / Locality",

                    leadingIcon =
                        Icons.Default.LocationOn
                )


                PremiumTextField(
                    value =
                        city,

                    onValueChange = {

                        city = it
                        validationError = null
                    },

                    label =
                        "City",

                    placeholder =
                        "Enter city"
                )


                PremiumTextField(
                    value =
                        address,

                    onValueChange = {

                        address = it
                        validationError = null
                    },

                    label =
                        "Full Address",

                    placeholder =
                        "Enter complete address",

                    minLines =
                        2,

                    maxLines =
                        4
                )
            }


            // =================================================
            // COORDINATES
            // =================================================

            EditSectionCard(
                title =
                    "Map Coordinates",

                subtitle =
                    "Optional latitude and longitude."
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    PremiumTextField(
                        value =
                            latitude,

                        onValueChange = {
                            latitude = it
                        },

                        label =
                            "Latitude",

                        modifier =
                            Modifier.weight(1f),

                        keyboardType =
                            KeyboardType.Decimal
                    )


                    PremiumTextField(
                        value =
                            longitude,

                        onValueChange = {
                            longitude = it
                        },

                        label =
                            "Longitude",

                        modifier =
                            Modifier.weight(1f),

                        keyboardType =
                            KeyboardType.Decimal
                    )
                }
            }


            // =================================================
            // SPORTS
            // =================================================

            EditSectionCard(
                title =
                    "Sports Available",

                subtitle =
                    "Select all sports available at this turf."
            ) {

                FlowRow(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    availableSports.forEach { sport ->

                        val selected =
                            selectedSports.contains(
                                sport
                            )

                        PremiumFilterChip(
                            selected =
                                selected,

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

                                validationError =
                                    null
                            },

                            label =
                                sport,

                            icon =
                                if (selected) {
                                    Icons.Default.SportsSoccer
                                } else {
                                    null
                                }
                        )
                    }
                }
            }


            // =================================================
            // AMENITIES
            // =================================================

            EditSectionCard(
                title =
                    "Amenities",

                subtitle =
                    "Select the facilities available."
            ) {

                FlowRow(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    availableAmenities.forEach { amenity ->

                        val selected =
                            selectedAmenities.contains(
                                amenity
                            )

                        PremiumFilterChip(
                            selected =
                                selected,

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

                            label =
                                amenity,

                            icon =
                                if (selected) {
                                    Icons.Default.CheckCircle
                                } else {
                                    null
                                }
                        )
                    }
                }
            }


            // =================================================
            // PRICING
            // =================================================

            EditSectionCard(
                title =
                    "Pricing",

                subtitle =
                    "Set the standard price per slot."
            ) {

                PremiumTextField(
                    value =
                        price,

                    onValueChange = {

                        price = it
                        validationError = null
                    },

                    label =
                        "Price per Slot",

                    placeholder =
                        "e.g. 650",

                    prefix =
                        "₹",

                    keyboardType =
                        KeyboardType.Decimal
                )
            }


            // =================================================
            // STATUS
            // =================================================

            EditSectionCard(
                title =
                    "Turf Status",

                subtitle =
                    "Control whether customers can view this turf."
            ) {

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    PremiumFilterChip(
                        selected =
                            status == "ACTIVE",

                        onClick = {
                            status = "ACTIVE"
                        },

                        label =
                            "Active",

                        icon =
                            if (status == "ACTIVE") {
                                Icons.Default.CheckCircle
                            } else {
                                null
                            }
                    )


                    PremiumFilterChip(
                        selected =
                            status == "INACTIVE",

                        onClick = {
                            status = "INACTIVE"
                        },

                        label =
                            "Inactive",

                        icon =
                            if (status == "INACTIVE") {
                                Icons.Default.Close
                            } else {
                                null
                            }
                    )
                }
            }


            // =================================================
            // VALIDATION ERROR
            // =================================================

            validationError?.let { message ->

                Surface(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(14.dp),

                    color =
                        ErrorRed.copy(
                            alpha = 0.08f
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            ErrorRed.copy(
                                alpha = 0.20f
                            )
                        )
                ) {

                    Row(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Surface(

                            modifier =
                                Modifier.size(34.dp),

                            shape =
                                CircleShape,

                            color =
                                ErrorRed.copy(
                                    alpha = 0.10f
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
                                        Modifier.size(17.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.width(9.dp)
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


            // =================================================
            // SAVE BUTTON
            // =================================================

            Button(

                onClick = {

                    validationError =
                        null

                    val cleanName =
                        name.trim()

                    val cleanLocation =
                        location.trim()

                    val cleanCity =
                        city.trim()

                    val turfPrice =
                        price
                            .trim()
                            .toDoubleOrNull()


                    if (cleanName.isBlank()) {

                        validationError =
                            "Please enter turf name."

                        return@Button
                    }


                    if (cleanLocation.isBlank()) {

                        validationError =
                            "Please enter turf location."

                        return@Button
                    }


                    if (cleanCity.isBlank()) {

                        validationError =
                            "Please enter city."

                        return@Button
                    }


                    if (selectedSports.isEmpty()) {

                        validationError =
                            "Please select at least one sport."

                        return@Button
                    }


                    if (
                        turfPrice == null ||
                        turfPrice <= 0
                    ) {

                        validationError =
                            "Please enter a valid price."

                        return@Button
                    }


                    val request =
                        UpdateTurfRequest(

                            name =
                                cleanName,

                            description =
                                description
                                    .trim()
                                    .ifBlank {
                                        null
                                    },

                            location =
                                cleanLocation,

                            city =
                                cleanCity,

                            address =
                                address
                                    .trim()
                                    .ifBlank {
                                        null
                                    },

                            latitude =
                                latitude
                                    .trim()
                                    .toDoubleOrNull(),

                            longitude =
                                longitude
                                    .trim()
                                    .toDoubleOrNull(),

                            sportsTypes =
                                selectedSports.toList(),

                            amenities =
                                selectedAmenities.toList(),

                            imageUrls =
                                existingImages.toList(),

                            price =
                                turfPrice,

                            status =
                                status
                        )


                    viewModel.updateTurf(

                        token =
                            token,

                        turfId =
                            turfId,

                        request =
                            request,

                        onSuccess = {

                            onSuccess()
                        }
                    )
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                enabled =
                    !isLoading &&
                            formLoaded,

                shape =
                    RoundedCornerShape(15.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PrimaryGreen,

                        contentColor =
                            Color(0xFF061008),

                        disabledContainerColor =
                            PrimaryGreen.copy(
                                alpha = 0.35f
                            ),

                        disabledContentColor =
                            Color(0xFF061008)
                                .copy(
                                    alpha = 0.55f
                                )
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(21.dp),

                        color =
                            Color(0xFF061008),

                        strokeWidth =
                            2.5.dp
                    )

                    Spacer(
                        modifier =
                            Modifier.width(9.dp)
                    )

                    Text(
                        text =
                            "Saving Changes...",

                        fontWeight =
                            FontWeight.Bold
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
                            "Save Changes",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }


            // =================================================
            // FOOTER
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
                        "Changes are saved to your turf listing.",

                    color =
                        MutedText,

                    fontSize =
                        10.sp
                )
            }
        }
    }
}


// =============================================================
// EDIT TURF TOP BAR
// Matches Subscription screen top bar
// =============================================================

@Composable
private fun EditTurfTopBar(
    onBackClick: () -> Unit,
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
                                top = 42.dp,
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
                                onBackClick,

                            enabled =
                                enabled
                        ) {

                            Icon(

                                imageVector =
                                    Icons
                                        .AutoMirrored
                                        .Filled
                                        .ArrowBack,

                                contentDescription =
                                    "Back",

                                tint =
                                    if (enabled) {
                                        PrimaryText
                                    } else {
                                        MutedText
                                    },

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
                                "Edit Turf",

                            color =
                                MutedText,

                            fontSize =
                                10.sp
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
// HERO CARD
// =============================================================

@Composable
private fun EditTurfHeroCard() {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Box {

            Box(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors =
                                    listOf(
                                        Color(0xFF071810),
                                        Color(0xFF0D2017),
                                        Color(0xFF06110D)
                                    )
                            )
                        )
            )


            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(

                    modifier =
                        Modifier.size(54.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    color =
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
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
                                Modifier.size(27.dp)
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )


                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Turf Information",

                        color =
                            PrimaryText,

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
                            "Update your turf, facilities and photos.",

                        color =
                            SecondaryText,

                        fontSize =
                            11.sp
                    )
                }
            }
        }
    }
}


// =============================================================
// SECTION CARD
// =============================================================

@Composable
private fun EditSectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
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
                Modifier.padding(17.dp),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(

                    modifier =
                        Modifier
                            .width(3.dp)
                            .height(20.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                            .background(
                                PrimaryGreen
                            )
                )

                Spacer(
                    modifier =
                        Modifier.width(9.dp)
                )

                Text(
                    text =
                        title,

                    color =
                        PrimaryText,

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Text(
                text =
                    subtitle,

                color =
                    MutedText,

                fontSize =
                    11.sp
            )


            HorizontalDivider(
                color =
                    Border.copy(
                        alpha = 0.65f
                    )
            )


            content()
        }
    }
}


// =============================================================
// PREMIUM TEXT FIELD
// =============================================================

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    prefix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            modifier.fillMaxWidth(),

        label = {
            Text(
                text =
                    label,
                fontSize =
                    12.sp
            )
        },

        placeholder = {

            if (placeholder != null) {

                Text(
                    text =
                        placeholder,

                    fontSize =
                        12.sp
                )
            }
        },

        leadingIcon = {

            if (leadingIcon != null) {

                Icon(
                    imageVector =
                        leadingIcon,

                    contentDescription =
                        null,

                    tint =
                        PrimaryGreen,

                    modifier =
                        Modifier.size(19.dp)
                )
            }
        },

        prefix = {

            if (prefix != null) {

                Text(
                    text =
                        prefix,

                    color =
                        PrimaryText,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        },

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        minLines =
            minLines,

        maxLines =
            maxLines,

        singleLine =
            maxLines == 1,

        shape =
            RoundedCornerShape(12.dp),

        colors =
            androidx.compose.material3.OutlinedTextFieldDefaults
                .colors(

                    focusedTextColor =
                        PrimaryText,

                    unfocusedTextColor =
                        PrimaryText,

                    focusedBorderColor =
                        PrimaryGreen,

                    unfocusedBorderColor =
                        Border,

                    focusedLabelColor =
                        LightGreen,

                    unfocusedLabelColor =
                        SecondaryText,

                    cursorColor =
                        PrimaryGreen,

                    focusedPlaceholderColor =
                        MutedText,

                    unfocusedPlaceholderColor =
                        MutedText,

                    focusedLeadingIconColor =
                        PrimaryGreen,

                    unfocusedLeadingIconColor =
                        MutedText
                )
    )
}


// =============================================================
// PREMIUM FILTER CHIP
// =============================================================

@Composable
private fun PremiumFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {

    FilterChip(

        selected =
            selected,

        onClick =
            onClick,

        label = {

            Text(
                text =
                    label,

                fontSize =
                    11.sp
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
                        Modifier.size(16.dp),

                    tint =
                        if (selected) {
                            LightGreen
                        } else {
                            SecondaryText
                        }
                )
            }
        }
    )
}


// =============================================================
// IMAGE ITEM
// =============================================================

@Composable
private fun TurfImageItem(
    imageModel: Any,
    onRemove: () -> Unit
) {

    Box(

        modifier =
            Modifier
                .size(
                    width = 150.dp,
                    height = 110.dp
                )
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(
                    SurfaceElevated
                )
    ) {

        AsyncImage(

            model =
                imageModel,

            contentDescription =
                "Turf image",

            modifier =
                Modifier.fillMaxSize(),

            contentScale =
                ContentScale.Crop
        )


        Surface(

            modifier =
                Modifier
                    .align(
                        Alignment.TopEnd
                    )
                    .padding(6.dp)
                    .size(30.dp),

            shape =
                CircleShape,

            color =
                Color.Black.copy(
                    alpha = 0.65f
                )
        ) {

            IconButton(
                onClick =
                    onRemove
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Close,

                    contentDescription =
                        "Remove image",

                    tint =
                        Color.White,

                    modifier =
                        Modifier.size(16.dp)
                )
            }
        }
    }
}


// =============================================================
// IMAGE URL
// =============================================================

private fun buildImageUrl(
    imageUrl: String
): String {

    val cleanUrl =
        imageUrl.trim()

    if (
        cleanUrl.startsWith("http://") ||
        cleanUrl.startsWith("https://")
    ) {

        return cleanUrl
    }

    return "http://10.0.2.2:8000/${cleanUrl.trimStart('/')}"
}