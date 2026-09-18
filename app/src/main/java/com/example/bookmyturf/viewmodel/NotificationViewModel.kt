package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    // =========================================================
    // REPOSITORY
    // =========================================================

    private val repository = NotificationRepository(
        RetrofitClient.api
    )


    // =========================================================
    // NOTIFICATIONS
    // =========================================================

    private val _notifications =
        MutableStateFlow<List<NotificationItem>>(emptyList())

    val notifications: StateFlow<List<NotificationItem>> =
        _notifications.asStateFlow()


    // =========================================================
    // UNREAD COUNT
    // =========================================================

    private val _unreadCount =
        MutableStateFlow(0)

    val unreadCount: StateFlow<Int> =
        _unreadCount.asStateFlow()


    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // ERROR
    // =========================================================

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()


    // =========================================================
    // LOAD NOTIFICATIONS
    // =========================================================

    fun loadNotifications(token: String) {

        viewModelScope.launch {

            _isLoading.value = true

            _errorMessage.value = null

            try {

                val response =
                    repository.getNotifications(
                        authorization = "Bearer $token"
                    )

                if (response.success) {

                    _notifications.value =
                        response.data?.notifications
                            ?: emptyList()

                    // Keep unread count synchronized
                    _unreadCount.value =
                        _notifications.value.count {
                            !it.isRead
                        }

                } else {

                    _errorMessage.value =
                        response.message
                            ?: "Unable to load notifications."
                }

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message
                        ?: "Unable to load notifications."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // LOAD UNREAD COUNT
    // =========================================================

    fun loadUnreadCount(token: String) {

        viewModelScope.launch {

            try {

                val response =
                    repository.getUnreadNotificationCount(
                        authorization = "Bearer $token"
                    )

                if (response.success) {

                    _unreadCount.value =
                        response.data?.unreadCount
                            ?: 0
                }

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message
                        ?: "Unable to load unread count."
            }
        }
    }


    // =========================================================
    // MARK SINGLE NOTIFICATION AS READ
    // =========================================================

    fun markAsRead(
        token: String,
        notificationId: String
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.markNotificationAsRead(
                        authorization = "Bearer $token",
                        notificationId = notificationId
                    )

                if (response.success) {

                    _notifications.value =
                        _notifications.value.map { notification ->

                            if (
                                notification.id ==
                                notificationId
                            ) {

                                notification.copy(
                                    isRead = true
                                )

                            } else {

                                notification
                            }
                        }

                    // Recalculate unread count
                    _unreadCount.value =
                        _notifications.value.count {
                            !it.isRead
                        }

                } else {

                    _errorMessage.value =
                        response.message
                            ?: "Unable to mark notification as read."
                }

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message
                        ?: "Unable to mark notification as read."
            }
        }
    }


    // =========================================================
    // MARK ALL NOTIFICATIONS AS READ
    // =========================================================

    fun markAllAsRead(token: String) {

        viewModelScope.launch {

            try {

                val response =
                    repository.markAllNotificationsAsRead(
                        authorization = "Bearer $token"
                    )

                if (response.success) {

                    _notifications.value =
                        _notifications.value.map { notification ->

                            notification.copy(
                                isRead = true
                            )
                        }

                    _unreadCount.value = 0

                } else {

                    _errorMessage.value =
                        response.message
                            ?: "Unable to mark all notifications as read."
                }

            } catch (exception: Exception) {

                _errorMessage.value =
                    exception.message
                        ?: "Unable to mark all notifications as read."
            }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _errorMessage.value = null
    }
}
