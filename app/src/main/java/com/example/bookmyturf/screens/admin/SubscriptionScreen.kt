package com.example.bookmyturf.screens.admin

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AdminViewModel

// =============================================================
// ADMIN SUBSCRIPTION SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSubscriptionScreen(
    token: String,
    viewModel: AdminViewModel,
    onSubscriptionActive: () -> Unit,
    onPaidPlanClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val subscriptionResponse by viewModel.subscription.collectAsState()

    // =========================================================
    // LOAD SUBSCRIPTION
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {
            viewModel.loadSubscriptionStatus(token)
        }
    }

    // =========================================================
    // SUBSCRIPTION DATA
    // =========================================================

    val data = subscriptionResponse?.data

    val subscription = data?.subscription

    val trialUsed =
        data?.trialUsed ?: false

    val plan =
        subscription?.plan ?: "NONE"

    val status =
        subscription?.status ?: "INACTIVE"

    val expiresAt =
        subscription?.expiresAt

    val isTrial =
        subscription?.isTrial ?: false

    val isActive =
        status.equals(
            "ACTIVE",
            ignoreCase = true
        )

    val isExpired =
        status.equals(
            "EXPIRED",
            ignoreCase = true
        )

    val isPro =
        plan.equals(
            "PRO",
            ignoreCase = true
        )

    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(
        containerColor = AdminOffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Subscription",
                            color = AdminDarkCharcoal,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Manage your plan",
                            color = AdminGray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AdminDarkGreen
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminWhite
                )
            )
        }

    ) { innerPadding ->

        when {

            // =================================================
            // LOADING
            // =================================================

            isLoading -> {

                SubscriptionLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            // =================================================
            // ERROR
            // =================================================

            !error.isNullOrBlank() -> {

                SubscriptionError(
                    message = error
                        ?: "Something went wrong.",

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),

                    onRetry = {

                        viewModel.clearError()

                        if (token.isNotBlank()) {

                            viewModel.loadSubscriptionStatus(
                                token
                            )
                        }
                    }
                )
            }

            // =================================================
            // CONTENT
            // =================================================

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(20.dp)
                ) {

                    // =================================================
                    // ACTIVE
                    // =================================================

                    if (
                        isActive &&
                        subscription != null
                    ) {

                        ActivePlanSection(
                            plan = plan,
                            isTrial = isTrial,
                            isPro = isPro,
                            expiresAt = expiresAt,
                            onUpgradeClick = onPaidPlanClick
                        )

                    }

                    // =================================================
                    // EXPIRED
                    // =================================================

                    else if (isExpired) {

                        ExpiredPlanSection(
                            onUpgradeClick =
                                onPaidPlanClick
                        )
                    }

                    // =================================================
                    // FIRST TIME
                    // =================================================

                    else if (
                        !trialUsed &&
                        subscription == null
                    ) {

                        FirstTimePlanSection(

                            onStartTrial = {

                                viewModel.startFreeTrial(

                                    token = token,

                                    onSuccess = {

                                        onSubscriptionActive()
                                    }
                                )
                            },

                            onPaidPlanClick =
                                onPaidPlanClick
                        )
                    }

                    // =================================================
                    // PAID PLAN REQUIRED
                    // =================================================

                    else {

                        PaidPlanRequiredSection(
                            onPaidPlanClick =
                                onPaidPlanClick
                        )
                    }
                }
            }
        }
    }
}


// =============================================================
// ACTIVE PLAN
// =============================================================

@Composable
private fun ActivePlanSection(
    plan: String,
    isTrial: Boolean,
    isPro: Boolean,
    expiresAt: String?,
    onUpgradeClick: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        SectionHeading(
            eyebrow = "YOUR PLAN",

            title = if (isTrial) {
                "Free Trial"
            } else {
                plan.replace(
                    "_",
                    " "
                )
            },

            subtitle = if (isTrial) {
                "Everything you need to get started."
            } else {
                "Your PRO subscription is active."
            }
        )

        // =========================================================
        // ACTIVE CARD
        // =========================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AdminDarkGreen
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement =
                    Arrangement.spacedBy(18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .background(
                                AdminLightGreen
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.WorkspacePremium,
                            contentDescription =
                                null,
                            tint = AdminDarkGreen
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = if (isTrial) {
                                "FREE TRIAL"
                            } else {
                                "PRO"
                            },
                            color = AdminWhite,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text = "ACTIVE",
                            color = AdminLightGreen,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }

                HorizontalDivider(
                    color =
                        AdminWhite.copy(
                            alpha = 0.16f
                        )
                )

                SubscriptionDetailRow(
                    label = "Expires",
                    value = formatDate(
                        expiresAt
                    )
                )

                SubscriptionDetailRow(
                    label = "Access",
                    value = if (isTrial) {
                        "Full access • 1 turf"
                    } else {
                        "Full access • Multiple turfs"
                    }
                )

                SubscriptionDetailRow(
                    label = "Booking access",
                    value = "Enabled"
                )
            }
        }

        // =========================================================
        // FEATURES
        // =========================================================

        IncludedFeaturesCard()

        // =========================================================
        // UPGRADE BUTTON
        // =========================================================

        if (isTrial) {

            Button(
                onClick = onUpgradeClick,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        AdminLightGreen,

                    contentColor =
                        AdminDarkCharcoal
                ),

                shape =
                    RoundedCornerShape(16.dp)
            ) {

                Text(
                    text = "Upgrade to PRO",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// =============================================================
// EXPIRED PLAN
// =============================================================

@Composable
private fun ExpiredPlanSection(
    onUpgradeClick: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        SectionHeading(
            eyebrow = "ACCOUNT STATUS",
            title = "Subscription Expired",
            subtitle = "Your account is currently view-only."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AdminWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                        .background(AdminOffWhite),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Schedule,

                        contentDescription =
                            null,

                        tint =
                            AdminForestGreen,

                        modifier =
                            Modifier.size(32.dp)
                    )
                }

                Text(
                    text =
                        "Your subscription has expired",

                    color =
                        AdminDarkCharcoal,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )

                Text(
                    text =
                        "Your existing turf data remains safe. " +
                                "You can continue viewing your data, " +
                                "but new bookings, payments and Admin " +
                                "actions are disabled.",

                    color =
                        AdminGray,

                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Button(
                    onClick =
                        onUpgradeClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminLightGreen,

                            contentColor =
                                AdminDarkCharcoal
                        ),

                    shape =
                        RoundedCornerShape(16.dp)
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
}


// =============================================================
// FIRST TIME
// =============================================================

@Composable
private fun FirstTimePlanSection(
    onStartTrial: () -> Unit,
    onPaidPlanClick: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        SectionHeading(
            eyebrow = "WELCOME",
            title = "Choose your plan",
            subtitle =
                "Start your turf business with the plan that suits you."
        )

        // =========================================================
        // FREE TRIAL
        // =========================================================

        PlanOptionCard(
            title = "15-Day Free Trial",

            subtitle =
                "Start managing your turf with no upfront cost.",

            features = listOf(
                "1 turf included",
                "Manage slots",
                "Manage bookings",
                "Business dashboard"
            ),

            highlighted = true
        )

        Button(
            onClick =
                onStartTrial,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        AdminLightGreen,

                    contentColor =
                        AdminDarkCharcoal
                ),

            shape =
                RoundedCornerShape(16.dp)
        ) {

            Text(
                text =
                    "Start Free Trial",

                fontWeight =
                    FontWeight.Bold
            )
        }

        // =========================================================
        // PRO
        // =========================================================

        PlanOptionCard(
            title = "PRO",

            subtitle =
                "For growing turf businesses.",

            features = listOf(
                "Multiple turfs",
                "Full Admin access",
                "Manage slots",
                "Manage bookings"
            ),

            highlighted = false
        )

        OutlinedButton(
            onClick =
                onPaidPlanClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(16.dp)
        ) {

            Text(
                text =
                    "View PRO Plans",

                color =
                    AdminDarkGreen,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =============================================================
// PAID PLAN REQUIRED
// =============================================================

@Composable
private fun PaidPlanRequiredSection(
    onPaidPlanClick: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        SectionHeading(
            eyebrow = "PLAN REQUIRED",
            title = "Choose a paid plan",
            subtitle =
                "Your free trial is no longer available."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AdminWhite
                )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            AdminOffWhite
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
                            Modifier.size(22.dp)
                    )
                }

                Text(
                    text =
                        "Paid subscription required",

                    color =
                        AdminDarkCharcoal,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Upgrade to continue using full Admin features.",

                    color =
                        AdminGray
                )

                Button(
                    onClick =
                        onPaidPlanClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminLightGreen,

                            contentColor =
                                AdminDarkCharcoal
                        ),

                    shape =
                        RoundedCornerShape(14.dp)
                ) {

                    Text(
                        text =
                            "View PRO Plans",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =============================================================
// PLAN OPTION CARD
// =============================================================

@Composable
private fun PlanOptionCard(
    title: String,
    subtitle: String,
    features: List<String>,
    highlighted: Boolean
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
                defaultElevation =
                    if (highlighted) 3.dp else 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            if (highlighted) {
                                AdminLightGreen
                            } else {
                                AdminOffWhite
                            }
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.WorkspacePremium,

                        contentDescription =
                            null,

                        tint =
                            AdminDarkGreen
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text =
                            title,

                        color =
                            AdminDarkCharcoal,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            subtitle,

                        color =
                            AdminGray
                    )
                }
            }

            features.forEach { feature ->

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,

                        contentDescription =
                            null,

                        tint =
                            AdminForestGreen,

                        modifier =
                            Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(9.dp)
                    )

                    Text(
                        text =
                            feature,

                        color =
                            AdminDarkCharcoal
                    )
                }
            }
        }
    }
}


// =============================================================
// INCLUDED FEATURES
// =============================================================

@Composable
private fun IncludedFeaturesCard() {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminWhite
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(13.dp)
        ) {

            Text(
                text =
                    "What's included",

                color =
                    AdminDarkCharcoal,

                fontWeight =
                    FontWeight.Bold
            )

            FeatureRow(
                text = "Manage your turf"
            )

            FeatureRow(
                text = "Create and manage slots"
            )

            FeatureRow(
                text = "Manage bookings"
            )

            FeatureRow(
                text = "View business activity"
            )
        }
    }
}


// =============================================================
// FEATURE ROW
// =============================================================

@Composable
private fun FeatureRow(
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector =
                Icons.Default.CheckCircle,

            contentDescription =
                null,

            tint =
                AdminForestGreen,

            modifier =
                Modifier.size(19.dp)
        )

        Spacer(
            modifier =
                Modifier.width(9.dp)
        )

        Text(
            text =
                text,

            color =
                AdminDarkCharcoal
        )
    }
}


// =============================================================
// SECTION HEADING
// =============================================================

@Composable
private fun SectionHeading(
    eyebrow: String,
    title: String,
    subtitle: String
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text =
                eyebrow,

            color =
                AdminForestGreen,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                title,

            color =
                AdminDarkCharcoal,

            style =
                MaterialTheme.typography.headlineSmall,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                subtitle,

            color =
                AdminGray,

            style =
                MaterialTheme.typography.bodyMedium
        )
    }
}


// =============================================================
// DETAIL ROW
// =============================================================

@Composable
private fun SubscriptionDetailRow(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                label,

            color =
                AdminWhite.copy(
                    alpha = 0.70f
                )
        )

        Text(
            text =
                value,

            color =
                AdminWhite,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}


// =============================================================
// LOADING
// =============================================================

@Composable
private fun SubscriptionLoading(
    modifier: Modifier
) {

    Box(
        modifier =
            modifier,

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
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
                    "Loading subscription...",

                color =
                    AdminGray
            )
        }
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun SubscriptionError(
    message: String,
    modifier: Modifier,
    onRetry: () -> Unit
) {

    Box(
        modifier =
            modifier.padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "Unable to load subscription",

                color =
                    AdminDarkCharcoal,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    message,

                color =
                    AdminGray,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Button(
                onClick =
                    onRetry,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDarkGreen,

                        contentColor =
                            AdminWhite
                    ),

                shape =
                    RoundedCornerShape(14.dp)
            ) {

                Text(
                    text =
                        "Retry"
                )
            }
        }
    }
}


// =============================================================
// DATE FORMAT
// =============================================================

private fun formatDate(
    value: String?
): String {

    if (value.isNullOrBlank()) {
        return "—"
    }

    return value
        .substringBefore("T")
        .takeIf {
            it.length == 10
        }
        ?: "—"
}

