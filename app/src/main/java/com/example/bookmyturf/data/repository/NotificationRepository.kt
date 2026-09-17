package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.GenericResponse
import com.example.bookmyturf.data.model.notification.NotificationListResponse
import com.example.bookmyturf.data.model.notification.UnreadCountResponse
import com.example.bookmyturf.data.remote.ApiService

class NotificationRepository(
    private val apiService: ApiService
) {

    suspend fun getNotifications(
        authorization: String
    ): NotificationListResponse {
        return apiService.getNotifications(authorization)
    }

    suspend fun getUnreadNotificationCount(
        authorization: String
    ): UnreadCountResponse {
        return apiService.getUnreadNotificationCount(authorization)
    }

    suspend fun markNotificationAsRead(
        authorization: String,
        notificationId: String
    ): GenericResponse {
        return apiService.markNotificationAsRead(
            authorization = authorization,
            notificationId = notificationId
        )
    }

    suspend fun markAllNotificationsAsRead(
        authorization: String
    ): GenericResponse {
        return apiService.markAllNotificationsAsRead(authorization)
    }
}