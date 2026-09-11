package com.example.bookmyturf

import android.app.Activity
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import androidx.navigation.compose.rememberNavController

import com.example.bookmyturf.navigation.AppNavigation
import com.example.bookmyturf.ui.theme.BookMyTurfTheme

import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

import org.json.JSONObject


class MainActivity :
    ComponentActivity(),
    PaymentResultWithDataListener {

    companion object {

        private val _paymentSuccess =
            MutableSharedFlow<PaymentSuccessData>(
                extraBufferCapacity = 1
            )

        val paymentSuccess =
            _paymentSuccess.asSharedFlow()


        private val _paymentError =
            MutableSharedFlow<String>(
                extraBufferCapacity = 1
            )

        val paymentError =
            _paymentError.asSharedFlow()
    }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        enableEdgeToEdge()

        /*
         * Preload Razorpay resources early.
         */
        Checkout.preload(
            applicationContext
        )

        setContent {

            BookMyTurfTheme {

                Surface(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    val navController =
                        rememberNavController()

                    AppNavigation(
                        navController =
                            navController
                    )
                }
            }
        }
    }

    fun startSubscriptionPayment(
        razorpayKey: String,
        orderId: String,
        amountPaise: Int,
        plan: String
    ) {
        try {
            val checkout = Checkout()

            checkout.setKeyID(razorpayKey)

            val options = JSONObject()

            options.put("name", "BookMyTurf")
            options.put(
                "description",
                if (plan == "YEARLY") {
                    "BookMyTurf PRO Yearly Subscription"
                } else {
                    "BookMyTurf PRO Monthly Subscription"
                }
            )

            // Amount must be in paise
            options.put("amount", amountPaise)

            options.put("currency", "INR")

            // IMPORTANT: Must be the order_id created by Laravel
            options.put("order_id", orderId)

            // Optional prefill
            val prefill = JSONObject()
            prefill.put("email", "")
            options.put("prefill", prefill)

            // Optional retry settings
            val retry = JSONObject()
            retry.put("enabled", true)
            retry.put("max_count", 4)
            options.put("retry", retry)

            // Theme
            val theme = JSONObject()
            theme.put("color", "#173D20")
            options.put("theme", theme)

            Log.d("RAZORPAY", "================================")
            Log.d("RAZORPAY", "Opening subscription checkout")
            Log.d("RAZORPAY", "Key = $razorpayKey")
            Log.d("RAZORPAY", "Order ID = $orderId")
            Log.d("RAZORPAY", "Amount Paise = $amountPaise")
            Log.d("RAZORPAY", "Plan = $plan")
            Log.d("RAZORPAY", "Options = $options")
            Log.d("RAZORPAY", "================================")

            checkout.open(this, options)

        } catch (e: Exception) {
            Log.e(
                "RAZORPAY",
                "Error starting Razorpay Checkout",
                e
            )

            _paymentError.tryEmit(
                e.message ?: "Unable to open Razorpay Checkout."
            )
        }
    }


    // =========================================================
    // RAZORPAY SUCCESS
    // =========================================================

    override fun onPaymentSuccess(
        razorpayPaymentId: String?,
        paymentData: PaymentData
    ) {

        Log.d(
            "RAZORPAY",
            "Payment Success"
        )

        Log.d(
            "RAZORPAY",
            "Payment ID = $razorpayPaymentId"
        )

        Log.d(
            "RAZORPAY",
            "Order ID = ${paymentData.orderId}"
        )

        Log.d(
            "RAZORPAY",
            "Signature = ${paymentData.signature}"
        )


        if (
            razorpayPaymentId.isNullOrBlank()
        ) {

            Log.e(
                "RAZORPAY",
                "Payment ID is missing"
            )

            _paymentError.tryEmit(
                "Payment ID is missing."
            )

            return
        }


        val orderId =
            paymentData.orderId

        val signature =
            paymentData.signature


        if (
            orderId.isNullOrBlank() ||
            signature.isNullOrBlank()
        ) {

            Log.e(
                "RAZORPAY",
                "Order ID or signature is missing"
            )

            _paymentError.tryEmit(
                "Payment verification information is incomplete."
            )

            return
        }


        _paymentSuccess.tryEmit(

            PaymentSuccessData(

                paymentId =
                    razorpayPaymentId,

                orderId =
                    orderId,

                signature =
                    signature
            )
        )
    }


    // =========================================================
    // RAZORPAY ERROR
    // =========================================================

    override fun onPaymentError(
        code: Int,
        response: String?,
        paymentData: PaymentData?
    ) {

        Log.e(
            "RAZORPAY",
            "Payment Error"
        )

        Log.e(
            "RAZORPAY",
            "Error Code = $code"
        )

        Log.e(
            "RAZORPAY",
            "Error Response = $response"
        )

        Log.e(
            "RAZORPAY",
            "Payment Data = $paymentData"
        )

        _paymentError.tryEmit(
            response
                ?: "Payment failed."
        )
    }
}


// =============================================================
// PAYMENT SUCCESS DATA
// =============================================================

data class PaymentSuccessData(

    val paymentId: String,

    val orderId: String,

    val signature: String
)

