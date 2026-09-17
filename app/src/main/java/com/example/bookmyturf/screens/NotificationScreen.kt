package com.example.bookmyturf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.viewmodel.NotificationViewModel

private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFFEAF4E6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    sessionManager: SessionManager,
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    val token = sessionManager.getToken()

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            viewModel.loadNotifications(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (notifications.any { !it.isRead }) {
                        IconButton(
                            onClick = {
                                if (!token.isNullOrBlank()) {
                                    viewModel.markAllAsRead(token)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "Mark all as read"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ForestGreen
                    )
                }

                !errorMessage.isNullOrBlank() -> {
                    Text(
                        text = errorMessage ?: "Something went wrong.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                notifications.isEmpty() -> {
                    EmptyNotifications()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = notifications,
                            key = { notification -> notification.id }
                        ) { notification ->

                            NotificationCard(
                                notification = notification,
                                onClick = {
                                    if (
                                        !notification.isRead &&
                                        !token.isNullOrBlank()
                                    ) {
                                        viewModel.markAsRead(
                                            token = token,
                                            notificationId = notification.id
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    val isUnread = !notification.isRead

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = if (isUnread) {
            LightGreen
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = if (isUnread) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = ForestGreen,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    if (isUnread) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(
                                    color = ForestGreen,
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                notification.createdAt?.let { createdAt ->
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = createdAt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotifications() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = ForestGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No notifications yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your booking updates will appear here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}