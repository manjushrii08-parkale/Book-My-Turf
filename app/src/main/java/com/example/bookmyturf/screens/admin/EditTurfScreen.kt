package com.example.bookmyturf.screens.admin

import android.app.TimePickerDialog
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
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
// COLORS
// =============================================================

private val TurfGreen = Color(0xFF14532D)
private val TurfLightGreen = Color(0xFFE8F5E9)
private val TurfGray = Color(0xFF64748B)


// =============================================================
// TIME VALIDATION
//
// Laravel format:
// h:i A
//
// Examples:
// 09:00 AM
// 01:30 PM
// 10:00 PM
// =============================================================

private fun isValidTime(
    value: String
): Boolean {

    return Regex(
        """^(0[1-9]|1[0-2]):[0-5]\d\s(AM|PM)$"""
    ).matches(
        value
            .trim()
            .uppercase()
    )
}


// =============================================================
// NORMALIZE TIME
// =============================================================

private fun normalizeTime(
    value: String
): String? {

    val trimmed =
        value
            .trim()
            .uppercase()

    return if (
        isValidTime(
            trimmed
        )
    ) {

        trimmed

    } else {

        null
    }
}


// =============================================================
// CONVERT TIME TO MINUTES
//
// Examples:
//
// 09:00 AM = 540
// 01:00 PM = 780
// 10:00 PM = 1320
//
// Used for comparing opening and closing times.
// =============================================================

private fun timeToMinutes(
    value: String
): Int {

    val parts =
        value
            .trim()
            .uppercase()
            .split(" ")

    val timeParts =
        parts[0]
            .split(":")

    var hour =
        timeParts[0]
            .toInt()

    val minute =
        timeParts[1]
            .toInt()

    val period =
        parts[1]

    if (
        period == "AM" &&
        hour == 12
    ) {

        hour = 0
    }

    if (
        period == "PM" &&
        hour != 12
    ) {

        hour += 12
    }

    return hour * 60 + minute
}


// =============================================================
// PARSE h:i A TIME
//
// Converts:
//
// 09:00 AM → 09:00
// 01:30 PM → 13:30
//
// Used when opening existing turf data.
// =============================================================

private fun parseTimeTo24Hour(
    value: String
): Pair<Int, Int>? {

    val normalized =
        normalizeTime(
            value
        )
            ?: return null

    val parts =
        normalized.split(" ")

    val timeParts =
        parts[0].split(":")

    var hour =
        timeParts[0]
            .toInt()

    val minute =
        timeParts[1]
            .toInt()

    val period =
        parts[1]

    if (
        period == "AM" &&
        hour == 12
    ) {

        hour = 0
    }

    if (
        period == "PM" &&
        hour != 12
    ) {

        hour += 12
    }

    return Pair(
        hour,
        minute
    )
}


// =============================================================
// SHOW TIME PICKER
//
// Display:
// 9:00 AM
// 1:30 PM
// 10:00 PM
//
// Save to state:
// 09:00 AM
// 01:30 PM
// 10:00 PM
// =============================================================

private fun showTimePicker(
    context: Context,
    initialTime: String,
    onTimeSelected: (String) -> Unit
) {

    val parsedTime =
        parseTimeTo24Hour(
            initialTime
        )

    val initialHour =
        parsedTime?.first
            ?: 9

    val initialMinute =
        parsedTime?.second
            ?: 0

    TimePickerDialog(

        context,

        { _, selectedHour, selectedMinute ->

            val period =
                if (
                    selectedHour < 12
                ) {

                    "AM"

                } else {

                    "PM"
                }

            var displayHour =
                selectedHour % 12

            if (
                displayHour == 0
            ) {

                displayHour = 12
            }

            val formattedTime =
                String.format(
                    "%02d:%02d %s",
                    displayHour,
                    selectedMinute,
                    period
                )

            onTimeSelected(
                formattedTime
            )
        },

        initialHour,

        initialMinute,

        // false = 12-hour picker with AM/PM
        false

    ).show()
}


// =============================================================
// EDIT TURF SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTurfScreen(

    token: String,

    turfId: Int,

    onBack: () -> Unit,

    onSuccess: () -> Unit

) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context =
        LocalContext.current


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

    var openingTime by remember {
        mutableStateOf("")
    }

    var closingTime by remember {
        mutableStateOf("")
    }

    var status by remember {
        mutableStateOf("ACTIVE")
    }


    // =========================================================
    // VALIDATION ERROR
    // =========================================================

    var validationError by remember {
        mutableStateOf<String?>(null)
    }


    // =========================================================
    // SPORTS
    // =========================================================

    val availableSports = listOf(

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

    val availableAmenities = listOf(

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
    // NEW IMAGE URIS
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

    LaunchedEffect(
        turfResponse
    ) {

        val turf: Turf =
            turfResponse
                ?.data
                ?.turf
                ?: return@LaunchedEffect


        if (!formLoaded) {

            // -------------------------------------------------
            // BASIC
            // -------------------------------------------------

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


            // -------------------------------------------------
            // LOCATION
            // -------------------------------------------------

            latitude =
                turf.latitude
                    ?.toString()
                    ?: ""

            longitude =
                turf.longitude
                    ?.toString()
                    ?: ""


            // -------------------------------------------------
            // PRICE
            // -------------------------------------------------

            price =
                turf.price.toString()


            // -------------------------------------------------
            // TIME
            //
            // Laravel should return:
            // 09:00 AM
            // 10:00 PM
            // -------------------------------------------------

            openingTime =
                turf.openingTime

            closingTime =
                turf.closingTime


            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            status =
                turf.status
                    .uppercase()


            // -------------------------------------------------
            // SPORTS
            // -------------------------------------------------

            selectedSports.clear()

            selectedSports.addAll(
                turf.sportsTypes
            )


            // -------------------------------------------------
            // AMENITIES
            // -------------------------------------------------

            selectedAmenities.clear()

            selectedAmenities.addAll(
                turf.amenities
            )


            // -------------------------------------------------
            // IMAGES
            // -------------------------------------------------

            existingImages.clear()

            existingImages.addAll(
                turf.imageUrls
            )


            formLoaded = true
        }
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(

                            text =
                                "Edit Turf",

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(

                            text =
                                "Update your turf details",

                            fontSize =
                                12.sp,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }
                },


                navigationIcon = {

                    IconButton(

                        onClick =
                            onBack

                    ) {

                        Icon(

                            imageVector =
                                Icons
                                    .AutoMirrored
                                    .Filled
                                    .ArrowBack,

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
                    .padding(
                        paddingValues
                    )
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(
                    16.dp
                )
        ) {


            // =================================================
            // HEADER
            // =================================================

            Text(

                text =
                    "Turf Information",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold
            )


            Text(

                text =
                    "Update your turf information, facilities, images and operating hours.",

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )


            // =================================================
            // IMAGES CARD
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                Color.White
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Image,

                            contentDescription =
                                null,

                            tint =
                                TurfGreen
                        )


                        Spacer(
                            modifier =
                                Modifier.width(
                                    8.dp
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
                                    "Turf Images",

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize =
                                    17.sp
                            )


                            Text(

                                text =
                                    "${existingImages.size + newImageUris.size} image(s)",

                                fontSize =
                                    12.sp,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
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
                            }
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Add,

                                contentDescription =
                                    null
                            )


                            Spacer(
                                modifier =
                                    Modifier.width(
                                        4.dp
                                    )
                            )


                            Text(
                                "Add"
                            )
                        }
                    }


                    // =================================================
                    // CURRENT IMAGES
                    // =================================================

                    if (
                        existingImages.isNotEmpty()
                    ) {

                        Text(

                            text =
                                "Current Images",

                            fontWeight =
                                FontWeight.SemiBold
                        )


                        LazyRow(

                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    10.dp
                                )
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
                                        imageUrl,

                                    onRemove = {

                                        existingImages
                                            .remove(
                                                imageUrl
                                            )
                                    }
                                )
                            }
                        }
                    }


                    // =================================================
                    // NEW IMAGES
                    // =================================================

                    if (
                        newImageUris.isNotEmpty()
                    ) {

                        Text(

                            text =
                                "New Images",

                            fontWeight =
                                FontWeight.SemiBold
                        )


                        LazyRow(

                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    10.dp
                                )
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

                                        newImageUris
                                            .remove(
                                                uri
                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }


            // =================================================
            // BASIC INFORMATION
            // =================================================

            SectionTitle(
                title =
                    "Basic Information"
            )


            OutlinedTextField(

                value =
                    name,

                onValueChange = {

                    name = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Turf Name")
                },

                placeholder = {
                    Text("Enter turf name")
                },

                singleLine = true
            )


            OutlinedTextField(

                value =
                    description,

                onValueChange = {

                    description = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Description")
                },

                placeholder = {
                    Text("Describe your turf")
                },

                minLines = 4,

                maxLines = 6
            )


            // =================================================
            // LOCATION
            // =================================================

            SectionTitle(
                title =
                    "Location"
            )


            OutlinedTextField(

                value =
                    location,

                onValueChange = {

                    location = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Location")
                },

                placeholder = {
                    Text("Area / Locality")
                },

                singleLine = true
            )


            OutlinedTextField(

                value =
                    city,

                onValueChange = {

                    city = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("City")
                },

                placeholder = {
                    Text("Enter city")
                },

                singleLine = true
            )


            OutlinedTextField(

                value =
                    address,

                onValueChange = {

                    address = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Full Address")
                },

                placeholder = {
                    Text("Enter complete address")
                },

                minLines = 3,

                maxLines = 5
            )


            // =================================================
            // LATITUDE / LONGITUDE
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {


                OutlinedTextField(

                    value =
                        latitude,

                    onValueChange = {

                        latitude = it
                    },

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    label = {
                        Text("Latitude")
                    },

                    keyboardOptions =
                        KeyboardOptions(

                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )


                OutlinedTextField(

                    value =
                        longitude,

                    onValueChange = {

                        longitude = it
                    },

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    label = {
                        Text("Longitude")
                    },

                    keyboardOptions =
                        KeyboardOptions(

                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )
            }


            // =================================================
            // SPORTS
            // =================================================

            SectionTitle(
                title =
                    "Sports Available"
            )


            Text(

                text =
                    "Select the sports available at this turf.",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )


            FlowRow(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                availableSports.forEach { sport ->


                    FilterChip(

                        selected =
                            selectedSports
                                .contains(
                                    sport
                                ),

                        onClick = {

                            if (
                                selectedSports
                                    .contains(
                                        sport
                                    )
                            ) {

                                selectedSports
                                    .remove(
                                        sport
                                    )

                            } else {

                                selectedSports
                                    .add(
                                        sport
                                    )
                            }
                        },

                        label = {
                            Text(
                                sport
                            )
                        }
                    )
                }
            }


            // =================================================
            // AMENITIES
            // =================================================

            SectionTitle(
                title =
                    "Amenities"
            )


            FlowRow(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                availableAmenities.forEach {
                        amenity ->


                    FilterChip(

                        selected =
                            selectedAmenities
                                .contains(
                                    amenity
                                ),

                        onClick = {

                            if (
                                selectedAmenities
                                    .contains(
                                        amenity
                                    )
                            ) {

                                selectedAmenities
                                    .remove(
                                        amenity
                                    )

                            } else {

                                selectedAmenities
                                    .add(
                                        amenity
                                    )
                            }
                        },

                        label = {
                            Text(
                                amenity
                            )
                        }
                    )
                }
            }


            // =================================================
            // PRICING
            // =================================================

            SectionTitle(
                title =
                    "Pricing"
            )


            OutlinedTextField(

                value =
                    price,

                onValueChange = {

                    price = it

                    validationError =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Price per Slot")
                },

                placeholder = {
                    Text("Example: 500")
                },

                leadingIcon = {

                    Text(

                        text =
                            "₹",

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
            // OPERATING HOURS
            // =================================================

            SectionTitle(
                title =
                    "Operating Hours"
            )


            Text(

                text =
                    "Select time using 12-hour format with AM/PM.",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

                color =
                    TurfGray
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {


                // =================================================
                // OPENING TIME
                // =================================================

                OutlinedTextField(

                    value =
                        openingTime,

                    onValueChange = {
                        // Read-only.
                        // Time selected from TimePicker.
                    },

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    label = {
                        Text(
                            "Opening Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "09:00 AM"
                        )
                    },

                    supportingText = {

                        Text(
                            "hh:mm AM/PM"
                        )
                    },

                    readOnly = true,

                    singleLine = true,

                    trailingIcon = {

                        IconButton(

                            onClick = {

                                showTimePicker(

                                    context =
                                        context,

                                    initialTime =
                                        openingTime

                                ) { selectedTime ->

                                    openingTime =
                                        selectedTime

                                    validationError =
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
                                    "Select Opening Time",

                                tint =
                                    TurfGreen
                            )
                        }
                    },

                    isError =
                        openingTime.isNotBlank() &&
                                !isValidTime(
                                    openingTime
                                )
                )


                // =================================================
                // CLOSING TIME
                // =================================================

                OutlinedTextField(

                    value =
                        closingTime,

                    onValueChange = {
                        // Read-only.
                        // Time selected from TimePicker.
                    },

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    label = {
                        Text(
                            "Closing Time"
                        )
                    },

                    placeholder = {
                        Text(
                            "10:00 PM"
                        )
                    },

                    supportingText = {

                        Text(
                            "hh:mm AM/PM"
                        )
                    },

                    readOnly = true,

                    singleLine = true,

                    trailingIcon = {

                        IconButton(

                            onClick = {

                                showTimePicker(

                                    context =
                                        context,

                                    initialTime =
                                        closingTime

                                ) { selectedTime ->

                                    closingTime =
                                        selectedTime

                                    validationError =
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
                                    "Select Closing Time",

                                tint =
                                    TurfGreen
                            )
                        }
                    },

                    isError =
                        closingTime.isNotBlank() &&
                                !isValidTime(
                                    closingTime
                                )
                )
            }


            // =================================================
            // SELECTED OPERATING HOURS
            // =================================================

            if (

                openingTime.isNotBlank() &&
                closingTime.isNotBlank()

            ) {

                Text(

                    text =
                        "Operating Hours: $openingTime - $closingTime",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        TurfGreen,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }


            // =================================================
            // STATUS
            // =================================================

            SectionTitle(
                title =
                    "Turf Status"
            )


            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {


                FilterChip(

                    selected =
                        status == "ACTIVE",

                    onClick = {

                        status =
                            "ACTIVE"
                    },

                    label = {
                        Text(
                            "Active"
                        )
                    }
                )


                FilterChip(

                    selected =
                        status == "INACTIVE",

                    onClick = {

                        status =
                            "INACTIVE"
                    },

                    label = {
                        Text(
                            "Inactive"
                        )
                    }
                )
            }


            // =================================================
            // VALIDATION ERROR
            // =================================================

            if (
                !validationError
                    .isNullOrBlank()
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults
                            .cardColors(

                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .errorContainer
                            )
                ) {

                    Text(

                        text =
                            validationError
                                ?: "",

                        modifier =
                            Modifier.padding(
                                14.dp
                            ),

                        color =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer
                    )
                }
            }


            // =================================================
            // API ERROR
            // =================================================

            if (
                !error.isNullOrBlank()
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults
                            .cardColors(

                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .errorContainer
                            )
                ) {

                    Text(

                        text =
                            error ?: "",

                        modifier =
                            Modifier.padding(
                                14.dp
                            ),

                        color =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer
                    )
                }
            }


            // =================================================
            // SUCCESS
            // =================================================

            if (
                !successMessage
                    .isNullOrBlank()
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults
                            .cardColors(

                                containerColor =
                                    TurfLightGreen
                            )
                ) {

                    Text(

                        text =
                            successMessage
                                ?: "",

                        modifier =
                            Modifier.padding(
                                14.dp
                            ),

                        color =
                            TurfGreen,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }


            // =================================================
            // UPDATE BUTTON
            // =================================================

            Button(

                onClick = {

                    // -------------------------------------------------
                    // CLEAR VALIDATION
                    // -------------------------------------------------

                    validationError =
                        null


                    // -------------------------------------------------
                    // CLEAN VALUES
                    // -------------------------------------------------

                    val cleanName =
                        name.trim()

                    val cleanLocation =
                        location.trim()

                    val cleanCity =
                        city.trim()

                    val cleanOpeningTime =
                        normalizeTime(
                            openingTime
                        )

                    val cleanClosingTime =
                        normalizeTime(
                            closingTime
                        )

                    val turfPrice =
                        price
                            .trim()
                            .toDoubleOrNull()


                    // -------------------------------------------------
                    // NAME
                    // -------------------------------------------------

                    if (
                        cleanName.isBlank()
                    ) {

                        validationError =
                            "Please enter turf name."

                        return@Button
                    }


                    // -------------------------------------------------
                    // LOCATION
                    // -------------------------------------------------

                    if (
                        cleanLocation.isBlank()
                    ) {

                        validationError =
                            "Please enter turf location."

                        return@Button
                    }


                    // -------------------------------------------------
                    // CITY
                    // -------------------------------------------------

                    if (
                        cleanCity.isBlank()
                    ) {

                        validationError =
                            "Please enter city."

                        return@Button
                    }


                    // -------------------------------------------------
                    // SPORTS
                    // -------------------------------------------------

                    if (
                        selectedSports.isEmpty()
                    ) {

                        validationError =
                            "Please select at least one sport."

                        return@Button
                    }


                    // -------------------------------------------------
                    // PRICE
                    // -------------------------------------------------

                    if (
                        turfPrice == null ||
                        turfPrice <= 0
                    ) {

                        validationError =
                            "Please enter a valid price."

                        return@Button
                    }


                    // -------------------------------------------------
                    // OPENING TIME
                    // -------------------------------------------------

                    if (
                        cleanOpeningTime == null
                    ) {

                        validationError =
                            "Please select a valid opening time."

                        return@Button
                    }


                    // -------------------------------------------------
                    // CLOSING TIME
                    // -------------------------------------------------

                    if (
                        cleanClosingTime == null
                    ) {

                        validationError =
                            "Please select a valid closing time."

                        return@Button
                    }


                    // -------------------------------------------------
                    // TIME LOGIC
                    // -------------------------------------------------

                    if (

                        timeToMinutes(
                            cleanOpeningTime
                        ) >=

                        timeToMinutes(
                            cleanClosingTime
                        )

                    ) {

                        validationError =
                            "Closing time must be later than opening time."

                        return@Button
                    }


                    // =================================================
                    // CREATE REQUEST
                    // =================================================

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
                                selectedSports
                                    .toList(),

                            amenities =
                                selectedAmenities
                                    .toList(),

                            imageUrls =
                                existingImages
                                    .toList(),

                            price =
                                turfPrice,

                            // =================================================
                            // Laravel h:i A format
                            //
                            // Example:
                            // 09:00 AM
                            // 10:00 PM
                            // =================================================

                            openingTime =
                                cleanOpeningTime,

                            closingTime =
                                cleanClosingTime,

                            status =
                                status
                        )


                    // =================================================
                    // UPDATE API
                    // =================================================

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
                        .height(
                            54.dp
                        ),

                enabled =
                    !isLoading &&
                            formLoaded,

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            ) {


                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(
                                22.dp
                            ),

                        color =
                            Color.White,

                        strokeWidth =
                            2.dp
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
                            Modifier.width(
                                8.dp
                            )
                    )


                    Text(

                        text =
                            "Save Changes",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }


            Spacer(

                modifier =
                    Modifier.height(
                        20.dp
                    )
            )
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SectionTitle(
    title: String
) {

    Column(

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(

            text =
                title,

            style =
                MaterialTheme
                    .typography
                    .titleMedium,

            fontWeight =
                FontWeight.Bold,

            color =
                TurfGreen
        )


        Spacer(
            modifier =
                Modifier.height(
                    6.dp
                )
        )


        HorizontalDivider(
            thickness =
                1.dp
        )
    }
}


// =============================================================
// TURF IMAGE ITEM
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
                    RoundedCornerShape(
                        14.dp
                    )
                )
                .background(
                    Color.LightGray
                )
    ) {


        AsyncImage(

            model =
                imageModel,

            contentDescription =
                "Turf Image",

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
                    .padding(
                        6.dp
                    ),

            shape =
                RoundedCornerShape(
                    50
                ),

            color =
                Color.Black.copy(
                    alpha = 0.65f
                )
        ) {

            IconButton(

                onClick =
                    onRemove,

                modifier =
                    Modifier.size(
                        32.dp
                    )
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Close,

                    contentDescription =
                        "Remove Image",

                    tint =
                        Color.White,

                    modifier =
                        Modifier.size(
                            18.dp
                        )
                )
            }
        }
    }
}