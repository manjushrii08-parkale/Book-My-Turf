package com.example.bookmyturf.screens.superadmin

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.SuperAdminDashboardData
import com.example.bookmyturf.viewmodel.SuperAdminViewModel
import kotlinx.coroutines.launch
import java.util.Locale

private val SuperAdminGreen = Color(0xFF14532D)
private val Green = Color(0xFF2E7D32)
private val LightGreen = Color(0xFFE8F5E9)

private val Background = Color(0xFFF5F7F6)
private val TextDark = Color(0xFF17201A)
private val TextGray = Color(0xFF6B7280)

private val WarningOrange = Color(0xFFF59E0B)
private val LightOrange = Color(0xFFFFF7E6)

private val Blue = Color(0xFF2563EB)
private val LightBlue = Color(0xFFEFF6FF)

private val Red = Color(0xFFDC2626)
private val LightRed = Color(0xFFFEF2F2)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminHomeScreen(
    token: String,
    onLogout: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onOwnersClick: () -> Unit = {},
    onTurfsClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    onSubscriptionsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    val viewModel: SuperAdminViewModel = viewModel()

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()


    // =========================================================
    // LOAD REAL DASHBOARD DATA
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {
            viewModel.loadDashboard(token)
        }
    }


    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {

                Spacer(
                    modifier = Modifier.height(25.dp)
                )

                // =================================================
                // DRAWER HEADER
                // =================================================

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = SuperAdminGreen,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Super Admin",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "Super Admin",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "System Administrator",
                        fontSize = 13.sp,
                        color = TextGray
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = LightGreen,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            )
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Green,
                                modifier = Modifier.size(15.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(5.dp)
                            )

                            Text(
                                text = "Secure Access",
                                color = Green,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(10.dp)
                )


                DrawerItem(
                    title = "Dashboard",
                    icon = Icons.Default.Dashboard,
                    selected = true
                ) {
                    scope.launch {
                        drawerState.close()
                    }
                }


                DrawerItem(
                    title = "Users",
                    icon = Icons.Default.Group
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onUsersClick()
                }


                DrawerItem(
                    title = "Turf Owners",
                    icon = Icons.Default.Business
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onOwnersClick()
                }


                DrawerItem(
                    title = "Turfs",
                    icon = Icons.Default.Business
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onTurfsClick()
                }


                DrawerItem(
                    title = "Bookings",
                    icon = Icons.Default.BookOnline
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onBookingsClick()
                }


                DrawerItem(
                    title = "Subscriptions",
                    icon = Icons.Default.Payment
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onSubscriptionsClick()
                }


                Spacer(
                    modifier = Modifier.weight(1f)
                )


                DrawerItem(
                    title = "Settings",
                    icon = Icons.Default.Settings
                ) {

                    scope.launch {
                        drawerState.close()
                    }

                    onSettingsClick()
                }


                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Logout",
                            color = Red
                        )
                    },
                    selected = false,
                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        onLogout()
                    },
                    icon = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Red
                        )
                    }
                )

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .navigationBarsPadding()
                )
            }
        }
    ) {

        Scaffold(

            containerColor = Background,

            topBar = {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Super Admin",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Text(
                                text = "Platform Overview",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {

                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = TextDark
                            )
                        }
                    },

                    actions = {

                        IconButton(
                            onClick = {}
                        ) {

                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = TextDark
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { paddingValues ->


            when {

                uiState.isLoading -> {

                    LoadingDashboard(
                        paddingValues = paddingValues
                    )
                }


                uiState.errorMessage != null -> {

                    ErrorDashboard(
                        paddingValues = paddingValues,
                        message = uiState.errorMessage ?: "Unknown error",
                        onRetry = {
                            viewModel.clearError()
                            viewModel.loadDashboard(token)
                        }
                    )
                }


                uiState.dashboard != null -> {

                    DashboardContent(
                        paddingValues = paddingValues,
                        dashboard = uiState.dashboard!!,
                        onUsersClick = onUsersClick,
                        onOwnersClick = onOwnersClick,
                        onTurfsClick = onTurfsClick,
                        onBookingsClick = onBookingsClick,
                        onSubscriptionsClick = onSubscriptionsClick
                    )
                }
            }
        }
    }
}


// =============================================================
// DRAWER ITEM
// =============================================================

@Composable
private fun DrawerItem(
    title: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit
) {

    NavigationDrawerItem(
        label = {
            Text(text = title)
        },
        selected = selected,
        onClick = onClick,
        icon = {

            Icon(
                imageVector = icon,
                contentDescription = title
            )
        }
    )
}


// =============================================================
// LOADING
// =============================================================

@Composable
private fun LoadingDashboard(
    paddingValues: PaddingValues
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(
                color = SuperAdminGreen
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Loading dashboard...",
                color = TextGray,
                fontSize = 14.sp
            )
        }
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun ErrorDashboard(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Red,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "Unable to load dashboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = message,
                    color = TextGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                androidx.compose.material3.Button(
                    onClick = onRetry
                ) {

                    Text("Retry")
                }
            }
        }
    }
}


// =============================================================
// DASHBOARD CONTENT
// =============================================================

@Composable
private fun DashboardContent(
    paddingValues: PaddingValues,
    dashboard: SuperAdminDashboardData,
    onUsersClick: () -> Unit,
    onOwnersClick: () -> Unit,
    onTurfsClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onSubscriptionsClick: () -> Unit
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),

        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 18.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            WelcomeCard()
        }


        item {

            SectionTitle(
                title = "Platform Overview"
            )
        }


        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                DashboardStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Users",
                    value = dashboard.users.total.toString(),
                    icon = Icons.Default.Group,
                    iconBackground = LightBlue,
                    iconColor = Blue
                )

                DashboardStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Turf Owners",
                    value = dashboard.admins.total.toString(),
                    icon = Icons.Default.Business,
                    iconBackground = LightGreen,
                    iconColor = Green
                )
            }
        }


        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                DashboardStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Turfs",
                    value = dashboard.turfs.total.toString(),
                    icon = Icons.Default.Business,
                    iconBackground = LightOrange,
                    iconColor = WarningOrange
                )

                DashboardStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Bookings",
                    value = dashboard.bookings.total.toString(),
                    icon = Icons.Default.BookOnline,
                    iconBackground = LightRed,
                    iconColor = Red
                )
            }
        }


        item {

            RevenueCard(
                revenue = dashboard.revenue.total
            )
        }


        item {

            SectionTitle(
                title = "Quick Actions"
            )
        }


        item {

            QuickActionCard(
                title = "Manage Users",
                subtitle = "View and manage platform users",
                icon = Icons.Default.Group,
                iconBackground = LightBlue,
                iconColor = Blue,
                onClick = onUsersClick
            )
        }


        item {

            QuickActionCard(
                title = "Manage Turf Owners",
                subtitle = "Monitor owner accounts",
                icon = Icons.Default.Business,
                iconBackground = LightGreen,
                iconColor = Green,
                onClick = onOwnersClick
            )
        }


        item {

            QuickActionCard(
                title = "Manage Turfs",
                subtitle = "View all registered turfs",
                icon = Icons.Default.Business,
                iconBackground = LightOrange,
                iconColor = WarningOrange,
                onClick = onTurfsClick
            )
        }


        item {

            QuickActionCard(
                title = "Manage Bookings",
                subtitle = "Monitor platform bookings",
                icon = Icons.Default.BookOnline,
                iconBackground = LightRed,
                iconColor = Red,
                onClick = onBookingsClick
            )
        }


        item {

            QuickActionCard(
                title = "Subscriptions",
                subtitle = "Monitor admin subscription plans",
                icon = Icons.Default.Payment,
                iconBackground = LightGreen,
                iconColor = Green,
                onClick = onSubscriptionsClick
            )
        }


        item {

            SectionTitle(
                title = "System Status"
            )
        }


        item {

            SystemStatusCard()
        }


        item {

            SectionTitle(
                title = "Recent Activity"
            )
        }


        item {

            RecentActivityCard(
                icon = Icons.Default.Group,
                title = "User registrations",
                subtitle = "${dashboard.users.total} users registered",
                time = "Today"
            )
        }


        item {

            RecentActivityCard(
                icon = Icons.Default.BookOnline,
                title = "Bookings",
                subtitle = "${dashboard.bookings.total} bookings on platform",
                time = "Today"
            )
        }


        item {

            RecentActivityCard(
                icon = Icons.Default.Payment,
                title = "Subscription activity",
                subtitle = "${dashboard.subscriptions.active} active subscriptions",
                time = "Today"
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}


// =============================================================
// WELCOME CARD
// =============================================================

@Composable
private fun WelcomeCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SuperAdminGreen
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.White.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Welcome back",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Super Admin",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Monitor and manage the entire Book My Turf platform from one place.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    )
}


// =============================================================
// STAT CARD
// =============================================================

@Composable
private fun DashboardStatCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconBackground: Color,
    iconColor: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        iconBackground,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = value,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}


// =============================================================
// REVENUE
// =============================================================

@Composable
private fun RevenueCard(
    revenue: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            LightGreen,
                            RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column {

                    Text(
                        text = "Total Platform Revenue",
                        fontSize = 13.sp,
                        color = TextGray
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = String.format(
                            Locale.getDefault(),
                            "₹%.2f",
                            revenue
                        ),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }
    }
}


// =============================================================
// QUICK ACTION
// =============================================================

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBackground: Color,
    iconColor: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        iconBackground,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextGray
            )
        }
    }
}


// =============================================================
// SYSTEM STATUS
// =============================================================

@Composable
private fun SystemStatusCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            SystemStatusRow(
                title = "API Server",
                status = "Operational"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            SystemStatusRow(
                title = "Database",
                status = "Connected"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            SystemStatusRow(
                title = "Authentication",
                status = "Secure"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            SystemStatusRow(
                title = "Payment Service",
                status = "Ready"
            )
        }
    }
}


// =============================================================
// SYSTEM STATUS ROW
// =============================================================

@Composable
private fun SystemStatusRow(
    title: String,
    status: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(
                        Green,
                        CircleShape
                    )
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Text(
                text = title,
                fontSize = 13.sp,
                color = TextDark
            )
        }

        Text(
            text = status,
            fontSize = 12.sp,
            color = Green,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// =============================================================
// RECENT ACTIVITY
// =============================================================

@Composable
private fun RecentActivityCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    time: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        LightGreen,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }

            Text(
                text = time,
                fontSize = 11.sp,
                color = TextGray
            )
        }
    }
}