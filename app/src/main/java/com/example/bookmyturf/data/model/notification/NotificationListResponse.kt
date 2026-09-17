package com.example.bookmyturf.data.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: NotificationListData? = null
)

data class NotificationListData(
    @SerializedName("notifications")
    val notifications: List<NotificationItem> = emptyList()
)

data class NotificationItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String = "Notification",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("booking_id")
    val bookingId: Int? = null,

    @SerializedName("is_read")
    val isRead: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String? = null
)