package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminDashboardStatistics
import com.example.bookmyturf.data.model.AdminDashboardUser
import com.example.bookmyturf.data.model.AdminSubscription

// =============================================================
// PREMIUM SUBSCRIPTION-STYLE COLORS
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)
private val WarningOrange = Color(0xFFFFB74D)


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
            .fillMaxSize()
            .background(Background)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(18.dp)
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
                Arrangement.spacedBy(10.dp)
        ) {

            DashboardMetricCard(
                title = "Turfs",
                value = totalTurfs.toString(),
                icon = Icons.Default.SportsSoccer,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Bookings",
                value = totalBookings.toString(),
                icon = Icons.Default.CalendarMonth,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Customers",
                value = totalCustomers.toString(),
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
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
            subtitle = "Manage your business"
        )

        QuickActionRow(
            icon = Icons.Default.SportsSoccer,
            title = "Manage Turfs",
            description = "View and manage your turf grounds",
            onClick = onManageTurfs
        )

        QuickActionRow(
            icon = Icons.Default.Timer,
            title = "Manage Slots",
            description = "Create and manage turf time slots",
            onClick = onManageTurfs
        )

        QuickActionRow(
            icon = Icons.Default.CalendarMonth,
            title = "Bookings",
            description = "View and manage customer bookings",
            onClick = onBookingsClick
        )

        QuickActionRow(
            icon = Icons.Default.Groups,
            title = "Customers",
            description = "View customers who booked your turfs",
            onClick = onBookingsClick
        )

        QuickActionRow(
            icon = Icons.Default.WorkspacePremium,
            title = "Subscription",
            description = "View and manage your current plan",
            onClick = onSubscriptionClick
        )

        // =====================================================
        // ACCOUNT
        // =====================================================


        Spacer(
            modifier = Modifier.height(6.dp)
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Good to see you",
            color = SecondaryText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "$adminName 👋",
            color = PrimaryText,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Here is your turf business overview.",
            color = MutedText,
            fontSize = 12.sp
        )
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
            Arrangement.spacedBy(3.dp)
    ) {

        Text(
            text = title,
            color = PrimaryText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            color = MutedText,
            fontSize = 11.sp
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

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
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
                        text = "CURRENT PLAN",
                        color = PrimaryGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = planText,
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                SubscriptionStatusBadge(
                    text = statusText,
                    active = isActive
                )
            }

            HorizontalDivider(
                color =
                    Border.copy(
                        alpha = 0.9f
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
                            SecondaryText,

                        fontSize =
                            11.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
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
                            PrimaryText,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                OutlinedButton(
                    onClick = onClick,

                    shape =
                        RoundedCornerShape(11.dp),

                    border =
                        BorderStroke(
                            1.dp,
                            Border.copy(
                                alpha = 0.9f
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
                            PrimaryText,

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
            if (active) {
                PrimaryGreen.copy(
                    alpha = 0.10f
                )
            } else {
                WarningOrange.copy(
                    alpha = 0.08f
                )
            }
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
                                PrimaryGreen
                            } else {
                                WarningOrange
                            }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(6.dp)
            )

            Text(
                text = text,
                color = PrimaryText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
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

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
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
                        text = "TODAY",
                        color = PrimaryGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text = "$todayBookings bookings",
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text = "Customer activity today",
                        color = MutedText,
                        fontSize = 11.sp
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text = "REVENUE",
                        color = MutedText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
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
                            LightGreen,

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
                    Border
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
                        PrimaryGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        "Keep your turf schedule up to date",

                    color =
                        SecondaryText,

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

    Surface(
        modifier =
            modifier.height(112.dp),

        shape =
            RoundedCornerShape(17.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
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
                                SurfaceElevated,

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
                        PrimaryGreen
                )
            }

            Column {

                Text(
                    text =
                        value,

                    color =
                        PrimaryText,

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        title,

                    color =
                        MutedText,

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

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
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
                                SurfaceElevated,
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
                            PrimaryGreen
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
                            PrimaryText,

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
                            MutedText,

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
                                    maxTurfs.coerceAtLeast(1)
                            ).coerceIn(
                            0f,
                            1f
                        )

                LinearProgressIndicator(
                    progress = {
                        progress
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(5.dp),

                    color =
                        PrimaryGreen,

                    trackColor =
                        SurfaceElevated
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
                    SecondaryText,

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
                            Border
                        )
                ) {

                    Text(
                        text =
                            "Manage",

                        color =
                            PrimaryText,

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
                                        SurfaceElevated,

                                    contentColor =
                                        SecondaryText
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
                                        PrimaryGreen,

                                    contentColor =
                                        Color(0xFF061008)
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
                                RoundedCornerShape(12.dp),

                            border =
                                BorderStroke(
                                    1.dp,
                                    Border
                                )
                        ) {

                            Text(
                                text =
                                    "Limit Reached",

                                color =
                                    MutedText,

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

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
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
                            SurfaceElevated,
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
                        PrimaryGreen
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
                        PrimaryText,

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
                        MutedText,

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
                    Modifier.size(20.dp),

                tint =
                    SecondaryText
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

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        color =
            SurfaceDark,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Column(
            modifier =
                Modifier.padding(17.dp),

            verticalArrangement =
                Arrangement.spacedBy(11.dp)
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
                                SurfaceElevated,
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
                            PrimaryGreen,

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
                            PrimaryText,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Your subscription has expired.",

                        color =
                            SecondaryText,

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
                    SecondaryText,

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
                            PrimaryGreen,

                        contentColor =
                            Color(0xFF061008)
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
        modifier =
            modifier
                .fillMaxSize()
                .background(Background)
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator(
            color =
                PrimaryGreen
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                "Loading dashboard...",

            color =
                SecondaryText,

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
        modifier =
            modifier
                .fillMaxSize()
                .background(Background)
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(
            modifier =
                Modifier.size(60.dp),

            shape =
                RoundedCornerShape(18.dp),

            color =
                SurfaceElevated
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

                    contentDescription =
                        null,

                    tint =
                        PrimaryGreen,

                    modifier =
                        Modifier.size(28.dp)
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        Text(
            text =
                "Unable to load dashboard",

            color =
                PrimaryText,

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
                SecondaryText,

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
                        PrimaryGreen,

                    contentColor =
                        Color(0xFF061008)
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