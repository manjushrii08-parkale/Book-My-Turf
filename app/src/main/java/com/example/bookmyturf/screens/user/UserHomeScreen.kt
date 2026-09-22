package com.example.bookmyturf.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.local.SessionManager

import com.example.bookmyturf.screens.user.components.SportFilterRow
import com.example.bookmyturf.screens.user.components.TurfHomeCard
import com.example.bookmyturf.screens.user.components.TurfSearchBar
import com.example.bookmyturf.screens.user.components.UserHomeTopBar
import com.example.bookmyturf.screens.user.components.UserWelcomeSection

import com.example.bookmyturf.viewmodel.FavoriteViewModel
import com.example.bookmyturf.viewmodel.NotificationViewModel
import com.example.bookmyturf.viewmodel.UserHomeViewModel


// ============================================================
// PREMIUM DARK COLORS
// ============================================================

private val ScreenBackground =
    Color(0xFF020C09)

private val DarkSurface =
    Color(0xFF071713)

private val ForestGreen =
    Color(0xFF123D24)

private val LightGreen =
    Color(0xFF7DBB4A)

private val White =
    Color.White

private val SecondaryWhite =
    Color(0xFFB8C8C0)

private val MutedText =
    Color(0xFF82948B)


// ============================================================
// USER HOME SCREEN
// ============================================================

@Composable
fun UserHomeScreen(
    onTurfClick: (Int) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: UserHomeViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel(),
    notificationViewModel: NotificationViewModel = viewModel()
) {

    // ========================================================
    // TURF STATE
    // ========================================================

    val turfs by viewModel.turfs.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val error by viewModel.error.collectAsState()


    // ========================================================
    // FAVORITE STATE
    // ========================================================

    val favoriteStatus by favoriteViewModel
        .favoriteStatus
        .collectAsState()

    val favoriteError by favoriteViewModel
        .error
        .collectAsState()


    // ========================================================
    // NOTIFICATION STATE
    // ========================================================

    val unreadNotificationCount by notificationViewModel
        .unreadCount
        .collectAsState()


    // ========================================================
    // SESSION
    // ========================================================

    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context)
    }

    val token = remember(sessionManager) {
        sessionManager.getToken()
    }


    // ========================================================
    // SEARCH
    // ========================================================

    var searchQuery by remember {
        mutableStateOf("")
    }


    // ========================================================
    // SELECTED SPORT
    // ========================================================

    var selectedSport by remember {
        mutableStateOf("All")
    }


    // ========================================================
    // LOAD DATA
    // ========================================================

    LaunchedEffect(token) {

        viewModel.loadTurfs()

        if (!token.isNullOrBlank()) {

            favoriteViewModel.loadFavorites(
                token = token
            )

            notificationViewModel.loadUnreadCount(
                token = token
            )
        }
    }


    // ========================================================
    // FILTER TURFS
    // ========================================================

    val filteredTurfs = turfs.filter { turf ->

        val searchText =
            searchQuery.trim()

        val matchesSearch =
            searchText.isBlank() ||
                    turf.name.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    turf.location.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    turf.city.contains(
                        searchText,
                        ignoreCase = true
                    )

        val matchesSport =
            selectedSport == "All" ||
                    turf.sportsTypes.any { sport ->

                        sport.equals(
                            selectedSport,
                            ignoreCase = true
                        )
                    }

        matchesSearch && matchesSport
    }


    // ========================================================
    // MAIN HOME SCREEN
    // ========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ScreenBackground
            )
    ) {


        // ====================================================
        // TOP BAR
        // ====================================================

        UserHomeTopBar(
            unreadNotificationCount =
                unreadNotificationCount,

            onNotificationClick = {
                onNotificationsClick()
            },

            onProfileClick = {
                onProfileClick()
            }
        )


        // ====================================================
        // VERTICAL HOME SCROLL
        // ====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = 90.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {


            // =================================================
            // HERO SECTION
            // =================================================

            item {

                UserWelcomeSection()
            }


            // =================================================
            // SEARCH BAR
            // =================================================

            item {

                TurfSearchBar(
                    query = searchQuery,

                    onQueryChange = {
                        searchQuery = it
                    }
                )
            }


            // =================================================
            // SPORTS SECTION TITLE
            // =================================================

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 18.dp,
                            bottom = 4.dp
                        )
                ) {

                    Text(
                        text =
                            "Choose your sport",

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Find the perfect place for your game",

                        fontSize =
                            12.sp,

                        color =
                            MutedText
                    )
                }
            }


            // =================================================
            // SPORTS FILTER
            // =================================================

            item {

                SportFilterRow(
                    selectedSport =
                        selectedSport,

                    onSportSelected = { sport ->

                        selectedSport =
                            sport
                    }
                )
            }


            // =================================================
            // AVAILABLE TURFS HEADER
            // =================================================

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 20.dp,
                            bottom = 8.dp
                        )
                ) {

                    Text(
                        text =
                            "Available Turfs",

                        fontSize =
                            22.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            when {

                                searchQuery.isNotBlank() -> {
                                    "Results for \"$searchQuery\""
                                }

                                selectedSport != "All" -> {
                                    "Best ${selectedSport.lowercase()} turfs"
                                }

                                else -> {
                                    "Book your game and start playing"
                                }
                            },

                        fontSize =
                            12.sp,

                        color =
                            MutedText
                    )
                }
            }


            // =================================================
            // LOADING STATE
            // =================================================

            if (isLoading) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            CircularProgressIndicator(
                                color =
                                    LightGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Text(
                                text =
                                    "Finding available turfs...",

                                fontSize =
                                    13.sp,

                                color =
                                    SecondaryWhite
                            )
                        }
                    }
                }
            }


            // =================================================
            // ERROR STATE
            // =================================================

            else if (error != null) {

                item {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp
                            ),

                        shape =
                            RoundedCornerShape(20.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    DarkSurface
                            ),

                        border =
                            BorderStroke(
                                1.dp,
                                Color(0xFF16382B)
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(26.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        ForestGreen,
                                        CircleShape
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Refresh,

                                    contentDescription =
                                        null,

                                    modifier =
                                        Modifier.size(27.dp),

                                    tint =
                                        LightGreen
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(14.dp)
                            )

                            Text(
                                text =
                                    "Unable to load turfs",

                                fontSize =
                                    18.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    White
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text =
                                    error
                                        ?: "Something went wrong.",

                                fontSize =
                                    12.sp,

                                color =
                                    MutedText
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )

                            Button(
                                onClick = {
                                    viewModel.loadTurfs()
                                },

                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            LightGreen,

                                        contentColor =
                                            Color(0xFF06130F)
                                    ),

                                shape =
                                    RoundedCornerShape(12.dp)
                            ) {

                                Text(
                                    text =
                                        "Try Again",

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }


            // =================================================
            // EMPTY STATE
            // =================================================

            else if (filteredTurfs.isEmpty()) {

                item {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp
                            ),

                        shape =
                            RoundedCornerShape(20.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    DarkSurface
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 42.dp,
                                    horizontal = 20.dp
                                ),

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        ForestGreen,
                                        CircleShape
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
                                        Modifier.size(29.dp),

                                    tint =
                                        LightGreen
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(15.dp)
                            )

                            Text(
                                text =
                                    if (
                                        searchQuery.isNotBlank() ||
                                        selectedSport != "All"
                                    ) {
                                        "No matching turfs"
                                    } else {
                                        "No turfs available"
                                    },

                                fontSize =
                                    18.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    White
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(7.dp)
                            )

                            Text(
                                text =
                                    "Try another sport, city or location.",

                                fontSize =
                                    12.sp,

                                color =
                                    MutedText
                            )
                        }
                    }
                }
            }


            // =================================================
            // VERTICAL TURF CARD LIST
            // =================================================

            else {

                items(
                    items = filteredTurfs,

                    key = { turf ->
                        turf.id
                    }
                ) { turf ->

                    TurfHomeCard(

                        turf =
                            turf,

                        isFavorite =
                            favoriteStatus[
                                turf.id
                            ] == true,

                        onClick = {

                            onTurfClick(
                                turf.id
                            )
                        },

                        onFavoriteClick = {

                            if (
                                !token.isNullOrBlank()
                            ) {

                                val currentlyFavorite =
                                    favoriteStatus[
                                        turf.id
                                    ] == true

                                if (
                                    currentlyFavorite
                                ) {

                                    favoriteViewModel
                                        .removeFavorite(
                                            token = token,
                                            turfId = turf.id
                                        )

                                } else {

                                    favoriteViewModel
                                        .addFavorite(
                                            token = token,
                                            turfId = turf.id
                                        )
                                }
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp
                            )
                    )
                }
            }


            // =================================================
            // FAVORITE ERROR
            // =================================================

            if (favoriteError != null) {

                item {

                    Text(
                        text =
                            favoriteError
                                ?: "Unable to update favorite.",

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 18.dp,
                                vertical = 8.dp
                            ),

                        fontSize =
                            12.sp,

                        color =
                            Color(0xFFFF6B6B)
                    )
                }
            }


            // =================================================
            // BOTTOM SPACE
            // =================================================

            item {

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )
            }
        }
    }
}