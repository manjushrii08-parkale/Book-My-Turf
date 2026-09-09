package com.example.bookmyturf.screens.user

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.MainActivity
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.PaymentRepository
import com.example.bookmyturf.viewmodel.PaymentViewModel
import com.example.bookmyturf.viewmodel.PaymentViewModelFactory

import com.razorpay.Checkout

import kotlinx.coroutines.flow.collectLatest

import org.json.JSONObject


// ============================================================
// SAME THEME AS USER HOME / TURF DETAILS / SUCCESS SCREEN
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val LightGray = Color(0xFFE5E5E0)


// ============================================================
// PAYMENT SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    bookingId: Int,
    onPaymentSuccess: () -> Unit,
    onBackClick: () -> Unit
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = LocalContext.current


    // =========================================================
    // SESSION
    // =========================================================

    val sessionManager =
        remember {
            SessionManager(context)
        }

    val token =
        remember {
            sessionManager.getToken()
        }


    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository =
        remember {
            PaymentRepository(
                RetrofitClient.paymentApi
            )
        }


    // =========================================================
    // VIEWMODEL
    // =========================================================

    val factory =
        remember {
            PaymentViewModelFactory(
                repository
            )
        }

    val paymentViewModel: PaymentViewModel =
        viewModel(
            factory = factory
        )


    // =========================================================
    // STATE
    // =========================================================

    val order by
    paymentViewModel.order.collectAsState()

    val isLoading by
    paymentViewModel.isLoading.collectAsState()

    val error by
    paymentViewModel.error.collectAsState()

    val verificationResult by
    paymentViewModel.verificationResult.collectAsState()

    val isVerifying by
    paymentViewModel.isVerifying.collectAsState()


    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    LaunchedEffect(bookingId) {

        if (!token.isNullOrBlank()) {

            Log.d(
                "RAZORPAY",
                "Creating Razorpay order"
            )

            Log.d(
                "RAZORPAY",
                "Booking ID = $bookingId"
            )

            paymentViewModel.createRazorpayOrder(
                token = token,
                bookingId = bookingId
            )

        } else {

            Log.e(
                "RAZORPAY",
                "Authentication token is missing"
            )
        }
    }


    // =========================================================
    // RAZORPAY SUCCESS CALLBACK
    // =========================================================

    LaunchedEffect(bookingId) {

        MainActivity.paymentSuccess.collectLatest { paymentData ->

            Log.d(
                "RAZORPAY",
                "================================"
            )

            Log.d(
                "RAZORPAY",
                "Payment success callback received"
            )

            Log.d(
                "RAZORPAY",
                "Payment ID = ${paymentData.paymentId}"
            )

            Log.d(
                "RAZORPAY",
                "Order ID = ${paymentData.orderId}"
            )

            Log.d(
                "RAZORPAY",
                "Signature = ${paymentData.signature}"
            )

            Log.d(
                "RAZORPAY",
                "Booking ID = $bookingId"
            )

            Log.d(
                "RAZORPAY",
                "================================"
            )


            // -------------------------------------------------
            // CALLBACK DATA
            // -------------------------------------------------

            val paymentId =
                paymentData.paymentId

            val razorpayOrderId =
                paymentData.orderId

            val signature =
                paymentData.signature


            // -------------------------------------------------
            // TOKEN VALIDATION
            // -------------------------------------------------

            if (token.isNullOrBlank()) {

                Log.e(
                    "RAZORPAY",
                    "Token missing. Cannot verify payment."
                )

                Toast.makeText(
                    context,
                    "Authentication expired. Please login again.",
                    Toast.LENGTH_LONG
                ).show()

                return@collectLatest
            }


            // -------------------------------------------------
            // ORDER ID VALIDATION
            // -------------------------------------------------

            if (razorpayOrderId.isNullOrBlank()) {

                Log.e(
                    "RAZORPAY",
                    "Razorpay Order ID missing."
                )

                Toast.makeText(
                    context,
                    "Payment verification data is incomplete.",
                    Toast.LENGTH_LONG
                ).show()

                return@collectLatest
            }


            // -------------------------------------------------
            // SIGNATURE VALIDATION
            // -------------------------------------------------

            if (signature.isNullOrBlank()) {

                Log.e(
                    "RAZORPAY",
                    "Razorpay Signature missing."
                )

                Toast.makeText(
                    context,
                    "Payment signature is missing.",
                    Toast.LENGTH_LONG
                ).show()

                return@collectLatest
            }


            // -------------------------------------------------
            // VERIFY PAYMENT WITH LARAVEL
            // -------------------------------------------------

            Log.d(
                "RAZORPAY",
                "Sending payment verification to Laravel..."
            )

            paymentViewModel.verifyRazorpayPayment(
                token = token,
                bookingId = bookingId,
                razorpayOrderId = razorpayOrderId,
                razorpayPaymentId = paymentId,
                razorpaySignature = signature
            )
        }
    }


    // =========================================================
    // PAYMENT VERIFICATION RESULT
    // =========================================================

    LaunchedEffect(verificationResult) {

        verificationResult?.let { result ->

            Log.d(
                "RAZORPAY",
                "================================"
            )

            Log.d(
                "RAZORPAY",
                "PAYMENT VERIFIED SUCCESSFULLY"
            )

            Log.d(
                "RAZORPAY",
                "Booking ID = ${result.booking_id}"
            )

            Log.d(
                "RAZORPAY",
                "Payment ID = ${result.payment_id}"
            )

            Log.d(
                "RAZORPAY",
                "Payment Status = ${result.payment_status}"
            )

            Log.d(
                "RAZORPAY",
                "Booking Status = ${result.booking_status}"
            )

            Log.d(
                "RAZORPAY",
                "================================"
            )


            Toast.makeText(
                context,
                "Payment successful!",
                Toast.LENGTH_SHORT
            ).show()


            // -------------------------------------------------
            // NAVIGATE ONLY AFTER LARAVEL VERIFICATION
            // -------------------------------------------------

            onPaymentSuccess()

            paymentViewModel.clearVerificationResult()
        }
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    Scaffold(

        containerColor =
            OffWhite,

        // =====================================================
        // TOP BAR
        // =====================================================

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Payment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {

                            if (!isVerifying) {
                                onBackClick()
                            }
                        },

                        enabled = !isVerifying
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                Charcoal
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            White,

                        titleContentColor =
                            Charcoal,

                        navigationIconContentColor =
                            Charcoal
                    )
            )
        }

    ) { innerPadding ->


        // =====================================================
        // CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
        ) {


            // =================================================
            // PAYMENT HEADER
            // =================================================

            Text(
                text =
                    "Complete Your Payment",

                fontSize =
                    26.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Charcoal
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text =
                    "Securely pay for your turf booking.",

                fontSize =
                    13.sp,

                color =
                    Gray
            )


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            // =================================================
            // BOOKING INFORMATION CARD
            // =================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            2.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                ) {

                    // =========================================
                    // CARD HEADER
                    // =========================================

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier =
                                Modifier
                                    .size(46.dp)
                                    .background(
                                        color =
                                            LightGreen.copy(
                                                alpha = 0.14f
                                            ),

                                        shape =
                                            RoundedCornerShape(
                                                13.dp
                                            )
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Payment,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(23.dp),

                                tint =
                                    ForestGreen
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
                                    "Booking Payment",

                                fontSize =
                                    16.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    Charcoal
                            )


                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )


                            Text(
                                text =
                                    "Book My Turf",

                                fontSize =
                                    11.sp,

                                color =
                                    Gray
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(18.dp)
                    )


                    HorizontalDivider(
                        color =
                            LightGray
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    // =========================================
                    // BOOKING ID
                    // =========================================

                    PaymentInfoRow(
                        label = "Booking ID",
                        value = "#$bookingId"
                    )


                    // =========================================
                    // AMOUNT
                    // =========================================

                    if (order != null) {

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        PaymentInfoRow(
                            label = "Amount",
                            value =
                                "₹${order!!.amount / 100}",
                            valueBold = true
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // PAYMENT STATE
            // =================================================

            when {

                // =================================================
                // CREATING ORDER
                // =================================================

                isLoading -> {

                    PaymentLoadingCard(
                        icon = Icons.Default.AccessTime,
                        title = "Preparing Payment",
                        message =
                            "Creating your secure payment order..."
                    )
                }


                // =================================================
                // VERIFYING PAYMENT
                // =================================================

                isVerifying -> {

                    PaymentLoadingCard(
                        icon = Icons.Default.Lock,
                        title = "Verifying Payment",
                        message =
                            "Please wait while we verify your payment..."
                    )
                }


                // =================================================
                // ERROR
                // =================================================

                error != null -> {

                    PaymentErrorCard(
                        error =
                            error
                                ?: "Payment error"
                    )
                }


                // =================================================
                // ORDER CREATED
                // =================================================

                order != null -> {

                    PaymentReadyCard(
                        amount =
                            order!!.amount / 100,

                        enabled =
                            !isVerifying,

                        onPayClick = {

                            try {

                                Log.d(
                                    "RAZORPAY",
                                    "================================"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "Pay Now clicked"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "Booking ID = $bookingId"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "Order ID = ${order!!.order_id}"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "Amount = ${order!!.amount}"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "================================"
                                )


                                val checkout =
                                    Checkout()


                                checkout.setKeyID(
                                    order!!.key_id
                                )


                                val options =
                                    JSONObject().apply {

                                        put(
                                            "name",
                                            "Book My Turf"
                                        )

                                        put(
                                            "description",
                                            "Turf Booking Payment"
                                        )

                                        put(
                                            "currency",
                                            order!!.currency
                                        )

                                        put(
                                            "amount",
                                            order!!.amount
                                        )

                                        put(
                                            "order_id",
                                            order!!.order_id
                                        )

                                        put(
                                            "prefill",
                                            JSONObject().apply {

                                                put(
                                                    "email",
                                                    ""
                                                )

                                                put(
                                                    "contact",
                                                    ""
                                                )
                                            }
                                        )
                                    }


                                Log.d(
                                    "RAZORPAY",
                                    "Opening Razorpay Checkout"
                                )


                                checkout.open(
                                    context as Activity,
                                    options
                                )

                            } catch (e: Exception) {

                                Log.e(
                                    "RAZORPAY",
                                    "Failed to open Razorpay Checkout",
                                    e
                                )


                                Toast.makeText(
                                    context,
                                    "Unable to open payment gateway",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }


                // =================================================
                // PREPARING
                // =================================================

                else -> {

                    PaymentLoadingCard(
                        icon = Icons.Default.AccessTime,
                        title = "Preparing Payment",
                        message =
                            "Please wait while we prepare your payment..."
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // SECURE PAYMENT INFORMATION
            // =================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            LightGreen.copy(
                                alpha = 0.08f
                            )
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Lock,

                        contentDescription =
                            "Secure payment",

                        modifier =
                            Modifier.size(20.dp),

                        tint =
                            ForestGreen
                    )


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
                                "Secure Payment",

                            fontSize =
                                13.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                DarkGreen
                        )


                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )


                        Text(
                            text =
                                "Your payment is securely processed through Razorpay.",

                            fontSize =
                                11.sp,

                            color =
                                Gray,

                            lineHeight =
                                17.sp
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            // =================================================
            // BACK BUTTON
            // =================================================

            OutlinedButton(
                onClick =
                    onBackClick,

                enabled =
                    !isVerifying,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),

                shape =
                    RoundedCornerShape(13.dp),

                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor =
                            DarkGreen
                    )
            ) {

                Text(
                    text =
                        "Back",

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================
// PAYMENT INFO ROW
// ============================================================

@Composable
private fun PaymentInfoRow(
    label: String,
    value: String,
    valueBold: Boolean = false
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

            fontSize =
                13.sp,

            color =
                Gray
        )


        Text(
            text =
                value,

            fontSize =
                if (valueBold) 18.sp else 14.sp,

            fontWeight =
                if (valueBold) {
                    FontWeight.ExtraBold
                } else {
                    FontWeight.Bold
                },

            color =
                if (valueBold) {
                    DarkGreen
                } else {
                    Charcoal
                }
        )
    }
}


// ============================================================
// PAYMENT LOADING CARD
// ============================================================

@Composable
private fun PaymentLoadingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    1.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier =
                    Modifier
                        .size(58.dp)
                        .background(
                            color =
                                LightGreen.copy(
                                    alpha = 0.14f
                                ),

                            shape =
                                CircleShape
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
                        Modifier.size(27.dp),

                    tint =
                        ForestGreen
                )
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            CircularProgressIndicator(
                modifier =
                    Modifier.size(28.dp),

                color =
                    ForestGreen,

                strokeWidth =
                    3.dp
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            Text(
                text =
                    title,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Charcoal,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )


            Text(
                text =
                    message,

                fontSize =
                    12.sp,

                color =
                    Gray,

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    18.sp
            )
        }
    }
}


// ============================================================
// PAYMENT ERROR CARD
// ============================================================

@Composable
private fun PaymentErrorCard(
    error: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    1.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(22.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier =
                    Modifier
                        .size(56.dp)
                        .background(
                            color =
                                Color(0xFFFFEBEE),

                            shape =
                                CircleShape
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ErrorOutline,

                    contentDescription =
                        "Payment error",

                    modifier =
                        Modifier.size(28.dp),

                    tint =
                        Color(0xFFC62828)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(13.dp)
            )


            Text(
                text =
                    "Payment Error",

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Charcoal
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(
                text =
                    error,

                fontSize =
                    12.sp,

                color =
                    Gray,

                textAlign =
                    TextAlign.Center,

                lineHeight =
                    18.sp
            )
        }
    }
}


// ============================================================
// PAYMENT READY CARD
// ============================================================

@Composable
private fun PaymentReadyCard(
    amount: Int,
    enabled: Boolean,
    onPayClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
        ) {

            // ================================================
            // READY HEADER
            // ================================================

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .background(
                                color =
                                    LightGreen.copy(
                                        alpha = 0.14f
                                    ),

                                shape =
                                    CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,

                        contentDescription =
                            "Payment ready",

                        modifier =
                            Modifier.size(24.dp),

                        tint =
                            ForestGreen
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
                            "Payment Ready",

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Charcoal
                    )


                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )


                    Text(
                        text =
                            "Your order is ready for payment.",

                        fontSize =
                            11.sp,

                        color =
                            Gray
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            HorizontalDivider(
                color =
                    LightGray
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // ================================================
            // TOTAL AMOUNT
            // ================================================

            Text(
                text =
                    "Total Amount",

                fontSize =
                    12.sp,

                color =
                    Gray
            )


            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )


            Text(
                text =
                    "₹$amount",

                fontSize =
                    28.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    DarkGreen
            )


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // ================================================
            // PAY NOW BUTTON
            // ================================================

            Button(
                onClick =
                    onPayClick,

                enabled =
                    enabled,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                shape =
                    RoundedCornerShape(13.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            DarkGreen,

                        disabledContainerColor =
                            DarkGreen.copy(
                                alpha = 0.5f
                            )
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Lock,

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
                        "Pay ₹$amount",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}
