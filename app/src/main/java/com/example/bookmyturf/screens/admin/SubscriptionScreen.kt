package com.example.bookmyturf.screens.admin
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.viewmodel.AdminViewModel


// =============================================================
// PREMIUM BOOKMYTURF COLORS
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


// =============================================================
// ADMIN SUBSCRIPTION SCREEN
// =============================================================

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

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            viewModel.loadSubscriptionStatus(token)
        }
    }

    val data = subscriptionResponse?.data

    val subscription = data?.subscription

    val trialUsed = data?.trialUsed ?: false

    val plan = subscription?.plan ?: "NONE"

    val status = subscription?.status ?: "INACTIVE"

    val expiresAt = subscription?.expiresAt

    val isTrial = subscription?.isTrial ?: false

    val isActive = status.equals(
        "ACTIVE",
        ignoreCase = true
    )

    val isExpired = status.equals(
        "EXPIRED",
        ignoreCase = true
    )

    val isPro = plan.equals(
        "PRO",
        ignoreCase = true
    )

    Scaffold(
        containerColor = Background,

        topBar = {
            PremiumSubscriptionTopBar(
                onBackClick = onBackClick
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
                            viewModel.loadSubscriptionStatus(token)
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
                            vertical = 12.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(22.dp)
                ) {

                    // =================================================
                    // ACTIVE PLAN
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
                            onUpgradeClick =
                                onPaidPlanClick
                        )

                    }

                    // =================================================
                    // EXPIRED PLAN
                    // =================================================

                    else if (isExpired) {

                        ExpiredPlanSection(
                            onUpgradeClick =
                                onPaidPlanClick
                        )

                    }

                    // =================================================
                    // FIRST TIME USER
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

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }
            }
        }
    }
}


// =============================================================
// PREMIUM TOP BAR
// =============================================================
@Composable
private fun PremiumSubscriptionTopBar(
    onBackClick: () -> Unit
) {
    Surface(
        color = Background,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF020907),
                            Color(0xFF071810),
                            Color(0xFF020907)
                        )
                    )
                )
                .statusBarsPadding()
        ) {
            Column {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                ) {

                    // Back button — left side
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 14.dp)
                            .size(42.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceElevated
                    ) {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = PrimaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // BookMyTurf — exact center
                    Text(
                        text = "BookMyTurf",
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }

                // Bottom divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Border,
                                    PrimaryGreen.copy(alpha = 0.18f),
                                    Border,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}
// =============================================================
// ACTIVE PLAN SECTION
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
            Arrangement.spacedBy(18.dp)
    ) {

        PremiumHeader(
            eyebrow =
                "MEMBERSHIP",

            title =
                if (isTrial) {
                    "Your free trial"
                } else {
                    "Your PRO membership"
                },

            subtitle =
                if (isTrial) {
                    "Everything you need to start managing your turf."
                } else {
                    "Your premium business access is active."
                }
        )

        // =====================================================
        // MEMBERSHIP HERO
        // =====================================================

        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(28.dp),

            color =
                SurfaceDark,

            border =
                BorderStroke(
                    1.dp,
                    PrimaryGreen.copy(
                        alpha = 0.25f
                    )
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF10271B),
                                SurfaceDark,
                                Background
                            )
                        )
                    )
                    .padding(23.dp)
            ) {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(22.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        PremiumIconBox(
                            icon =
                                Icons.Default.WorkspacePremium
                        )

                        Spacer(
                            modifier =
                                Modifier.width(14.dp)
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
                                        "PRO MEMBERSHIP"
                                    },

                                color =
                                    PrimaryText,

                                fontSize =
                                    18.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                letterSpacing =
                                    0.3.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(5.dp)
                            )

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(
                                            LightGreen
                                        )
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(7.dp)
                                )

                                Text(
                                    text =
                                        "ACTIVE",

                                    color =
                                        LightGreen,

                                    fontSize =
                                        10.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    letterSpacing =
                                        1.2.sp
                                )
                            }
                        }
                    }

                    Text(
                        text =
                            if (isTrial) {
                                "Your business is ready to play."
                            } else {
                                "Everything your turf business needs."
                            },

                        color =
                            SecondaryText,

                        fontSize =
                            13.sp,

                        lineHeight =
                            19.sp
                    )

                    HorizontalDivider(
                        color =
                            Border
                    )

                    PremiumDetailRow(
                        icon =
                            Icons.Default.CalendarMonth,

                        label =
                            "Valid until",

                        value =
                            formatDate(expiresAt)
                    )

                    PremiumDetailRow(
                        icon =
                            Icons.Default.SportsSoccer,

                        label =
                            "Turf capacity",

                        value =
                            if (isTrial) {
                                "1 turf"
                            } else {
                                "Multiple turfs"
                            }
                    )

                    PremiumDetailRow(
                        icon =
                            Icons.Default.CheckCircle,

                        label =
                            "Booking management",

                        value =
                            "Enabled"
                    )
                }
            }
        }

        // =====================================================
        // TRIAL BANNER
        // =====================================================

        if (isTrial) {

            TrialPremiumBanner()

            PremiumPrimaryButton(
                text =
                    "Upgrade to PRO",

                icon =
                    Icons.Default.ArrowForward,

                onClick =
                    onUpgradeClick
            )
        }

        IncludedFeatures()
    }
}


// =============================================================
// PREMIUM HEADER
// =============================================================

@Composable
private fun PremiumHeader(
    eyebrow: String,
    title: String,
    subtitle: String
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(5.dp)
    ) {

        Text(
            text =
                eyebrow,

            color =
                PrimaryGreen,

            fontSize =
                9.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                1.7.sp
        )

        Text(
            text =
                title,

            color =
                PrimaryText,

            fontSize =
                27.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing =
                (-0.6).sp
        )

        Text(
            text =
                subtitle,

            color =
                SecondaryText,

            fontSize =
                12.sp,

            lineHeight =
                18.sp
        )
    }
}


// =============================================================
// PREMIUM ICON BOX
// =============================================================

@Composable
private fun PremiumIconBox(
    icon: ImageVector
) {

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                PrimaryGreen.copy(
                    alpha = 0.13f
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
                Modifier.size(28.dp),

            tint =
                BrightGreen
        )
    }
}


// =============================================================
// TRIAL BANNER
// =============================================================

@Composable
private fun TrialPremiumBanner() {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(19.dp),

        color =
            SurfaceElevated,

        border =
            BorderStroke(
                1.dp,
                Border
            )
    ) {

        Row(
            modifier =
                Modifier.padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryGreen.copy(
                            alpha = 0.11f
                        )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Timer,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(19.dp),

                    tint =
                        LightGreen
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
                        "Your trial is active",

                    color =
                        PrimaryText,

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Upgrade whenever you need more turf capacity.",

                    color =
                        SecondaryText,

                    fontSize =
                        10.sp,

                    lineHeight =
                        15.sp
                )
            }
        }
    }
}


// =============================================================
// INCLUDED FEATURES
// =============================================================

@Composable
private fun IncludedFeatures() {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

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
                Modifier.padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
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
                        PrimaryGreen
                )

                Spacer(
                    modifier =
                        Modifier.width(9.dp)
                )

                Column {

                    Text(
                        text =
                            "Included with your access",

                        color =
                            PrimaryText,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Text(
                        text =
                            "Essential tools for your turf business",

                        color =
                            MutedText,

                        fontSize =
                            10.sp
                    )
                }
            }

            HorizontalDivider(
                color =
                    Border
            )

            FeatureItem(
                text =
                    "Manage your turf"
            )

            FeatureItem(
                text =
                    "Create and manage slots"
            )

            FeatureItem(
                text =
                    "Manage customer bookings"
            )

            FeatureItem(
                text =
                    "View business activity"
            )
        }
    }
}


// =============================================================
// FEATURE ITEM
// =============================================================

@Composable
private fun FeatureItem(
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    PrimaryGreen.copy(
                        alpha = 0.10f
                    )
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.Check,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(12.dp),

                tint =
                    LightGreen
            )
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Text(
            text =
                text,

            color =
                SecondaryText,

            fontSize =
                12.sp
        )
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
            Arrangement.spacedBy(18.dp)
    ) {

        PremiumHeader(
            eyebrow =
                "ACCOUNT STATUS",

            title =
                "Subscription expired",

            subtitle =
                "Renew your membership to restore full management access."
        )

        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(27.dp),

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
                    Modifier.padding(25.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.spacedBy(13.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            PrimaryGreen.copy(
                                alpha = 0.09f
                            )
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Schedule,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(29.dp),

                        tint =
                            LightGreen
                    )
                }

                Text(
                    text =
                        "Your plan has expired",

                    color =
                        PrimaryText,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Your existing turf information remains available, " +
                                "but management features are restricted " +
                                "until you choose a new plan.",

                    color =
                        SecondaryText,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp,

                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                PremiumPrimaryButton(
                    text =
                        "Choose a PRO Plan",

                    icon =
                        Icons.Default.ArrowForward,

                    onClick =
                        onUpgradeClick
                )
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
            Arrangement.spacedBy(18.dp)
    ) {

        PremiumHeader(
            eyebrow =
                "WELCOME TO BOOKMYTURF",

            title =
                "Start your business",

            subtitle =
                "Choose how you want to begin managing your turf."
        )

        PremiumPlanCard(
            title =
                "15-Day Free Trial",

            subtitle =
                "Explore the essentials with no upfront cost.",

            badge =
                "FREE",

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
                false
        )

        PremiumPrimaryButton(
            text =
                "Start Free Trial",

            icon =
                Icons.Default.ArrowForward,

            onClick =
                onStartTrial
        )

        PremiumProCard(
            onClick =
                onPaidPlanClick
        )

        SecurityNote()
    }
}


// =============================================================
// PREMIUM PRO CARD
// =============================================================

@Composable
private fun PremiumProCard(
    onClick: () -> Unit
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(27.dp),

        color =
            SurfaceHighlight,

        border =
            BorderStroke(
                1.dp,
                PrimaryGreen.copy(
                    alpha = 0.42f
                )
            )
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    PrimaryGreen,
                                    LightGreen,
                                    PrimaryGreen
                                )
                        )
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text =
                        "PREMIUM BUSINESS ACCESS",

                    color =
                        Color(0xFF071008),

                    fontSize =
                        9.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        1.3.sp,

                    modifier =
                        Modifier.align(
                            Alignment.Center
                        )
                )
            }

            Column(
                modifier =
                    Modifier.padding(21.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .background(
                                PrimaryGreen.copy(
                                    alpha = 0.13f
                                )
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
                                Modifier.size(25.dp),

                            tint =
                                BrightGreen
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
                                "PRO",

                            color =
                                PrimaryText,

                            fontSize =
                                20.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "For growing turf businesses",

                            color =
                                SecondaryText,

                            fontSize =
                                11.sp
                        )
                    }
                }

                HorizontalDivider(
                    color =
                        Border
                )

                FeatureItem(
                    text =
                        "Multiple turfs"
                )

                FeatureItem(
                    text =
                        "Full Admin access"
                )

                FeatureItem(
                    text =
                        "Advanced slot management"
                )

                FeatureItem(
                    text =
                        "Complete booking management"
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                PremiumSecondaryButton(
                    text =
                        "Explore PRO Plans",

                    icon =
                        Icons.Default.ArrowForward,

                    onClick =
                        onClick
                )
            }
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
            Arrangement.spacedBy(18.dp)
    ) {

        PremiumHeader(
            eyebrow =
                "ACCESS REQUIRED",

            title =
                "Choose a paid plan",

            subtitle =
                "Your free trial has already been used."
        )

        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(26.dp),

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
                    Modifier.padding(23.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    PremiumIconBox(
                        icon =
                            Icons.Default.Lock
                    )

                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                "Paid subscription required",

                            color =
                                PrimaryText,

                            fontSize =
                                16.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Continue managing your business with PRO.",

                            color =
                                SecondaryText,

                            fontSize =
                                11.sp,

                            lineHeight =
                                16.sp
                        )
                    }
                }

                PremiumPrimaryButton(
                    text =
                        "View PRO Plans",

                    icon =
                        Icons.Default.ArrowForward,

                    onClick =
                        onPaidPlanClick
                )
            }
        }
    }
}


// =============================================================
// PREMIUM PLAN CARD
// =============================================================

@Composable
private fun PremiumPlanCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    features: List<String>,
    highlighted: Boolean
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(25.dp),

        color =
            if (highlighted) {
                SurfaceHighlight
            } else {
                SurfaceDark
            },

        border =
            BorderStroke(
                1.dp,
                if (highlighted) {
                    PrimaryGreen.copy(
                        alpha = 0.32f
                    )
                } else {
                    Border
                }
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(
                            RoundedCornerShape(15.dp)
                        )
                        .background(
                            SurfaceHighlight
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
                            LightGreen
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
                            17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            subtitle,

                        color =
                            SecondaryText,

                        fontSize =
                            11.sp
                    )
                }

                Surface(
                    shape =
                        RoundedCornerShape(8.dp),

                    color =
                        SurfaceHighlight
                ) {

                    Text(
                        text =
                            badge,

                        color =
                            LightGreen,

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            0.8.sp,

                        modifier =
                            Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            )
                    )
                }
            }

            HorizontalDivider(
                color =
                    Border
            )

            features.forEach { feature ->

                FeatureItem(
                    text =
                        feature
                )
            }
        }
    }
}


// =============================================================
// DETAIL ROW
// =============================================================

@Composable
private fun PremiumDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(CircleShape)
                .background(
                    SurfaceHighlight
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
                    Modifier.size(15.dp),

                tint =
                    SecondaryText
            )
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Text(
            text =
                label,

            color =
                MutedText,

            fontSize =
                11.sp,

            modifier =
                Modifier.weight(1f)
        )

        Text(
            text =
                value,

            color =
                PrimaryText,

            fontSize =
                11.sp,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}


// =============================================================
// PRIMARY BUTTON
// =============================================================

@Composable
private fun PremiumPrimaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Button(
        onClick =
            onClick,

        modifier =
            Modifier
                .fillMaxWidth()
                .height(55.dp),

        shape =
            RoundedCornerShape(17.dp),

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
                text,

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.width(9.dp)
        )

        Icon(
            imageVector =
                icon,

            contentDescription =
                null,

            modifier =
                Modifier.size(18.dp)
        )
    }
}


// =============================================================
// SECONDARY BUTTON
// =============================================================

@Composable
private fun PremiumSecondaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    OutlinedButton(
        onClick =
            onClick,

        modifier =
            Modifier
                .fillMaxWidth()
                .height(53.dp),

        shape =
            RoundedCornerShape(16.dp),

        border =
            BorderStroke(
                1.dp,
                PrimaryGreen.copy(
                    alpha = 0.55f
                )
            ),

        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor =
                    PrimaryText
            )
    ) {

        Text(
            text =
                text,

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.width(8.dp)
        )

        Icon(
            imageVector =
                icon,

            contentDescription =
                null,

            modifier =
                Modifier.size(17.dp),

            tint =
                LightGreen
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
                    horizontal = 5.dp
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
                Modifier.size(12.dp),

            tint =
                MutedText
        )

        Spacer(
            modifier =
                Modifier.width(6.dp)
        )

        Text(
            text =
                "Secure payment integration will be available soon.",

            color =
                MutedText,

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
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryGreen.copy(
                            alpha = 0.10f
                        )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(26.dp),

                    color =
                        PrimaryGreen,

                    strokeWidth =
                        2.5.dp
                )
            }

            Text(
                text =
                    "Loading membership...",

                color =
                    SecondaryText,

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

        Surface(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(25.dp),

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
                    Modifier.padding(25.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            ErrorRed.copy(
                                alpha = 0.09f
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
                            ErrorRed,

                        modifier =
                            Modifier.size(26.dp)
                    )
                }

                Text(
                    text =
                        "Unable to load subscription",

                    color =
                        PrimaryText,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )

                Text(
                    text =
                        message,

                    color =
                        SecondaryText,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp,

                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Button(
                    onClick =
                        onRetry,

                    shape =
                        RoundedCornerShape(14.dp),

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