package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
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

    // =========================================================
    // UI STATE
    // =========================================================

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

    val data =
        subscriptionResponse?.data

    val subscription =
        data?.subscription

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

        containerColor =
            AdminOffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text =
                                "Subscription",

                            color =
                                AdminDarkCharcoal,

                            fontSize =
                                18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Plans & access",

                            color =
                                AdminGray,

                            fontSize =
                                10.sp
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick =
                            onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                AdminDarkGreen
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            AdminWhite
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
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                innerPadding
                            )
                )
            }

            // =================================================
            // ERROR
            // =================================================

            !error.isNullOrBlank() -> {

                SubscriptionError(
                    message =
                        error
                            ?: "Something went wrong.",

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                innerPadding
                            ),

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
                            horizontal = 18.dp,
                            vertical = 18.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {

                    // =================================================
                    // ACTIVE
                    // =================================================

                    if (
                        isActive &&
                        subscription != null
                    ) {

                        ActivePlanSection(
                            plan =
                                plan,

                            isTrial =
                                isTrial,

                            isPro =
                                isPro,

                            expiresAt =
                                expiresAt,

                            onUpgradeClick =
                                onPaidPlanClick
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

                                    token =
                                        token,

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
            Arrangement.spacedBy(14.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        SectionHeading(
            eyebrow =
                "CURRENT SUBSCRIPTION",

            title =
                if (isTrial) {
                    "Free Trial"
                } else {
                    plan
                        .replace("_", " ")
                        .uppercase()
                },

            subtitle =
                if (isTrial) {
                    "Your trial is active and ready to use."
                } else if (isPro) {
                    "Your PRO plan is active."
                } else {
                    "Your subscription is currently active."
                }
        )

        // =====================================================
        // ACTIVE CARD
        // =====================================================

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AdminDarkGreen
                ),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(50.dp)
                                .clip(
                                    RoundedCornerShape(15.dp)
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

                            modifier =
                                Modifier.size(27.dp),

                            tint =
                                AdminDarkGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(13.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                if (isTrial) {
                                    "FREE TRIAL"
                                } else {
                                    "PRO PLAN"
                                },

                            color =
                                AdminWhite,

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Box(
                                modifier =
                                    Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(
                                            AdminLightGreen
                                        )
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(6.dp)
                            )

                            Text(
                                text =
                                    "ACTIVE",

                                color =
                                    AdminLightGreen,

                                fontSize =
                                    10.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                letterSpacing =
                                    0.8.sp
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color =
                        AdminWhite.copy(
                            alpha = 0.14f
                        )
                )

                SubscriptionDetailRow(
                    label =
                        "Expires",

                    value =
                        formatDate(
                            expiresAt
                        )
                )

                SubscriptionDetailRow(
                    label =
                        "Access",

                    value =
                        if (isTrial) {
                            "Full access • 1 turf"
                        } else {
                            "Full access • Multiple turfs"
                        }
                )

                SubscriptionDetailRow(
                    label =
                        "Bookings",

                    value =
                        "Enabled"
                )
            }
        }

        // =====================================================
        // TRIAL MESSAGE
        // =====================================================

        if (isTrial) {

            TrialInfoCard()

            Button(
                onClick =
                    onUpgradeClick,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(53.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminLightGreen,

                        contentColor =
                            AdminDarkCharcoal
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.WorkspacePremium,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Upgrade to PRO",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        // =====================================================
        // FEATURES
        // =====================================================

        IncludedFeaturesCard()
    }
}


// =============================================================
// TRIAL INFO
// =============================================================

@Composable
private fun TrialInfoCard() {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        color =
            AdminLightGreen.copy(
                alpha = 0.13f
            )
    ) {

        Row(
            modifier =
                Modifier.padding(15.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Default.Timer,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(20.dp),

                tint =
                    AdminForestGreen
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Text(
                text =
                    "Enjoy your free trial. Upgrade to PRO " +
                            "when you need more turf capacity.",

                color =
                    AdminDarkCharcoal,

                fontSize =
                    11.sp,

                lineHeight =
                    17.sp
            )
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
            Arrangement.spacedBy(14.dp)
    ) {

        SectionHeading(
            eyebrow =
                "ACCOUNT STATUS",

            title =
                "Subscription Expired",

            subtitle =
                "Your account is currently in view-only mode."
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp),

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
                    Modifier.padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(64.dp)
                            .clip(
                                RoundedCornerShape(18.dp)
                            )
                            .background(
                                AdminOffWhite
                            ),

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
                            Modifier.size(31.dp)
                    )
                }

                Text(
                    text =
                        "Your subscription has expired",

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )

                Text(
                    text =
                        "Your existing turf information remains available, " +
                                "but management features are restricted until " +
                                "you choose a new plan.",

                    color =
                        AdminGray,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp,

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

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(53.dp),

                    shape =
                        RoundedCornerShape(14.dp),

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
                            "Choose a PRO Plan",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =============================================================
// FIRST TIME PLAN
// =============================================================

@Composable
private fun FirstTimePlanSection(
    onStartTrial: () -> Unit,
    onPaidPlanClick: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        SectionHeading(
            eyebrow =
                "WELCOME",

            title =
                "Start your turf business",

            subtitle =
                "Begin with a free trial or choose PRO from the start."
        )

        // =====================================================
        // FREE TRIAL
        // =====================================================

        PlanOptionCard(
            title =
                "15-Day Free Trial",

            subtitle =
                "Start with no upfront cost.",

            badge =
                "FREE TO START",

            icon =
                Icons.Default.Timer,

            features =
                listOf(
                    "1 turf included",
                    "Manage slots",
                    "Manage bookings",
                    "Business dashboard"
                ),

            highlighted =
                true
        )

        Button(
            onClick =
                onStartTrial,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(53.dp),

            shape =
                RoundedCornerShape(14.dp),

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
                    "Start Free Trial",

                fontWeight =
                    FontWeight.Bold
            )
        }

        // =====================================================
        // PRO
        // =====================================================

        PlanOptionCard(
            title =
                "PRO",

            subtitle =
                "For growing turf businesses.",

            badge =
                "PAID PLAN",

            icon =
                Icons.Default.WorkspacePremium,

            features =
                listOf(
                    "Multiple turfs",
                    "Full Admin access",
                    "Manage slots",
                    "Manage bookings"
                ),

            highlighted =
                false
        )

        OutlinedButton(
            onClick =
                onPaidPlanClick,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(53.dp),

            shape =
                RoundedCornerShape(14.dp),

            border =
                BorderStroke(
                    1.dp,
                    AdminForestGreen.copy(
                        alpha = 0.55f
                    )
                )
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

        // =====================================================
        // NOTE
        // =====================================================

        SecurityNote()
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
            Arrangement.spacedBy(14.dp)
    ) {

        SectionHeading(
            eyebrow =
                "PLAN REQUIRED",

            title =
                "Choose a paid plan",

            subtitle =
                "Your free trial is no longer available."
        )

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
                    Modifier.padding(20.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(46.dp)
                            .clip(
                                RoundedCornerShape(13.dp)
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

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Upgrade to continue using full Admin features " +
                                "for your turf business.",

                    color =
                        AdminGray,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp
                )

                Button(
                    onClick =
                        onPaidPlanClick,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(53.dp),

                    shape =
                        RoundedCornerShape(14.dp),

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
    badge: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
                    if (highlighted) {
                        3.dp
                    } else {
                        1.dp
                    }
            )
    ) {

        Column(
            modifier =
                Modifier.padding(19.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
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

                Box(
                    modifier =
                        Modifier
                            .size(46.dp)
                            .clip(
                                RoundedCornerShape(13.dp)
                            )
                            .background(
                                if (highlighted) {
                                    AdminLightGreen.copy(
                                        alpha = 0.25f
                                    )
                                } else {
                                    AdminOffWhite
                                }
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
                            Modifier.size(23.dp),

                        tint =
                            AdminForestGreen
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(11.dp)
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
                            17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
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

                Surface(
                    shape =
                        RoundedCornerShape(7.dp),

                    color =
                        if (highlighted) {
                            AdminLightGreen.copy(
                                alpha = 0.20f
                            )
                        } else {
                            AdminOffWhite
                        }
                ) {

                    Text(
                        text =
                            badge,

                        modifier =
                            Modifier.padding(
                                horizontal = 7.dp,
                                vertical = 5.dp
                            ),

                        color =
                            AdminForestGreen,

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(
                color =
                    Color(0xFFE8ECE8)
            )

            // =================================================
            // FEATURES
            // =================================================

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
                            Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            feature,

                        color =
                            AdminDarkCharcoal,

                        fontSize =
                            12.sp
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
            RoundedCornerShape(19.dp),

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
                Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.VerifiedUser,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(19.dp),

                    tint =
                        AdminForestGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        "What's included",

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            HorizontalDivider(
                color =
                    Color(0xFFE8ECE8)
            )

            FeatureRow(
                text =
                    "Manage your turf"
            )

            FeatureRow(
                text =
                    "Create and manage slots"
            )

            FeatureRow(
                text =
                    "Manage customer bookings"
            )

            FeatureRow(
                text =
                    "View business activity"
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

        Box(
            modifier =
                Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        AdminLightGreen
                    )
        )

        Spacer(
            modifier =
                Modifier.width(9.dp)
        )

        Text(
            text =
                text,

            color =
                AdminDarkCharcoal,

            fontSize =
                12.sp
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

            fontSize =
                10.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                1.sp
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

            fontSize =
                12.sp,

            lineHeight =
                18.sp
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
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text =
                label,

            color =
                AdminWhite.copy(
                    alpha = 0.70f
                ),

            fontSize =
                12.sp
        )

        Text(
            text =
                value,

            color =
                AdminWhite,

            fontSize =
                12.sp,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}


// =============================================================
// SECURITY NOTE
// =============================================================

@Composable
private fun SecurityNote() {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp
                ),

        horizontalArrangement =
            Arrangement.Center,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector =
                Icons.Default.Lock,

            contentDescription =
                null,

            modifier =
                Modifier.size(13.dp),

            tint =
                AdminGray
        )

        Spacer(
            modifier =
                Modifier.width(5.dp)
        )

        Text(
            text =
                "Secure payment integration will be available soon.",

            color =
                AdminGray,

            fontSize =
                10.sp,

            textAlign =
                TextAlign.Center
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
                    AdminGray,

                fontSize =
                    12.sp
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
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Box(
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(
                            CircleShape
                        )
                        .background(
                            AdminLightGreen.copy(
                                alpha = 0.16f
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
                        Modifier.size(27.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(
                text =
                    "Unable to load subscription",

                color =
                    AdminDarkCharcoal,

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold
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
                    18.sp
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
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

