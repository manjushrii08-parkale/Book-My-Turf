package com.example.bookmyturf.screens.user

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.filled.Wifi

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.review.Review
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.ReviewRepository
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.ReviewViewModel
import com.example.bookmyturf.viewmodel.ReviewViewModelFactory
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory

import java.util.Locale

// ============================================================
// SAME USER THEME
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)
private val SoftGray = Color(0xFFA0A0A0)

private val BorderGray = Color(0xFFE5E5E0)

private val SoftGreen = Color(0xFFF1F7ED)
private val LightGreenBackground = Color(0xFFEAF4E5)

// ============================================================
// TURF DETAILS SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TurfDetailsScreen(
    turfId: Int,
    onBackClick: () -> Unit,
    onBookNowClick: (Int) -> Unit
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = LocalContext.current

    // =========================================================
    // TURF VIEW MODEL
    // =========================================================

    val turfRepository = remember {
        TurfRepository()
    }

    val turfFactory = remember {
        TurfDetailsViewModelFactory(
            repository = turfRepository
        )
    }

    val turfViewModel: TurfDetailsViewModel = viewModel(
        factory = turfFactory
    )

    // =========================================================
    // REVIEW VIEW MODEL
    // =========================================================

    val reviewRepository = remember {
        ReviewRepository(
            RetrofitClient.api
        )
    }

    val reviewFactory = remember {
        ReviewViewModelFactory(
            repository = reviewRepository
        )
    }

    val reviewViewModel: ReviewViewModel = viewModel(
        factory = reviewFactory
    )

    // =========================================================
    // SESSION
    // =========================================================

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }

    // =========================================================
    // TURF STATE
    // =========================================================

    val turf by turfViewModel.turf.collectAsState()

    val isLoading by turfViewModel.isLoading.collectAsState()

    val error by turfViewModel.error.collectAsState()

    // =========================================================
    // REVIEW STATE
    // =========================================================

    val reviewUiState by reviewViewModel.uiState.collectAsState()

    // =========================================================
    // LOAD TURF
    // =========================================================

    LaunchedEffect(turfId) {

        turfViewModel.loadTurf(
            turfId
        )
    }

    // =========================================================
    // LOAD REVIEWS
    // =========================================================

    LaunchedEffect(
        turfId,
        token
    ) {

        if (
            !token.isNullOrBlank() &&
            turfId > 0
        ) {

            reviewViewModel.loadTurfReviews(
                token = token,
                turfId = turfId
            )
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        when {

            // =====================================================
            // LOADING
            // =====================================================

            isLoading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(58.dp)
                                .background(
                                    color =
                                        LightGreenBackground,
                                    shape =
                                        CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(28.dp),

                            color =
                                ForestGreen,

                            strokeWidth =
                                3.dp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Text(
                        text =
                            "Loading turf details...",

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Please wait a moment",

                        fontSize =
                            12.sp,

                        color =
                            Gray
                    )
                }
            }

            // =====================================================
            // ERROR
            // =====================================================

            error != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(58.dp)
                                .background(
                                    color =
                                        Color(0xFFFFF2F2),
                                    shape =
                                        CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Cancel,

                            contentDescription =
                                null,

                            tint =
                                Color(0xFFB3261E),

                            modifier =
                                Modifier.size(27.dp)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Text(
                        text =
                            "Unable to load turf",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            error
                                ?: "Something went wrong.",

                        fontSize =
                            13.sp,

                        color =
                            Gray
                    )
                }
            }

            // =====================================================
            // TURF DATA
            // =====================================================

            turf != null -> {

                val currentTurf = turf!!

                // =================================================
                // RATING
                // =================================================

                val displayRating =
                    if (
                        reviewUiState.totalReviews > 0
                    ) {
                        reviewUiState.averageRating
                    } else {
                        currentTurf.rating
                    }

                val displayReviewCount =
                    if (
                        reviewUiState.totalReviews > 0
                    ) {
                        reviewUiState.totalReviews
                    } else {
                        currentTurf.reviewCount
                    }

                // =================================================
                // FULL LOCATION TEXT
                // =================================================

                val locationText =
                    buildString {

                        append(
                            currentTurf.name
                        )

                        if (
                            currentTurf.address
                                ?.isNotBlank() == true
                        ) {

                            append(
                                ", ${currentTurf.address}"
                            )
                        }

                        if (
                            currentTurf.location.isNotBlank()
                        ) {

                            append(
                                ", ${currentTurf.location}"
                            )
                        }

                        if (
                            currentTurf.city.isNotBlank()
                        ) {

                            append(
                                ", ${currentTurf.city}"
                            )
                        }
                    }

                Scaffold(

                    containerColor =
                        OffWhite,

                    // =================================================
                    // TOP APP BAR
                    // =================================================

                    topBar = {

                        TopAppBar(

                            title = {

                                Text(
                                    text =
                                        currentTurf.name,

                                    fontSize =
                                        18.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        Charcoal
                                )
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

                                        tint =
                                            Charcoal
                                    )
                                }
                            },

                            colors =
                                TopAppBarDefaults
                                    .topAppBarColors(
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

                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(
                                    innerPadding
                                )
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .padding(
                                    horizontal =
                                        18.dp,

                                    vertical =
                                        18.dp
                                )
                    ) {

                        // =================================================
                        // TURF HEADER
                        // =================================================

                        Text(
                            text =
                                currentTurf.name,

                            fontSize =
                                28.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                Charcoal,

                            lineHeight =
                                33.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Box(
                                modifier =
                                    Modifier
                                        .size(28.dp)
                                        .background(
                                            color =
                                                LightGreenBackground,
                                            shape =
                                                CircleShape
                                        ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.LocationOn,

                                    contentDescription =
                                        "Location",

                                    modifier =
                                        Modifier.size(
                                            16.dp
                                        ),

                                    tint =
                                        ForestGreen
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )

                            Text(
                                text =
                                    "${currentTurf.location}, ${currentTurf.city}",

                                fontSize =
                                    13.sp,

                                color =
                                    Gray
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(14.dp)
                        )

                        // =================================================
                        // RATING SUMMARY
                        // =================================================

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
                                ),

                            border =
                                BorderStroke(
                                    1.dp,
                                    BorderGray
                                ),

                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation =
                                        0.dp
                                )
                        ) {

                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            16.dp
                                        ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier =
                                        Modifier
                                            .size(48.dp)
                                            .background(
                                                color =
                                                    LightGreenBackground,
                                                shape =
                                                    RoundedCornerShape(
                                                        14.dp
                                                    )
                                            ),

                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Star,

                                        contentDescription =
                                            "Rating",

                                        modifier =
                                            Modifier.size(
                                                24.dp
                                            ),

                                        tint =
                                            LightGreen
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.width(12.dp)
                                )

                                Column(
                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                ) {

                                    Text(
                                        text =
                                            if (
                                                displayReviewCount > 0
                                            ) {

                                                String.format(
                                                    Locale.getDefault(),
                                                    "%.1f",
                                                    displayRating
                                                )

                                            } else {

                                                "No rating"
                                            },

                                        fontSize =
                                            18.sp,

                                        fontWeight =
                                            FontWeight.ExtraBold,

                                        color =
                                            Charcoal
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(2.dp)
                                    )

                                    Text(
                                        text =
                                            when {

                                                reviewUiState.isLoading ->
                                                    "Loading reviews..."

                                                displayReviewCount == 0 ->
                                                    "Be the first to review"

                                                displayReviewCount == 1 ->
                                                    "1 customer review"

                                                else ->
                                                    "$displayReviewCount customer reviews"
                                            },

                                        fontSize =
                                            12.sp,

                                        color =
                                            Gray
                                    )
                                }

                                if (
                                    displayReviewCount > 0 &&
                                    !reviewUiState.isLoading
                                ) {

                                    StarRating(
                                        rating =
                                            displayRating.toInt()
                                    )
                                }
                            }
                        }

                        // =================================================
                        // CUSTOMER REVIEWS
                        // =================================================

                        if (
                            reviewUiState.totalReviews > 0
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.height(24.dp)
                            )

                            SectionTitle(
                                icon = {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Star,

                                        contentDescription =
                                            null,

                                        modifier =
                                            Modifier.size(
                                                19.dp
                                            ),

                                        tint =
                                            ForestGreen
                                    )
                                },

                                title =
                                    "Customer Reviews",

                                subtitle =
                                    "What customers say about this turf"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            if (
                                reviewUiState.isLoading
                            ) {

                                ReviewsLoadingCard()

                            } else if (
                                reviewUiState.reviews.isNotEmpty()
                            ) {

                                Column(
                                    verticalArrangement =
                                        Arrangement.spacedBy(
                                            12.dp
                                        )
                                ) {

                                    reviewUiState
                                        .reviews
                                        .forEach { review ->

                                            ReviewCard(
                                                review =
                                                    review
                                            )
                                        }
                                }

                            } else {

                                EmptyReviewsCard()
                            }
                        }

                        // =================================================
                        // ABOUT TURF
                        // =================================================

                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )

                        SectionTitle(
                            title =
                                "About Turf",

                            subtitle =
                                "Everything you need to know"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                currentTurf.description
                                    ?: "No description available.",

                            fontSize =
                                14.sp,

                            lineHeight =
                                22.sp,

                            color =
                                Gray
                        )

                        Spacer(
                            modifier =
                                Modifier.height(26.dp)
                        )

                        // =================================================
                        // SPORTS
                        // =================================================

                        SectionTitle(
                            icon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.SportsSoccer,

                                    contentDescription =
                                        null,

                                    modifier =
                                        Modifier.size(
                                            19.dp
                                        ),

                                    tint =
                                        ForestGreen
                                )
                            },

                            title =
                                "Sports",

                            subtitle =
                                "Sports available at this turf"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        InfoCard {

                            Text(
                                text =
                                    currentTurf.sportsTypes
                                        .joinToString(
                                            " • "
                                        ),

                                fontSize =
                                    14.sp,

                                color =
                                    Charcoal,

                                lineHeight =
                                    22.sp
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(26.dp)
                        )

                        // =================================================
                        // AMENITIES
                        // =================================================

                        SectionTitle(
                            icon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.Sports,

                                    contentDescription =
                                        null,

                                    modifier =
                                        Modifier.size(
                                            19.dp
                                        ),

                                    tint =
                                        ForestGreen
                                )
                            },

                            title =
                                "Amenities",

                            subtitle =
                                "Everything available for players"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        if (
                            currentTurf.amenities.isEmpty()
                        ) {

                            InfoCard {

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.CheckCircle,

                                        contentDescription =
                                            null,

                                        modifier =
                                            Modifier.size(
                                                20.dp
                                            ),

                                        tint =
                                            SoftGray
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(
                                                10.dp
                                            )
                                    )

                                    Text(
                                        text =
                                            "No amenities available",

                                        fontSize =
                                            13.sp,

                                        color =
                                            Gray
                                    )
                                }
                            }

                        } else {

                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(
                                        10.dp
                                    )
                            ) {

                                currentTurf.amenities
                                    .chunked(2)
                                    .forEach { rowAmenities ->

                                        Row(
                                            modifier =
                                                Modifier.fillMaxWidth(),

                                            horizontalArrangement =
                                                Arrangement.spacedBy(
                                                    10.dp
                                                )
                                        ) {

                                            rowAmenities
                                                .forEach { amenity ->

                                                    AmenityTile(
                                                        modifier =
                                                            Modifier.weight(
                                                                1f
                                                            ),

                                                        amenity =
                                                            amenity
                                                    )
                                                }

                                            if (
                                                rowAmenities.size == 1
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

                        // =================================================
                        // LOCATION
                        // =================================================

                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )

                        SectionTitle(
                            icon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.LocationOn,

                                    contentDescription =
                                        null,

                                    modifier =
                                        Modifier.size(
                                            19.dp
                                        ),

                                    tint =
                                        ForestGreen
                                )
                            },

                            title =
                                "Location",

                            subtitle =
                                "Find this turf easily"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        LocationCard(
                            address =
                                currentTurf.address,

                            location =
                                currentTurf.location,

                            city =
                                currentTurf.city,

                            onOpenMaps = {

                                openGoogleMaps(
                                    context = context,

                                    query =
                                        locationText
                                )
                            }
                        )

                        // =================================================
                        // CANCELLATION POLICY
                        // =================================================

                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )

                        SectionTitle(
                            icon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.Cancel,

                                    contentDescription =
                                        null,

                                    modifier =
                                        Modifier.size(
                                            19.dp
                                        ),

                                    tint =
                                        ForestGreen
                                )
                            },

                            title =
                                "Cancellation Policy",

                            subtitle =
                                "Please review before booking"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        InfoCard {

                            Column {

                                Text(
                                    text =
                                        "Free cancellation up to 24 hours before the booking.",

                                    fontSize =
                                        14.sp,

                                    fontWeight =
                                        FontWeight.Medium,

                                    color =
                                        Charcoal,

                                    lineHeight =
                                        21.sp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        "Cancellations within 24 hours may not be eligible for a refund.",

                                    fontSize =
                                        13.sp,

                                    color =
                                        Gray,

                                    lineHeight =
                                        20.sp
                                )
                            }
                        }

                        // =================================================
                        // BOOK NOW — FINAL ACTION
                        // =================================================

                        Spacer(
                            modifier =
                                Modifier.height(32.dp)
                        )

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    20.dp
                                ),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        White
                                ),

                            border =
                                BorderStroke(
                                    1.dp,
                                    BorderGray
                                ),

                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation =
                                        2.dp
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            18.dp
                                        )
                            ) {

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier =
                                            Modifier
                                                .size(46.dp)
                                                .background(
                                                    color =
                                                        LightGreenBackground,
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
                                                Icons.Default.AccessTime,

                                            contentDescription =
                                                null,

                                            modifier =
                                                Modifier.size(
                                                    23.dp
                                                ),

                                            tint =
                                                ForestGreen
                                        )
                                    }

                                    Spacer(
                                        modifier =
                                            Modifier.width(
                                                12.dp
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
                                                "Ready to play?",

                                            fontSize =
                                                17.sp,

                                            fontWeight =
                                                FontWeight.ExtraBold,

                                            color =
                                                Charcoal
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(
                                                    3.dp
                                                )
                                        )

                                        Text(
                                            text =
                                                "Select your date and time slot",

                                            fontSize =
                                                11.sp,

                                            color =
                                                Gray
                                        )
                                    }

                                    Column(
                                        horizontalAlignment =
                                            Alignment.End
                                    ) {

                                        Text(
                                            text =
                                                "Starting from",

                                            fontSize =
                                                10.sp,

                                            color =
                                                SoftGray
                                        )

                                        Text(
                                            text =
                                                "₹${currentTurf.price.toInt()}",

                                            fontSize =
                                                18.sp,

                                            fontWeight =
                                                FontWeight.ExtraBold,

                                            color =
                                                DarkGreen
                                        )
                                    }
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            16.dp
                                        )
                                )

                                Button(
                                    onClick = {

                                        onBookNowClick(
                                            currentTurf.id
                                        )
                                    },

                                    modifier =
                                        Modifier.fillMaxWidth(),

                                    shape =
                                        RoundedCornerShape(
                                            14.dp
                                        ),

                                    colors =
                                        ButtonDefaults.buttonColors(
                                            containerColor =
                                                DarkGreen
                                        ),

                                    contentPadding =
                                        PaddingValues(
                                            vertical =
                                                15.dp
                                        )
                                ) {

                                    Text(
                                        text =
                                            "Book Now",

                                        fontSize =
                                            15.sp,

                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // =================================================
                        // BOTTOM SPACE
                        // =================================================

                        Spacer(
                            modifier =
                                Modifier.height(
                                    32.dp
                                )
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// AMENITY TILE
// ============================================================

@Composable
private fun AmenityTile(
    modifier: Modifier = Modifier,
    amenity: String
) {

    val icon =
        amenityIcon(
            amenity
        )

    Card(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal =
                            13.dp,

                        vertical =
                            13.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(38.dp)
                        .background(
                            color =
                                LightGreenBackground,

                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        amenity,

                    modifier =
                        Modifier.size(
                            20.dp
                        ),

                    tint =
                        ForestGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(
                        10.dp
                    )
            )

            Text(
                text =
                    amenity.trim(),

                modifier =
                    Modifier.weight(
                        1f
                    ),

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    Charcoal,

                maxLines =
                    2
            )
        }
    }
}

// ============================================================
// AMENITY ICON
// ============================================================

private fun amenityIcon(
    amenity: String
) = when {

    amenity.contains(
        "parking",
        ignoreCase = true
    ) ->
        Icons.Default.LocalParking

    amenity.contains(
        "water",
        ignoreCase = true
    ) ||
            amenity.contains(
                "drinking",
                ignoreCase = true
            ) ->
        Icons.Default.WaterDrop

    amenity.contains(
        "washroom",
        ignoreCase = true
    ) ||
            amenity.contains(
                "toilet",
                ignoreCase = true
            ) ||
            amenity.contains(
                "wc",
                ignoreCase = true
            ) ->
        Icons.Default.Wc

    amenity.contains(
        "changing",
        ignoreCase = true
    ) ||
            amenity.contains(
                "change",
                ignoreCase = true
            ) ->
        Icons.Default.MeetingRoom

    amenity.contains(
        "light",
        ignoreCase = true
    ) ||
            amenity.contains(
                "flood",
                ignoreCase = true
            ) ->
        Icons.Default.Lightbulb

    amenity.contains(
        "seat",
        ignoreCase = true
    ) ||
            amenity.contains(
                "sitting",
                ignoreCase = true
            ) ->
        Icons.Default.EventSeat

    amenity.contains(
        "wifi",
        ignoreCase = true
    ) ||
            amenity.contains(
                "internet",
                ignoreCase = true
            ) ->
        Icons.Default.Wifi

    amenity.contains(
        "security",
        ignoreCase = true
    ) ||
            amenity.contains(
                "guard",
                ignoreCase = true
            ) ->
        Icons.Default.Security

    else ->
        Icons.Default.CheckCircle
}

// ============================================================
// LOCATION CARD
// ============================================================

@Composable
private fun LocationCard(
    address: String?,
    location: String,
    city: String,
    onOpenMaps: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    )
        ) {

            Row(
                verticalAlignment =
                    Alignment.Top
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .background(
                                color =
                                    LightGreenBackground,

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
                            Icons.Default.LocationOn,

                        contentDescription =
                            "Location",

                        modifier =
                            Modifier.size(
                                23.dp
                            ),

                        tint =
                            ForestGreen
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            12.dp
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
                            "Turf Location",

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )

                    if (
                        !address.isNullOrBlank()
                    ) {

                        Text(
                            text =
                                address,

                            fontSize =
                                13.sp,

                            lineHeight =
                                19.sp,

                            color =
                                Gray
                        )
                    }

                    if (
                        location.isNotBlank() ||
                        city.isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    3.dp
                                )
                        )

                        Text(
                            text =
                                listOf(
                                    location,
                                    city
                                )
                                    .filter {
                                        it.isNotBlank()
                                    }
                                    .joinToString(
                                        ", "
                                    ),

                            fontSize =
                                12.sp,

                            color =
                                SoftGray
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            HorizontalDivider(
                color =
                    BorderGray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        13.dp
                    )
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick =
                                onOpenMaps
                        )
                        .background(
                            color =
                                DarkGreen,

                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )
                        )
                        .padding(
                            horizontal =
                                14.dp,

                            vertical =
                                12.dp
                        ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Directions,

                    contentDescription =
                        "Open Google Maps",

                    modifier =
                        Modifier.size(
                            20.dp
                        ),

                    tint =
                        White
                )

                Spacer(
                    modifier =
                        Modifier.width(
                            9.dp
                        )
                )

                Text(
                    text =
                        "Open in Google Maps",

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        White
                )

                Text(
                    text =
                        "›",

                    fontSize =
                        23.sp,

                    color =
                        White,

                    fontWeight =
                        FontWeight.Light
                )
            }
        }
    }
}

// ============================================================
// OPEN GOOGLE MAPS
// ============================================================

private fun openGoogleMaps(
    context: android.content.Context,
    query: String
) {

    try {

        val mapsIntent =
            Intent(
                Intent.ACTION_VIEW,

                Uri.parse(
                    "geo:0,0?q=${Uri.encode(query)}"
                )
            ).apply {

                setPackage(
                    "com.google.android.apps.maps"
                )
            }

        context.startActivity(
            mapsIntent
        )

    } catch (
        _: ActivityNotFoundException
    ) {

        val browserIntent =
            Intent(
                Intent.ACTION_VIEW,

                Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=${
                        Uri.encode(
                            query
                        )
                    }"
                )
            )

        context.startActivity(
            browserIntent
        )
    }
}

// ============================================================
// CUSTOMER REVIEW CARD
// ============================================================

@Composable
private fun ReviewCard(
    review: Review
) {

    val reviewerName =
        review.user
            ?.name
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "Customer"

    val reviewerInitial =
        reviewerName
            .trim()
            .firstOrNull()
            ?.uppercaseChar()
            ?.toString()
            ?: "C"

    val formattedDate =
        formatReviewDate(
            review.createdAt
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
                    White
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(42.dp)
                            .background(
                                color =
                                    LightGreenBackground,

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            reviewerInitial,

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            DarkGreen
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
                            reviewerName,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                3.dp
                            )
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        StarRating(
                            rating =
                                review.rating
                        )

                        if (
                            formattedDate.isNotBlank()
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    formattedDate,

                                fontSize =
                                    11.sp,

                                color =
                                    SoftGray
                            )
                        }
                    }
                }
            }

            if (
                !review.comment
                    .isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            13.dp
                        )
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                color =
                                    SoftGreen,

                                shape =
                                    RoundedCornerShape(
                                        12.dp
                                    )
                            )
                            .padding(
                                13.dp
                            )
                ) {

                    Text(
                        text =
                            review.comment
                                ?.trim()
                                ?: "",

                        fontSize =
                            13.sp,

                        lineHeight =
                            20.sp,

                        color =
                            Charcoal
                    )
                }

            } else {

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                Text(
                    text =
                        "Customer left a rating without a comment.",

                    fontSize =
                        12.sp,

                    color =
                        SoftGray
                )
            }
        }
    }
}

// ============================================================
// STAR RATING
// ============================================================

@Composable
private fun StarRating(
    rating: Int
) {

    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                2.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        repeat(5) { index ->

            Icon(
                imageVector =
                    Icons.Default.Star,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(
                        15.dp
                    ),

                tint =
                    if (
                        index < rating
                    ) {
                        LightGreen
                    } else {
                        BorderGray
                    }
            )
        }
    }
}

// ============================================================
// REVIEWS LOADING
// ============================================================

@Composable
private fun ReviewsLoadingCard() {

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
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            CircularProgressIndicator(
                modifier =
                    Modifier.size(
                        22.dp
                    ),

                color =
                    ForestGreen,

                strokeWidth =
                    2.5.dp
            )

            Spacer(
                modifier =
                    Modifier.width(
                        12.dp
                    )
            )

            Text(
                text =
                    "Loading customer reviews...",

                fontSize =
                    13.sp,

                color =
                    Gray
            )
        }
    }
}

// ============================================================
// EMPTY REVIEWS
// ============================================================

@Composable
private fun EmptyReviewsCard() {

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
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        20.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .background(
                            color =
                                LightGreenBackground,

                            shape =
                                CircleShape
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Star,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(
                            22.dp
                        ),

                    tint =
                        LightGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Text(
                text =
                    "No customer reviews yet",

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Charcoal
            )

            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )

            Text(
                text =
                    "Customer experiences will appear here.",

                fontSize =
                    12.sp,

                color =
                    Gray
            )
        }
    }
}

// ============================================================
// DATE FORMATTER
// ============================================================

private fun formatReviewDate(
    createdAt: String?
): String {

    if (
        createdAt.isNullOrBlank()
    ) {
        return ""
    }

    return try {

        val date =
            createdAt.substringBefore(
                "T"
            )

        val parts =
            date.split("-")

        if (
            parts.size == 3
        ) {

            val year =
                parts[0]

            val month =
                parts[1].toInt()

            val day =
                parts[2]

            val monthName =
                when (month) {

                    1 -> "Jan"
                    2 -> "Feb"
                    3 -> "Mar"
                    4 -> "Apr"
                    5 -> "May"
                    6 -> "Jun"
                    7 -> "Jul"
                    8 -> "Aug"
                    9 -> "Sep"
                    10 -> "Oct"
                    11 -> "Nov"
                    12 -> "Dec"

                    else -> ""
                }

            if (
                monthName.isNotBlank()
            ) {

                "$day $monthName $year"

            } else {

                date
            }

        } else {

            date
        }

    } catch (
        _: Exception
    ) {

        createdAt.substringBefore(
            "T"
        )
    }
}

// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun SectionTitle(
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

            if (
                icon != null
            ) {

                Spacer(
                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )
            }

            Text(
                text =
                    title,

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )
        }

        if (
            subtitle != null
        ) {

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
                    11.sp,

                color =
                    Gray
            )
        }
    }
}

// ============================================================
// INFO CARD
// ============================================================

@Composable
private fun InfoCard(
    content: @Composable () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                14.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        border =
            BorderStroke(
                1.dp,
                BorderGray
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    0.dp
            )
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        15.dp
                    )
        ) {

            content()
        }
    }
}
