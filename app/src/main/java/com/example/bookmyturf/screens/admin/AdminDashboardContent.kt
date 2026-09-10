package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
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
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // REAL DASHBOARD DATA
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
    // SUBSCRIPTION DATA
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
            Arrangement.spacedBy(18.dp)
    ) {


        // =====================================================
        // WELCOME HEADER
        // =====================================================

        WelcomeHeader(
            admin = admin
        )


        // =====================================================
        // SUBSCRIPTION STATUS
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
                onUpgradeClick = onSubscriptionClick
            )
        }


        // =====================================================
        // BUSINESS OVERVIEW
        // =====================================================

        DashboardSectionHeader(
            title = "Business Overview",
            subtitle = "A quick look at your turf business"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            ProfessionalStatCard(
                title = "Total Turfs",
                value = totalTurfs.toString(),
                icon = Icons.Default.SportsSoccer,
                modifier = Modifier.weight(1f)
            )

            ProfessionalStatCard(
                title = "Total Bookings",
                value = totalBookings.toString(),
                icon = Icons.Default.CalendarMonth,
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            ProfessionalStatCard(
                title = "Customers",
                value = totalCustomers.toString(),
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
            )

            ProfessionalStatCard(
                title = "Turf Capacity",
                value = if (maxTurfs != null) {
                    "$totalTurfs / $maxTurfs"
                } else {
                    "$totalTurfs / ∞"
                },
                icon = Icons.Default.SportsSoccer,
                modifier = Modifier.weight(1f)
            )
        }


        // =====================================================
        // TODAY'S PERFORMANCE
        // =====================================================

        DashboardSectionHeader(
            title = "Today's Performance",
            subtitle = "Your business activity for today"
        )


        TodayPerformanceCard(
            todayBookings = todayBookings,
            todayRevenue = todayRevenue
        )


        // =====================================================
        // TURF MANAGEMENT
        // =====================================================

        DashboardSectionHeader(
            title = "Turf Management",
            subtitle = if (maxTurfs != null) {
                "$totalTurfs of $maxTurfs turfs used"
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
            subtitle = "Frequently used management options"
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
            description = "View customer bookings",
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

        AdminAccountCard(
            admin = admin
        )


        Spacer(
            modifier = Modifier.height(8.dp)
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
        verticalArrangement =
            Arrangement.spacedBy(5.dp)
    ) {

        Text(
            text = "Welcome back",
            color = AdminGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "$adminName 👋",
            color = AdminDarkCharcoal,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Manage your turf business from one place.",
            color = AdminGray,
            fontSize = 13.sp
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
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminDarkGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "CURRENT PLAN",
                        color = AdminLightGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = planText,
                        color = AdminWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


                StatusPill(
                    text = statusText
                )
            }


            HorizontalDivider(
                color = AdminWhite.copy(
                    alpha = 0.14f
                )
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = if (isExpired) {
                            "Your plan has expired"
                        } else {
                            "Subscription access"
                        },
                        color = AdminWhite.copy(
                            alpha = 0.75f
                        ),
                        fontSize = 12.sp
                    )

                    Text(
                        text = if (isExpired) {
                            "Upgrade to restore full access"
                        } else if (isTrial) {
                            "15-day trial • 1 turf"
                        } else {
                            "Full Admin access"
                        },
                        color = AdminWhite,
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.Medium
                    )
                }


                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        AdminWhite.copy(
                            alpha = 0.45f
                        )
                    )
                ) {

                    Text(
                        text = if (isExpired) {
                            "Upgrade"
                        } else {
                            "View Plan"
                        },
                        color = AdminWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =============================================================
// STATUS PILL
// =============================================================

@Composable
private fun StatusPill(
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(7.dp)
        ) {

            drawCircle(
                color = AdminLightGreen,
                radius = size.minDimension / 2
            )
        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Text(
            text = text,
            color = AdminWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


// =============================================================
// STAT CARD
// =============================================================

@Composable
private fun ProfessionalStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(126.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            AdminOffWhite,
                            RoundedCornerShape(11.dp)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp),
                        tint = AdminForestGreen
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    color = AdminGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
            }


            Text(
                text = value,
                color = AdminDarkCharcoal,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =============================================================
// TODAY PERFORMANCE CARD
// =============================================================

@Composable
private fun TodayPerformanceCard(
    todayBookings: Int,
    todayRevenue: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "TODAY",
                    color = AdminForestGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "$todayBookings bookings",
                    color = AdminDarkCharcoal,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Customer activity today",
                    color = AdminGray,
                    fontSize = 12.sp
                )
            }


            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text = "REVENUE",
                    color = AdminGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = formatIndianCurrency(
                        todayRevenue
                    ),
                    color = AdminDarkGreen,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            AdminOffWhite,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = AdminDarkGreen,
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
                        text = "Your Turfs",
                        color = AdminDarkCharcoal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (maxTurfs != null) {
                            "$totalTurfs / $maxTurfs used"
                        } else {
                            "$totalTurfs active turfs"
                        },
                        color = AdminGray,
                        fontSize = 12.sp
                    )
                }
            }


            // =====================================================
            // CAPACITY
            // =====================================================

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
                    modifier = Modifier.fillMaxWidth(),
                    color = AdminForestGreen,
                    trackColor = AdminOffWhite
                )
            }


            // =====================================================
            // ACTIONS
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = onManageTurfs,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Manage",
                        color = AdminDarkGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                when {

                    isExpired -> {

                        Button(
                            onClick = onManageTurfs,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AdminOffWhite,
                                contentColor = AdminGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text = "View Only"
                            )
                        }
                    }


                    canAddTurf -> {

                        Button(
                            onClick = onAddTurf,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    AdminLightGreen,
                                contentColor =
                                    AdminDarkCharcoal
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text = "Add Turf",
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }


                    else -> {

                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text = "Limit Reached"
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        AdminOffWhite,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AdminDarkGreen,
                    modifier = Modifier.size(20.dp)
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
                    color = AdminDarkCharcoal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    color = AdminGray,
                    fontSize = 12.sp
                )
            }


            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open $title",
                tint = AdminForestGreen,
                modifier = Modifier.size(22.dp)
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
            Arrangement.spacedBy(3.dp)
    ) {

        Text(
            text = title,
            color = AdminDarkCharcoal,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            color = AdminGray,
            fontSize = 12.sp
        )
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AdminForestGreen,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "View-Only Mode",
                    color = AdminDarkCharcoal,
                    fontWeight = FontWeight.Bold
                )
            }


            HorizontalDivider(
                color = AdminOffWhite
            )


            Text(
                text =
                    "Your subscription has expired. " +
                            "Existing business data remains " +
                            "available, but management actions " +
                            "are disabled.",
                color = AdminGray,
                fontSize = 12.sp
            )


            Button(
                onClick = onUpgradeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminLightGreen,
                    contentColor = AdminDarkCharcoal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "Upgrade to PRO",
                    fontWeight = FontWeight.Bold
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
            .background(AdminOffWhite)
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = AdminForestGreen
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Loading dashboard...",
            color = AdminGray,
            fontSize = 13.sp
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
            .background(AdminOffWhite)
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = AdminForestGreen,
            modifier = Modifier.size(42.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Unable to load dashboard",
            color = AdminDarkCharcoal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = message,
            color = AdminGray,
            textAlign = TextAlign.Center,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = AdminDarkGreen,
                contentColor = AdminWhite
            ),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "Retry",
                fontWeight = FontWeight.Bold
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