package com.example.bookmyturf

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
import com.razorpay.PaymentResultWithDataListener
import com.razorpay.PaymentData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            BookMyTurfTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val navController =
                        rememberNavController()

                    AppNavigation(
                        navController = navController
                    )
                }
            }
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

            return
        }


        _paymentSuccess.tryEmit(

            PaymentSuccessData(

                paymentId = razorpayPaymentId,

                orderId = paymentData.orderId,

                signature = paymentData.signature
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
            response ?: "Payment failed"
        )
    }
}


// =============================================================
// PAYMENT SUCCESS DATA
// =============================================================

data class PaymentSuccessData(

    val paymentId: String,

    val orderId: String?,

    val signature: String?
)