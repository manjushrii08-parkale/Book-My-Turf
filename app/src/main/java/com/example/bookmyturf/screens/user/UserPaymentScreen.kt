package com.example.bookmyturf.screens.user

import android.app.Activity
import android.util.Log
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
// PREMIUM BOOK MY TURF THEME
// ============================================================

private val Background = Color(0xFF020C09)
private val CardBackground = Color(0xFF071713)
private val SecondarySurface = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFB7E77A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFF9EAEA6)
private val MutedText = Color(0xFF718079)

private val BorderColor = Color(0xFF1B3028)

private val ErrorRed = Color(0xFFFF6B6B)
private val ErrorSurface = Color(0xFF21100F)


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

    // ========================================================
    // CONTEXT
    // ========================================================

    val context = LocalContext.current


    // ========================================================
    // SESSION
    // ========================================================

    val sessionManager = remember {
        SessionManager(context)
    }

    val token = remember {
        sessionManager.getToken()
    }


    // ========================================================
    // REPOSITORY
    // ========================================================

    val repository = remember {
        PaymentRepository(
            RetrofitClient.paymentApi
        )
    }


    // ========================================================
    // VIEWMODEL
    // ========================================================

    val factory = remember {
        PaymentViewModelFactory(
            repository
        )
    }

    val paymentViewModel: PaymentViewModel =
        viewModel(
            factory = factory
        )


    // ========================================================
    // STATE
    // ========================================================

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


    // ========================================================
    // CREATE RAZORPAY ORDER
    // ========================================================

    LaunchedEffect(bookingId) {

        if (!token.isNullOrBlank()) {

            Log.d(
                "RAZORPAY",
                "================================"
            )

            Log.d(
                "RAZORPAY",
                "Creating Razorpay order"
            )

            Log.d(
                "RAZORPAY",
                "Booking ID = $bookingId"
            )

            Log.d(
                "RAZORPAY",
                "================================"
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

            Toast.makeText(
                context,
                "Authentication expired. Please login again.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    // ========================================================
    // RAZORPAY SUCCESS CALLBACK
    // ========================================================

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


            val paymentId =
                paymentData.paymentId

            val razorpayOrderId =
                paymentData.orderId

            val signature =
                paymentData.signature


            // ------------------------------------------------
            // TOKEN VALIDATION
            // ------------------------------------------------

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


            // ------------------------------------------------
            // PAYMENT ID VALIDATION
            // ------------------------------------------------

            if (paymentId.isNullOrBlank()) {

                Log.e(
                    "RAZORPAY",
                    "Razorpay Payment ID missing."
                )

                Toast.makeText(
                    context,
                    "Payment ID is missing.",
                    Toast.LENGTH_LONG
                ).show()

                return@collectLatest
            }


            // ------------------------------------------------
            // ORDER ID VALIDATION
            // ------------------------------------------------

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


            // ------------------------------------------------
            // SIGNATURE VALIDATION
            // ------------------------------------------------

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


            // ------------------------------------------------
            // VERIFY PAYMENT WITH LARAVEL
            // ------------------------------------------------

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


    // ========================================================
    // PAYMENT VERIFICATION RESULT
    // ========================================================

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


            // ------------------------------------------------
            // NAVIGATE ONLY AFTER LARAVEL VERIFICATION
            // ------------------------------------------------

            onPaymentSuccess()

            paymentViewModel.clearVerificationResult()
        }
    }


    // ========================================================
    // MAIN UI
    // ========================================================

    Scaffold(

        containerColor = Background,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Payment",
                            color = PrimaryText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Booking #$bookingId",
                            color = MutedText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
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

                            contentDescription = "Back",

                            tint = PrimaryText
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                        titleContentColor = PrimaryText,
                        navigationIconContentColor = PrimaryText
                    )
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 20.dp)
                .padding(
                    top = 12.dp,
                    bottom = 24.dp
                )
                .navigationBarsPadding()
        ) {


            // =================================================
            // PAGE INTRO
            // =================================================

            Text(
                text = "Complete your booking",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                letterSpacing = (-0.5).sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Review the amount and continue securely.",
                fontSize = 13.sp,
                color = SecondaryText
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================
            // AMOUNT HERO
            // =================================================

            PaymentAmountSection(
                bookingId = bookingId,
                amount = order?.amount?.div(100)
            )


            Spacer(
                modifier = Modifier.height(26.dp)
            )


            // =================================================
            // PAYMENT STATE
            // =================================================

            when {

                // ------------------------------------------------
                // LOADING
                // ------------------------------------------------

                isLoading -> {

                    PaymentProcessingSection(
                        icon = Icons.Default.AccessTime,
                        title = "Preparing payment",
                        message =
                            "Creating your secure payment order..."
                    )
                }


                // ------------------------------------------------
                // VERIFYING
                // ------------------------------------------------

                isVerifying -> {

                    PaymentProcessingSection(
                        icon = Icons.Default.Lock,
                        title = "Verifying payment",
                        message =
                            "Please wait while we confirm your payment."
                    )
                }


                // ------------------------------------------------
                // ERROR
                // ------------------------------------------------

                error != null -> {

                    PaymentErrorSection(
                        error =
                            error ?: "Unable to prepare payment."
                    )
                }


                // ------------------------------------------------
                // ORDER READY
                // ------------------------------------------------

                order != null -> {

                    PaymentActionSection(
                        amount =
                            order!!.amount / 100,

                        enabled =
                            !isVerifying,

                        onPayClick = {

                            // =================================================
                            // RAZORPAY CHECKOUT
                            // =================================================

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
                                    "Currency = ${order!!.currency}"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "Key ID = ${order!!.key_id}"
                                )

                                Log.d(
                                    "RAZORPAY",
                                    "================================"
                                )


                                // ---------------------------------------------
                                // CREATE CHECKOUT
                                // ---------------------------------------------

                                val checkout =
                                    Checkout()


                                // ---------------------------------------------
                                // SET RAZORPAY KEY
                                // ---------------------------------------------

                                checkout.setKeyID(
                                    order!!.key_id
                                )


                                // ---------------------------------------------
                                // CHECKOUT OPTIONS
                                // ---------------------------------------------

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
                                            "theme.color",
                                            "#7DBB4A"
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
                                    "Opening Razorpay Checkout..."
                                )


                                // ---------------------------------------------
                                // OPEN RAZORPAY
                                // ---------------------------------------------

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
                                    "Unable to open payment gateway.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }


                // ------------------------------------------------
                // DEFAULT
                // ------------------------------------------------

                else -> {

                    PaymentProcessingSection(
                        icon = Icons.Default.AccessTime,
                        title = "Preparing payment",
                        message =
                            "Please wait while we prepare your payment."
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // SECURITY FOOTER
            // =================================================

            SecurePaymentFooter()


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // BOOKING REFERENCE
            // =================================================

            Text(
                text = "Booking reference #$bookingId",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 11.sp,
                color = MutedText,
                textAlign = TextAlign.Center
            )
        }
    }
}


// ============================================================
// AMOUNT HERO
// ============================================================

@Composable
private fun PaymentAmountSection(
    bookingId: Int,
    amount: Int?
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardBackground,
                shape = RoundedCornerShape(24.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color =
                                PrimaryGreen.copy(
                                    alpha = 0.12f
                                ),
                            shape =
                                RoundedCornerShape(13.dp)
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp),
                        tint = LightGreen
                    )
                }


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column {

                    Text(
                        text = "Turf booking",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryText
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Booking #$bookingId",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            Text(
                text = "TOTAL AMOUNT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            if (amount != null) {

                Text(
                    text = "₹$amount",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BrightGreen,
                    letterSpacing = (-1).sp
                )

            } else {

                Text(
                    text = "—",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            HorizontalDivider(
                color = BorderColor
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Payment method",
                    fontSize = 12.sp,
                    color = SecondaryText
                )

                Text(
                    text = "Razorpay",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                )
            }
        }
    }
}


// ============================================================
// PAYMENT ACTION
// ============================================================

@Composable
private fun PaymentActionSection(
    amount: Int,
    enabled: Boolean,
    onPayClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(
                        color = PrimaryGreen,
                        shape = RoundedCornerShape(50)
                    )
            )

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Text(
                text = "Ready for payment",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )
        }


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        Button(
            onClick = onPayClick,
            enabled = enabled,

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),

            shape = RoundedCornerShape(17.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Color(0xFF10200F),
                disabledContainerColor =
                    PrimaryGreen.copy(alpha = 0.35f),
                disabledContentColor =
                    PrimaryText.copy(alpha = 0.5f)
            ),

            elevation =
                ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
        ) {

            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Text(
                text = "Pay ₹$amount",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Text(
            text = "You will be redirected to Razorpay",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 11.sp,
            color = MutedText,
            textAlign = TextAlign.Center
        )
    }
}


// ============================================================
// PROCESSING SECTION
// ============================================================

@Composable
private fun PaymentProcessingSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardBackground,
                shape = RoundedCornerShape(20.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color =
                            PrimaryGreen.copy(
                                alpha = 0.10f
                            ),
                        shape =
                            RoundedCornerShape(16.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(23.dp),
                    tint = LightGreen
                )
            }


            Spacer(
                modifier = Modifier.height(17.dp)
            )


            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                color = PrimaryGreen,
                strokeWidth = 2.5.dp
            )


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(5.dp)
            )


            Text(
                text = message,
                fontSize = 12.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}


// ============================================================
// ERROR SECTION
// ============================================================

@Composable
private fun PaymentErrorSection(
    error: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ErrorSurface,
                shape = RoundedCornerShape(20.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(23.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color =
                            ErrorRed.copy(
                                alpha = 0.10f
                            ),
                        shape =
                            RoundedCornerShape(16.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ErrorOutline,

                    contentDescription =
                        "Payment error",

                    modifier =
                        Modifier.size(25.dp),

                    tint = ErrorRed
                )
            }


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            Text(
                text = "Payment unavailable",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = error,
                fontSize = 12.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}


// ============================================================
// SECURITY FOOTER
// ============================================================

@Composable
private fun SecurePaymentFooter() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SecondarySurface.copy(
                    alpha = 0.55f
                ),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Secure payment",
            modifier = Modifier.size(18.dp),
            tint = LightGreen
        )


        Spacer(
            modifier = Modifier.width(10.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Secure checkout",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text =
                    "Payment is securely processed by Razorpay.",

                fontSize = 10.sp,
                color = MutedText,
                lineHeight = 15.sp
            )
        }
    }
}