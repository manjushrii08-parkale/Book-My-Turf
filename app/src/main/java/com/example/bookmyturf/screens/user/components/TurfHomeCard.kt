package com.example.bookmyturf.screens.user.components

import android.util.Log

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage

import com.example.bookmyturf.data.model.turf.Turf


// ============================================================
// BOOK MY TURF - PREMIUM COLORS
// ============================================================

private val Background = Color(0xFF020C09)

private val CardBackground = Color(0xFF071713)

private val ImagePlaceholder = Color(0xFF10231C)

private val PrimaryGreen = Color(0xFF7DBB4A)

private val LightGreen = Color(0xFFA8D86E)

private val BrightGreen = Color(0xFFB7E77A)

private val White = Color(0xFFF5F8F6)

private val SecondaryText = Color(0xFF9EAEA6)

private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)

private val FavoriteRed = Color(0xFFFF5A5F)


// ============================================================
// PREMIUM TURF HOME CARD
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
            .height(184
                .dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),

        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),

            horizontalArrangement =
                Arrangement.spacedBy(13.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // ====================================================
            // IMAGE
            // ====================================================

            TurfImageSection(
                turfName = turf.name,
                images = images,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,

                modifier = Modifier
                    .width(148.dp)
                    .fillMaxHeight()
            )


            // ====================================================
            // RIGHT CONTENT
            // ====================================================

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(
                        top = 4.dp,
                        bottom = 3.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(7.dp)
            ) {

                // =================================================
                // TURF NAME
                // =================================================

                Text(
                    text = turf.name,

                    fontSize = 17.sp,

                    lineHeight = 21.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = White,

                    maxLines = 2,

                    overflow =
                        TextOverflow.Ellipsis
                )


                // =================================================
                // LOCATION
                // =================================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.Top
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(23.dp)
                                .clip(CircleShape)
                                .background(
                                    PrimaryGreen.copy(
                                        alpha = 0.12f
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
                                Modifier.size(13.dp),

                            tint =
                                PrimaryGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        text = buildString {

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

                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(
                                    top = 2.dp
                                ),

                        fontSize = 10.sp,

                        lineHeight = 14.sp,

                        color =
                            SecondaryText,

                        maxLines = 2,

                        overflow =
                            TextOverflow.Ellipsis
                    )
                }


                // =================================================
                // SPORTS - EMOJI ONLY
                // =================================================

                if (
                    turf.sportsTypes.isNotEmpty()
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(6.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        turf.sportsTypes
                            .take(4)
                            .forEach { sport ->

                                SportEmoji(
                                    sport = sport
                                )
                            }
                    }
                }


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                // =================================================
                // BOOK NOW BUTTON
                // =================================================

                Button(
                    onClick = {
                        // Same action as previous VIEW button
                        onClick()
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(35.dp),

                    shape =
                        RoundedCornerShape(11.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                PrimaryGreen,

                            contentColor =
                                Background
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 12.dp,
                            vertical = 0.dp
                        )
                ) {

                    Text(
                        text = "BOOK NOW",

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        letterSpacing =
                            0.5.sp
                    )
                }
            }
        }
    }
}


// ============================================================
// SPORT EMOJI
// ============================================================

@Composable
private fun SportEmoji(
    sport: String
) {

    val emoji = when {

        sport.contains(
            "football",
            ignoreCase = true
        ) -> "⚽"

        sport.contains(
            "soccer",
            ignoreCase = true
        ) -> "⚽"

        sport.contains(
            "cricket",
            ignoreCase = true
        ) -> "🏏"

        sport.contains(
            "badminton",
            ignoreCase = true
        ) -> "🏸"

        sport.contains(
            "tennis",
            ignoreCase = true
        ) -> "🎾"

        sport.contains(
            "basketball",
            ignoreCase = true
        ) -> "🏀"

        sport.contains(
            "volleyball",
            ignoreCase = true
        ) -> "🏐"

        sport.contains(
            "table",
            ignoreCase = true
        ) -> "🏓"

        sport.contains(
            "hockey",
            ignoreCase = true
        ) -> "🏑"

        else -> "🏅"
    }


    Surface(
        modifier =
            Modifier.size(31.dp),

        shape =
            CircleShape,

        color =
            Color(0xFF10251C),

        border =
            BorderStroke(
                width = 1.dp,
                color =
                    BorderColor
            )
    ) {

        Box(
            modifier =
                Modifier.fillMaxSize(),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = emoji,

                fontSize =
                    14.sp
            )
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
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(19.dp)
            )
    ) {

        if (images.isNotEmpty()) {

            val pagerState =
                rememberPagerState(
                    initialPage = 0,
                    pageCount = {
                        images.size
                    }
                )


            // ====================================================
            // IMAGE PAGER
            // ====================================================

            HorizontalPager(
                state =
                    pagerState,

                modifier =
                    Modifier.fillMaxSize()
            ) { page ->

                Box(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

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


                    // ==========================================
                    // IMAGE
                    // ==========================================

                    AsyncImage(
                        model =
                            images[page],

                        contentDescription =
                            "$turfName image ${page + 1}",

                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(
                                    RoundedCornerShape(
                                        19.dp
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


                    // ==========================================
                    // PREMIUM IMAGE OVERLAY
                    // ==========================================

                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(
                                                alpha = 0.08f
                                            ),

                                            Color.Transparent,

                                            Color.Black.copy(
                                                alpha = 0.50f
                                            )
                                        )
                                    )
                                )
                    )


                    // ==========================================
                    // LOADING
                    // ==========================================

                    if (
                        isLoading &&
                        !hasError
                    ) {

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(
                                        ImagePlaceholder
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(25.dp),

                                strokeWidth =
                                    2.5.dp,

                                color =
                                    LightGreen
                            )
                        }
                    }


                    // ==========================================
                    // ERROR
                    // ==========================================

                    if (hasError) {

                        Box(
                            modifier =
                                Modifier
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
                                        Modifier.size(27.dp),

                                    tint =
                                        SecondaryText
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(5.dp)
                                )

                                Text(
                                    text =
                                        "Image unavailable",

                                    fontSize =
                                        8.sp,

                                    fontWeight =
                                        FontWeight.Medium,

                                    color =
                                        SecondaryText
                                )
                            }
                        }
                    }
                }
            }


            // ====================================================
            // FAVORITE BUTTON
            // ====================================================

            PremiumFavoriteButton(
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,

                modifier =
                    Modifier.align(
                        Alignment.TopEnd
                    )
            )


            // ====================================================
            // IMAGE COUNTER
            // ====================================================

            if (images.size > 1) {

                Surface(
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomStart
                            )
                            .padding(8.dp),

                    shape =
                        RoundedCornerShape(8.dp),

                    color =
                        Background.copy(
                            alpha = 0.82f
                        ),

                    border =
                        BorderStroke(
                            width = 1.dp,
                            color =
                                Color.White.copy(
                                    alpha = 0.08f
                                )
                        )
                ) {

                    Text(
                        text =
                            "${pagerState.currentPage + 1}/${images.size}",

                        modifier =
                            Modifier.padding(
                                horizontal = 7.dp,
                                vertical = 4.dp
                            ),

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            White
                    )
                }


                // =================================================
                // PAGE INDICATORS
                // =================================================

                Row(
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(
                                bottom = 9.dp
                            ),

                    horizontalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    images.forEachIndexed { index, _ ->

                        Box(
                            modifier =
                                Modifier
                                    .size(
                                        width =
                                            if (
                                                pagerState.currentPage ==
                                                index
                                            ) {
                                                13.dp
                                            } else {
                                                5.dp
                                            },

                                        height =
                                            5.dp
                                    )
                                    .clip(
                                        CircleShape
                                    )
                                    .background(
                                        if (
                                            pagerState.currentPage ==
                                            index
                                        ) {
                                            BrightGreen
                                        } else {
                                            Color.White.copy(
                                                alpha = 0.55f
                                            )
                                        }
                                    )
                        )
                    }
                }
            }

        } else {

            // ====================================================
            // NO IMAGE
            // ====================================================

            Box(
                modifier =
                    Modifier
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
                            Modifier.size(29.dp),

                        tint =
                            SecondaryText
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "No Image",

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            SecondaryText
                    )
                }
            }


            // ====================================================
            // FAVORITE BUTTON
            // ====================================================

            PremiumFavoriteButton(
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,

                modifier =
                    Modifier.align(
                        Alignment.TopEnd
                    )
            )
        }
    }
}


// ============================================================
// PREMIUM FAVORITE BUTTON
// ============================================================

@Composable
private fun PremiumFavoriteButton(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .padding(9.dp)
            .size(35.dp),

        shape =
            CircleShape,

        color =
            Background.copy(
                alpha = 0.88f
            ),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    Color.White.copy(
                        alpha = 0.12f
                    )
            )
    ) {

        IconButton(
            onClick =
                onFavoriteClick,

            modifier =
                Modifier.size(35.dp)
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
                    Modifier.size(17.dp),

                tint =
                    if (isFavorite) {
                        FavoriteRed
                    } else {
                        White
                    }
            )
        }
    }
}