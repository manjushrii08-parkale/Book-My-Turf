package com.example.bookmyturf.screens.admin

import android.util.Log
import android.widget.Toast

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
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

import com.example.bookmyturf.MainActivity
import com.example.bookmyturf.viewmodel.AdminViewModel

import kotlinx.coroutines.flow.collectLatest


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
// ADMIN PAID PLANS SCREEN
// =============================================================

@Composable
fun AdminPaidPlansScreen(
    token: String,
    viewModel: AdminViewModel,
    onPlanActivated: () -> Unit,
    onBackClick: () -> Unit = {}
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = androidx.compose.ui.platform.LocalContext.current

    val activity =
        context as? MainActivity


    // =========================================================
    // VIEWMODEL STATE
    // =========================================================

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()


    // =========================================================
    // RAZORPAY PAYMENT SUCCESS
    // =========================================================

    LaunchedEffect(Unit) {

        MainActivity.paymentSuccess.collectLatest { payment ->

            viewModel.verifySubscriptionPayment(

                token =
                    token,

                razorpayOrderId =
                    payment.orderId,

                razorpayPaymentId =
                    payment.paymentId,

                razorpaySignature =
                    payment.signature,

                onSuccess = {

                    Toast.makeText(
                        context,
                        "PRO subscription activated successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    onPlanActivated()
                }
            )
        }
    }


    // =========================================================
    // RAZORPAY PAYMENT ERROR
    // =========================================================

    LaunchedEffect(Unit) {

        MainActivity.paymentError.collectLatest { message ->

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    // =========================================================
    // SELECT PLAN
    // =========================================================

    fun selectPlan(
        plan: String
    ) {

        if (isLoading) {
            return
        }

        Log.d(
            "RAZORPAY_SUBSCRIPTION",
            "================================"
        )

        Log.d(
            "RAZORPAY_SUBSCRIPTION",
            "Pay button clicked"
        )

        Log.d(
            "RAZORPAY_SUBSCRIPTION",
            "Plan = $plan"
        )

        Log.d(
            "RAZORPAY_SUBSCRIPTION",
            "Token available = ${token.isNotBlank()}"
        )

        Log.d(
            "RAZORPAY_SUBSCRIPTION",
            "================================"
        )

        viewModel.createSubscriptionOrder(

            token = token,

            plan = plan,

            onOrderCreated = { response ->

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Order callback received"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "success = ${response.success}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "message = ${response.message}"
                )

                val data =
                    response.data

                if (data == null) {

                    Log.e(
                        "RAZORPAY_SUBSCRIPTION",
                        "Order data is NULL"
                    )

                    Toast.makeText(
                        context,
                        "Payment order information is missing.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@createSubscriptionOrder
                }

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Subscription ID = ${data.subscription_id}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Order ID = ${data.order_id}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Amount = ${data.amount}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Amount Paise = ${data.amount_paise}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Currency = ${data.currency}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Plan = ${data.plan}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Razorpay key available = ${
                        data.razorpay_key.isNotBlank()
                    }"
                )

                val currentActivity =
                    activity

                if (currentActivity == null) {

                    Log.e(
                        "RAZORPAY_SUBSCRIPTION",
                        "MainActivity is NULL"
                    )

                    Toast.makeText(
                        context,
                        "Unable to open payment screen.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@createSubscriptionOrder
                }

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Opening Razorpay Checkout..."
                )

                currentActivity.startSubscriptionPayment(

                    razorpayKey =
                        data.razorpay_key,

                    orderId =
                        data.order_id,

                    amountPaise =
                        data.amount_paise,

                    plan =
                        data.plan
                )
            }
        )
    }


    // =========================================================
    // UI
    // =========================================================

    Scaffold(

        containerColor =
            Background,

        topBar = {

            PremiumPaidPlansTopBar(
                onBackClick =
                    onBackClick
            )
        }

    ) { innerPadding ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 14.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(20.dp)
        ) {

            // =================================================
            // HERO
            // =================================================

            ProHeroCard()


            // =================================================
            // SECTION TITLE
            // =================================================

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text =
                        "CHOOSE YOUR PLAN",

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
                        "Upgrade your business",

                    color =
                        PrimaryText,

                    fontSize =
                        25.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        (-0.5).sp
                )

                Text(
                    text =
                        "Get full management access for your turf business.",

                    color =
                        SecondaryText,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp
                )
            }


            // =================================================
            // ERROR
            // =================================================

            if (!error.isNullOrBlank()) {

                SubscriptionErrorBanner(
                    message =
                        error ?: ""
                )
            }


            // =================================================
            // MONTHLY PLAN
            // =================================================

            PremiumPlanCard(

                title =
                    "Monthly",

                price =
                    "₹499",

                period =
                    "/ month",

                description =
                    "Flexible month-to-month access",

                badge =
                    null,

                isRecommended =
                    false,

                buttonText =
                    if (isLoading) {
                        "Processing..."
                    } else {
                        "Pay ₹499"
                    },

                enabled =
                    !isLoading,

                onClick =
                    {
                        selectPlan(
                            "MONTHLY"
                        )
                    }
            )


            // =================================================
            // YEARLY PLAN
            // =================================================

            PremiumPlanCard(

                title =
                    "Yearly",

                price =
                    "₹4,999",

                period =
                    "/ year",

                description =
                    "Best value for long-term use",

                badge =
                    "SAVE ₹989",

                isRecommended =
                    true,

                buttonText =
                    if (isLoading) {
                        "Processing..."
                    } else {
                        "Pay ₹4,999"
                    },

                enabled =
                    !isLoading,

                onClick =
                    {
                        selectPlan(
                            "YEARLY"
                        )
                    }
            )


            // =================================================
            // INCLUDED FEATURES
            // =================================================

            IncludedFeaturesCard()


            // =================================================
            // SECURITY
            // =================================================

            SecurityNote()


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )
        }
    }
}


// =============================================================
// PREMIUM TOP BAR
// =============================================================

@Composable
private fun PremiumPaidPlansTopBar(
    onBackClick: () -> Unit
) {

    Surface(
        color =
            Background,

        shadowElevation =
            0.dp
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                ) {

                    // -------------------------------------------------
                    // BACK BUTTON
                    // -------------------------------------------------

                    Surface(
                        modifier =
                            Modifier
                                .align(
                                    Alignment.CenterStart
                                )
                                .padding(
                                    start = 14.dp
                                )
                                .size(42.dp),

                        shape =
                            RoundedCornerShape(14.dp),

                        color =
                            SurfaceElevated
                    ) {

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
                                    PrimaryText,

                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }
                    }


                    // -------------------------------------------------
                    // CENTER TITLE
                    // -------------------------------------------------

                    Text(
                        text =
                            "BookMyTurf",

                        modifier =
                            Modifier.align(
                                Alignment.Center
                            ),

                        color =
                            PrimaryText,

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            (-0.3).sp
                    )
                }


                // -------------------------------------------------
                // DIVIDER
                // -------------------------------------------------

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors =
                                        listOf(
                                            Color.Transparent,
                                            Border,
                                            PrimaryGreen.copy(
                                                alpha = 0.18f
                                            ),
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
// PRO HERO CARD
// =============================================================

@Composable
private fun ProHeroCard() {

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
                PrimaryGreen.copy(
                    alpha = 0.28f
                )
            )
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xFF10271B),
                                    SurfaceDark,
                                    Background
                                )
                        )
                    )
        ) {

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(21.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // -------------------------------------------------
                // ICON
                // -------------------------------------------------

                Box(
                    modifier =
                        Modifier
                            .size(58.dp)
                            .clip(
                                RoundedCornerShape(18.dp)
                            )
                            .background(
                                PrimaryGreen.copy(
                                    alpha = 0.14f
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
                            Modifier.size(29.dp),

                        tint =
                            BrightGreen
                    )
                }


                Spacer(
                    modifier =
                        Modifier.width(15.dp)
                )


                // -------------------------------------------------
                // HERO TEXT
                // -------------------------------------------------

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "PRO ACCESS",

                        color =
                            PrimaryGreen,

                        fontSize =
                            9.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.4.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Unlock PRO",

                        color =
                            PrimaryText,

                        fontSize =
                            19.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Manage more turfs and grow your business.",

                        color =
                            SecondaryText,

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp
                    )
                }
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

    price: String,

    period: String,

    description: String,

    badge: String?,

    isRecommended: Boolean,

    buttonText: String,

    enabled: Boolean,

    onClick: () -> Unit
) {

    Surface(

        modifier =
            Modifier
                .fillMaxWidth()
                .then(

                    if (isRecommended) {

                        Modifier.border(
                            width = 1.dp,

                            color =
                                PrimaryGreen.copy(
                                    alpha = 0.48f
                                ),

                            shape =
                                RoundedCornerShape(
                                    25.dp
                                )
                        )

                    } else {

                        Modifier
                    }
                ),

        shape =
            RoundedCornerShape(25.dp),

        color =
            if (isRecommended) {
                SurfaceHighlight
            } else {
                SurfaceDark
            }
    ) {

        Column(

            modifier =
                Modifier.padding(21.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            // =====================================================
            // RECOMMENDED BADGE
            // =====================================================

            if (isRecommended) {

                Surface(

                    shape =
                        RoundedCornerShape(9.dp),

                    color =
                        PrimaryGreen
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

                        Icon(

                            imageVector =
                                Icons.Default.WorkspacePremium,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(13.dp),

                            tint =
                                Color(0xFF071008)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(5.dp)
                        )

                        Text(

                            text =
                                "BEST VALUE",

                            color =
                                Color(0xFF071008),

                            fontSize =
                                8.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                0.9.sp
                        )
                    }
                }
            }


            // =====================================================
            // HEADER
            // =====================================================

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
                            title,

                        color =
                            PrimaryText,

                        fontSize =
                            19.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            description,

                        color =
                            SecondaryText,

                        fontSize =
                            11.sp,

                        lineHeight =
                            16.sp
                    )
                }


                if (badge != null) {

                    Surface(

                        shape =
                            RoundedCornerShape(8.dp),

                        color =
                            PrimaryGreen.copy(
                                alpha = 0.12f
                            )
                    ) {

                        Text(

                            text =
                                badge,

                            modifier =
                                Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 6.dp
                                ),

                            color =
                                LightGreen,

                            fontSize =
                                8.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            // =====================================================
            // PRICE
            // =====================================================

            Row(
                verticalAlignment =
                    Alignment.Bottom
            ) {

                Text(
                    text =
                        price,

                    color =
                        PrimaryText,

                    fontSize =
                        33.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        (-0.7).sp
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        period,

                    modifier =
                        Modifier.padding(
                            bottom = 6.dp
                        ),

                    color =
                        MutedText,

                    fontSize =
                        11.sp
                )
            }


            HorizontalDivider(
                color =
                    Border
            )


            // =====================================================
            // FEATURES
            // =====================================================

            PremiumPlanFeature(
                icon =
                    Icons.Default.SportsSoccer,

                text =
                    "Manage your turfs"
            )

            PremiumPlanFeature(
                icon =
                    Icons.Default.Timer,

                text =
                    "Create and manage slots"
            )

            PremiumPlanFeature(
                icon =
                    Icons.Default.CalendarMonth,

                text =
                    "View customer bookings"
            )

            PremiumPlanFeature(
                icon =
                    Icons.Default.Groups,

                text =
                    "Manage customer activity"
            )


            // =====================================================
            // BUTTON
            // =====================================================

            if (isRecommended) {

                Button(

                    enabled =
                        enabled,

                    onClick =
                        onClick,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(53.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                PrimaryGreen,

                            contentColor =
                                Color(0xFF061008),

                            disabledContainerColor =
                                PrimaryGreen.copy(
                                    alpha = 0.45f
                                ),

                            disabledContentColor =
                                Color(0xFF061008).copy(
                                    alpha = 0.65f
                                )
                        )
                ) {

                    if (isLoadingIndicator(buttonText)) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(18.dp),

                            color =
                                Color(0xFF061008),

                            strokeWidth =
                                2.dp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(9.dp)
                        )
                    }

                    Text(
                        text =
                            buttonText,

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    if (!isLoadingIndicator(buttonText)) {

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Icon(
                            imageVector =
                                Icons.Default.ArrowForward,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(17.dp)
                        )
                    }
                }

            } else {

                OutlinedButton(

                    enabled =
                        enabled,

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
                                PrimaryText,

                            disabledContentColor =
                                MutedText
                        )
                ) {

                    if (isLoadingIndicator(buttonText)) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(18.dp),

                            color =
                                PrimaryGreen,

                            strokeWidth =
                                2.dp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(9.dp)
                        )
                    }

                    Text(
                        text =
                            buttonText,

                        fontSize =
                            13.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    if (!isLoadingIndicator(buttonText)) {

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Icon(
                            imageVector =
                                Icons.Default.ArrowForward,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(17.dp),

                            tint =
                                LightGreen
                        )
                    }
                }
            }
        }
    }
}


// =============================================================
// LOADING TEXT CHECK
// =============================================================

private fun isLoadingIndicator(
    text: String
): Boolean {
    return text == "Processing..."
}


// =============================================================
// PLAN FEATURE
// =============================================================

@Composable
private fun PremiumPlanFeature(
    icon: ImageVector,
    text: String
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
                    .size(27.dp)
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
                    icon,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(14.dp),

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
// INCLUDED FEATURES CARD
// =============================================================

@Composable
private fun IncludedFeaturesCard() {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(23.dp),

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
                Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(

                    modifier =
                        Modifier
                            .size(38.dp)
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
                            Icons.Default.CheckCircle,

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
                        Modifier.width(11.dp)
                )


                Column {

                    Text(

                        text =
                            "What's included",

                        color =
                            PrimaryText,

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(

                        text =
                            "Everything you need to manage your business",

                        color =
                            MutedText,

                        fontSize =
                            10.sp
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
                    Modifier.height(8.dp)
            )


            IncludedFeature(

                title =
                    "Turf Management",

                description =
                    "Add and manage your turf grounds."
            )


            IncludedFeature(

                title =
                    "Slot Management",

                description =
                    "Create and manage booking time slots."
            )


            IncludedFeature(

                title =
                    "Booking Management",

                description =
                    "View and manage customer bookings."
            )


            IncludedFeature(

                title =
                    "Business Dashboard",

                description =
                    "Track bookings, customers and revenue."
            )
        }
    }
}


// =============================================================
// INCLUDED FEATURE
// =============================================================

@Composable
private fun IncludedFeature(

    title: String,

    description: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                ),

        verticalAlignment =
            Alignment.Top
    ) {

        Box(

            modifier =
                Modifier
                    .size(22.dp)
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
                    Modifier.size(13.dp),

                tint =
                    LightGreen
            )
        }


        Spacer(
            modifier =
                Modifier.width(10.dp)
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
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold
            )


            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )


            Text(

                text =
                    description,

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


// =============================================================
// ERROR BANNER
// =============================================================

@Composable
private fun SubscriptionErrorBanner(
    message: String
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(17.dp),

        color =
            ErrorRed.copy(
                alpha = 0.08f
            ),

        border =
            BorderStroke(
                1.dp,
                ErrorRed.copy(
                    alpha = 0.20f
                )
            )
    ) {

        Row(

            modifier =
                Modifier.padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier =
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            ErrorRed.copy(
                                alpha = 0.10f
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

                    modifier =
                        Modifier.size(15.dp),

                    tint =
                        ErrorRed
                )
            }


            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )


            Text(

                text =
                    message,

                color =
                    PrimaryText,

                fontSize =
                    11.sp,

                lineHeight =
                    16.sp
            )
        }
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
                "Payments secured by Razorpay",

            color =
                MutedText,

            fontSize =
                10.sp,

            textAlign =
                TextAlign.Center
        )
    }
}