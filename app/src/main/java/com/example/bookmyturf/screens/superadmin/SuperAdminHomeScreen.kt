package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.SuperAdminDashboardData
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.SuperAdminViewModel
import java.text.NumberFormat
import java.util.Locale

private val BorderGray = Color(0xFFE3E8E1)
private val SoftGreen = Color(0xFFEAF3E6)
private val SoftBlue = Color(0xFFEAF1FA)
private val SoftOrange = Color(0xFFFFF1DF)
private val SoftPurple = Color(0xFFF1EBFA)
private val TextDark = AdminDarkCharcoal

private data class BottomItem(
    val title: String,
    val icon: ImageVector
)

private data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBackground: Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminDashboardScreen(
    onLogout: () -> Unit,
    onUsersClick: () -> Unit,
    onAdminsClick: () -> Unit = {},
    onSubscriptionsClick: () -> Unit = {},
    onTurfsClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {}
) {
    val viewModel: SuperAdminViewModel = viewModel()

    val dashboard by viewModel.dashboard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedBottomItem by remember {
        mutableIntStateOf(0)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    BackHandler {
        if (selectedBottomItem != 0) {
            selectedBottomItem = 0
        } else {
            showLogoutDialog = true
        }
    }

    val bottomItems = listOf(
        BottomItem("Dashboard", Icons.Default.Dashboard),
        BottomItem("Users", Icons.Default.People),
        BottomItem("Admins", Icons.Default.AdminPanelSettings),
        BottomItem("Reports", Icons.Default.Analytics)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AdminOffWhite,

        topBar = {
            SuperAdminTopBar(
                onRefresh = {
                    viewModel.loadDashboard()
                },
                onLogout = {
                    showLogoutDialog = true
                }
            )
        },

        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = AdminWhite,
                tonalElevation = 5.dp
            ) {
                bottomItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedBottomItem == index,
                        onClick = {
                            selectedBottomItem = index

                            when (index) {
                                1 -> onUsersClick()
                                2 -> onAdminsClick()
                                3 -> onSubscriptionsClick()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        when {
            isLoading && dashboard == null -> {
                LoadingContent(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            error != null && dashboard == null -> {
                ErrorContent(
                    message = error ?: "Unable to load dashboard.",
                    onRetry = {
                        viewModel.loadDashboard()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            dashboard != null -> {
                when (selectedBottomItem) {
                    0 -> {
                        DashboardContent(
                            data = dashboard!!,
                            innerPadding = innerPadding,
                            onUsersClick = onUsersClick,
                            onAdminsClick = onAdminsClick,
                            onSubscriptionsClick = onSubscriptionsClick,
                            onTurfsClick = onTurfsClick,
                            onBookingsClick = onBookingsClick
                        )
                    }

                    1 -> {
                        SectionPlaceholder(
                            title = "Users Management",
                            subtitle = "Manage registered users and account status.",
                            icon = Icons.Default.People,
                            onOpen = onUsersClick,
                            innerPadding = innerPadding
                        )
                    }

                    2 -> {
                        SectionPlaceholder(
                            title = "Admin Management",
                            subtitle = "Manage administrators and access.",
                            icon = Icons.Default.AdminPanelSettings,
                            onOpen = onAdminsClick,
                            innerPadding = innerPadding
                        )
                    }

                    3 -> {
                        SectionPlaceholder(
                            title = "Reports & Analytics",
                            subtitle = "Review revenue, subscriptions and bookings.",
                            icon = Icons.Default.Analytics,
                            onOpen = onSubscriptionsClick,
                            innerPadding = innerPadding
                        )
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(
                    text = "Logout",
                    color = TextDark,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout from the Super Admin panel?",
                    color = AdminGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text(
                        text = "Logout",
                        color = AdminDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = AdminGray
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuperAdminTopBar(
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "BookMyTurf",
                    color = AdminDarkCharcoal,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Super Admin Panel",
                    color = AdminGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },

        navigationIcon = {
            Surface(
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = AdminLightGreen.copy(alpha = 0.18f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SupervisorAccount,
                        contentDescription = "Super Admin",
                        tint = AdminDarkGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
        },

        actions = {
            Surface(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = AdminOffWhite
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onRefresh
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh dashboard",
                            modifier = Modifier.size(21.dp),
                            tint = AdminDarkGreen
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = AdminLightGreen.copy(alpha = 0.18f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onLogout
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            modifier = Modifier.size(21.dp),
                            tint = AdminDarkGreen
                        )
                    }
                }
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AdminWhite,
            titleContentColor = AdminDarkCharcoal,
            actionIconContentColor = AdminDarkGreen
        )
    )
}

@Composable
private fun DashboardContent(
    data: SuperAdminDashboardData,
    innerPadding: PaddingValues,
    onUsersClick: () -> Unit,
    onAdminsClick: () -> Unit,
    onSubscriptionsClick: () -> Unit,
    onTurfsClick: () -> Unit,
    onBookingsClick: () -> Unit
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(
            Locale.Builder()
                .setLanguage("en")
                .setRegion("IN")
                .build()
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 18.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Dashboard Overview",
                    color = TextDark,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Monitor your complete BookMyTurf platform.",
                    color = AdminGray,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            RevenueCard(
                revenue = currencyFormatter.format(
                    data.subscriptions.total_paid_revenue
                ),
                paidSubscriptions = data.subscriptions.paid,
                activeSubscriptions = data.subscriptions.active
            )
        }

        item {
            Text(
                text = "Platform Statistics",
                color = TextDark,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Users",
                    value = data.users.total.toString(),
                    subtitle = "${data.users.active} active",
                    icon = Icons.Default.People,
                    iconBackground = SoftBlue,
                    onClick = onUsersClick
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Admins",
                    value = data.admins.total.toString(),
                    subtitle = "${data.admins.active} active",
                    icon = Icons.Default.AdminPanelSettings,
                    iconBackground = SoftGreen,
                    onClick = onAdminsClick
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Turfs",
                    value = data.turfs.total.toString(),
                    subtitle = "Registered turfs",
                    icon = Icons.Default.SportsSoccer,
                    iconBackground = SoftOrange,
                    onClick = onTurfsClick
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Bookings",
                    value = data.bookings.total.toString(),
                    subtitle = "Total bookings",
                    icon = Icons.Default.CalendarMonth,
                    iconBackground = SoftPurple,
                    onClick = onBookingsClick
                )
            }
        }

        item {
            Text(
                text = "Subscription Overview",
                color = TextDark,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            SubscriptionCard(
                total = data.subscriptions.total,
                active = data.subscriptions.active,
                inactive = data.subscriptions.inactive,
                paid = data.subscriptions.paid,
                freeTrials = data.subscriptions.free_trials,
                pendingPayments = data.subscriptions.pending_payments,
                onClick = onSubscriptionsClick
            )
        }

        item {
            Text(
                text = "Quick Actions",
                color = TextDark,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val actions = listOf(
                    QuickAction(
                        title = "Manage Users",
                        subtitle = "View, block or unblock users",
                        icon = Icons.Default.People,
                        iconBackground = SoftBlue,
                        onClick = onUsersClick
                    ),
                    QuickAction(
                        title = "Manage Admins",
                        subtitle = "Review administrator accounts",
                        icon = Icons.Default.AdminPanelSettings,
                        iconBackground = SoftGreen,
                        onClick = onAdminsClick
                    ),
                    QuickAction(
                        title = "Subscription Reports",
                        subtitle = "Review plans, trials and payments",
                        icon = Icons.Default.Subscriptions,
                        iconBackground = SoftOrange,
                        onClick = onSubscriptionsClick
                    ),
                    QuickAction(
                        title = "Booking Reports",
                        subtitle = "Review platform booking activity",
                        icon = Icons.Default.CalendarMonth,
                        iconBackground = SoftPurple,
                        onClick = onBookingsClick
                    )
                )

                actions.forEach { action ->
                    QuickActionCard(action)
                }
            }
        }
    }
}

@Composable
private fun RevenueCard(
    revenue: String,
    paidSubscriptions: Int,
    activeSubscriptions: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminDarkGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Paid Revenue",
                        color = AdminWhite.copy(alpha = 0.78f),
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = revenue,
                        color = AdminWhite,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AdminLightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Revenue",
                        tint = AdminDarkGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RevenueInfo(
                    modifier = Modifier.weight(1f),
                    title = "Paid Plans",
                    value = paidSubscriptions.toString()
                )

                RevenueInfo(
                    modifier = Modifier.weight(1f),
                    title = "Active Plans",
                    value = activeSubscriptions.toString()
                )
            }
        }
    }
}

@Composable
private fun RevenueInfo(
    modifier: Modifier,
    title: String,
    value: String
) {
    Surface(
        modifier = modifier,
        color = AdminWhite.copy(alpha = 0.10f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = AdminWhite.copy(alpha = 0.70f),
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = AdminWhite,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBackground: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = BorderGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AdminDarkGreen,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                color = AdminGray,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = value,
                color = TextDark,
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = AdminDarkGreen,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SubscriptionCard(
    total: Int,
    active: Int,
    inactive: Int,
    paid: Int,
    freeTrials: Int,
    pendingPayments: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = BorderGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Subscriptions",
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "$total total subscriptions",
                        color = AdminGray,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open subscriptions",
                    tint = AdminDarkGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SubscriptionRow("Active subscriptions", active, total, AdminDarkGreen)
            Spacer(modifier = Modifier.height(12.dp))

            SubscriptionRow("Inactive subscriptions", inactive, total, AdminGray)
            Spacer(modifier = Modifier.height(12.dp))

            SubscriptionRow("Paid subscriptions", paid, total, AdminLightGreen)
            Spacer(modifier = Modifier.height(12.dp))

            SubscriptionRow("Free trials", freeTrials, total, Color(0xFF7B61A8))
            Spacer(modifier = Modifier.height(12.dp))

            SubscriptionRow("Pending payments", pendingPayments, total, Color(0xFFD58A27))
        }
    }
}

@Composable
private fun SubscriptionRow(
    label: String,
    value: Int,
    total: Int,
    color: Color
) {
    val progress = if (total > 0) {
        value.toFloat() / total.toFloat()
    } else {
        0f
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = AdminGray,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )

            Text(
                text = value.toString(),
                color = TextDark,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE9EDE7))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickAction
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                action.onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = BorderGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(action.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = AdminDarkGreen,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = action.title,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = action.subtitle,
                    color = AdminGray,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open ${action.title}",
                tint = AdminDarkGreen
            )
        }
    }
}

@Composable
private fun SectionPlaceholder(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onOpen: () -> Unit,
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AdminWhite
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(SoftGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = AdminDarkGreen,
                        modifier = Modifier.size(35.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = title,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    color = AdminGray,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(18.dp))

                TextButton(
                    onClick = onOpen
                ) {
                    Text(
                        text = "Open Section",
                        color = AdminDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = AdminDarkGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading dashboard...",
                color = AdminGray
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Something went wrong",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = AdminGray,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(18.dp))

            TextButton(
                onClick = onRetry
            ) {
                Text(
                    text = "Try Again",
                    color = AdminDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}