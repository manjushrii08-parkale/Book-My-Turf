package com.example.bookmyturf.screens.admin

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
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

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.text.KeyboardOptions

import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage

import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AdminViewModel


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
            AdminOffWhite,

        snackbarHost = {

            SnackbarHost(
                hostState =
                    snackbarHostState
            )
        },

        topBar = {

            TopAppBar(

                title = {

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(1.dp)
                    ) {

                        Text(
                            text = "Edit Turf",
                            color =
                                AdminDarkCharcoal,
                            fontWeight =
                                FontWeight.Bold,
                            fontSize =
                                19.sp
                        )

                        Text(
                            text =
                                "Update your turf details",
                            color =
                                AdminGray,
                            fontSize =
                                11.sp
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
                                Icons
                                    .AutoMirrored
                                    .Filled
                                    .ArrowBack,
                            contentDescription =
                                "Back",
                            tint =
                                AdminDarkGreen
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            AdminWhite,
                        titleContentColor =
                            AdminDarkCharcoal,
                        navigationIconContentColor =
                            AdminDarkGreen
                    )
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
                        horizontal = 16.dp,
                        vertical = 14.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {


            // =================================================
            // HEADER
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AdminDarkGreen
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(

                        modifier =
                            Modifier
                                .size(54.dp)
                                .clip(
                                    RoundedCornerShape(16.dp)
                                )
                                .background(
                                    AdminLightGreen
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Storefront,
                            contentDescription =
                                null,
                            tint =
                                AdminDarkGreen,
                            modifier =
                                Modifier.size(28.dp)
                        )
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
                                AdminWhite,
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
                                AdminWhite.copy(
                                    alpha = 0.82f
                                ),
                            fontSize =
                                12.sp
                        )
                    }
                }
            }


            // =================================================
            // TURF IMAGES
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
                                AdminDarkCharcoal,
                            fontSize =
                                15.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Current + new photos",
                            color =
                                AdminGray,
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
                        enabled = !isLoading,
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

                        Text("Add")
                    }
                }


                if (existingImages.isNotEmpty()) {

                    Text(
                        text =
                            "Current Images",
                        color =
                            AdminDarkCharcoal,
                        fontWeight =
                            FontWeight.SemiBold,
                        fontSize =
                            13.sp
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items = existingImages,
                            key = { it }
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
                            "New Images",
                        color =
                            AdminDarkCharcoal,
                        fontWeight =
                            FontWeight.SemiBold,
                        fontSize =
                            13.sp
                    )

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items = newImageUris,
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
                title = "Basic Information",
                subtitle =
                    "Update the main information of your turf."
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = {

                        name = it
                        validationError = null
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
                    value = description,
                    onValueChange = {

                        description = it
                        validationError = null
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("Description")
                    },
                    placeholder = {
                        Text("Describe your turf")
                    },
                    minLines = 3,
                    maxLines = 5
                )
            }


            // =================================================
            // LOCATION
            // =================================================

            EditSectionCard(
                title = "Location",
                subtitle =
                    "Keep your turf location information accurate."
            ) {

                OutlinedTextField(
                    value = location,
                    onValueChange = {

                        location = it
                        validationError = null
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("Location")
                    },
                    placeholder = {
                        Text("Area / Locality")
                    },
                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.LocationOn,
                            contentDescription =
                                null,
                            tint =
                                AdminDarkGreen
                        )
                    },
                    singleLine = true
                )


                OutlinedTextField(
                    value = city,
                    onValueChange = {

                        city = it
                        validationError = null
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
                    value = address,
                    onValueChange = {

                        address = it
                        validationError = null
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
            // COORDINATES
            // =================================================

            EditSectionCard(
                title = "Map Coordinates",
                subtitle =
                    "Optional latitude and longitude."
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = latitude,
                        onValueChange = {
                            latitude = it
                        },
                        modifier =
                            Modifier.weight(1f),
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
                        value = longitude,
                        onValueChange = {
                            longitude = it
                        },
                        modifier =
                            Modifier.weight(1f),
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
            }


            // =================================================
            // SPORTS
            // =================================================

            EditSectionCard(
                title = "Sports Available",
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

                        FilterChip(
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

                                validationError = null
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
                                            Modifier.size(17.dp),
                                        tint =
                                            AdminDarkGreen
                                    )
                                }
                            }
                        )
                    }
                }
            }


            // =================================================
            // AMENITIES
            // =================================================

            EditSectionCard(
                title = "Amenities",
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

                        FilterChip(
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

                            label = {
                                Text(amenity)
                            },

                            leadingIcon = {

                                if (selected) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.CheckCircle,
                                        contentDescription =
                                            null,
                                        modifier =
                                            Modifier.size(17.dp),
                                        tint =
                                            AdminForestGreen
                                    )
                                }
                            }
                        )
                    }
                }
            }


            // =================================================
            // PRICING
            // =================================================

            EditSectionCard(
                title = "Pricing",
                subtitle =
                    "Set the standard price per slot."
            ) {

                OutlinedTextField(
                    value = price,
                    onValueChange = {

                        price = it
                        validationError = null
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("Price per Slot")
                    },
                    placeholder = {
                        Text("e.g. 650")
                    },
                    leadingIcon = {

                        Text(
                            text = "₹",
                            color =
                                AdminDarkCharcoal,
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
            }


            // =================================================
            // STATUS
            // =================================================

            EditSectionCard(
                title = "Turf Status",
                subtitle =
                    "Control whether customers can view this turf."
            ) {

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    FilterChip(
                        selected =
                            status == "ACTIVE",
                        onClick = {
                            status = "ACTIVE"
                        },
                        label = {
                            Text("Active")
                        }
                    )

                    FilterChip(
                        selected =
                            status == "INACTIVE",
                        onClick = {
                            status = "INACTIVE"
                        },
                        label = {
                            Text("Inactive")
                        }
                    )
                }
            }


            // =================================================
            // VALIDATION ERROR
            // =================================================

            validationError?.let { message ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(14.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(
                                    0xFFFFF1F2
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

                        Icon(
                            imageVector =
                                Icons.Default.Close,
                            contentDescription =
                                null,
                            tint =
                                Color(0xFFBE123C),
                            modifier =
                                Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text = message,
                            color =
                                Color(0xFFBE123C),
                            fontSize =
                                13.sp,
                            fontWeight =
                                FontWeight.Medium
                        )
                    }
                }
            }


            // =================================================
            // SAVE
            // =================================================

            Button(

                onClick = {

                    validationError = null

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

                        token = token,

                        turfId = turfId,

                        request = request,

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
                            AdminDarkGreen,
                        contentColor =
                            AdminWhite,
                        disabledContainerColor =
                            AdminDarkGreen.copy(
                                alpha = 0.45f
                            )
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(22.dp),
                        color =
                            AdminWhite,
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
                            null
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
                        AdminForestGreen,
                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        "Changes are saved to your turf listing.",
                    color =
                        AdminGray,
                    fontSize =
                        11.sp
                )
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
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(17.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = title,
                color =
                    AdminDarkCharcoal,
                fontSize =
                    17.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = subtitle,
                color =
                    AdminGray,
                fontSize =
                    12.sp
            )

            content()
        }
    }
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
                    AdminOffWhite
                )
    ) {

        AsyncImage(
            model = imageModel,
            contentDescription = "Turf image",
            modifier =
                Modifier.fillMaxSize(),
            contentScale =
                ContentScale.Crop
        )


        IconButton(
            onClick = onRemove,
            modifier =
                Modifier
                    .align(
                        Alignment.TopEnd
                    )
                    .padding(4.dp)
                    .size(30.dp)
                    .background(
                        Color.Black.copy(
                            alpha = 0.65f
                        ),
                        RoundedCornerShape(50)
                    )
        ) {

            Icon(
                imageVector =
                    Icons.Default.Close,
                contentDescription =
                    "Remove image",
                tint =
                    AdminWhite,
                modifier =
                    Modifier.size(17.dp)
            )
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