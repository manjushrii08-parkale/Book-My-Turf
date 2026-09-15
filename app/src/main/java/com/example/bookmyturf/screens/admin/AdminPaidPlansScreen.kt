package com.example.bookmyturf.screens.admin

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.bookmyturf.MainActivity
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AdminViewModel

import kotlinx.coroutines.flow.collectLatest


// =============================================================
// ADMIN PAID PLANS SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
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

    val context =
        LocalContext.current

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

                val data = response.data

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

                val currentActivity = activity

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
            AdminOffWhite,

        topBar = {

            TopAppBar(

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

                title = {

                    Column {

                        Text(
                            text =
                                "PRO Plans",

                            color =
                                AdminDarkCharcoal,

                            fontSize =
                                18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Choose your subscription",

                            color =
                                AdminGray,

                            fontSize =
                                10.sp
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

        Column(

            modifier =
                Modifier
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
            // HERO
            // =================================================

            ProHeroCard()


            // =================================================
            // TITLE
            // =================================================

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {

                Text(
                    text =
                        "Choose your plan",

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Upgrade your turf business with full management access.",

                    color =
                        AdminGray,

                    fontSize =
                        12.sp
                )
            }


            // =================================================
            // ERROR
            // =================================================

            if (!error.isNullOrBlank()) {

                Surface(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    color =
                        Color(0xFFFFEBEE)
                ) {

                    Text(
                        text =
                            error ?: "",

                        modifier =
                            Modifier.padding(12.dp),

                        color =
                            Color(0xFFC62828),

                        fontSize =
                            12.sp
                    )
                }
            }


            // =================================================
            // MONTHLY
            // =================================================

            AdminPlanCard(

                title =
                    "Monthly Plan",

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
            // YEARLY
            // =================================================

            AdminPlanCard(

                title =
                    "Yearly Plan",

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
            // INCLUDED
            // =================================================

            IncludedFeaturesCard()


            // =================================================
            // SECURITY
            // =================================================

            SecurityNote()


            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )
        }
    }
}


// =============================================================
// PRO HERO CARD
// =============================================================

@Composable
private fun ProHeroCard() {

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
                defaultElevation =
                    3.dp
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(19.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(

                modifier =
                    Modifier.size(48.dp),

                shape =
                    RoundedCornerShape(14.dp),

                color =
                    AdminLightGreen
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.WorkspacePremium,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(26.dp),

                        tint =
                            AdminDarkGreen
                    )
                }
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
                        "Unlock PRO",

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


                Text(

                    text =
                        "Manage more turfs and grow your business.",

                    color =
                        AdminWhite.copy(
                            alpha = 0.75f
                        ),

                    fontSize =
                        11.sp,

                    lineHeight =
                        16.sp
                )
            }
        }
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

    badge: String?,

    isRecommended: Boolean,

    buttonText: String,

    enabled: Boolean,

    onClick: () -> Unit
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .then(

                    if (isRecommended) {

                        Modifier.border(
                            width = 1.5.dp,

                            color =
                                AdminForestGreen,

                            shape =
                                RoundedCornerShape(
                                    20.dp
                                )
                        )

                    } else {

                        Modifier
                    }
                ),

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
                    if (isRecommended) {
                        4.dp
                    } else {
                        1.dp
                    }
            )
    ) {

        Column(

            modifier =
                Modifier.padding(19.dp)
        ) {

            // =================================================
            // BADGE
            // =================================================

            if (isRecommended) {

                Surface(

                    shape =
                        RoundedCornerShape(50.dp),

                    color =
                        AdminDarkGreen
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
                                Modifier.size(14.dp),

                            tint =
                                AdminLightGreen
                        )


                        Spacer(
                            modifier =
                                Modifier.width(5.dp)
                        )


                        Text(

                            text =
                                "BEST VALUE",

                            color =
                                AdminWhite,

                            fontSize =
                                9.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                0.8.sp
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )
            }


            // =================================================
            // HEADER
            // =================================================

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
                            AdminDarkCharcoal,

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
                            description,

                        color =
                            AdminGray,

                        fontSize =
                            11.sp
                    )
                }


                if (badge != null) {

                    Surface(

                        shape =
                            RoundedCornerShape(8.dp),

                        color =
                            AdminLightGreen.copy(
                                alpha = 0.20f
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
                                AdminForestGreen,

                            fontSize =
                                8.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // PRICE
            // =================================================

            Row(

                verticalAlignment =
                    Alignment.Bottom
            ) {

                Text(

                    text =
                        price,

                    color =
                        AdminDarkGreen,

                    fontSize =
                        32.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )


                Text(

                    text =
                        period,

                    modifier =
                        Modifier.padding(
                            bottom = 5.dp
                        ),

                    color =
                        AdminGray,

                    fontSize =
                        12.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )


            HorizontalDivider(
                color =
                    Color(0xFFE7ECE7)
            )


            Spacer(
                modifier =
                    Modifier.height(11.dp)
            )


            // =================================================
            // FEATURES
            // =================================================

            PlanFeature(
                icon =
                    Icons.Default.SportsSoccer,

                text =
                    "Manage your turfs"
            )


            PlanFeature(
                icon =
                    Icons.Default.Timer,

                text =
                    "Create and manage slots"
            )


            PlanFeature(
                icon =
                    Icons.Default.CalendarMonth,

                text =
                    "View customer bookings"
            )


            PlanFeature(
                icon =
                    Icons.Default.Groups,

                text =
                    "Manage customer activity"
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // BUTTON
            // =================================================

            if (isRecommended) {

                Button(

                    enabled =
                        enabled,

                    onClick =
                        onClick,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(49.dp),

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
                            buttonText,

                        fontWeight =
                            FontWeight.Bold
                    )
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
                            .height(49.dp),

                    shape =
                        RoundedCornerShape(12.dp),

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
                            buttonText,

                        color =
                            AdminForestGreen,

                        fontWeight =
                            FontWeight.SemiBold
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
    icon: ImageVector,
    text: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 4.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(

            modifier =
                Modifier.size(23.dp),

            shape =
                CircleShape,

            color =
                AdminLightGreen.copy(
                    alpha = 0.18f
                )
        ) {

            Box(
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
                        AdminForestGreen
                )
            }
        }


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
// INCLUDED FEATURES CARD
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
                defaultElevation =
                    1.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(18.dp)
        ) {

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


            Spacer(
                modifier =
                    Modifier.height(11.dp)
            )


            HorizontalDivider(
                color =
                    Color(0xFFE8ECE8)
            )


            Spacer(
                modifier =
                    Modifier.height(7.dp)
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
                    vertical = 7.dp
                ),

        verticalAlignment =
            Alignment.Top
    ) {

        Icon(

            imageVector =
                Icons.Default.CheckCircle,

            contentDescription =
                null,

            modifier =
                Modifier.size(18.dp),

            tint =
                AdminForestGreen
        )


        Spacer(
            modifier =
                Modifier.width(9.dp)
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
                    AdminGray,

                fontSize =
                    10.sp,

                lineHeight =
                    15.sp
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
            Modifier.fillMaxWidth(),

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
                "Payments secured by Razorpay",

            color =
                AdminGray,

            fontSize =
                10.sp,

            textAlign =
                TextAlign.Center
        )
    }
}

