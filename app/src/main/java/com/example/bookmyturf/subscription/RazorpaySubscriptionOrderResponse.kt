package com.example.bookmyturf.data.model.notification

import com.google.gson.annotations.SerializedName

data class RazorpaySubscriptionOrderResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: RazorpaySubscriptionOrderData? = null
)

data class RazorpaySubscriptionOrderData(
    @SerializedName("subscription_id")
    val subscriptionId: Int? = null,

    @SerializedName("order_id")
    val orderId: String? = null,

    @SerializedName("amount")
    val amount: String? = null,

    @SerializedName("amount_paise")
    val amountPaise: Int? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("plan")
    val plan: String? = null,

    @SerializedName("razorpay_key")
    val razorpayKey: String? = null
)