package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminDashboardStatistics
import com.example.bookmyturf.data.model.AdminDashboardUser
import com.example.bookmyturf.data.model.AdminSubscription
import com.example.bookmyturf.screens.admin.components.AdminAccountCard
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import androidx.compose.foundation.layout.fillMaxSize

// =============================================================
// ADMIN DASHBOARD
// =============================================================

@Composable
fun AdminDashboardContent(
    modifier: Modifier,
    admin: AdminDashboardUser?,
    subscription: AdminSubscription?,
    statistics: AdminDashboardStatistics?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onManageTurfs: () -> Unit,
    onSubscriptionClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    onAddTurfClick: () -> Unit = {}
) {

    // =========================================================
    // LOADING
    // =========================================================

    if (isLoading) {
        DashboardLoading(
            modifier = modifier
        )
        return
    }

    // =========================================================
    // ERROR
    // =========================================================

    if (!error.isNullOrBlank()) {
        DashboardError(
            modifier = modifier,
            message = error,
            onRetry = onRetry
        )
        return
    }

    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    val totalTurfs =
        statistics?.totalTurfs ?: 0

    val maxTurfs =
        statistics?.maxTurfs

    val totalBookings =
        statistics?.totalBookings ?: 0

    val totalCustomers =
        statistics?.totalCustomers ?: 0

    val todayBookings =
        statistics?.todayBookings ?: 0

    val todayRevenue =
        statistics?.todayRevenue ?: 0.0

    // =========================================================
    // SUBSCRIPTION
    // =========================================================

    val plan =
        subscription
            ?.plan
            ?.uppercase()
            ?: "NONE"

    val status =
        subscription
            ?.status
            ?.uppercase()
            ?: "INACTIVE"

    val isTrial =
        subscription?.isTrial == true ||
                plan == "FREE_TRIAL"

    val isActive =
        status == "ACTIVE"

    val isExpired =
        status == "EXPIRED"

    // =========================================================
    // TURF PERMISSION
    // =========================================================

    val canAddTurf =
        isActive &&
                !isExpired &&
                (
                        plan == "PRO" ||
                                (
                                        isTrial &&
                                                totalTurfs < 1
                                        )
                        )

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    Column(
        modifier = modifier
            .background(AdminOffWhite)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 18.dp,
                vertical = 18.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        // =====================================================
        // WELCOME
        // =====================================================

        WelcomeHeader(
            admin = admin
        )

        // =====================================================
        // SUBSCRIPTION
        // =====================================================

        SubscriptionSummaryCard(
            subscription = subscription,
            isActive = isActive,
            isExpired = isExpired,
            isTrial = isTrial,
            onClick = onSubscriptionClick
        )

        // =====================================================
        // EXPIRED ACCESS
        // =====================================================

        if (isExpired) {

            ExpiredAccessCard(
                onUpgradeClick =
                    onSubscriptionClick
            )
        }

        // =====================================================
        // TODAY OVERVIEW
        // =====================================================

        DashboardSectionHeader(
            title = "Today's Overview",
            subtitle = "Your business at a glance"
        )

        TodayOverviewCard(
            todayBookings = todayBookings,
            todayRevenue = todayRevenue
        )

        // =====================================================
        // BUSINESS STATS
        // =====================================================

        DashboardSectionHeader(
            title = "Business Overview",
            subtitle = "Your overall turf performance"
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            DashboardMetricCard(
                title = "Turfs",
                value = totalTurfs.toString(),
                icon =
                    Icons.Default.SportsSoccer,
                modifier =
                    Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Bookings",
                value = totalBookings.toString(),
                icon =
                    Icons.Default.CalendarMonth,
                modifier =
                    Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Customers",
                value = totalCustomers.toString(),
                icon =
                    Icons.Default.Groups,
                modifier =
                    Modifier.weight(1f)
            )
        }

        // =====================================================
        // TURF MANAGEMENT
        // =====================================================

        DashboardSectionHeader(
            title = "Turf Management",
            subtitle =
                if (maxTurfs != null) {
                    "$totalTurfs of $maxTurfs turf slots used"
                } else {
                    "$totalTurfs active turfs"
                }
        )

        TurfManagementCard(
            totalTurfs = totalTurfs,
            maxTurfs = maxTurfs,
            canAddTurf = canAddTurf,
            isExpired = isExpired,
            onManageTurfs = onManageTurfs,
            onAddTurf = onAddTurfClick
        )

        // =====================================================
        // QUICK ACTIONS
        // =====================================================

        DashboardSectionHeader(
            title = "Quick Actions",
            subtitle =
                "Manage your business"
        )

        QuickActionRow(
            icon =
                Icons.Default.SportsSoccer,
            title =
                "Manage Turfs",
            description =
                "View and manage your turf grounds",
            onClick =
                onManageTurfs
        )

        QuickActionRow(
            icon =
                Icons.Default.Timer,
            title =
                "Manage Slots",
            description =
                "Create and manage turf time slots",
            onClick =
                onManageTurfs
        )

        QuickActionRow(
            icon =
                Icons.Default.CalendarMonth,
            title =
                "Bookings",
            description =
                "View and manage customer bookings",
            onClick =
                onBookingsClick
        )

        QuickActionRow(
            icon =
                Icons.Default.Groups,
            title =
                "Customers",
            description =
                "View customers who booked your turfs",
            onClick =
                onBookingsClick
        )

        QuickActionRow(
            icon =
                Icons.Default.WorkspacePremium,
            title =
                "Subscription",
            description =
                "View and manage your current plan",
            onClick =
                onSubscriptionClick
        )

        // =====================================================
        // ACCOUNT
        // =====================================================

        AdminAccountCard(
            admin = admin
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )
    }
}


// =============================================================
// WELCOME HEADER
// =============================================================

@Composable
private fun WelcomeHeader(
    admin: AdminDashboardUser?
) {

    val adminName =
        admin?.name
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "Admin"

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    "Good to see you",

                color =
                    AdminGray,

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.Medium
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    "$adminName 👋",

                color =
                    AdminDarkCharcoal,

                style =
                    MaterialTheme.typography.headlineSmall,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    "Here is your turf business overview.",

                color =
                    AdminGray,

                fontSize =
                    12.sp
            )
        }
    }
}


// =============================================================
// SECTION HEADER
// =============================================================

@Composable
private fun DashboardSectionHeader(
    title: String,
    subtitle: String
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(2.dp)
    ) {

        Text(
            text =
                title,

            color =
                AdminDarkCharcoal,

            style =
                MaterialTheme.typography.titleMedium,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                subtitle,

            color =
                AdminGray,

            fontSize =
                11.sp
        )
    }
}


// =============================================================
// SUBSCRIPTION SUMMARY
// =============================================================

@Composable
private fun SubscriptionSummaryCard(
    subscription: AdminSubscription?,
    isActive: Boolean,
    isExpired: Boolean,
    isTrial: Boolean,
    onClick: () -> Unit
) {

    val planText =
        if (isTrial) {
            "FREE TRIAL"
        } else {
            subscription
                ?.plan
                ?.replace("_", " ")
                ?.uppercase()
                ?: "NO PLAN"
        }

    val statusText =
        when {
            isActive ->
                "ACTIVE"

            isExpired ->
                "EXPIRED"

            else ->
                "INACTIVE"
        }

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminDarkGreen
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(13.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "CURRENT PLAN",

                        color =
                            AdminLightGreen,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            planText,

                        color =
                            AdminWhite,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                SubscriptionStatusBadge(
                    text =
                        statusText,
                    active =
                        isActive
                )
            }

            HorizontalDivider(
                color =
                    AdminWhite.copy(
                        alpha = 0.12f
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            when {

                                isExpired ->
                                    "Your subscription has expired"

                                isTrial ->
                                    "Free trial access"

                                else ->
                                    "Subscription access"
                            },

                        color =
                            AdminWhite.copy(
                                alpha = 0.70f
                            ),

                        fontSize =
                            11.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            when {

                                isExpired ->
                                    "Upgrade to restore management access"

                                isTrial ->
                                    "15-day trial • 1 turf included"

                                else ->
                                    "Full management access"
                            },

                        color =
                            AdminWhite,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }

                OutlinedButton(
                    onClick =
                        onClick,

                    shape =
                        RoundedCornerShape(11.dp),

                    border =
                        BorderStroke(
                            1.dp,
                            AdminWhite.copy(
                                alpha = 0.40f
                            )
                        )
                ) {

                    Text(
                        text =
                            if (isExpired) {
                                "Upgrade"
                            } else {
                                "View Plan"
                            },

                        color =
                            AdminWhite,

                        fontSize =
                            11.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =============================================================
// SUBSCRIPTION STATUS
// =============================================================

@Composable
private fun SubscriptionStatusBadge(
    text: String,
    active: Boolean
) {

    Surface(
        shape =
            RoundedCornerShape(50.dp),

        color =
            AdminWhite.copy(
                alpha = 0.10f
            )
    ) {

        Row(
            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier.size(7.dp)
            ) {

                Canvas(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    drawCircle(
                        color =
                            if (active) {
                                AdminLightGreen
                            } else {
                                Color(0xFFFFB74D)
                            }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )

            Text(
                text =
                    text,

                color =
                    AdminWhite,

                fontSize =
                    10.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =============================================================
// TODAY OVERVIEW
// =============================================================

@Composable
private fun TodayOverviewCard(
    todayBookings: Int,
    todayRevenue: Double
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "TODAY",

                        color =
                            AdminForestGreen,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "$todayBookings bookings",

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            "Customer activity today",

                        color =
                            AdminGray,

                        fontSize =
                            11.sp
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text =
                            "REVENUE",

                        color =
                            AdminGray,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            0.8.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            formatIndianCurrency(
                                todayRevenue
                            ),

                        color =
                            AdminDarkGreen,

                        fontSize =
                            22.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            HorizontalDivider(
                color =
                    Color(0xFFE8ECE8)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CalendarMonth,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(16.dp),

                    tint =
                        AdminForestGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        "Keep your turf schedule up to date",

                    color =
                        AdminGray,

                    fontSize =
                        11.sp
                )
            }
        }
    }
}


// =============================================================
// BUSINESS METRIC CARD
// =============================================================

@Composable
private fun DashboardMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier
) {

    Card(
        modifier =
            modifier.height(112.dp),

        shape =
            RoundedCornerShape(17.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(13.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Box(
                modifier =
                    Modifier
                        .size(36.dp)
                        .background(
                            color =
                                AdminOffWhite,
                            shape =
                                RoundedCornerShape(
                                    10.dp
                                )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(18.dp),

                    tint =
                        AdminForestGreen
                )
            }

            Column {

                Text(
                    text =
                        value,

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        title,

                    color =
                        AdminGray,

                    fontSize =
                        10.sp
                )
            }
        }
    }
}


// =============================================================
// TURF MANAGEMENT
// =============================================================

@Composable
private fun TurfManagementCard(
    totalTurfs: Int,
    maxTurfs: Int?,
    canAddTurf: Boolean,
    isExpired: Boolean,
    onManageTurfs: () -> Unit,
    onAddTurf: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(13.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .background(
                                AdminOffWhite,
                                RoundedCornerShape(
                                    12.dp
                                )
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
                            Modifier.size(21.dp),

                        tint =
                            AdminDarkGreen
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Your Turfs",

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            if (maxTurfs != null) {
                                "$totalTurfs / $maxTurfs used"
                            } else {
                                "$totalTurfs active turfs"
                            },

                        color =
                            AdminGray,

                        fontSize =
                            11.sp
                    )
                }
            }

            // =================================================
            // CAPACITY
            // =================================================

            if (maxTurfs != null) {

                val progress =
                    (
                            totalTurfs.toFloat() /
                                    maxTurfs.coerceAtLeast(
                                        1
                                    )
                            ).coerceIn(
                            0f,
                            1f
                        )

                LinearProgressIndicator(
                    progress = {
                        progress
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        AdminForestGreen,

                    trackColor =
                        Color(0xFFE9EEE9)
                )
            }

            // =================================================
            // STATUS MESSAGE
            // =================================================

            Text(
                text =
                    when {

                        isExpired ->
                            "Subscription expired. Management is currently restricted."

                        canAddTurf ->
                            "You can add another turf."

                        maxTurfs != null &&
                                totalTurfs >= maxTurfs ->
                            "Your current plan has reached its turf limit."

                        else ->
                            "Your turf capacity is available."
                    },

                color =
                    AdminGray,

                fontSize =
                    11.sp,

                lineHeight =
                    16.sp
            )

            // =================================================
            // ACTIONS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick =
                        onManageTurfs,

                    modifier =
                        Modifier.weight(1f),

                    shape =
                        RoundedCornerShape(12.dp),

                    border =
                        BorderStroke(
                            1.dp,
                            AdminForestGreen.copy(
                                alpha = 0.45f
                            )
                        )
                ) {

                    Text(
                        text =
                            "Manage",

                        color =
                            AdminDarkGreen,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }

                when {

                    isExpired -> {

                        Button(
                            onClick =
                                onManageTurfs,

                            modifier =
                                Modifier.weight(1f),

                            shape =
                                RoundedCornerShape(12.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFFE8ECE8),
                                    contentColor =
                                        AdminGray
                                )
                        ) {

                            Text(
                                text =
                                    "View Only",

                                fontSize =
                                    12.sp
                            )
                        }
                    }

                    canAddTurf -> {

                        Button(
                            onClick =
                                onAddTurf,

                            modifier =
                                Modifier.weight(1f),

                            shape =
                                RoundedCornerShape(12.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        AdminLightGreen,

                                    contentColor =
                                        AdminDarkCharcoal
                                )
                        ) {

                            Text(
                                text =
                                    "Add Turf",

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }

                    else -> {

                        OutlinedButton(
                            onClick = {},

                            enabled =
                                false,

                            modifier =
                                Modifier.weight(1f),

                            shape =
                                RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text =
                                    "Limit Reached",

                                fontSize =
                                    11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// =============================================================
// QUICK ACTION
// =============================================================

@Composable
private fun QuickActionRow(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),

        onClick =
            onClick
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 15.dp,
                        vertical = 13.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(42.dp)
                        .background(
                            AdminOffWhite,
                            RoundedCornerShape(
                                12.dp
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(20.dp),

                    tint =
                        AdminDarkGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        title,

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                Text(
                    text =
                        description,

                    color =
                        AdminGray,

                    fontSize =
                        11.sp
                )
            }

            Icon(
                imageVector =
                    Icons.Default.ChevronRight,

                contentDescription =
                    "Open $title",

                modifier =
                    Modifier.size(21.dp),

                tint =
                    AdminForestGreen
            )
        }
    }
}


// =============================================================
// EXPIRED ACCESS
// =============================================================

@Composable
private fun ExpiredAccessCard(
    onUpgradeClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(17.dp),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .background(
                                AdminOffWhite,
                                RoundedCornerShape(
                                    11.dp
                                )
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Lock,

                        contentDescription =
                            null,

                        tint =
                            AdminForestGreen,

                        modifier =
                            Modifier.size(20.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Column {

                    Text(
                        text =
                            "View-Only Mode",

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Your subscription has expired.",

                        color =
                            AdminGray,

                        fontSize =
                            11.sp
                    )
                }
            }

            Text(
                text =
                    "Your existing business data remains available, " +
                            "but management actions are restricted until " +
                            "you upgrade your plan.",

                color =
                    AdminGray,

                fontSize =
                    11.sp,

                lineHeight =
                    17.sp
            )

            Button(
                onClick =
                    onUpgradeClick,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminLightGreen,

                        contentColor =
                            AdminDarkCharcoal
                    )
            ) {

                Text(
                    text =
                        "Upgrade to PRO",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// =============================================================
// LOADING
// =============================================================

@Composable
private fun DashboardLoading(
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                AdminOffWhite
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator(
            color =
                AdminForestGreen
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                "Loading dashboard...",

            color =
                AdminGray,

            fontSize =
                13.sp
        )
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun DashboardError(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                AdminOffWhite
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Icon(
            imageVector =
                Icons.Default.Lock,

            contentDescription =
                null,

            tint =
                AdminForestGreen,

            modifier =
                Modifier.size(42.dp)
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        Text(
            text =
                "Unable to load dashboard",

            color =
                AdminDarkCharcoal,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Text(
            text =
                message,

            color =
                AdminGray,

            fontSize =
                12.sp,

            textAlign =
                TextAlign.Center,

            lineHeight =
                17.sp
        )

        Spacer(
            modifier =
                Modifier.height(17.dp)
        )

        Button(
            onClick =
                onRetry,

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        AdminDarkGreen,

                    contentColor =
                        AdminWhite
                )
        ) {

            Text(
                text =
                    "Retry",

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =============================================================
// CURRENCY
// =============================================================

private fun formatIndianCurrency(
    amount: Double
): String {

    return "₹${String.format("%,.0f", amount)}"
}

