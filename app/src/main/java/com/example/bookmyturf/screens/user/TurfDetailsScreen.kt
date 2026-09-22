package com.example.bookmyturf.screens.user

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.filled.Wifi

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.net.toUri

import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage

import com.example.bookmyturf.R
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.review.Review
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.ReviewRepository
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.ReviewViewModel
import com.example.bookmyturf.viewmodel.ReviewViewModelFactory
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory

import java.util.Locale


// ============================================================
// PREMIUM BOOKMYTURF COLORS
// ============================================================

private val Background = Color(0xFF020C09)
private val CardBackground = Color(0xFF071713)
private val SecondarySurface = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFB7E77A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFF9EAEA6)
private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)
private val StarColor = Color(0xFFFFC857)
private val ErrorRed = Color(0xFFFF6B6B)


// ============================================================
// TURF DETAILS SCREEN
// ============================================================

@Composable
fun TurfDetailsScreen(
    turfId: Int,
    onBackClick: () -> Unit,
    onBookNowClick: (Int) -> Unit
) {

    val context = LocalContext.current

    // ========================================================
    // TURF VIEW MODEL
    // ========================================================

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

    // ========================================================
    // REVIEW VIEW MODEL
    // ========================================================

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

    // ========================================================
    // SESSION
    // ========================================================

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }

    // ========================================================
    // STATES
    // ========================================================

    val turf by turfViewModel.turf.collectAsState()

    val isLoading by turfViewModel.isLoading.collectAsState()

    val error by turfViewModel.error.collectAsState()

    val reviewUiState by reviewViewModel.uiState.collectAsState()

    // ========================================================
    // LOAD TURF
    // ========================================================

    LaunchedEffect(turfId) {

        turfViewModel.loadTurf(
            turfId
        )
    }

    // ========================================================
    // LOAD REVIEWS
    // ========================================================

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

    // ========================================================
    // SCREEN
    // ========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        when {

            isLoading -> {

                PremiumLoadingState()
            }

            error != null -> {

                PremiumErrorState(
                    error = error
                        ?: "Something went wrong.",
                    onBackClick = onBackClick
                )
            }

            turf != null -> {

                val currentTurf = turf!!

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

                val locationText =
                    buildLocationText(
                        currentTurf
                    )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // ====================================================
                    // SCROLLABLE CONTENT
                    // ====================================================

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {

                        // =================================================
                        // HERO
                        // =================================================

                        PremiumTurfHero(
                            currentTurf = currentTurf,
                            onBackClick = onBackClick
                        )

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 18.dp
                                )
                        ) {

                            // =============================================
                            // ABOUT TURF
                            // =============================================

                            PremiumSectionTitle(
                                eyebrow = "THE VENUE",
                                title = "About this turf",
                                subtitle = "Everything you need to know"
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            PremiumAboutCard(
                                turfName = currentTurf.name,
                                description =
                                    currentTurf.description
                                        ?.trim()
                                        ?.ifEmpty {
                                            "No description available."
                                        }
                                        ?: "No description available."
                            )

                            // =============================================
                            // AMENITIES
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            PremiumSectionTitle(
                                eyebrow = "PREMIUM FACILITIES",
                                title = "Amenities",
                                subtitle = "Everything you need for a comfortable game"
                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            if (currentTurf.amenities.isEmpty()) {

                                PremiumInfoCard {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        PremiumIconBox {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp),
                                                tint = PrimaryGreen
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Amenities not listed",
                                                color = PrimaryText,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Please check with the venue before booking.",
                                                color = SecondaryText,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }

                            } else {

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    currentTurf.amenities
                                        .chunked(2)
                                        .forEachIndexed { rowIndex, rowAmenities ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(22.dp)
                                            ) {
                                                rowAmenities.forEach { amenity ->
                                                    PremiumAmenityItem(
                                                        modifier = Modifier.weight(1f),
                                                        amenity = amenity
                                                    )
                                                }

                                                if (rowAmenities.size == 1) {
                                                    Spacer(modifier = Modifier.weight(1f))
                                                }
                                            }

                                            if (rowIndex < (currentTurf.amenities.size + 1) / 2 - 1) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(vertical = 3.dp),
                                                    color = BorderColor.copy(alpha = 0.65f),
                                                    thickness = 1.dp
                                                )
                                            }
                                        }
                                }
                            }

                            // =============================================
                            // SPORTS AVAILABLE
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            PremiumSectionTitle(
                                eyebrow = "PLAY YOUR GAME",
                                title = "Sports Available",
                                subtitle = "Choose from the sports offered at this venue"
                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            if (currentTurf.sportsTypes.isEmpty()) {

                                PremiumInfoCard {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        PremiumIconBox {
                                            Icon(
                                                imageVector = Icons.Default.SportsSoccer,
                                                contentDescription = null,
                                                modifier = Modifier.size(21.dp),
                                                tint = LightGreen
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Sports not listed",
                                                color = PrimaryText,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Contact the venue for available sports.",
                                                color = SecondaryText,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }

                            } else {

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    currentTurf.sportsTypes
                                        .chunked(2)
                                        .forEachIndexed { rowIndex, rowSports ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(22.dp)
                                            ) {
                                                rowSports.forEach { sport ->
                                                    PremiumSportItem(
                                                        modifier = Modifier.weight(1f),
                                                        sport = sport
                                                    )
                                                }

                                                if (rowSports.size == 1) {
                                                    Spacer(modifier = Modifier.weight(1f))
                                                }
                                            }

                                            if (rowIndex < (currentTurf.sportsTypes.size + 1) / 2 - 1) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(vertical = 3.dp),
                                                    color = BorderColor.copy(alpha = 0.65f),
                                                    thickness = 1.dp
                                                )
                                            }
                                        }
                                }
                            }

                            // =============================================
                            // LOCATION
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            PremiumSectionTitle(
                                eyebrow = "FIND THE VENUE",
                                title = "Location",
                                subtitle = "Find this turf easily"
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            PremiumLocationCard(
                                address =
                                    currentTurf.address,
                                location =
                                    currentTurf.location,
                                city =
                                    currentTurf.city,
                                onDirectionsClick = {

                                    openGoogleMaps(
                                        context = context,
                                        query = locationText
                                    )
                                }
                            )

                            // =============================================
                            // REVIEWS
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            PremiumSectionTitle(
                                eyebrow = "PLAYER EXPERIENCES",
                                title = "Customer reviews",
                                subtitle =
                                    if (
                                        displayReviewCount > 0
                                    ) {
                                        "$displayReviewCount customer reviews"
                                    } else {
                                        "What customers say about this turf"
                                    }
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            PremiumRatingSummary(
                                rating = displayRating,
                                reviewCount = displayReviewCount,
                                isLoading =
                                    reviewUiState.isLoading
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            when {

                                reviewUiState.isLoading -> {

                                    PremiumReviewsLoadingCard()
                                }

                                reviewUiState.reviews.isNotEmpty() -> {

                                    Column(
                                        verticalArrangement =
                                            Arrangement.spacedBy(
                                                10.dp
                                            )
                                    ) {

                                        reviewUiState
                                            .reviews
                                            .forEach { review ->

                                                PremiumReviewCard(
                                                    review = review
                                                )
                                            }
                                    }
                                }

                                else -> {

                                    PremiumEmptyReviewsCard()
                                }
                            }

                            // =============================================
                            // CANCELLATION
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            PremiumSectionTitle(
                                eyebrow = "BOOK WITH CONFIDENCE",
                                title = "Cancellation policy",
                                subtitle = "Please review before booking"
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            PremiumCancellationCard()

                            // =============================================
                            // BOTTOM SPACE
                            // =============================================

                            Spacer(
                                modifier = Modifier.height(125.dp)
                            )
                        }
                    }

                    // ====================================================
                    // STICKY BOOKING BAR
                    // ====================================================

                    PremiumBookingBar(
                        price = currentTurf.price,
                        onBookNowClick = {
                            onBookNowClick(
                                currentTurf.id
                            )
                        }
                    )
                }
            }
        }
    }
}


// ============================================================
// HERO
// ============================================================

@Composable
private fun PremiumTurfHero(
    currentTurf: Turf,
    onBackClick: () -> Unit
) {

    val images = currentTurf.imageUrls

    val safeImages =
        if (images.isEmpty()) {
            listOf("")
        } else {
            images
        }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            safeImages.size
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(315.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
    ) {

        // ========================================================
        // IMAGE PAGER
        // ========================================================

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            val imageUrl = safeImages[page]

            if (
                imageUrl.isBlank()
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.bookmyturf_hero
                    ),
                    contentDescription =
                        currentTurf.name,
                    modifier =
                        Modifier.fillMaxSize(),
                    contentScale =
                        ContentScale.Crop
                )

            } else {

                AsyncImage(
                    model = imageUrl,
                    contentDescription =
                        currentTurf.name,
                    modifier =
                        Modifier.fillMaxSize(),
                    contentScale =
                        ContentScale.Crop,
                    placeholder =
                        painterResource(
                            id = R.drawable.bookmyturf_hero
                        ),
                    error =
                        painterResource(
                            id = R.drawable.bookmyturf_hero
                        )
                )
            }
        }

        // ========================================================
        // HERO GRADIENT
        // ========================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(
                                alpha = 0.48f
                            ),
                            Color.Transparent,
                            Background.copy(
                                alpha = 0.98f
                            )
                        )
                    )
                )
        )

        // ========================================================
        // BACK BUTTON
        // ========================================================

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    start = 12.dp,
                    top = 8.dp
                )
                .size(44.dp)
                .background(
                    Color.Black.copy(
                        alpha = 0.45f
                    ),
                    CircleShape
                )
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription =
                    "Back",
                tint =
                    PrimaryText
            )
        }

        // ========================================================
        // IMAGE COUNTER
        // ========================================================

        if (
            safeImages.size > 1
        ) {

            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .align(
                        Alignment.TopEnd
                    )
                    .padding(
                        top = 15.dp,
                        end = 15.dp
                    )
                    .background(
                        Color.Black.copy(
                            alpha = 0.50f
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
            ) {

                Text(
                    text =
                        "${pagerState.currentPage + 1}/${safeImages.size}",
                    color =
                        PrimaryText,
                    fontSize =
                        10.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        // ========================================================
        // PAGE INDICATORS
        // ========================================================

        if (
            safeImages.size > 1
        ) {

            Row(
                modifier = Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(
                        bottom = 91.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(5.dp)
            ) {

                safeImages.indices.forEach { index ->

                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(
                                if (
                                    index ==
                                    pagerState.currentPage
                                ) {
                                    20.dp
                                } else {
                                    5.dp
                                }
                            )
                            .clip(
                                CircleShape
                            )
                            .background(
                                if (
                                    index ==
                                    pagerState.currentPage
                                ) {
                                    BrightGreen
                                } else {
                                    Color.White.copy(
                                        alpha = 0.45f
                                    )
                                }
                            )
                    )
                }
            }
        }

        // ========================================================
        // HERO DETAILS
        // ========================================================

        Column(
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    bottom = 19.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .background(
                        PrimaryGreen.copy(
                            alpha = 0.18f
                        ),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 9.dp,
                        vertical = 5.dp
                    )
            ) {

                Text(
                    text = "TURF DETAILS",
                    color = BrightGreen,
                    fontSize = 8.sp,
                    fontWeight =
                        FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = currentTurf.name,
                color = PrimaryText,
                fontSize = 27.sp,
                lineHeight = 31.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                maxLines = 2,
                overflow =
                    TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier =
                        Modifier.size(16.dp),
                    tint =
                        LightGreen
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text =
                        "${currentTurf.location}, ${currentTurf.city}",
                    color =
                        SecondaryText,
                    fontSize =
                        11.sp,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )
            }
        }
    }
}


// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun PremiumSectionTitle(
    eyebrow: String,
    title: String,
    subtitle: String? = null
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = eyebrow,
            color = LightGreen,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = title,
            color = PrimaryText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )

        if (
            subtitle != null
        ) {

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                color = MutedText,
                fontSize = 10.sp
            )
        }
    }
}


// ============================================================
// PREMIUM ABOUT TURF
// ============================================================

@Composable
private fun PremiumAboutCard(
    turfName: String,
    description: String
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val shouldExpand =
        description.length > 220

    val visibleDescription =
        if (
            expanded ||
            !shouldExpand
        ) {
            description
        } else {
            description
                .take(220)
                .trimEnd()
                .plus("...")
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(24.dp)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0B2118),
                        CardBackground,
                        Color(0xFF06120E)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape =
                    RoundedCornerShape(24.dp)
            )
    ) {

        // Subtle decorative glow

        Box(
            modifier = Modifier
                .align(
                    Alignment.TopEnd
                )
                .size(115.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PrimaryGreen.copy(
                                alpha = 0.12f
                            ),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.padding(19.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(44.dp)
                        .clip(
                            RoundedCornerShape(
                                50.dp
                            )
                        )
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BrightGreen,
                                    PrimaryGreen
                                )
                            )
                        )
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "VENUE OVERVIEW",
                        color = LightGreen,
                        fontSize = 8.sp,
                        fontWeight =
                            FontWeight.Bold,
                        letterSpacing = 1.3.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = turfName,
                        color = PrimaryText,
                        fontSize = 17.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            HorizontalDivider(
                color = BorderColor
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = visibleDescription,
                color = SecondaryText,
                fontSize = 12.sp,
                lineHeight = 20.sp
            )

            if (
                shouldExpand
            ) {

                Spacer(
                    modifier = Modifier.height(13.dp)
                )

                Row(
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    },
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            if (expanded) {
                                "Show less"
                            } else {
                                "Read more"
                            },
                        color =
                            BrightGreen,
                        fontSize =
                            10.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text =
                            if (expanded) {
                                "↑"
                            } else {
                                "→"
                            },
                        color =
                            BrightGreen,
                        fontSize =
                            12.sp,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// ============================================================
// PREMIUM AMENITY TILE
// ============================================================

@Composable
private fun PremiumAmenityItem(
    modifier: Modifier,
    amenity: String
) {
    val icon = amenityIcon(amenity)

    Row(
        modifier = modifier
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SecondarySurface.copy(alpha = 0.75f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = amenity,
                modifier = Modifier.size(19.dp),
                tint = LightGreen
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = amenity.trim(),
                color = PrimaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Available",
                color = MutedText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PremiumSportItem(
    modifier: Modifier,
    sport: String
) {
    val icon = Icons.Default.SportsSoccer
    val displayName = sport.trim().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    Row(
        modifier = modifier
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(PrimaryGreen.copy(alpha = 0.13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = sport,
                modifier = Modifier.size(22.dp),
                tint = BrightGreen
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                color = PrimaryText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Available for booking",
                color = MutedText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
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
private fun PremiumLocationCard(
    address: String?,
    location: String,
    city: String,
    onDirectionsClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(23.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    CardBackground
            ),
        border =
            BorderStroke(
                1.dp,
                BorderColor
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.Top
            ) {

                PremiumIconBox {

                    Icon(
                        imageVector =
                            Icons.Default.LocationOn,
                        contentDescription =
                            "Location",
                        modifier =
                            Modifier.size(21.dp),
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
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Turf Location",
                        color =
                            PrimaryText,
                        fontSize =
                            14.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    if (
                        !address.isNullOrBlank()
                    ) {

                        Text(
                            text =
                                address,
                            color =
                                SecondaryText,
                            fontSize =
                                11.sp,
                            lineHeight =
                                18.sp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
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
                        color =
                            MutedText,
                        fontSize =
                            10.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            HorizontalDivider(
                color = BorderColor
            )

            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        PrimaryGreen
                    )
                    .clickable(
                        onClick =
                            onDirectionsClick
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 11.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Directions,
                    contentDescription =
                        "Get directions",
                    modifier =
                        Modifier.size(18.dp),
                    tint =
                        Background
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Get Directions",
                    modifier =
                        Modifier.weight(1f),
                    color =
                        Background,
                    fontSize =
                        12.sp,
                    fontWeight =
                        FontWeight.ExtraBold
                )

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier =
                        Modifier.size(17.dp),
                    tint =
                        Background
                )
            }
        }
    }
}


// ============================================================
// RATING SUMMARY
// ============================================================

@Composable
private fun PremiumRatingSummary(
    rating: Double,
    reviewCount: Int,
    isLoading: Boolean
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    CardBackground
            ),
        border =
            BorderStroke(
                1.dp,
                BorderColor
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        SecondarySurface
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
                        Modifier.size(25.dp),
                    tint =
                        StarColor
                )
            }

            Spacer(
                modifier =
                    Modifier.width(13.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        if (
                            reviewCount > 0
                        ) {
                            String.format(
                                Locale.getDefault(),
                                "%.1f",
                                rating
                            )
                        } else {
                            "No rating"
                        },
                    color =
                        PrimaryText,
                    fontSize =
                        20.sp,
                    fontWeight =
                        FontWeight.ExtraBold
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                Text(
                    text =
                        when {

                            isLoading ->
                                "Loading reviews..."

                            reviewCount == 0 ->
                                "Be the first to review"

                            reviewCount == 1 ->
                                "1 customer review"

                            else ->
                                "$reviewCount customer reviews"
                        },
                    color =
                        SecondaryText,
                    fontSize =
                        10.sp
                )
            }

            if (
                reviewCount > 0 &&
                !isLoading
            ) {

                StarRating(
                    rating =
                        rating.toInt()
                )
            }
        }
    }
}


// ============================================================
// REVIEW CARD
// ============================================================

@Composable
private fun PremiumReviewCard(
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
            RoundedCornerShape(19.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    CardBackground
            ),
        border =
            BorderStroke(
                1.dp,
                BorderColor
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            SecondarySurface
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            reviewerInitial,
                        color =
                            LightGreen,
                        fontSize =
                            15.sp,
                        fontWeight =
                            FontWeight.ExtraBold
                    )
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
                            reviewerName,
                        color =
                            PrimaryText,
                        fontSize =
                            13.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
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
                                    Modifier.width(8.dp)
                            )

                            Text(
                                text =
                                    formattedDate,
                                color =
                                    MutedText,
                                fontSize =
                                    9.sp
                            )
                        }
                    }
                }
            }

            if (
                !review.comment.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            SecondarySurface
                        )
                        .padding(12.dp)
                ) {

                    Text(
                        text =
                            review.comment.trim(),
                        color =
                            SecondaryText,
                        fontSize =
                            11.sp,
                        lineHeight =
                            18.sp
                    )
                }

            } else {

                Spacer(
                    modifier =
                        Modifier.height(9.dp)
                )

                Text(
                    text =
                        "Customer left a rating without a comment.",
                    color =
                        MutedText,
                    fontSize =
                        10.sp
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
            Arrangement.spacedBy(2.dp),
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
                    Modifier.size(13.dp),
                tint =
                    if (
                        index < rating
                    ) {
                        StarColor
                    } else {
                        BorderColor
                    }
            )
        }
    }
}


// ============================================================
// REVIEWS LOADING
// ============================================================

@Composable
private fun PremiumReviewsLoadingCard() {

    PremiumInfoCard {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            CircularProgressIndicator(
                modifier =
                    Modifier.size(22.dp),
                color =
                    PrimaryGreen,
                strokeWidth =
                    2.5.dp
            )

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Text(
                text =
                    "Loading customer reviews...",
                color =
                    SecondaryText,
                fontSize =
                    11.sp
            )
        }
    }
}


// ============================================================
// EMPTY REVIEWS
// ============================================================

@Composable
private fun PremiumEmptyReviewsCard() {

    PremiumInfoCard {

        Column(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        SecondarySurface
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
                        Modifier.size(23.dp),
                    tint =
                        StarColor
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "No customer reviews yet",
                color =
                    PrimaryText,
                fontSize =
                    13.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Customer experiences will appear here.",
                color =
                    MutedText,
                fontSize =
                    10.sp
            )
        }
    }
}


// ============================================================
// CANCELLATION CARD
// ============================================================

@Composable
private fun PremiumCancellationCard() {

    PremiumInfoCard {

        Column {

            Row(
                verticalAlignment =
                    Alignment.Top
            ) {

                PremiumIconBox {

                    Icon(
                        imageVector =
                            Icons.Default.AccessTime,
                        contentDescription =
                            null,
                        modifier =
                            Modifier.size(20.dp),
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
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Free cancellation up to 24 hours",
                        color =
                            PrimaryText,
                        fontSize =
                            13.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Cancellations within 24 hours may not be eligible for a refund.",
                        color =
                            SecondaryText,
                        fontSize =
                            11.sp,
                        lineHeight =
                            18.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(11.dp)
                        )
                        .background(
                            SecondarySurface
                        )
                        .padding(11.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CheckCircle,
                    contentDescription =
                        null,
                    modifier =
                        Modifier.size(15.dp),
                    tint =
                        PrimaryGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        "Please review the cancellation terms before confirming your booking.",
                    color =
                        SecondaryText,
                    fontSize =
                        9.sp,
                    lineHeight =
                        14.sp
                )
            }
        }
    }
}


// ============================================================
// PREMIUM BOOKING BAR
// ============================================================

@Composable
private fun PremiumBookingBar(
    price: Double,
    onBookNowClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 20.dp,
                top = 16.dp,
                end = 20.dp,
                bottom = 24.dp
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(24.dp)
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0A1E17),
                            CardBackground,
                            Color(0xFF0B2118)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape =
                        RoundedCornerShape(24.dp)
                )
        ) {

            // Subtle CTA glow

            Box(
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                PrimaryGreen.copy(
                                    alpha = 0.10f
                                ),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // Price section

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "STARTING FROM",
                        color =
                            MutedText,
                        fontSize =
                            7.sp,
                        fontWeight =
                            FontWeight.Bold,
                        letterSpacing =
                            1.2.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Row(
                        verticalAlignment =
                            Alignment.Bottom
                    ) {

                        Text(
                            text =
                                "₹${price.toInt()}",
                            color =
                                PrimaryText,
                            fontSize =
                                21.sp,
                            fontWeight =
                                FontWeight.ExtraBold
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "/ slot",
                            color =
                                SecondaryText,
                            fontSize =
                                9.sp
                        )
                    }
                }

                // Booking button

                Button(
                    onClick =
                        onBookNowClick,
                    modifier =
                        Modifier.height(49.dp),
                    shape =
                        RoundedCornerShape(15.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                PrimaryGreen,
                            contentColor =
                                Background
                        ),
                    contentPadding =
                        PaddingValues(
                            horizontal = 17.dp
                        )
                ) {

                    Text(
                        text =
                            "BOOK NOW",
                        fontSize =
                            10.sp,
                        fontWeight =
                            FontWeight.ExtraBold,
                        letterSpacing =
                            0.7.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription =
                            null,
                        modifier =
                            Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}


// ============================================================
// PREMIUM ICON BOX
// ============================================================

@Composable
private fun PremiumIconBox(
    content: @Composable () -> Unit
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(13.dp)
            )
            .background(
                SecondarySurface
            ),
        contentAlignment =
            Alignment.Center
    ) {

        content()
    }
}


// ============================================================
// INFO CARD
// ============================================================

@Composable
private fun PremiumInfoCard(
    content: @Composable () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(19.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    CardBackground
            ),
        border =
            BorderStroke(
                1.dp,
                BorderColor
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {

            content()
        }
    }
}


// ============================================================
// BUILD LOCATION
// ============================================================

private fun buildLocationText(
    turf: Turf
): String {

    return listOf(
        turf.name,
        turf.address,
        turf.location,
        turf.city
    )
        .filter {
            !it.isNullOrBlank()
        }
        .joinToString(", ")
}


// ============================================================
// LOADING STATE
// ============================================================

@Composable
private fun PremiumLoadingState() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Background
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    SecondarySurface
                ),
            contentAlignment =
                Alignment.Center
        ) {

            CircularProgressIndicator(
                modifier =
                    Modifier.size(30.dp),
                color =
                    PrimaryGreen,
                strokeWidth =
                    3.dp
            )
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            text =
                "Loading turf details...",
            color =
                PrimaryText,
            fontSize =
                14.sp,
            fontWeight =
                FontWeight.Medium
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text =
                "Please wait a moment",
            color =
                SecondaryText,
            fontSize =
                11.sp
        )
    }
}


// ============================================================
// ERROR STATE
// ============================================================

@Composable
private fun PremiumErrorState(
    error: String,
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Background
            )
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    ErrorRed.copy(
                        alpha = 0.12f
                    )
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.Cancel,
                contentDescription =
                    null,
                modifier =
                    Modifier.size(28.dp),
                tint =
                    ErrorRed
            )
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            text =
                "Unable to load turf",
            color =
                PrimaryText,
            fontSize =
                19.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                error,
            color =
                SecondaryText,
            fontSize =
                12.sp
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            onClick =
                onBackClick,
            shape =
                RoundedCornerShape(12.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        PrimaryGreen,
                    contentColor =
                        Background
                )
        ) {

            Text(
                text =
                    "GO BACK",
                fontSize =
                    11.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// ============================================================
// FORMAT REVIEW DATE
// ============================================================

private fun formatReviewDate(
    createdAt: String?
): String {

    val rawDate =
        createdAt
            ?.takeIf {
                it.isNotBlank()
            }
            ?: return ""

    return try {

        val date =
            rawDate.substringBefore(
                "T"
            )

        val parts =
            date.split("-")

        if (
            parts.size != 3
        ) {
            return date
        }

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

    } catch (
        _: Exception
    ) {

        rawDate.substringBefore(
            "T"
        )
    }
}


// ============================================================
// GOOGLE MAPS
// ============================================================

private fun openGoogleMaps(
    context: android.content.Context,
    query: String
) {

    val encodedQuery =
        android.net.Uri.encode(
            query
        )

    try {

        val mapsIntent =
            Intent(
                Intent.ACTION_VIEW,
                "geo:0,0?q=$encodedQuery".toUri()
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
                "https://www.google.com/maps/search/?api=1&query=$encodedQuery"
                    .toUri()
            )

        try {

            context.startActivity(
                browserIntent
            )

        } catch (
            _: ActivityNotFoundException
        ) {
            // No Maps or browser application available.
        }
    }
}
