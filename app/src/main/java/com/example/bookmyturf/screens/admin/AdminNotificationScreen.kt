package com.example.bookmyturf.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.viewmodel.NotificationViewModel

import java.text.SimpleDateFormat
import java.util.Locale


// =============================================================
// PREMIUM DARK THEME
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)


// =============================================================
// ADMIN NOTIFICATION SCREEN
// =============================================================

@Composable
fun AdminNotificationScreen(
    token: String,
    viewModel: NotificationViewModel,
    onBackClick: () -> Unit
) {

    // =========================================================
    // STATE
    // =========================================================

    val notifications by viewModel.notifications.collectAsState()

    val unreadCount by viewModel.unreadCount.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()


    // =========================================================
    // LOAD NOTIFICATIONS
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {

            viewModel.loadNotifications(token)
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
        containerColor = Background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Background)
        ) {

            // =================================================
            // PREMIUM TOP BAR
            // =================================================

            NotificationTopBar(
                unreadCount = unreadCount,
                onBackClick = onBackClick,
                onMarkAllRead = {
                    viewModel.markAllAsRead(token)
                }
            )


            // =================================================
            // CONTENT
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when {

                    // =============================================
                    // LOADING
                    // =============================================

                    isLoading && notifications.isEmpty() -> {

                        NotificationLoading()
                    }


                    // =============================================
                    // ERROR
                    // =============================================

                    errorMessage != null &&
                            notifications.isEmpty() -> {

                        NotificationError(
                            message =
                                errorMessage
                                    ?: "Unable to load notifications.",

                            onRetry = {

                                viewModel.clearError()

                                viewModel.loadNotifications(token)
                            }
                        )
                    }


                    // =============================================
                    // EMPTY
                    // =============================================

                    notifications.isEmpty() -> {

                        NotificationEmpty()
                    }


                    // =============================================
                    // NOTIFICATION LIST
                    // =============================================

                    else -> {

                        NotificationList(
                            notifications = notifications,

                            onNotificationClick = { notification ->

                                if (!notification.isRead) {

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


// =============================================================
// PREMIUM TOP BAR
// =============================================================

@Composable
private fun NotificationTopBar(
    unreadCount: Int,
    onBackClick: () -> Unit,
    onMarkAllRead: () -> Unit
) {

    /*
     * The status-bar inset is included INSIDE the top bar.
     *
     * This makes the premium dark gradient continue behind
     * the Android status-bar area instead of starting below it.
     */

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF071810),
                        Background
                    )
                )
            )
            .windowInsetsPadding(
                WindowInsets.statusBars
            )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // =================================================
            // HEADER ROW
            // =================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 14.dp,
                        end = 14.dp,
                        top = 10.dp,
                        bottom = 14.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =================================================
                // BACK BUTTON
                // =================================================

                Surface(
                    modifier =
                        Modifier.size(42.dp),

                    shape =
                        RoundedCornerShape(14.dp),

                    color =
                        SurfaceElevated,

                    border =
                        BorderStroke(
                            width = 1.dp,
                            color = Border
                        )
                ) {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                PrimaryText,

                            modifier =
                                Modifier.size(20.dp)
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )


                // =================================================
                // TITLE
                // =================================================

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Notifications",

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            (-0.3).sp,

                        color =
                            PrimaryText
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            if (unreadCount > 0) {

                                if (unreadCount == 1) {
                                    "1 unread notification"
                                } else {
                                    "$unreadCount unread notifications"
                                }

                            } else {

                                "You're all caught up"
                            },

                        fontSize =
                            11.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            if (unreadCount > 0) {
                                LightGreen
                            } else {
                                MutedText
                            }
                    )
                }


                // =================================================
                // READ ALL
                // =================================================

                if (unreadCount > 0) {

                    Surface(
                        modifier =
                            Modifier
                                .clip(
                                    RoundedCornerShape(11.dp)
                                )
                                .clickable(
                                    onClick = onMarkAllRead
                                ),

                        shape =
                            RoundedCornerShape(11.dp),

                        color =
                            SurfaceHighlight,

                        border =
                            BorderStroke(
                                width = 1.dp,
                                color = Border
                            )
                    ) {

                        Row(
                            modifier =
                                Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 8.dp
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
                                    Modifier.size(16.dp),

                                tint =
                                    PrimaryGreen
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
                                    PrimaryText
                            )
                        }
                    }
                }
            }


            // =================================================
            // DIVIDER
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Border,
                                PrimaryGreen.copy(
                                    alpha = 0.18f
                                ),
                                Border,
                                Color.Transparent
                            )
                        )
                    )
            )
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
                top = 18.dp,
                end = 16.dp,
                bottom = 28.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        items(
            items = notifications,

            key = { notification ->
                notification.id
            }
        ) { notification ->

            NotificationCard(
                notification = notification,

                onClick = {
                    onNotificationClick(notification)
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
        modifier = Modifier
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
                SurfaceElevated
            } else {
                SurfaceDark
            },

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    if (isUnread) {
                        PrimaryGreen.copy(
                            alpha = 0.32f
                        )
                    } else {
                        Border.copy(
                            alpha = 0.75f
                        )
                    }
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),

            verticalAlignment =
                Alignment.Top
        ) {

            // =================================================
            // ICON
            // =================================================

            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (isUnread) {
                                SurfaceHighlight
                            } else {
                                SurfaceDark
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
                            PrimaryGreen
                        } else {
                            MutedText
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
                // TITLE + UNREAD DOT
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
                                PrimaryText
                            } else {
                                SecondaryText
                            },

                        maxLines =
                            1,

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
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        PrimaryGreen
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
                        SecondaryText,

                    maxLines =
                        3,

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
                        MutedText
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
                SurfaceHighlight,

            border =
                BorderStroke(
                    width = 1.dp,
                    color =
                        Border.copy(
                            alpha = 0.8f
                        )
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
                        PrimaryGreen,

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
                PrimaryText
        )


        Spacer(
            modifier =
                Modifier.height(5.dp)
        )


        Text(
            text =
                "Please wait",

            fontSize =
                12.sp,

            color =
                MutedText
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
                SurfaceHighlight,

            border =
                BorderStroke(
                    width = 1.dp,
                    color =
                        Border.copy(
                            alpha = 0.9f
                        )
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
                        PrimaryGreen
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
                FontWeight.Bold,

            color =
                PrimaryText
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
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
                SecondaryText,

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
                ErrorRed.copy(
                    alpha = 0.08f
                ),

            border =
                BorderStroke(
                    width = 1.dp,
                    color =
                        ErrorRed.copy(
                            alpha = 0.22f
                        )
                )
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
                        Modifier.size(36.dp),

                    tint =
                        ErrorRed
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
                PrimaryText,

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
                SecondaryText,

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
                        PrimaryGreen,

                    contentColor =
                        Background
                )
        ) {

            Text(
                text =
                    "Try Again",

                fontWeight =
                    FontWeight.Bold
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
            inputFormat.parse(createdAt)

        if (date != null) {

            outputFormat.format(date)

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
                fallbackInput.parse(createdAt)

            if (date != null) {

                outputFormat.format(date)

            } else {

                createdAt
            }

        } catch (fallbackException: Exception) {

            createdAt
        }
    }
}