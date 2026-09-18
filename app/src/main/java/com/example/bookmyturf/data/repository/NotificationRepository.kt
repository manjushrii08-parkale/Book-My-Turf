package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.GenericResponse
import com.example.bookmyturf.data.model.notification.NotificationListResponse
import com.example.bookmyturf.data.model.notification.UnreadCountResponse
import com.example.bookmyturf.data.remote.ApiService

class NotificationRepository(
    private val apiService: ApiService
) {

    // =========================================================
    // GET ALL NOTIFICATIONS
    // =========================================================

    suspend fun getNotifications(
        authorization: String
    ): NotificationListResponse {

        return apiService.getNotifications(
            authorization = authorization
        )
    }


    // =========================================================
    // GET UNREAD NOTIFICATION COUNT
    // =========================================================

    suspend fun getUnreadNotificationCount(
        authorization: String
    ): UnreadCountResponse {

        return apiService.getUnreadNotificationCount(
            authorization = authorization
        )
    }


    // =========================================================
    // MARK SINGLE NOTIFICATION AS READ
    // =========================================================

    suspend fun markNotificationAsRead(
        authorization: String,
        notificationId: String
    ): GenericResponse {

        return apiService.markNotificationAsRead(
            authorization = authorization,
            notificationId = notificationId
        )
    }


    // =========================================================
    // MARK ALL NOTIFICATIONS AS READ
    // =========================================================

    suspend fun markAllNotificationsAsRead(
        authorization: String
    ): GenericResponse {

        return apiService.markAllNotificationsAsRead(
            authorization = authorization
        )
    }
}
