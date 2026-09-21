package com.example.bookmyturf.screens.superadmin

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.data.model.SuperAdminUser
import com.example.bookmyturf.viewmodel.SuperAdminUsersViewModel

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val ScreenBackground = Color(0xFFF5F8F5)
private val CardWhite = Color.White
private val TextDark = Color(0xFF26332A)
private val TextGray = Color(0xFF78847B)
private val BorderGreen = Color(0xFFD9E6DA)

private val DangerRed = Color(0xFFC62828)
private val DangerBackground = Color(0xFFFFEEEE)

private val ActiveBackground = Color(0xFFE7F4E9)
private val ActiveText = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminUsersScreen(
    onBackClick: () -> Unit,
    onUserClick: (Int) -> Unit,
    onDashboardClick: () -> Unit,
    onAdminsClick: () -> Unit,
    onReportsClick: () -> Unit
) {
    val viewModel: SuperAdminUsersViewModel = viewModel()

    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    BackHandler {
        onBackClick()
    }

    val filteredUsers = users.filter { user ->
        user.name.contains(
            other = searchQuery,
            ignoreCase = true
        ) ||
                user.email.contains(
                    other = searchQuery,
                    ignoreCase = true
                ) ||
                user.phone.orEmpty().contains(
                    other = searchQuery,
                    ignoreCase = true
                ) ||
                user.id.toString().contains(
                    other = searchQuery,
                    ignoreCase = true
                )
    }

    Scaffold(
        topBar = {
            SuperAdminTopBar(
                onBackClick = onBackClick,
                onRefreshClick = {
                    viewModel.loadUsers()
                }
            )
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 5.dp
            ) {

                // Dashboard
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onDashboardClick()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = {
                        Text(
                            text = "Dashboard"
                        )
                    }
                )

                // Users
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        // Already on Users screen
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Users"
                        )
                    },
                    label = {
                        Text(
                            text = "Users"
                        )
                    }
                )

                // Admins
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onAdminsClick()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admins"
                        )
                    },
                    label = {
                        Text(
                            text = "Admins"
                        )
                    }
                )

                // Reports
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onReportsClick()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Reports"
                        )
                    },
                    label = {
                        Text(
                            text = "Reports"
                        )
                    }
                )
            }
        },

        containerColor = ScreenBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search users",
                        tint = ForestGreen
                    )
                },
                placeholder = {
                    Text(
                        text = "Search by name, email, ID",
                        color = TextGray
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ForestGreen,
                    unfocusedBorderColor = BorderGreen,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = ForestGreen,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                )
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            when {

                isLoading -> {
                    LoadingState()
                }

                error != null -> {
                    ErrorState(
                        message = error ?: "Something went wrong.",
                        onRetry = {
                            viewModel.loadUsers()
                        }
                    )
                }

                filteredUsers.isEmpty() -> {
                    EmptyUsersState()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 4.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        items(
                            items = filteredUsers,
                            key = { user ->
                                user.id
                            }
                        ) { user ->

                            UserCard(
                                user = user,

                                onClick = {
                                    onUserClick(user.id)
                                },

                                onBlockClick = {
                                    viewModel.blockUser(user.id)
                                },

                                onActivateClick = {
                                    viewModel.activateUser(user.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuperAdminTopBar(
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Users Management",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },

        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreen
                )
            }
        },

        actions = {
            IconButton(
                onClick = onRefreshClick
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh users",
                    tint = ForestGreen
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = DarkGreen,
            navigationIconContentColor = DarkGreen,
            actionIconContentColor = ForestGreen
        )
    )
}

@Composable
private fun UserCard(
    user: SuperAdminUser,
    onClick: () -> Unit,
    onBlockClick: () -> Unit,
    onActivateClick: () -> Unit
) {
    val isActive = user.status.equals(
        other = "ACTIVE",
        ignoreCase = true
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                Color(0xFFE3F1E5)
                            } else {
                                DangerBackground
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = if (isActive) {
                            ForestGreen
                        } else {
                            DangerRed
                        },
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = user.name,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "User ID: ${user.id}",
                        color = TextGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                StatusBadge(
                    status = user.status
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8FAF8),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = BorderGreen
                )
            ) {

                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    UserInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user.email
                    )

                    UserInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = user.phone ?: "Not added"
                    )

                    UserInfoRow(
                        icon = Icons.Default.Person,
                        label = "Date of Birth",
                        value = user.date_of_birth ?: "Not added"
                    )

                    UserInfoRow(
                        icon = Icons.Default.CheckCircle,
                        label = "Joined",
                        value = user.created_at.substringBefore("T")
                    )
                }
            }

            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = ForestGreen
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ForestGreen
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "View User Details",
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (isActive) {

                Button(
                    onClick = onBlockClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DangerRed,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Block User",
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {

                Button(
                    onClick = onActivateClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreen,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Activate User",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun UserInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .size(19.dp)
                .padding(top = 1.dp)
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = label,
                color = TextGray,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                color = TextDark,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StatusBadge(
    status: String
) {
    val isActive = status.equals(
        other = "ACTIVE",
        ignoreCase = true
    )

    val badgeColor = if (isActive) {
        ActiveBackground
    } else {
        DangerBackground
    }

    val textColor = if (isActive) {
        ActiveText
    } else {
        DangerRed
    }

    Surface(
        color = badgeColor,
        shape = RoundedCornerShape(50.dp)
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = status,
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = ForestGreen
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreen
                ),
                shape = RoundedCornerShape(14.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Retry",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EmptyUsersState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = "No users found.",
                color = TextGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}