package com.example.bookmyturf.data.model.notification

import com.google.gson.annotations.SerializedName

data class UnreadCountResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: UnreadCountData? = null
)

data class UnreadCountData(
    @SerializedName("unread_count")
    val unreadCount: Int = 0
)