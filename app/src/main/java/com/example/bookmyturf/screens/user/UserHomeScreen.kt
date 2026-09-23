package com.example.bookmyturf.screens.user

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
// BOOKMYTURF PREMIUM DARK THEME
// ============================================================

private val Background = Color(0xFF020907)

private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val ErrorRed = Color(0xFFFF6B6B)


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

    val context = androidx.compose.ui.platform.LocalContext.current

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
    // SPORT FILTER
    // ========================================================

    var selectedSport by remember {
        mutableStateOf("All")
    }


    // ========================================================
    // LOAD HOME DATA
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

        val searchText = searchQuery.trim()

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
    // SCREEN
    // ========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // ====================================================
        // TOP BAR
        // ====================================================

        UserHomeTopBar(
            unreadNotificationCount = unreadNotificationCount,
            onNotificationClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )


        // ====================================================
        // MAIN CONTENT
        // ====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            contentPadding = PaddingValues(
                bottom = 95.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(0.dp)
        ) {

            // =================================================
            // WELCOME HERO
            // =================================================

            item {

                UserWelcomeSection()
            }


            // =================================================
            // SEARCH
            // =================================================

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                TurfSearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                    }
                )
            }


            // =================================================
            // SPORT HEADER
            // =================================================

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 22.dp,
                            bottom = 9.dp
                        )
                ) {

                    Text(
                        text = "Choose your sport",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        letterSpacing = (-0.2).sp
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Find a place that matches your game",
                        fontSize = 12.sp,
                        color = MutedText
                    )
                }
            }


            // =================================================
            // SPORT FILTER
            // =================================================

            item {

                SportFilterRow(
                    selectedSport = selectedSport,
                    onSportSelected = {
                        selectedSport = it
                    }
                )
            }


            // =================================================
            // TURF SECTION HEADER
            // =================================================

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 22.dp,
                            bottom = 10.dp
                        )
                ) {

                    Text(
                        text = "Available turfs",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        letterSpacing = (-0.25).sp
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = when {

                            searchQuery.isNotBlank() ->
                                "Results for \"$searchQuery\""

                            selectedSport != "All" ->
                                "Best ${selectedSport.lowercase()} turfs"

                            else ->
                                "${filteredTurfs.size} places ready to play"
                        },
                        fontSize = 12.sp,
                        color = MutedText
                    )
                }
            }


            // =================================================
            // LOADING
            // =================================================

            if (isLoading) {

                item {

                    HomeLoadingState()
                }
            }


            // =================================================
            // ERROR
            // =================================================

            else if (error != null) {

                item {

                    HomeErrorState(
                        message =
                            error ?: "Something went wrong.",
                        onRetry = {
                            viewModel.loadTurfs()
                        }
                    )
                }
            }


            // =================================================
            // EMPTY
            // =================================================

            else if (filteredTurfs.isEmpty()) {

                item {

                    HomeEmptyState(
                        filtered =
                            searchQuery.isNotBlank() ||
                                    selectedSport != "All"
                    )
                }
            }


            // =================================================
            // TURF LIST
            // =================================================

            else {

                items(
                    items = filteredTurfs,
                    key = { turf ->
                        turf.id
                    }
                ) { turf ->

                    TurfHomeCard(
                        turf = turf,

                        isFavorite =
                            favoriteStatus[turf.id] == true,

                        onClick = {
                            onTurfClick(turf.id)
                        },

                        onFavoriteClick = {

                            if (!token.isNullOrBlank()) {

                                val currentlyFavorite =
                                    favoriteStatus[turf.id] == true

                                if (currentlyFavorite) {

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
                                horizontal = 18.dp,
                                vertical = 6.dp
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
                                horizontal = 20.dp,
                                vertical = 10.dp
                            ),

                        fontSize = 11.sp,

                        color = ErrorRed
                    )
                }
            }


            // =================================================
            // BOTTOM SPACE
            // =================================================

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}


// ============================================================
// LOADING STATE
// ============================================================

@Composable
private fun HomeLoadingState() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                strokeWidth = 2.5.dp,
                color = PrimaryGreen
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Finding places to play...",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryText
            )
        }
    }
}


// ============================================================
// ERROR STATE
// ============================================================

@Composable
private fun HomeErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 6.dp
            ),

        shape = RoundedCornerShape(20.dp),

        color = SurfaceDark
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 30.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        SurfaceHighlight,
                        CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = PrimaryGreen
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Unable to load turfs",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = message,
                fontSize = 12.sp,
                color = MutedText
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = onRetry,

                shape = RoundedCornerShape(11.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background
                )
            ) {

                Text(
                    text = "Try Again",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================
// EMPTY STATE
// ============================================================

@Composable
private fun HomeEmptyState(
    filtered: Boolean
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 6.dp
            ),

        shape = RoundedCornerShape(20.dp),

        color = SurfaceDark
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 34.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        SurfaceHighlight,
                        CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.SportsSoccer,

                    contentDescription = null,

                    modifier =
                        Modifier.size(28.dp),

                    tint =
                        PrimaryGreen
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text =
                    if (filtered) {
                        "No matching turfs"
                    } else {
                        "No turfs available"
                    },

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    PrimaryText
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    if (filtered) {
                        "Try another sport, city or location."
                    } else {
                        "New places to play will appear here."
                    },

                fontSize = 12.sp,

                color =
                    MutedText
            )
        }
    }
}
