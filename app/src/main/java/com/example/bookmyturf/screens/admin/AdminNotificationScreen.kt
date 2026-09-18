package com.example.bookmyturf.screens.admin

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NotificationsNone

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.NotificationViewModel

import java.text.SimpleDateFormat
import java.util.Locale


// =============================================================
// ADMIN NOTIFICATION SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNotificationScreen(
    token: String,
    viewModel: NotificationViewModel,
    onBackClick: () -> Unit
) {

    // =========================================================
    // STATE
    // =========================================================

    val notifications by
    viewModel.notifications.collectAsState()

    val unreadCount by
    viewModel.unreadCount.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val errorMessage by
    viewModel.errorMessage.collectAsState()


    // =========================================================
    // LOAD NOTIFICATIONS
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {

            viewModel.loadNotifications(
                token
            )
        }
    }


    // =========================================================
    // SYSTEM BACK
    // =========================================================

    BackHandler {

        onBackClick()
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        containerColor =
            AdminOffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Notifications",

                            fontSize = 20.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                AdminDarkGreen
                        )

                        if (unreadCount > 0) {

                            Text(
                                text =
                                    if (unreadCount == 1) {
                                        "1 unread notification"
                                    } else {
                                        "$unreadCount unread notifications"
                                    },

                                fontSize = 11.sp,

                                fontWeight =
                                    FontWeight.Medium,

                                color =
                                    AdminGray
                            )
                        } else {

                            Text(
                                text =
                                    "You're all caught up",

                                fontSize = 11.sp,

                                fontWeight =
                                    FontWeight.Medium,

                                color =
                                    AdminGray
                            )
                        }
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                AdminDarkGreen,

                            modifier =
                                Modifier.size(23.dp)
                        )
                    }
                },

                actions = {

                    if (unreadCount > 0) {

                        Surface(

                            modifier =
                                Modifier
                                    .padding(
                                        end = 12.dp
                                    )
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {

                                        viewModel.markAllAsRead(
                                            token
                                        )
                                    },

                            color =
                                AdminLightGreen.copy(
                                    alpha = 0.18f
                                )
                        ) {

                            Row(

                                modifier =
                                    Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 7.dp
                                    ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.DoneAll,

                                    contentDescription =
                                        "Mark all as read",

                                    modifier =
                                        Modifier.size(17.dp),

                                    tint =
                                        AdminDarkGreen
                                )

                                Spacer(
                                    modifier =
                                        Modifier.width(5.dp)
                                )

                                Text(
                                    text =
                                        "Read all",

                                    fontSize =
                                        11.sp,

                                    fontWeight =
                                        FontWeight.SemiBold,

                                    color =
                                        AdminDarkGreen
                                )
                            }
                        }
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(

                        containerColor =
                            AdminWhite,

                        titleContentColor =
                            AdminDarkGreen,

                        navigationIconContentColor =
                            AdminDarkGreen,

                        actionIconContentColor =
                            AdminDarkGreen
                    )
            )
        }

    ) { paddingValues ->

        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues
                    )
        ) {

            when {

                // =================================================
                // LOADING
                // =================================================

                isLoading &&
                        notifications.isEmpty() -> {

                    NotificationLoading()
                }


                // =================================================
                // ERROR
                // =================================================

                errorMessage != null &&
                        notifications.isEmpty() -> {

                    NotificationError(

                        message =
                            errorMessage
                                ?: "Unable to load notifications.",

                        onRetry = {

                            viewModel.clearError()

                            viewModel.loadNotifications(
                                token
                            )
                        }
                    )
                }


                // =================================================
                // EMPTY
                // =================================================

                notifications.isEmpty() -> {

                    NotificationEmpty()
                }


                // =================================================
                // NOTIFICATION LIST
                // =================================================

                else -> {

                    NotificationList(

                        notifications =
                            notifications,

                        onNotificationClick = {
                                notification ->

                            if (!notification.isRead) {

                                viewModel.markAsRead(

                                    token =
                                        token,

                                    notificationId =
                                        notification.id
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


// =============================================================
// NOTIFICATION LIST
// =============================================================

@Composable
private fun NotificationList(
    notifications: List<NotificationItem>,
    onNotificationClick:
        (NotificationItem) -> Unit
) {

    LazyColumn(

        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 28.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        items(

            items =
                notifications,

            key = {
                    notification ->
                notification.id
            }

        ) { notification ->

            NotificationCard(

                notification =
                    notification,

                onClick = {

                    onNotificationClick(
                        notification
                    )
                }
            )
        }
    }
}


// =============================================================
// NOTIFICATION CARD
// =============================================================

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {

    val isUnread =
        !notification.isRead


    Surface(

        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .clickable(
                    onClick = onClick
                ),

        shape =
            RoundedCornerShape(18.dp),

        color =
            if (isUnread) {

                AdminWhite

            } else {

                AdminWhite.copy(
                    alpha = 0.82f
                )
            },

        border =
            if (isUnread) {

                BorderStroke(
                    width = 1.dp,
                    color =
                        AdminLightGreen.copy(
                            alpha = 0.45f
                        )
                )

            } else {

                BorderStroke(
                    width = 1.dp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .outlineVariant
                            .copy(
                                alpha = 0.45f
                            )
                )
            },

        tonalElevation =
            if (isUnread) {
                2.dp
            } else {
                0.dp
            },

        shadowElevation =
            if (isUnread) {
                2.dp
            } else {
                1.dp
            }
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.Top
        ) {

            // =================================================
            // NOTIFICATION ICON
            // =================================================

            Box(

                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(
                            CircleShape
                        )
                        .background(

                            if (isUnread) {

                                AdminDarkGreen

                            } else {

                                AdminOffWhite
                            }
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector =
                        Icons.Default.NotificationsNone,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(22.dp),

                    tint =
                        if (isUnread) {

                            AdminWhite

                        } else {

                            AdminGray
                        }
                )
            }


            Spacer(
                modifier =
                    Modifier.width(13.dp)
            )


            // =================================================
            // CONTENT
            // =================================================

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                // =================================================
                // TITLE + STATUS
                // =================================================

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        text =
                            notification.title,

                        modifier =
                            Modifier.weight(1f),

                        fontSize =
                            15.sp,

                        fontWeight =
                            if (isUnread) {

                                FontWeight.Bold

                            } else {

                                FontWeight.SemiBold
                            },

                        color =
                            if (isUnread) {

                                AdminDarkGreen

                            } else {

                                MaterialTheme
                                    .colorScheme
                                    .onSurface
                            },

                        maxLines = 1,

                        overflow =
                            TextOverflow.Ellipsis
                    )


                    if (isUnread) {

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Box(

                            modifier =
                                Modifier
                                    .size(9.dp)
                                    .clip(
                                        CircleShape
                                    )
                                    .background(
                                        AdminDarkGreen
                                    )
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )


                // =================================================
                // MESSAGE
                // =================================================

                Text(

                    text =
                        notification.message,

                    fontSize =
                        13.sp,

                    lineHeight =
                        19.sp,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,

                    maxLines = 3,

                    overflow =
                        TextOverflow.Ellipsis
                )


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                // =================================================
                // DATE
                // =================================================

                Text(

                    text =
                        formatNotificationDate(
                            notification.createdAt
                        ),

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Medium,

                    color =
                        AdminGray
                )
            }
        }
    }
}


// =============================================================
// LOADING STATE
// =============================================================

@Composable
private fun NotificationLoading() {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(68.dp),

            shape =
                CircleShape,

            color =
                AdminLightGreen.copy(
                    alpha = 0.18f
                )
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                CircularProgressIndicator(

                    modifier =
                        Modifier.size(30.dp),

                    color =
                        AdminDarkGreen,

                    strokeWidth =
                        3.dp
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        Text(

            text =
                "Loading notifications...",

            fontSize =
                15.sp,

            fontWeight =
                FontWeight.SemiBold,

            color =
                AdminDarkGreen
        )
    }
}


// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun NotificationEmpty() {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(84.dp),

            shape =
                CircleShape,

            color =
                AdminLightGreen.copy(
                    alpha = 0.18f
                )
        ) {

            Box(

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector =
                        Icons.Default.NotificationsNone,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(40.dp),

                    tint =
                        AdminDarkGreen
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        Text(

            text =
                "You're all caught up",

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                AdminDarkGreen
        )


        Spacer(
            modifier =
                Modifier.height(7.dp)
        )


        Text(

            text =
                "New booking and account updates will appear here.",

            modifier =
                Modifier.fillMaxWidth(),

            fontSize =
                13.sp,

            lineHeight =
                19.sp,

            color =
                AdminGray,

            textAlign =
                TextAlign.Center
        )
    }
}


// =============================================================
// ERROR STATE
// =============================================================

@Composable
private fun NotificationError(
    message: String,
    onRetry: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(72.dp),

            shape =
                CircleShape,

            color =
                MaterialTheme
                    .colorScheme
                    .errorContainer
        ) {

            Box(

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector =
                        Icons.Default.ErrorOutline,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(38.dp),

                    tint =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        Text(

            text =
                "Unable to load notifications",

            fontSize =
                18.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                AdminDarkGreen,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(

            text =
                message,

            modifier =
                Modifier.fillMaxWidth(),

            fontSize =
                13.sp,

            lineHeight =
                19.sp,

            color =
                AdminGray,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        Button(

            onClick =
                onRetry,

            modifier =
                Modifier.height(46.dp),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        AdminDarkGreen
                )
        ) {

            Text(
                text =
                    "Try Again",

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


// =============================================================
// DATE FORMATTER
// =============================================================

private fun formatNotificationDate(
    createdAt: String?
): String {

    if (createdAt.isNullOrBlank()) {

        return ""
    }


    return try {

        val inputFormat =
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                Locale.getDefault()
            )

        val outputFormat =
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

        val date =
            inputFormat.parse(
                createdAt
            )

        if (date != null) {

            outputFormat.format(
                date
            )

        } else {

            createdAt
        }

    } catch (exception: Exception) {

        try {

            val fallbackInput =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.getDefault()
                )

            val outputFormat =
                SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
                )

            val date =
                fallbackInput.parse(
                    createdAt
                )

            if (date != null) {

                outputFormat.format(
                    date
                )

            } else {

                createdAt
            }

        } catch (fallbackException: Exception) {

            createdAt
        }
    }
}
