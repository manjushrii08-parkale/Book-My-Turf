package com.example.bookmyturf.screens.admin.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminSubscription
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val TurfGreen = Color(0xFF14532D)
private val TurfGreenLight = Color(0xFFDCFCE7)
private val TurfGray = Color(0xFF64748B)
private val TurfOrange = Color(0xFFF59E0B)
private val TurfRed = Color(0xFFDC2626)


// =============================================================
// FORMAT SUBSCRIPTION DATE
// =============================================================

private fun formatSubscriptionDate(
    date: String?
): String {

    if (date.isNullOrBlank()) {
        return "-"
    }

    return try {

        val instant = Instant.parse(date)

        val formatter =
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault())

        formatter.format(instant)

    } catch (e: Exception) {

        date
    }
}


// =============================================================
// ADMIN SUBSCRIPTION CARD
// =============================================================

@Composable
fun AdminSubscriptionCard(
    subscription: AdminSubscription?,
    modifier: Modifier = Modifier
) {

    // =========================================================
    // BASIC DATA
    // =========================================================

    val status =
        subscription?.status ?: "INACTIVE"

    val isActive =
        status.equals(
            "ACTIVE",
            ignoreCase = true
        )

    val isTrial =
        subscription?.isTrial == true


    // =========================================================
    // FORMATTED DATES
    // =========================================================

    val startedDate =
        formatSubscriptionDate(
            subscription?.startedAt
        )

    val expiresDate =
        formatSubscriptionDate(
            subscription?.expiresAt
        )


    // =========================================================
    // STATUS COLORS
    // =========================================================

    val statusColor = when {

        isActive ->
            TurfGreen

        status.equals(
            "EXPIRED",
            ignoreCase = true
        ) ->
            TurfRed

        else ->
            TurfOrange
    }


    val statusBackground = when {

        isActive ->
            TurfGreenLight

        status.equals(
            "EXPIRED",
            ignoreCase = true
        ) ->
            Color(0xFFFEE2E2)

        else ->
            Color(0xFFFEF3C7)
    }


    // =========================================================
    // CARD
    // =========================================================

    Card(

        modifier =
            modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(20.dp)
        ) {


            // =================================================
            // HEADER
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // =================================================
                // SUBSCRIPTION ICON
                // =================================================

                Box(

                    modifier =
                        Modifier
                            .size(46.dp)
                            .clip(
                                RoundedCornerShape(14.dp)
                            )
                            .background(
                                TurfGreenLight
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            if (isTrial) {

                                Icons.Default.Timer

                            } else {

                                Icons.Default.CreditCard
                            },

                        contentDescription =
                            null,

                        tint =
                            TurfGreen
                    )
                }


                Spacer(
                    modifier =
                        Modifier.size(12.dp)
                )


                // =================================================
                // PLAN NAME
                // =================================================

                Column(

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            "Subscription",

                        fontSize =
                            13.sp,

                        color =
                            TurfGray
                    )


                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )


                    Text(

                        text =
                            subscription?.plan
                                ?: "No Active Plan",

                        fontSize =
                            19.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }


                // =================================================
                // STATUS
                // =================================================

                Box(

                    modifier =
                        Modifier
                            .clip(
                                RoundedCornerShape(50.dp)
                            )
                            .background(
                                statusBackground
                            )
                            .padding(
                                horizontal = 11.dp,
                                vertical = 6.dp
                            )
                ) {

                    Row(

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        if (isActive) {

                            Icon(

                                imageVector =
                                    Icons.Default.CheckCircle,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(14.dp),

                                tint =
                                    statusColor
                            )


                            Spacer(
                                modifier =
                                    Modifier.size(4.dp)
                            )
                        }


                        Text(

                            text =
                                status,

                            fontSize =
                                10.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                statusColor
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            // =================================================
            // DATE INFORMATION
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {


                // =================================================
                // STARTED DATE
                // =================================================

                SubscriptionInfoItem(

                    modifier =
                        Modifier.weight(1f),

                    icon =
                        Icons.Default.CalendarMonth,

                    label =
                        "Started",

                    value =
                        startedDate
                )


                // =================================================
                // EXPIRES DATE
                // =================================================

                SubscriptionInfoItem(

                    modifier =
                        Modifier.weight(1f),

                    icon =
                        Icons.Default.CalendarMonth,

                    label =
                        "Expires",

                    value =
                        expiresDate
                )
            }


            // =================================================
            // TRIAL INFORMATION
            // =================================================

            if (
                isTrial &&
                isActive
            ) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )


                Box(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                TurfGreenLight
                            )
                            .padding(12.dp)
                ) {

                    Text(

                        text =
                            "You're currently using your 15-day free trial.",

                        fontSize =
                            12.sp,

                        color =
                            TurfGreen,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }


            // =================================================
            // AMOUNT
            // =================================================

            if (
                subscription?.amount != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )


                Text(

                    text =
                        "Amount: ₹${subscription.amount}",

                    fontSize =
                        13.sp,

                    color =
                        TurfGray
                )
            }
        }
    }
}


// =============================================================
// SUBSCRIPTION INFO ITEM
// =============================================================

@Composable
private fun SubscriptionInfoItem(

    modifier: Modifier = Modifier,

    icon: ImageVector,

    label: String,

    value: String
) {

    Row(

        modifier =
            modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    Color(0xFFF8FAFC)
                )
                .padding(11.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // =====================================================
        // ICON
        // =====================================================

        Icon(

            imageVector =
                icon,

            contentDescription =
                null,

            modifier =
                Modifier.size(18.dp),

            tint =
                TurfGreen
        )


        Spacer(
            modifier =
                Modifier.size(8.dp)
        )


        // =====================================================
        // LABEL + VALUE
        // =====================================================

        Column {

            Text(

                text =
                    label,

                fontSize =
                    10.sp,

                color =
                    TurfGray
            )


            Text(

                text =
                    value,

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}