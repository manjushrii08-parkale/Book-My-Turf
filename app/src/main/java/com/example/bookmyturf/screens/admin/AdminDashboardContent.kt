package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminDashboardStatistics
import com.example.bookmyturf.data.model.AdminDashboardUser
import com.example.bookmyturf.data.model.AdminSubscription
import com.example.bookmyturf.screens.admin.components.AdminAccountCard
import com.example.bookmyturf.screens.admin.components.AdminQuickAction
import com.example.bookmyturf.screens.admin.components.AdminStatCard
import com.example.bookmyturf.screens.admin.components.AdminSubscriptionCard
import androidx.compose.material.icons.filled.Timer
private val TurfGreen = Color(0xFF14532D)
private val TurfGray = Color(0xFF64748B)

@Composable
fun AdminDashboardContent(
    modifier: Modifier,
    admin: AdminDashboardUser?,
    subscription: AdminSubscription?,
    statistics: AdminDashboardStatistics?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onManageTurfs: () -> Unit
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator(
                    color = TurfGreen
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Loading dashboard...",
                    color = TurfGray,
                    fontSize = 13.sp
                )
            }
        }

        // =====================================================
        // ERROR
        // =====================================================

        if (!error.isNullOrBlank()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    onClick = onRetry
                ) {
                    Text("Retry")
                }
            }
        }

        // =====================================================
        // WELCOME
        // =====================================================

        Text(
            text = "Welcome back, ${admin?.name ?: "Admin"} 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Manage your turf business from one place.",
            color = TurfGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =====================================================
        // SUBSCRIPTION
        // =====================================================

        AdminSubscriptionCard(
            subscription = subscription
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =====================================================
        // BUSINESS OVERVIEW
        // =====================================================

        Text(
            text = "Business Overview",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "A quick summary of your turf business",
            color = TurfGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

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

        // =====================================================
        // STATISTICS ROW 1
        // =====================================================

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

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =====================================================
        // STATISTICS ROW 2
        // =====================================================

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

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =====================================================
        // TODAY'S PERFORMANCE
        // =====================================================

        Text(
            text = "Today's Performance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Your business activity for today",
            color = TurfGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
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
                value = "₹${String.format("%,.0f", todayRevenue)}",
                icon = Icons.Default.CreditCard,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // =====================================================
        // QUICK ACTIONS
        // =====================================================

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Frequently used management options",
            color = TurfGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =====================================================
        // MANAGE TURFS
        // =====================================================

        AdminQuickAction(
            icon = Icons.Default.SportsSoccer,
            title = "Manage Turfs",
            description = "Add, edit and manage your turf grounds",
            onClick = onManageTurfs
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =====================================================
        // MANAGE SLOTS
        // =====================================================

        AdminQuickAction(
            icon = Icons.Default.Timer,
            title = "Manage Slots",
            description = "Create and manage turf time slots",
            onClick = {}
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =====================================================
        // BOOKINGS
        // =====================================================

        AdminQuickAction(
            icon = Icons.Default.CalendarMonth,
            title = "Bookings",
            description = "View and manage customer bookings",
            onClick = {}
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =====================================================
        // CUSTOMERS
        // =====================================================

        AdminQuickAction(
            icon = Icons.Default.Groups,
            title = "Customers",
            description = "View customers who booked your turfs",
            onClick = {}
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =====================================================
        // ACCOUNT
        // =====================================================

        AdminAccountCard(
            admin = admin
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}