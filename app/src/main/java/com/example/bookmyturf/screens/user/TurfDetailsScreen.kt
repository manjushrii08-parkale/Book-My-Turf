package com.example.bookmyturf.screens.user
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.repository.TurfRepository
import com.example.bookmyturf.viewmodel.TurfDetailsViewModel
import com.example.bookmyturf.viewmodel.TurfDetailsViewModelFactory

// ============================================================
// SAME THEME AS USER HOME SCREEN
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

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
    // VIEW MODEL
    // =========================================================

    val repository = remember {
        TurfRepository()
    }

    val factory = remember {
        TurfDetailsViewModelFactory(
            repository = repository
        )
    }

    val viewModel: TurfDetailsViewModel = viewModel(
        factory = factory
    )

    // =========================================================
    // STATE
    // =========================================================

    val turf by viewModel.turf.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val error by viewModel.error.collectAsState()

    // =========================================================
    // LOAD TURF
    // =========================================================

    LaunchedEffect(turfId) {
        viewModel.loadTurf(turfId)
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

            // =================================================
            // LOADING
            // =================================================

            isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator(
                        color = ForestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading turf details...",
                        fontSize = 13.sp,
                        color = Gray
                    )
                }
            }

            // =================================================
            // ERROR
            // =================================================

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

                    Text(
                        text = "Unable to load turf",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            error ?: "Something went wrong.",
                        fontSize = 13.sp,
                        color = Gray
                    )
                }
            }

            // =================================================
            // TURF DATA
            // =================================================

            turf != null -> {

                val currentTurf = turf!!

                Scaffold(

                    containerColor = OffWhite,

                    // =================================================
                    // FIXED TOP APP BAR
                    // =================================================

                    topBar = {

                        TopAppBar(

                            title = {

                                Text(
                                    text =
                                        currentTurf.name,
                                    fontSize = 18.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color = Charcoal
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
                                        tint = Charcoal
                                    )
                                }
                            },

                            colors =
                                TopAppBarDefaults.topAppBarColors(
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

                    // =================================================
                    // SCROLLABLE CONTENT
                    // =================================================

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(
                                horizontal = 18.dp,
                                vertical = 18.dp
                            )
                    ) {

                        // =================================================
                        // TURF NAME
                        // =================================================

                        Text(
                            text =
                                currentTurf.name,
                            fontSize = 27.sp,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color = Charcoal
                        )

                        Spacer(
                            modifier =
                                Modifier.height(7.dp)
                        )

                        // =================================================
                        // LOCATION
                        // =================================================

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocationOn,
                                contentDescription =
                                    "Location",
                                modifier =
                                    Modifier.size(18.dp),
                                tint =
                                    ForestGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(5.dp)
                            )

                            Text(
                                text =
                                    "${currentTurf.location}, ${currentTurf.city}",
                                fontSize = 13.sp,
                                color = Gray
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        // =================================================
                        // RATING
                        // =================================================

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Star,
                                contentDescription =
                                    "Rating",
                                modifier =
                                    Modifier.size(18.dp),
                                tint =
                                    LightGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                text =
                                    "${currentTurf.rating}",
                                fontSize = 14.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    Charcoal
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                text =
                                    "(${currentTurf.reviewCount} reviews)",
                                fontSize = 12.sp,
                                color = Gray
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(20.dp)
                        )

                        // =================================================
                        // BOOKING CARD
                        // =================================================

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(18.dp),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        White
                                ),
                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp)
                            ) {

                                // =========================================
                                // BOOKING HEADER
                                // =========================================

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier =
                                            Modifier
                                                .size(46.dp)
                                                .background(
                                                    LightGreen.copy(
                                                        alpha = 0.14f
                                                    ),
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
                                                Modifier.size(23.dp),
                                            tint =
                                                ForestGreen
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
                                                "Book this turf",
                                            fontSize = 16.sp,
                                            fontWeight =
                                                FontWeight.Bold,
                                            color =
                                                Charcoal
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(3.dp)
                                        )

                                        Text(
                                            text =
                                                "Select your date and time slot",
                                            fontSize = 11.sp,
                                            color =
                                                Gray
                                        )
                                    }

                                    Text(
                                        text =
                                            "₹${currentTurf.price.toInt()}",
                                        fontSize = 17.sp,
                                        fontWeight =
                                            FontWeight.ExtraBold,
                                        color =
                                            DarkGreen
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(16.dp)
                                )

                                // =========================================
                                // BOOK NOW
                                // =========================================

                                Button(
                                    onClick = {

                                        onBookNowClick(
                                            currentTurf.id
                                        )
                                    },

                                    modifier =
                                        Modifier.fillMaxWidth(),

                                    shape =
                                        RoundedCornerShape(13.dp),

                                    colors =
                                        ButtonDefaults.buttonColors(
                                            containerColor =
                                                DarkGreen
                                        ),

                                    contentPadding =
                                        PaddingValues(
                                            vertical = 14.dp
                                        )
                                ) {

                                    Text(
                                        text = "Book Now",
                                        fontSize = 15.sp,
                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )

                        HorizontalDivider(
                            color =
                                Color(0xFFE5E5E0)
                        )

                        Spacer(
                            modifier =
                                Modifier.height(24.dp)
                        )

                        // =================================================
                        // ABOUT TURF
                        // =================================================

                        SectionTitle(
                            title = "About Turf",
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
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Gray
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
                                        Modifier.size(19.dp),
                                    tint =
                                        ForestGreen
                                )
                            },
                            title = "Sports",
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
                                        .joinToString(" • "),
                                fontSize = 14.sp,
                                color =
                                    Charcoal,
                                lineHeight = 22.sp
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(24.dp)
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
                                        Modifier.size(19.dp),
                                    tint =
                                        ForestGreen
                                )
                            },
                            title = "Amenities",
                            subtitle =
                                "Facilities available"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        InfoCard {

                            Text(
                                text =
                                    currentTurf.amenities
                                        .joinToString(" • "),
                                fontSize = 14.sp,
                                color =
                                    Charcoal,
                                lineHeight = 22.sp
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(24.dp)
                        )

                        // =================================================
                        // CANCELLATION POLICY
                        // =================================================

                        SectionTitle(
                            icon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.Cancel,
                                    contentDescription =
                                        null,
                                    modifier =
                                        Modifier.size(19.dp),
                                    tint =
                                        ForestGreen
                                )
                            },
                            title = "Cancellation Policy",
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
                                    fontSize = 14.sp,
                                    fontWeight =
                                        FontWeight.Medium,
                                    color =
                                        Charcoal,
                                    lineHeight = 21.sp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        "Cancellations within 24 hours may not be eligible for a refund.",
                                    fontSize = 13.sp,
                                    color =
                                        Gray,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        // =================================================
                        // ADDRESS
                        // =================================================

                        currentTurf.address?.let { address ->

                            Spacer(
                                modifier =
                                    Modifier.height(24.dp)
                            )

                            SectionTitle(
                                icon = {

                                    Icon(
                                        imageVector =
                                            Icons.Default.LocationOn,
                                        contentDescription =
                                            null,
                                        modifier =
                                            Modifier.size(19.dp),
                                        tint =
                                            ForestGreen
                                    )
                                },
                                title = "Address"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            InfoCard {

                                Text(
                                    text = address,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp,
                                    color =
                                        Charcoal
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(40.dp)
                        )
                    }
                }
            }
        }
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

            if (icon != null) {

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                color = Charcoal
            )
        }

        if (subtitle != null) {

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Gray
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
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
        ) {

            content()
        }
    }
}

