package com.example.bookmyturf.screens

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.ui.theme.UserDarkGreen
import com.example.bookmyturf.ui.theme.UserLightGreen
import com.example.bookmyturf.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// ============================================================
// COLORS
// ============================================================

private val DarkGreen = UserDarkGreen
private val ForestGreen = androidx.compose.ui.graphics.Color(0xFF2E6B35)
private val LightGreen = UserLightGreen
private val OffWhite = androidx.compose.ui.graphics.Color(0xFFF8F8F5)
private val White = androidx.compose.ui.graphics.Color.White
private val Charcoal = androidx.compose.ui.graphics.Color(0xFF1C1C1C)
private val Gray = androidx.compose.ui.graphics.Color(0xFF737373)

// ============================================================
// NOTIFICATION SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    sessionManager: SessionManager,
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {

    // ========================================================
    // STATE
    // ========================================================

    val notifications by
    viewModel.notifications.collectAsStateWithLifecycle()

    val isLoading by
    viewModel.isLoading.collectAsStateWithLifecycle()

    val errorMessage by
    viewModel.errorMessage.collectAsStateWithLifecycle()

    val unreadCount =
        notifications.count { !it.isRead }

    // ========================================================
    // TOKEN
    // ========================================================

    val token = sessionManager.getToken()

    // ========================================================
    // LOAD NOTIFICATIONS
    // ========================================================

    LaunchedEffect(token) {

        if (!token.isNullOrBlank()) {

            viewModel.loadNotifications(
                token
            )
        }
    }

    // ========================================================
    // SCAFFOLD
    // ========================================================

    Scaffold(

        containerColor = OffWhite,

        topBar = {

            Column {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Notifications",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Charcoal
                            )

                            Text(
                                text =
                                    when {
                                        unreadCount > 0 ->
                                            "$unreadCount unread notification" +
                                                    if (unreadCount > 1) "s"
                                                    else ""

                                        else ->
                                            "You're all caught up"
                                    },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray
                            )
                        }
                    },

                    navigationIcon = {

                        Surface(

                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp),

                            shape = RoundedCornerShape(12.dp),

                            color =
                                DarkGreen.copy(
                                    alpha = 0.08f
                                )
                        ) {

                            IconButton(
                                onClick = onBackClick
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.ArrowBack,

                                    contentDescription =
                                        "Back",

                                    modifier =
                                        Modifier.size(21.dp),

                                    tint =
                                        DarkGreen
                                )
                            }
                        }
                    },

                    actions = {

                        if (unreadCount > 0) {

                            Surface(

                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clip(
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {

                                        if (
                                            !token.isNullOrBlank()
                                        ) {

                                            viewModel.markAllAsRead(
                                                token
                                            )
                                        }
                                    },

                                shape =
                                    RoundedCornerShape(12.dp),

                                color =
                                    LightGreen.copy(
                                        alpha = 0.16f
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
                                            Modifier.size(17.dp),

                                        tint =
                                            DarkGreen
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(5.dp)
                                    )

                                    Text(

                                        text = "Read all",

                                        fontSize = 11.sp,

                                        fontWeight =
                                            FontWeight.Bold,

                                        color =
                                            DarkGreen
                                    )
                                }
                            }
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = White
                        )
                )

                HorizontalDivider(
                    color =
                        androidx.compose.ui.graphics.Color(
                            0xFFE8E8E8
                        )
                )
            }
        }

    ) { paddingValues ->

        // ====================================================
        // CONTENT
        // ====================================================

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {

                // ============================================
                // LOADING
                // ============================================

                isLoading -> {

                    NotificationLoadingState()
                }

                // ============================================
                // ERROR
                // ============================================

                !errorMessage.isNullOrBlank() -> {

                    NotificationErrorState(

                        message =
                            errorMessage
                                ?: "Something went wrong.",

                        onRetry = {

                            if (
                                !token.isNullOrBlank()
                            ) {

                                viewModel.loadNotifications(
                                    token
                                )
                            }
                        }
                    )
                }

                // ============================================
                // EMPTY
                // ============================================

                notifications.isEmpty() -> {

                    EmptyNotifications()
                }

                // ============================================
                // NOTIFICATION LIST
                // ============================================

                else -> {

                    LazyColumn(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 28.dp
                            ),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        // ====================================
                        // SUMMARY HEADER
                        // ====================================

                        item {

                            NotificationSummary(
                                totalCount =
                                    notifications.size,
                                unreadCount =
                                    unreadCount
                            )
                        }

                        // ====================================
                        // NOTIFICATIONS
                        // ====================================

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

                                    if (
                                        !notification.isRead &&
                                        !token.isNullOrBlank()
                                    ) {

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
    }
}

// ============================================================
// SUMMARY HEADER
// ============================================================

@Composable
private fun NotificationSummary(
    totalCount: Int,
    unreadCount: Int
) {

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        color =
            White,

        border =
            BorderStroke(
                width = 1.dp,
                color =
                    androidx.compose.ui.graphics.Color(
                        0xFFE7E7E7
                    )
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            LightGreen.copy(
                                alpha = 0.16f
                            )
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
                        Modifier.size(23.dp),

                    tint =
                        DarkGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        if (unreadCount > 0) {
                            "You have $unreadCount new updates"
                        } else {
                            "All notifications are read"
                        },

                    fontSize = 14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Charcoal
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(

                    text =
                        "$totalCount notification" +
                                if (totalCount != 1) {
                                    "s"
                                } else {
                                    ""
                                } +
                                " in total",

                    fontSize = 11.sp,

                    color =
                        Gray
                )
            }

            if (unreadCount > 0) {

                Surface(

                    shape =
                        CircleShape,

                    color =
                        DarkGreen
                ) {

                    Text(

                        text =
                            if (unreadCount > 99) {
                                "99+"
                            } else {
                                unreadCount.toString()
                            },

                        modifier =
                            Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            ),

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            White
                    )
                }
            }
        }
    }
}

// ============================================================
// NOTIFICATION CARD
// ============================================================

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
            White,

        tonalElevation =
            if (isUnread) 2.dp else 0.dp,

        shadowElevation =
            if (isUnread) 2.dp else 0.dp,

        border =
            BorderStroke(

                width =
                    if (isUnread) 1.5.dp
                    else 1.dp,

                color =
                    if (isUnread) {
                        LightGreen.copy(
                            alpha = 0.65f
                        )
                    } else {
                        androidx.compose.ui.graphics.Color(
                            0xFFE8E8E8
                        )
                    }
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.Top
        ) {

            // ================================================
            // ICON
            // ================================================

            Box(

                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(

                            if (isUnread) {
                                DarkGreen
                            } else {
                                LightGreen.copy(
                                    alpha = 0.14f
                                )
                            }
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector =
                        Icons.Default.NotificationsNone,

                    contentDescription =
                        "Notification",

                    modifier =
                        Modifier.size(22.dp),

                    tint =
                        if (isUnread) {
                            White
                        } else {
                            ForestGreen
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.width(13.dp)
            )

            // ================================================
            // CONTENT
            // ================================================

            Column(

                modifier =
                    Modifier.weight(1f)
            ) {

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

                        fontSize = 15.sp,

                        fontWeight =
                            if (isUnread) {
                                FontWeight.ExtraBold
                            } else {
                                FontWeight.SemiBold
                            },

                        color =
                            if (isUnread) {
                                DarkGreen
                            } else {
                                Charcoal
                            },

                        maxLines = 1,

                        overflow =
                            TextOverflow.Ellipsis
                    )

                    if (isUnread) {

                        Spacer(
                            modifier =
                                Modifier.width(7.dp)
                        )

                        Box(

                            modifier =
                                Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        ForestGreen
                                    )
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(

                    text =
                        notification.message,

                    fontSize = 13.sp,

                    lineHeight = 19.sp,

                    color =
                        androidx.compose.ui.graphics.Color(
                            0xFF555555
                        ),

                    maxLines = 3,

                    overflow =
                        TextOverflow.Ellipsis
                )

                if (
                    !notification.createdAt.isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(9.dp)
                    )

                    Row(

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Schedule,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(14.dp),

                            tint =
                                Gray
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(

                            text =
                                formatNotificationDate(
                                    notification.createdAt
                                        ?: ""
                                ),

                            fontSize = 10.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color =
                                Gray
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// LOADING STATE
// ============================================================

@Composable
private fun NotificationLoadingState() {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(76.dp),

            shape =
                CircleShape,

            color =
                LightGreen.copy(
                    alpha = 0.16f
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
                        DarkGreen,

                    strokeWidth =
                        3.dp
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(

            text =
                "Loading notifications...",

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.SemiBold,

            color =
                Charcoal
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(

            text =
                "Please wait a moment",

            fontSize =
                11.sp,

            color =
                Gray
        )
    }
}

// ============================================================
// EMPTY STATE
// ============================================================

@Composable
private fun EmptyNotifications() {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(28.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(88.dp),

            shape =
                CircleShape,

            color =
                LightGreen.copy(
                    alpha = 0.16f
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
                        DarkGreen
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
                Charcoal
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Text(

            text =
                "New booking updates and important\n"
                        + "account notifications will appear here.",

            fontSize =
                12.sp,

            lineHeight =
                18.sp,

            color =
                Gray
        )
    }
}

// ============================================================
// ERROR STATE
// ============================================================

@Composable
private fun NotificationErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(28.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Surface(

            modifier =
                Modifier.size(76.dp),

            shape =
                CircleShape,

            color =
                MaterialTheme.colorScheme.error.copy(
                    alpha = 0.10f
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
                        Modifier.size(34.dp),

                    tint =
                        MaterialTheme.colorScheme.error
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
                FontWeight.ExtraBold,

            color =
                Charcoal
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Text(

            text =
                message,

            modifier =
                Modifier.padding(
                    horizontal = 20.dp
                ),

            fontSize =
                12.sp,

            lineHeight =
                18.sp,

            color =
                Gray
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(

            onClick =
                onRetry,

            shape =
                RoundedCornerShape(12.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        DarkGreen
                )
        ) {

            Icon(

                imageVector =
                    Icons.Default.Refresh,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(17.dp)
            )

            Spacer(
                modifier =
                    Modifier.width(7.dp)
            )

            Text(
                text = "Try Again",
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

// ============================================================
// DATE FORMATTER
// ============================================================

private fun formatNotificationDate(
    value: String
): String {

    return try {

        val normalized =
            value.substringBefore(".")

        val inputFormat =
            if (normalized.contains("T")) {
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    Locale.getDefault()
                )
            } else {
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                )
            }

        val date =
            inputFormat.parse(normalized)

        if (date != null) {

            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(date)

        } else {

            value
        }

    } catch (_: Exception) {

        value
    }
}
