package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmyturf.data.model.turf.Turf

// =============================================================
// COLORS
// =============================================================

private val TurfGreen = Color(0xFF14532D)
private val TurfGreenLight = Color(0xFFE8F5E9)
private val TurfGray = Color(0xFF64748B)
private val TurfBackground = Color(0xFFF8FAFC)
private val TurfRed = Color(0xFFDC2626)
private val TurfDark = Color(0xFF0F172A)

// =============================================================
// ADMIN TURFS CONTENT
// =============================================================

@Composable
fun AdminTurfsContent(
    modifier: Modifier = Modifier,
    turfs: List<Turf>,
    isLoading: Boolean,
    error: String?,
    onAddTurf: () -> Unit,
    onEditTurf: (Turf) -> Unit,
    onManageSlots: (Turf) -> Unit,
    onDeleteTurf: (Turf) -> Unit,
    onRetry: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TurfBackground)
            .padding(horizontal = 16.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "My Turfs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TurfDark
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text =
                        if (turfs.isEmpty()) {
                            "Manage your turf grounds"
                        } else {
                            "${turfs.size} turf${
                                if (turfs.size == 1) {
                                    ""
                                } else {
                                    "s"
                                }
                            } available"
                        },
                    color = TurfGray,
                    fontSize = 13.sp
                )
            }


            Button(
                onClick = onAddTurf,
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Turf"
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Add Turf"
                )
            }
        }


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                CircularProgressIndicator(
                    color = TurfGreen
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Loading your turfs...",
                    color = TurfGray,
                    fontSize = 14.sp
                )
            }

            return@Column
        }


        // =====================================================
        // ERROR
        // =====================================================

        if (!error.isNullOrBlank()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Unable to load turfs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TurfDark
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = error,
                    color = TurfGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Retry"
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // EMPTY STATE
        // =====================================================

        if (turfs.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Surface(
                    modifier = Modifier.size(82.dp),
                    shape = CircleShape,
                    color = TurfGreenLight
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = null,
                            modifier = Modifier.size(42.dp),
                            tint = TurfGreen
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                Text(
                    text = "No Turfs Added",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TurfDark
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text =
                        "Add your first turf to start managing bookings.",
                    color = TurfGray,
                    fontSize = 14.sp
                )


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                Button(
                    onClick = onAddTurf,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Add New Turf"
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // TURF LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(
                bottom = 30.dp
            )
        ) {

            items(
                items = turfs,
                key = { it.id }
            ) { turf ->
                TurfAdminCard(
                    turf = turf,

                    onEdit = {

                        onEditTurf(
                            turf
                        )
                    },

                    onManageSlots = {

                        onManageSlots(
                            turf
                        )
                    },

                    onDelete = {

                        onDeleteTurf(
                            turf
                        )
                    }
                )
            }
        }
    }

}

// =============================================================
// TURF ADMIN CARD
// =============================================================

@Composable
private fun TurfAdminCard(
    turf: Turf,
    onEdit: () -> Unit,
    onManageSlots: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // =================================================
            // IMAGE GALLERY
            // =================================================

            TurfImageGallery(
                imageUrls = turf.imageUrls
            )


            // =================================================
            // INFORMATION
            // =================================================

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // =================================================
                // NAME + ACTIONS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = turf.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TurfDark
                        )


                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )


                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = TurfGreen
                            )


                            Spacer(
                                modifier = Modifier.width(4.dp)
                            )


                            Text(
                                text = turf.city,
                                color = TurfGray,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                        }
                    }


                    IconButton(
                        onClick = onEdit
                    ) {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Turf",
                            tint = TurfGreen
                        )
                    }


                    IconButton(
                        onClick = onDelete
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Turf",
                            tint = TurfRed
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                // =================================================
                // LOCATION
                // =================================================

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC)
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(19.dp),
                            tint = TurfGreen
                        )


                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )


                        Column {

                            Text(
                                text = "Location",
                                color = TurfGray,
                                fontSize = 11.sp
                            )


                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )


                            Text(
                                text = turf.location,
                                color = TurfDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }


                Spacer(
                    modifier = Modifier.height(10.dp)
                )


                // =================================================
                // PRICE + TIME
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    TurfInfoBox(
                        modifier = Modifier.weight(1f),
                        title = "Price / Slot",
                        value =
                            "₹${
                                formatPrice(
                                    turf.price
                                )
                            }",
                        icon = Icons.Default.Star
                    )


                    TurfInfoBox(
                        modifier = Modifier.weight(1f),
                        title = "Opening Hours",

                        // Laravel format:
                        // h:i A
                        // Example:
                        // 09:00 AM - 10:00 PM

                        value =
                            "${formatTurfTime(turf.openingTime)} - " +
                                    formatTurfTime(turf.closingTime),

                        icon = Icons.Default.AccessTime
                    )
                }


                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                // =================================================
                // STATUS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Turf Status",
                        color = TurfGray,
                        fontSize = 13.sp
                    )


                    Spacer(
                        modifier = Modifier.weight(1f)
                    )


                    TurfStatusBadge(
                        status = turf.status
                    )
                }
                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Button(
                    onClick = {
                        onManageSlots()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Manage Slots"
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Text(
                        text = "Manage Slots",
                        fontWeight = FontWeight.SemiBold
                    )
                }


                // =================================================
                // DESCRIPTION
                // =================================================

                if (
                    !turf.description.isNullOrBlank()
                ) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )


                    Text(
                        text = turf.description,
                        color = TurfGray,
                        fontSize = 13.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }

}

// =============================================================
// IMAGE GALLERY
// =============================================================

@Composable
private fun TurfImageGallery(
    imageUrls: List<String>
) {
    val pagerState =
        rememberPagerState(
            initialPage = 0,
            pageCount = {

                if (
                    imageUrls.isEmpty()
                ) {
                    1
                } else {
                    imageUrls.size
                }
            }
        )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.15f)
            .clip(
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp
                )
            )
    ) {

        if (imageUrls.isEmpty()) {

            // =================================================
            // NO IMAGE
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        TurfGreenLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Image,
                        contentDescription = null,
                        modifier =
                            Modifier.size(48.dp),
                        tint =
                            TurfGreen
                    )


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    Text(
                        text = "No Images",
                        color = TurfGreen,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

        } else {

            // =================================================
            // SWIPEABLE IMAGES
            // =================================================

            HorizontalPager(
                state = pagerState,
                modifier =
                    Modifier.fillMaxSize()
            ) { page ->

                val rawUrl =
                    imageUrls[page]

                val imageUrl =
                    buildImageUrl(
                        rawUrl
                    )


                AsyncImage(
                    model = imageUrl,
                    contentDescription =
                        "Turf image ${page + 1}",
                    modifier =
                        Modifier.fillMaxSize(),
                    contentScale =
                        ContentScale.Crop
                )
            }


            // =================================================
            // IMAGE COUNT
            // =================================================

            if (imageUrls.size > 1) {

                Surface(
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(12.dp),
                    shape =
                        RoundedCornerShape(50),
                    color =
                        Color.Black.copy(
                            alpha = 0.65f
                        )
                ) {

                    Text(
                        text =
                            "${pagerState.currentPage + 1}/${imageUrls.size}",
                        color =
                            Color.White,
                        fontSize =
                            12.sp,
                        fontWeight =
                            FontWeight.Bold,
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    10.dp,
                                vertical =
                                    6.dp
                            )
                    )
                }


                // =================================================
                // DOT INDICATORS
                // =================================================

                Row(
                    modifier = Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(
                            bottom = 12.dp
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(5.dp)
                ) {

                    imageUrls.forEachIndexed {
                            index,
                            _ ->

                        Box(
                            modifier = Modifier
                                .size(
                                    if (
                                        pagerState.currentPage ==
                                        index
                                    ) {
                                        8.dp
                                    } else {
                                        6.dp
                                    }
                                )
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    if (
                                        pagerState.currentPage ==
                                        index
                                    ) {
                                        Color.White
                                    } else {
                                        Color.White.copy(
                                            alpha = 0.5f
                                        )
                                    }
                                )
                        )
                    }
                }
            }
        }
    }

}

// =============================================================
// TURF INFO BOX
// =============================================================

@Composable
private fun TurfInfoBox(
    modifier: Modifier,
    title: String,
    value: String,
    icon: ImageVector
) {

    Surface(
        modifier = modifier,
        shape =
            RoundedCornerShape(12.dp),
        color =
            Color(0xFFF8FAFC)
    ) {

        Row(
            modifier =
                Modifier.padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier =
                    Modifier.size(19.dp),
                tint =
                    TurfGreen
            )


            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )


            Column {

                Text(
                    text = title,
                    color = TurfGray,
                    fontSize = 11.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )


                Text(
                    text = value,
                    color = TurfDark,
                    fontSize = 13.sp,
                    fontWeight =
                        FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }

}

// =============================================================
// STATUS BADGE
// =============================================================

@Composable
private fun TurfStatusBadge(
    status: String
) {

    val isActive =
        status.equals(
            "ACTIVE",
            ignoreCase = true
        )


    Surface(
        shape =
            RoundedCornerShape(50),

        color =
            if (isActive) {
                TurfGreenLight
            } else {
                Color(0xFFFEE2E2)
            }
    ) {

        Row(
            modifier =
                Modifier.padding(
                    horizontal = 11.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        if (isActive) {
                            TurfGreen
                        } else {
                            TurfRed
                        }
                    )
            )


            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )


            Text(
                text =
                    status.uppercase(),

                color =
                    if (isActive) {
                        TurfGreen
                    } else {
                        TurfRed
                    },

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }


}

// =============================================================
// FORMAT LARAVEL TIME
// =============================================================
//
// Expected format:
//
// h:i A
//
// Examples:
//
// 09:00 AM
// 10:30 PM
//
// =============================================================

private fun formatTurfTime(
    time: String
): String {
    return time
        .trim()
        .uppercase()

}

// =============================================================
// IMAGE URL BUILDER
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


// =========================================================
// ANDROID EMULATOR -> LARAVEL
// =========================================================

    return "http://10.0.2.2:8000/${
        cleanUrl.trimStart('/')
    }"


}

// =============================================================
// PRICE FORMAT
// =============================================================

private fun formatPrice(
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
