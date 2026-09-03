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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.screens.user.components.TurfHomeCard
import com.example.bookmyturf.viewmodel.FavoriteViewModel


// ============================================================
// COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)


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

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // BACK BUTTON
            // =================================================

            IconButton(

                onClick = onBackClick
            ) {

                Icon(

                    imageVector =
                        Icons.Default.ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        DarkGreen
                )
            }


            // =================================================
            // TITLE
            // =================================================

            Column(

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        "My Favorites",

                    fontSize =
                        22.sp,

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
                        "Your saved turfs, ready whenever you are.",

                    fontSize =
                        12.sp,

                    color =
                        Gray
                )
            }
        }


        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Box(

                modifier =
                    Modifier.fillMaxSize(),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(

                        color =
                            ForestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(

                        text =
                            "Loading your favorites...",

                        fontSize =
                            13.sp,

                        color =
                            Gray
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // ERROR
        // =====================================================

        if (error != null) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                contentAlignment =
                    Alignment.Center
            ) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                White
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
                                .padding(28.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(42.dp),

                            tint =
                                ForestGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(

                            text =
                                "Unable to load favorites",

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
                                12.sp,

                            color =
                                Gray
                        )
                    }
                }
            }

            return@Column
        }


        // =====================================================
        // EMPTY FAVORITES
        // =====================================================

        if (favorites.isEmpty()) {

            Box(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Favorite,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(64.dp),

                        tint =
                            LightGreen.copy(
                                alpha = 0.45f
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(

                        text =
                            "No Favorites Yet",

                        fontSize =
                            21.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(7.dp)
                    )

                    Text(

                        text =
                            "Tap the heart on a turf to save it here.",

                        fontSize =
                            13.sp,

                        color =
                            Gray
                    )
                }
            }

            return@Column
        }


        // =====================================================
        // FAVORITES LIST
        // =====================================================

        LazyColumn(

            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(
                    top = 4.dp,
                    bottom = 28.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            items(

                items =
                    favorites,

                key = { favorite ->

                    favorite.id
                }

            ) { favorite ->

                val turf =
                    favorite.turf


                // =================================================
                // TURF CARD
                // =================================================

                if (turf != null) {

                    TurfHomeCard(

                        turf =
                            turf,

                        isFavorite =
                            true,

                        onClick = {

                            onTurfClick(
                                turf.id
                            )
                        },

                        onFavoriteClick = {

                            if (
                                !token.isNullOrBlank()
                            ) {

                                favoriteViewModel.removeFavorite(

                                    token =
                                        token,

                                    turfId =
                                        turf.id
                                )
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 6.dp
                                )
                    )
                }
            }


            // =====================================================
            // BOTTOM SPACE
            // =====================================================

            item {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )
            }
        }
    }
}
