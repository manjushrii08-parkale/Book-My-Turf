package com.example.bookmyturf.screens.user.components

import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmyturf.data.model.turf.Turf


// ============================================================
// BOOKMYTURF PREMIUM COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val White = Color(0xFFFFFFFF)
private val SoftGreen = Color(0xFFF0F5EA)
private val SoftGray = Color(0xFFF3F4F0)
private val ImagePlaceholder = Color(0xFFEFF1EC)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val FavoriteRed = Color(0xFFE53935)


// ============================================================
// TURF HOME CARD
// ============================================================

@Composable
fun TurfHomeCard(
    turf: Turf,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val images = turf.imageUrls

    Log.d(
        "TURF_IMAGE_DEBUG",
        "Turf: ${turf.name} | Images: $images"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column {

            // =================================================
            // IMAGE
            // =================================================

            TurfImageSection(
                turfName = turf.name,
                images = images,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )

            // =================================================
            // TURF INFORMATION
            // =================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 15.dp
                    )
            ) {

                // =============================================
                // NAME + RATING
                // =============================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = turf.name,

                        modifier = Modifier.weight(1f),

                        fontSize = 19.sp,

                        fontWeight = FontWeight.ExtraBold,

                        color = Charcoal,

                        maxLines = 1,

                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = SoftGreen
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            ),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Star,

                                contentDescription =
                                    "Rating",

                                modifier =
                                    Modifier.size(15.dp),

                                tint = LightGreen
                            )

                            Text(
                                text = String.format(
                                    "%.1f",
                                    turf.rating
                                ),

                                modifier = Modifier.padding(
                                    start = 3.dp
                                ),

                                fontSize = 11.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color = DarkGreen
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                // =============================================
                // LOCATION
                // =============================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.LocationOn,

                        contentDescription =
                            "Location",

                        modifier =
                            Modifier.size(17.dp),

                        tint = ForestGreen
                    )

                    Text(
                        text =
                            buildString {

                                if (
                                    turf.location.isNotBlank()
                                ) {
                                    append(
                                        turf.location
                                    )
                                }

                                if (
                                    turf.location.isNotBlank() &&
                                    turf.city.isNotBlank()
                                ) {
                                    append(", ")
                                }

                                if (
                                    turf.city.isNotBlank()
                                ) {
                                    append(
                                        turf.city
                                    )
                                }
                            }.ifBlank {
                                "Location unavailable"
                            },

                        modifier = Modifier
                            .padding(start = 5.dp)
                            .weight(1f),

                        fontSize = 12.sp,

                        color = Gray,

                        maxLines = 1,

                        overflow =
                            TextOverflow.Ellipsis
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // =============================================
                // SPORTS
                // =============================================

                if (turf.sportsTypes.isNotEmpty()) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(6.dp)
                    ) {

                        turf.sportsTypes
                            .take(3)
                            .forEach { sport ->

                                Surface(
                                    shape =
                                        RoundedCornerShape(
                                            50.dp
                                        ),

                                    color = SoftGray
                                ) {

                                    Text(
                                        text = sport,

                                        modifier =
                                            Modifier.padding(
                                                horizontal = 9.dp,
                                                vertical = 5.dp
                                            ),

                                        fontSize = 10.sp,

                                        fontWeight =
                                            FontWeight.Medium,

                                        color = Gray,

                                        maxLines = 1,

                                        overflow =
                                            TextOverflow.Ellipsis
                                    )
                                }
                            }

                        if (
                            turf.sportsTypes.size > 3
                        ) {

                            Surface(
                                shape =
                                    RoundedCornerShape(
                                        50.dp
                                    ),

                                color = SoftGray
                            ) {

                                Text(
                                    text =
                                        "+${turf.sportsTypes.size - 3}",

                                    modifier =
                                        Modifier.padding(
                                            horizontal = 9.dp,
                                            vertical = 5.dp
                                        ),

                                    fontSize = 10.sp,

                                    fontWeight =
                                        FontWeight.Medium,

                                    color = Gray
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                // =============================================
                // DIVIDER
                // =============================================

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            SoftGray
                        )
                )

                Spacer(
                    modifier = Modifier.height(13.dp)
                )

                // =============================================
                // PRICE + ACTION
                // =============================================

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    // =========================================
                    // PRICE
                    // =========================================

                    Column {

                        Text(
                            text = "Starting from",

                            fontSize = 10.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color = Gray
                        )

                        Row(
                            verticalAlignment =
                                Alignment.Bottom
                        ) {

                            Text(
                                text =
                                    "₹${turf.price}",

                                fontSize = 21.sp,

                                fontWeight =
                                    FontWeight.ExtraBold,

                                color = DarkGreen
                            )

                            Text(
                                text = " / hour",

                                modifier =
                                    Modifier.padding(
                                        start = 3.dp,
                                        bottom = 3.dp
                                    ),

                                fontSize = 11.sp,

                                color = Gray
                            )
                        }
                    }

                    // =========================================
                    // VIEW TURF
                    // =========================================

                    Surface(
                        modifier = Modifier.clickable {
                            onClick()
                        },

                        shape =
                            RoundedCornerShape(13.dp),

                        color = LightGreen
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 10.dp
                            ),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = "View Turf",

                                fontSize = 12.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color = DarkGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(5.dp)
                            )

                            Icon(
                                imageVector =
                                    Icons.Default.ArrowForward,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(15.dp),

                                tint = DarkGreen
                            )
                        }
                    }
                }
            }
        }
    }
}


// ============================================================
// TURF IMAGE SECTION
// ============================================================

@Composable
private fun TurfImageSection(
    turfName: String,
    images: List<String>,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(195.dp)
    ) {

        // =====================================================
        // IMAGES AVAILABLE
        // =====================================================

        if (images.isNotEmpty()) {

            val pagerState =
                rememberPagerState(
                    initialPage = 0,
                    pageCount = {
                        images.size
                    }
                )

            HorizontalPager(
                state = pagerState,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
            ) { page ->

                var isLoading by remember(
                    images[page]
                ) {
                    mutableStateOf(true)
                }

                var hasError by remember(
                    images[page]
                ) {
                    mutableStateOf(false)
                }

                // =============================================
                // IMAGE
                // =============================================

                AsyncImage(
                    model = images[page],

                    contentDescription =
                        "$turfName image ${page + 1}",

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(195.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 22.dp,
                                topEnd = 22.dp
                            )
                        ),

                    contentScale =
                        ContentScale.Crop,

                    onLoading = {

                        isLoading = true
                        hasError = false

                        Log.d(
                            "TURF_IMAGE",
                            "LOADING: ${images[page]}"
                        )
                    },

                    onSuccess = {

                        isLoading = false
                        hasError = false

                        Log.d(
                            "TURF_IMAGE",
                            "SUCCESS: ${images[page]}"
                        )
                    },

                    onError = { result ->

                        isLoading = false
                        hasError = true

                        Log.e(
                            "TURF_IMAGE",
                            "ERROR: ${images[page]}",
                            result.result.throwable
                        )
                    }
                )

                // =============================================
                // LOADING
                // =============================================

                if (isLoading && !hasError) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                ImagePlaceholder
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(30.dp),

                            strokeWidth = 3.dp,

                            color = ForestGreen
                        )
                    }
                }

                // =============================================
                // ERROR
                // =============================================

                if (hasError) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                ImagePlaceholder
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
                                    Icons.Default.BrokenImage,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(34.dp),

                                tint = Gray
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text =
                                    "Image unavailable",

                                fontSize = 11.sp,

                                fontWeight =
                                    FontWeight.Medium,

                                color = Gray
                            )
                        }
                    }
                }
            }

            // =================================================
            // FAVORITE BUTTON
            // =================================================

            IconButton(
                onClick = onFavoriteClick,

                modifier = Modifier
                    .padding(12.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        White.copy(
                            alpha = 0.94f
                        )
                    )
                    .align(
                        Alignment.TopEnd
                    )
            ) {

                Icon(
                    imageVector =
                        if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },

                    contentDescription =
                        if (isFavorite) {
                            "Remove favorite"
                        } else {
                            "Add favorite"
                        },

                    modifier =
                        Modifier.size(20.dp),

                    tint =
                        if (isFavorite) {
                            FavoriteRed
                        } else {
                            DarkGreen
                        }
                )
            }

            // =================================================
            // IMAGE COUNTER
            // =================================================

            if (images.size > 1) {

                Surface(
                    modifier = Modifier
                        .align(
                            Alignment.TopStart
                        )
                        .padding(12.dp),

                    shape =
                        RoundedCornerShape(50.dp),

                    color =
                        White.copy(
                            alpha = 0.93f
                        )
                ) {

                    Text(
                        text =
                            "${pagerState.currentPage + 1}/${images.size}",

                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color = DarkGreen
                    )
                }

                // =================================================
                // PAGE INDICATORS
                // =================================================

                Row(
                    modifier = Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(
                            bottom = 11.dp
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(5.dp)
                ) {

                    images.forEachIndexed { index, _ ->

                        Box(
                            modifier = Modifier
                                .size(
                                    if (
                                        pagerState.currentPage ==
                                        index
                                    ) {
                                        17.dp
                                    } else {
                                        6.dp
                                    }
                                )
                                .clip(
                                    RoundedCornerShape(
                                        50.dp
                                    )
                                )
                                .background(
                                    White.copy(
                                        alpha =
                                            if (
                                                pagerState.currentPage ==
                                                index
                                            ) {
                                                0.95f
                                            } else {
                                                0.55f
                                            }
                                    )
                                )
                        )
                    }
                }
            }

        } else {

            // =================================================
            // NO IMAGE
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 22.dp,
                            topEnd = 22.dp
                        )
                    )
                    .background(
                        ImagePlaceholder
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
                            Icons.Default.BrokenImage,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(36.dp),

                        tint = Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "No Image Available",

                        fontSize = 12.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color = Gray
                    )
                }
            }

            // =================================================
            // FAVORITE BUTTON WITHOUT IMAGE
            // =================================================

            IconButton(
                onClick = onFavoriteClick,

                modifier = Modifier
                    .padding(12.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        White.copy(
                            alpha = 0.94f
                        )
                    )
                    .align(
                        Alignment.TopEnd
                    )
            ) {

                Icon(
                    imageVector =
                        if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },

                    contentDescription =
                        if (isFavorite) {
                            "Remove favorite"
                        } else {
                            "Add favorite"
                        },

                    modifier =
                        Modifier.size(20.dp),

                    tint =
                        if (isFavorite) {
                            FavoriteRed
                        } else {
                            DarkGreen
                        }
                )
            }
        }
    }
}

