package com.example.bookmyturf.screens.admin.turf
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminTurfRepository
import com.example.bookmyturf.viewmodel.AdminTurfViewModel
import androidx.compose.foundation.layout.Row

// =============================================================
// PREMIUM ADMIN COLORS
// Same visual system as Subscription + Dashboard
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
// ADMIN TURFS SCREEN
// =============================================================

@Composable
fun AdminTurfsScreen(
    token: String,
    onAddTurf: () -> Unit
) {

    // =====================================================
    // REPOSITORY
    // =====================================================

    val repository = remember {
        AdminTurfRepository(
            RetrofitClient.api
        )
    }

    // =====================================================
    // FACTORY
    // =====================================================

    val factory = remember {
        AdminTurfViewModelFactory(
            repository
        )
    }

    // =====================================================
    // VIEWMODEL
    // =====================================================

    val viewModel: AdminTurfViewModel = viewModel(
        factory = factory
    )

    // =====================================================
    // STATE
    // =====================================================

    val turfs by
    viewModel.turfs.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()

    // =====================================================
    // LOAD TURFS
    // =====================================================

    LaunchedEffect(token) {
        viewModel.loadTurfs(token)
    }

    // =====================================================
    // SCREEN
    // =====================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
    ) {

        // =================================================
        // HEADER
        // =================================================

        Text(
            text = "Turf Management",
            color = PrimaryText,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Manage your turf grounds and availability.",
            color = SecondaryText,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =================================================
        // ADD TURF
        // =================================================

        Button(
            onClick = onAddTurf,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Color(0xFF061008)
            )
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(19.dp)
            )

            Text(
                text = "Add Turf",
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =================================================
        // LOADING
        // =================================================

        if (isLoading) {

            TurfLoadingState()
        }

        // =================================================
        // ERROR
        // =================================================

        if (!error.isNullOrBlank()) {

            TurfErrorState(
                message = error ?: "",
                onRetry = {
                    viewModel.clearError()
                    viewModel.loadTurfs(token)
                }
            )
        }

        // =================================================
        // EMPTY
        // =================================================

        if (
            !isLoading &&
            error.isNullOrBlank() &&
            turfs.isEmpty()
        ) {

            TurfEmptyState(
                onAddTurf = onAddTurf
            )
        }

        // =================================================
        // TURF LIST
        // =================================================

        if (
            !isLoading &&
            error.isNullOrBlank() &&
            turfs.isNotEmpty()
        ) {

            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(turfs) { turf ->

                    TurfCard(
                        name = turf.name,
                        location = turf.location,
                        city = turf.city,
                        price = turf.price,
                        status = turf.status,
                        slotsCount = turf.slotsCount ?: 0
                    )
                }
            }
        }
    }
}


// =============================================================
// TURF CARD
// =============================================================

@Composable
private fun TurfCard(
    name: String,
    location: String,
    city: String,
    price: Any?,
    status: String,
    slotsCount: Int
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
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(46.dp)
                            .background(
                                SurfaceElevated,
                                RoundedCornerShape(13.dp)
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.SportsSoccer,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(23.dp),

                        tint =
                            PrimaryGreen
                    )
                }

                Spacer(
                    modifier =
                        Modifier.size(12.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = name,
                        color = PrimaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text = city,
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }

                TurfStatusBadge(
                    status = status
                )
            }

            HorizontalDivider(
                color =
                    Border
            )

            // =================================================
            // LOCATION
            // =================================================

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {

                Text(
                    text = "LOCATION",
                    color = PrimaryGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.9.sp
                )

                Text(
                    text = location,
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }

            // =================================================
            // DETAILS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                TurfInfoItem(
                    modifier =
                        Modifier.weight(1f),

                    title = "PRICE",

                    value =
                        "₹$price / slot"
                )

                TurfInfoItem(
                    modifier =
                        Modifier.weight(1f),

                    title = "SLOTS",

                    value =
                        slotsCount.toString()
                )
            }

            // =================================================
            // FOOTER
            // =================================================

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp),

                color =
                    SurfaceElevated
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp,
                                vertical = 10.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "Turf availability",

                        color =
                            SecondaryText,

                        fontSize =
                            11.sp,

                        modifier =
                            Modifier.weight(1f)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.ChevronRight,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(18.dp),

                        tint =
                            MutedText
                    )
                }
            }
        }
    }
}


// =============================================================
// TURF STATUS BADGE
// =============================================================

@Composable
private fun TurfStatusBadge(
    status: String
) {

    val normalizedStatus =
        status
            .uppercase()
            .trim()

    val isActive =
        normalizedStatus == "ACTIVE"

    Surface(
        shape =
            RoundedCornerShape(50.dp),

        color =
            if (isActive) {
                PrimaryGreen.copy(
                    alpha = 0.10f
                )
            } else {
                SurfaceElevated
            }
    ) {

        Row(
            modifier =
                Modifier.padding(
                    horizontal = 9.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(6.dp)
                        .background(
                            color =
                                if (isActive) {
                                    PrimaryGreen
                                } else {
                                    MutedText
                                },

                            shape =
                                RoundedCornerShape(50.dp)
                        )
            )

            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )

            Text(
                text =
                    normalizedStatus,

                color =
                    if (isActive) {
                        LightGreen
                    } else {
                        SecondaryText
                    },

                fontSize =
                    9.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =============================================================
// TURF INFO ITEM
// =============================================================

@Composable
private fun TurfInfoItem(
    modifier: Modifier,
    title: String,
    value: String
) {

    Surface(
        modifier = modifier,

        shape =
            RoundedCornerShape(12.dp),

        color =
            SurfaceElevated
    ) {

        Column(
            modifier =
                Modifier.padding(11.dp),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text =
                    title,

                color =
                    MutedText,

                fontSize =
                    9.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    0.8.sp
            )

            Text(
                text =
                    value,

                color =
                    PrimaryText,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


// =============================================================
// LOADING STATE
// =============================================================

@Composable
private fun TurfLoadingState() {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(60.dp),

            shape =
                RoundedCornerShape(18.dp),

            color =
                SurfaceElevated
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(27.dp),

                    color =
                        PrimaryGreen,

                    strokeWidth =
                        2.5.dp
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(13.dp)
        )

        Text(
            text =
                "Loading turfs...",

            color =
                SecondaryText,

            fontSize =
                12.sp
        )
    }
}


// =============================================================
// ERROR STATE
// =============================================================

@Composable
private fun TurfErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 30.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(58.dp),

            shape =
                RoundedCornerShape(17.dp),

            color =
                ErrorRed.copy(
                    alpha = 0.08f
                )
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Refresh,

                    contentDescription =
                        null,

                    tint =
                        ErrorRed,

                    modifier =
                        Modifier.size(25.dp)
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(13.dp)
        )

        Text(
            text =
                "Unable to load turfs",

            color =
                PrimaryText,

            fontSize =
                15.sp,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                message,

            color =
                SecondaryText,

            fontSize =
                11.sp
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        OutlinedButton(
            onClick = onRetry,

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
                    Icons.Default.Refresh,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(16.dp),

                tint =
                    PrimaryText
            )

            Spacer(
                modifier =
                    Modifier.width(7.dp)
            )

            Text(
                text =
                    "Retry",

                color =
                    PrimaryText,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun TurfEmptyState(
    onAddTurf: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(70.dp),

            shape =
                RoundedCornerShape(20.dp),

            color =
                SurfaceElevated
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(31.dp),

                    tint =
                        PrimaryGreen
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            text =
                "No Turfs Yet",

            color =
                PrimaryText,

            style =
                MaterialTheme.typography.titleLarge,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "Add your first turf to get started.",

            color =
                SecondaryText,

            fontSize =
                12.sp
        )

        Spacer(
            modifier =
                Modifier.height(17.dp)
        )

        Button(
            onClick = onAddTurf,

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        PrimaryGreen,

                    contentColor =
                        Color(0xFF061008)
                )
        ) {

            Icon(
                imageVector =
                    Icons.Default.Add,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(18.dp)
            )

            Spacer(
                modifier =
                    Modifier.width(7.dp)
            )

            Text(
                text =
                    "Add Turf",

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}