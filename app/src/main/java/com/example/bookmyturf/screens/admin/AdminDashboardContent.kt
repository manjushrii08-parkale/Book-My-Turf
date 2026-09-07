package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminDashboardStatistics
import com.example.bookmyturf.data.model.AdminDashboardUser
import com.example.bookmyturf.data.model.AdminSubscription
import com.example.bookmyturf.screens.admin.components.AdminAccountCard
import com.example.bookmyturf.screens.admin.components.AdminQuickAction
import com.example.bookmyturf.screens.admin.components.AdminStatCard
import com.example.bookmyturf.screens.admin.components.AdminSubscriptionCard
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
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
    // DATA
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

    val isExpired =
        status == "EXPIRED"

    val isActive =
        status == "ACTIVE"

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
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // =====================================================
        // WELCOME
        // =====================================================

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = "Welcome back,",
                color = AdminGray,
                fontSize = 14.sp
            )

            Text(
                text = "${admin?.name ?: "Admin"} 👋",
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


        // =====================================================
        // SUBSCRIPTION
        // =====================================================

        AdminSubscriptionCard(
            subscription = subscription
        )


        // =====================================================
        // EXPIRED NOTICE
        // =====================================================

        if (isExpired) {

            ExpiredAccessCard(
                onUpgradeClick = onSubscriptionClick
            )
        }


        // =====================================================
        // BUSINESS OVERVIEW
        // =====================================================

        SectionHeader(
            title = "Business Overview",
            subtitle = "A quick summary of your turf business"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AdminStatCard(
                title = "Total Turfs",
                value = totalTurfs.toString(),
                icon = Icons.Default.SportsSoccer,
                modifier = Modifier.weight(1f)
            )

            AdminStatCard(
                title = "Total Bookings",
                value = totalBookings.toString(),
                icon = Icons.Default.CalendarMonth,
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AdminStatCard(
                title = "Customers",
                value = totalCustomers.toString(),
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
            )

            AdminStatCard(
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

        SectionHeader(
            title = "Today's Performance",
            subtitle = "Your business activity for today"
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AdminStatCard(
                title = "Today's Bookings",
                value = todayBookings.toString(),
                icon = Icons.Default.CalendarMonth,
                modifier = Modifier.weight(1f)
            )

            AdminStatCard(
                title = "Today's Revenue",
                value = formatIndianCurrency(todayRevenue),
                icon = Icons.Default.CreditCard,
                modifier = Modifier.weight(1f)
            )
        }


        // =====================================================
        // TURF MANAGEMENT
        // =====================================================

        SectionHeader(
            title = "Turf Management",
            subtitle = if (maxTurfs != null) {
                "$totalTurfs of $maxTurfs turfs used"
            } else {
                "$totalTurfs turfs"
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

        SectionHeader(
            title = "Quick Actions",
            subtitle = "Frequently used management options"
        )


        AdminQuickAction(
            icon = Icons.Default.SportsSoccer,
            title = "Manage Turfs",
            description = "View and manage your turf grounds",
            onClick = onManageTurfs
        )


        AdminQuickAction(
            icon = Icons.Default.Timer,
            title = "Manage Slots",
            description = "Create and manage turf time slots",
            onClick = onManageTurfs
        )


        AdminQuickAction(
            icon = Icons.Default.CalendarMonth,
            title = "Bookings",
            description = "View and manage customer bookings",
            onClick = onBookingsClick
        )


        AdminQuickAction(
            icon = Icons.Default.Groups,
            title = "Customers",
            description = "View customers who booked your turfs",
            onClick = onBookingsClick
        )


        AdminQuickAction(
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
            modifier = Modifier.height(10.dp)
        )
    }
}


// =============================================================
// SECTION HEADER
// =============================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
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
            fontSize = 13.sp
        )
    }
}


// =============================================================
// TURF MANAGEMENT CARD
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
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = AdminDarkGreen,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Your Turfs",
                        color = AdminDarkCharcoal,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (maxTurfs != null) {
                            "$totalTurfs / $maxTurfs used"
                        } else {
                            "$totalTurfs active turfs"
                        },
                        color = AdminGray,
                        fontSize = 13.sp
                    )
                }
            }


            // =====================================================
            // CAPACITY
            // =====================================================

            if (maxTurfs != null) {

                androidx.compose.material3.LinearProgressIndicator(
                    progress = {
                        (
                                totalTurfs.toFloat() /
                                        maxTurfs.coerceAtLeast(1)
                                ).coerceIn(
                                0f,
                                1f
                            )
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = onManageTurfs,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(13.dp)
                ) {

                    Text(
                        text = "Manage",
                        color = AdminDarkGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                when {

                    isExpired -> {

                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(13.dp)
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
                                containerColor = AdminLightGreen,
                                contentColor = AdminDarkCharcoal
                            ),
                            shape = RoundedCornerShape(13.dp)
                        ) {

                            Text(
                                text = "Add Turf",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    else -> {

                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(13.dp)
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
// EXPIRED ACCESS CARD
// =============================================================

@Composable
private fun ExpiredAccessCard(
    onUpgradeClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AdminForestGreen,
                    modifier = Modifier.size(24.dp)
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
                text = "Your subscription has expired. " +
                        "Existing business data remains available, " +
                        "but management actions are disabled.",
                color = AdminGray,
                fontSize = 13.sp
            )

            Button(
                onClick = onUpgradeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminLightGreen,
                    contentColor = AdminDarkCharcoal
                ),
                shape = RoundedCornerShape(14.dp)
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = AdminForestGreen
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Loading dashboard...",
            color = AdminGray
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            shape = RoundedCornerShape(14.dp)
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
