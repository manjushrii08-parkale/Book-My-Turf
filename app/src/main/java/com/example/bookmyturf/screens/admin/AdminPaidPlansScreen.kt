package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite

// =============================================================
// LOCAL COLORS
// =============================================================

private val AdminBlue = Color(0xFF2563EB)
private val AdminBlueLight = Color(0xFFEFF6FF)


// =============================================================
// ADMIN PAID PLANS SCREEN
// =============================================================

@Composable
fun AdminPaidPlansScreen(
    token: String,
    onPlanSelected: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminOffWhite)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            )
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Surface(
            modifier = Modifier
                .size(62.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            color = AdminLightGreen.copy(alpha = 0.20f)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = AdminForestGreen
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Choose Your Plan",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AdminDarkCharcoal,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Select a plan to continue managing your turf business.",
            modifier = Modifier.fillMaxWidth(),
            color = AdminGray,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =====================================================
        // MONTHLY PLAN
        // =====================================================

        AdminPlanCard(
            title = "Monthly Plan",
            price = "₹499",
            period = "/ month",
            description = "Flexible monthly subscription",
            isRecommended = false,
            buttonText = "Select Monthly Plan",
            onClick = {
                // Payment will be connected later
                onPlanSelected()
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // =====================================================
        // YEARLY PLAN
        // =====================================================

        AdminPlanCard(
            title = "Yearly Plan",
            price = "₹4,999",
            period = "/ year",
            description = "Best value for long-term use",
            isRecommended = true,
            buttonText = "Select Yearly Plan",
            onClick = {
                // Payment will be connected later
                onPlanSelected()
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // =====================================================
        // FEATURES INCLUDED
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AdminWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Included with every plan",
                    color = AdminDarkCharcoal,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                PlanFeature(
                    text = "Manage your turfs"
                )

                PlanFeature(
                    text = "Manage slots"
                )

                PlanFeature(
                    text = "View bookings"
                )

                PlanFeature(
                    text = "Admin dashboard"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Secure payment will be available soon.",
            modifier = Modifier.fillMaxWidth(),
            color = AdminGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}


// =============================================================
// PLAN CARD
// =============================================================

@Composable
private fun AdminPlanCard(
    title: String,
    price: String,
    period: String,
    description: String,
    isRecommended: Boolean,
    buttonText: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isRecommended) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = AdminForestGreen,
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isRecommended) {
                3.dp
            } else {
                1.dp
            }
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // =================================================
            // RECOMMENDED LABEL
            // =================================================

            if (isRecommended) {

                Surface(
                    shape = RoundedCornerShape(50),
                    color = AdminForestGreen
                ) {

                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = AdminWhite
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text = "RECOMMENDED",
                            color = AdminWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            // =================================================
            // TITLE
            // =================================================

            Text(
                text = title,
                color = AdminDarkCharcoal,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = description,
                color = AdminGray,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // PRICE
            // =================================================

            Row(
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = price,
                    color = AdminDarkGreen,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = period,
                    modifier = Modifier.padding(
                        bottom = 4.dp
                    ),
                    color = AdminGray,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // FEATURES
            // =================================================

            PlanFeature(
                text = "Manage your turfs"
            )

            PlanFeature(
                text = "Manage slots"
            )

            PlanFeature(
                text = "View bookings"
            )

            PlanFeature(
                text = "Admin dashboard"
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // ACTION BUTTON
            // =================================================

            if (isRecommended) {

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
                    )
                ) {

                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            } else {

                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AdminWhite,
                        contentColor = AdminForestGreen
                    )
                ) {

                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


// =============================================================
// PLAN FEATURE
// =============================================================

@Composable
private fun PlanFeature(
    text: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(22.dp),
            shape = CircleShape,
            color = AdminLightGreen.copy(alpha = 0.20f)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AdminForestGreen
                )
            }
        }

        Spacer(
            modifier = Modifier.width(9.dp)
        )

        Text(
            text = text,
            color = AdminDarkCharcoal,
            fontSize = 13.sp
        )
    }
}

