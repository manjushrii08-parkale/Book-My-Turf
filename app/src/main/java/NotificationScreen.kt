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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.notification.NotificationItem
import com.example.bookmyturf.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// ============================================================
// PREMIUM DARK THEME
// ============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)
private val ErrorRed = Color(0xFFFF6B6B)

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

            viewModel.loadNotifications(token)
        }
    }

    // ========================================================
    // SCREEN
    // ========================================================

    Scaffold(

        containerColor = Background,

        topBar = {

            Column {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Notifications",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryText
                            )

                            Text(
                                text =
                                    if (unreadCount > 0) {
                                        "$unreadCount unread notification" +
                                                if (unreadCount > 1) "s"
                                                else ""
                                    } else {
                                        "You're all caught up"
                                    },
                                fontSize = 11.sp,
                                color = SecondaryText
                            )
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

                                modifier =
                                    Modifier.size(21.dp),

                                tint =
                                    PrimaryText
                            )
                        }
                    },

                    actions = {

                        if (unreadCount > 0) {

                            Surface(

                                modifier =
                                    Modifier
                                        .padding(end = 14.dp)
                                        .clip(
                                            RoundedCornerShape(11.dp)
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
                                    RoundedCornerShape(11.dp),

                                color =
                                    SurfaceHighlight
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
                                            Modifier.size(16.dp),

                                        tint =
                                            LightGreen
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(5.dp)
                                    )

                                    Text(

                                        text = "Read all",

                                        fontSize = 10.sp,

                                        fontWeight =
                                            FontWeight.Bold,

                                        color =
                                            PrimaryText
                                    )
                                }
                            }
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor =
                                Background,

                            titleContentColor =
                                PrimaryText,

                            navigationIconContentColor =
                                PrimaryText
                        )
                )

                HorizontalDivider(
                    color = Border,
                    thickness = 1.dp
                )
            }
        }

    ) { paddingValues ->

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {

                // =================================================
                // LOADING
                // =================================================

                isLoading -> {

                    NotificationLoadingState()
                }

                // =================================================
                // ERROR
                // =================================================

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

                // =================================================
                // EMPTY
                // =================================================

                notifications.isEmpty() -> {

                    EmptyNotifications()
                }

                // =================================================
                // LIST
                // =================================================

                else -> {

                    LazyColumn(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                top = 20.dp,
                                bottom = 30.dp
                            ),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        // =========================================
                        // SUMMARY
                        // =========================================

                        item {

                            NotificationSummary(
                                totalCount =
                                    notifications.size,

                                unreadCount =
                                    unreadCount
                            )
                        }

                        // =========================================
                        // NOTIFICATIONS
                        // =========================================

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
// SUMMARY
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
            SurfaceElevated
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
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            PrimaryGreen.copy(
                                alpha = 0.13f
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
                        LightGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(13.dp)
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

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        PrimaryText
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
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

                    fontSize =
                        11.sp,

                    color =
                        SecondaryText
                )
            }

            if (unreadCount > 0) {

                Surface(

                    shape =
                        CircleShape,

                    color =
                        PrimaryGreen
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

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Background
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
            if (isUnread) {
                SurfaceHighlight
            } else {
                SurfaceElevated
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
            // ICON
            // =================================================

            Box(

                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(

                            if (isUnread) {
                                PrimaryGreen.copy(
                                    alpha = 0.16f
                                )
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
                        "Notification",

                    modifier =
                        Modifier.size(21.dp),

                    tint =
                        if (isUnread) {
                            LightGreen
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
                            14.sp,

                        fontWeight =
                            if (isUnread) {
                                FontWeight.Bold
                            } else {
                                FontWeight.SemiBold
                            },

                        color =
                            PrimaryText,

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
                                    .size(7.dp)
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

                Text(

                    text =
                        notification.message,

                    fontSize =
                        12.sp,

                    lineHeight =
                        18.sp,

                    color =
                        SecondaryText,

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
                                Modifier.size(13.dp),

                            tint =
                                MutedText
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

                            fontSize =
                                10.sp,

                            fontWeight =
                                FontWeight.Medium,

                            color =
                                MutedText
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
            Modifier
                .fillMaxSize()
                .padding(28.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Box(

            modifier =
                Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryGreen.copy(
                            alpha = 0.12f
                        )
                    ),

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

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Text(

            text =
                "Loading notifications...",

            fontSize =
                14.sp,

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
                "Please wait a moment",

            fontSize =
                11.sp,

            color =
                SecondaryText
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
                .padding(28.dp)
                .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Box(

            modifier =
                Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryGreen.copy(
                            alpha = 0.12f
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
                    Modifier.size(40.dp),

                tint =
                    LightGreen
            )
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
                "New booking updates and important\n" +
                        "account notifications will appear here.",

            fontSize =
                12.sp,

            lineHeight =
                18.sp,

            color =
                SecondaryText
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
                .padding(28.dp)
                .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Box(

            modifier =
                Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        ErrorRed.copy(
                            alpha = 0.10f
                        )
                    ),

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
                    ErrorRed
            )
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
                PrimaryText
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
                SecondaryText
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
                RoundedCornerShape(13.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        PrimaryGreen,

                    contentColor =
                        Background
                ),

            elevation =
                ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
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

                text =
                    "Try Again",

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
