package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.screens.user.components.TurfHomeCard
import com.example.bookmyturf.viewmodel.FavoriteViewModel

// ============================================================
// PREMIUM BOOK MY TURF COLORS
// ============================================================

private val Background = Color(0xFF020907)

private val Surface = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)

private val ErrorRed = Color(0xFFFF7777)
private val ErrorSurface = Color(0xFF1A0D0D)


// ============================================================
// USER FAVORITES SCREEN
// ============================================================

@Composable
fun UserFavoritesScreen(

    onBackClick: () -> Unit,

    onTurfClick: (Int) -> Unit,

    favoriteViewModel: FavoriteViewModel

) {

    // =========================================================
    // FAVORITES STATE
    // =========================================================

    val favorites by
    favoriteViewModel.favorites.collectAsState()

    val isLoading by
    favoriteViewModel.isLoading.collectAsState()

    val error by
    favoriteViewModel.error.collectAsState()


    // =========================================================
    // SESSION
    // =========================================================

    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }


    // =========================================================
    // LOAD FAVORITES
    // =========================================================

    LaunchedEffect(token) {

        if (!token.isNullOrBlank()) {

            favoriteViewModel.loadFavorites(
                token = token
            )
        }
    }


    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // =====================================================
        // SUBTLE TOP GLOW
        // =====================================================

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .alpha(0.055f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PrimaryGreen,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            // =================================================
            // PREMIUM TOP BAR
            // =================================================

            PremiumFavoritesTopBar(
                favoriteCount = favorites.size,
                onBackClick = onBackClick
            )


            // =================================================
            // LOADING
            // =================================================

            if (isLoading) {

                FavoritesLoadingState()

                return@Column
            }


            // =================================================
            // ERROR
            // =================================================

            if (error != null) {

                FavoritesErrorState(
                    message = error
                        ?: "Something went wrong."
                )

                return@Column
            }


            // =================================================
            // EMPTY
            // =================================================

            if (favorites.isEmpty()) {

                FavoritesEmptyState()

                return@Column
            }


            // =================================================
            // CONTENT HEADER
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 17.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Saved turfs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryText,
                        letterSpacing = (-0.2).sp
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Places you've saved to play later",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }

                // Small count
                Box(
                    modifier = Modifier
                        .background(
                            color = SurfaceHighlight,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Border,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 11.dp,
                            vertical = 8.dp
                        )
                ) {

                    Text(
                        text = "${favorites.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LightGreen
                    )
                }
            }


            // =================================================
            // FAVORITES LIST
            // =================================================

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize(),

                contentPadding = PaddingValues(
                    top = 1.dp,
                    bottom = 28.dp
                ),

                verticalArrangement = Arrangement.spacedBy(
                    2.dp
                )

            ) {

                items(

                    items = favorites,

                    key = { favorite ->
                        favorite.id
                    }

                ) { favorite ->

                    val turf = favorite.turf

                    if (turf != null) {

                        TurfHomeCard(

                            turf = turf,

                            isFavorite = true,

                            onClick = {

                                onTurfClick(
                                    turf.id
                                )
                            },

                            onFavoriteClick = {

                                if (!token.isNullOrBlank()) {

                                    favoriteViewModel.removeFavorite(

                                        token = token,

                                        turfId = turf.id
                                    )
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 6.dp
                                )
                        )
                    }
                }


                item {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
        }
    }
}


// ============================================================
// PREMIUM TOP BAR
// ============================================================

@Composable
private fun PremiumFavoritesTopBar(
    favoriteCount: Int,
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // -------------------------------------------------
            // BACK BUTTON
            // -------------------------------------------------

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Border,
                        shape = RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(42.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        tint = PrimaryText
                    )
                }
            }


            Spacer(
                modifier = Modifier.width(14.dp)
            )


            // -------------------------------------------------
            // TITLE
            // -------------------------------------------------

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Favorites",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText,
                    letterSpacing = (-0.3).sp
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Your saved turfs",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }


            // -------------------------------------------------
            // FAVORITE ICON + COUNT
            // -------------------------------------------------

            Box(
                modifier = Modifier
                    .background(
                        color = PrimaryGreen.copy(
                            alpha = 0.09f
                        ),
                        shape = RoundedCornerShape(13.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = PrimaryGreen.copy(
                            alpha = 0.18f
                        ),
                        shape = RoundedCornerShape(13.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 8.dp
                    )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = LightGreen
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = favoriteCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrightGreen
                    )
                }
            }
        }


        // -----------------------------------------------------
        // SUBTLE DIVIDER
        // -----------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )
    }
}


// ============================================================
// LOADING STATE
// ============================================================

@Composable
private fun FavoritesLoadingState() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        color = SurfaceHighlight,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(27.dp),
                    color = LightGreen,
                    strokeWidth = 2.5.dp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Loading favorites",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Finding your saved turfs...",
                fontSize = 12.sp,
                color = MutedText
            )
        }
    }
}


// ============================================================
// ERROR STATE
// ============================================================

@Composable
private fun FavoritesErrorState(
    message: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(22.dp)
                )
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .background(
                        color = ErrorSurface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(27.dp),
                    tint = ErrorRed
                )
            }

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            Text(
                text = "Unable to load favorites",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = message,
                fontSize = 12.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}


// ============================================================
// EMPTY STATE
// ============================================================

@Composable
private fun FavoritesEmptyState() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------------------------------------
            // FAVORITE ICON
            // -------------------------------------------------

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = SurfaceHighlight,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Border,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .background(
                            color = PrimaryGreen.copy(
                                alpha = 0.10f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(31.dp),
                        tint = LightGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Nothing saved yet",
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.3).sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "When you find a turf you love,\ntap the heart to save it here.",
                fontSize = 13.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = MutedText
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Your saved turfs will appear here",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }
        }
    }
}