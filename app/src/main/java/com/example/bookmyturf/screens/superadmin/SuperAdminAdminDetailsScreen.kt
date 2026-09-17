package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.data.model.SuperAdminAdmin
import java.util.Locale

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)
private val ScreenBackground = Color(0xFFF7FAF7)
private val CardWhite = Color.White
private val TextGray = Color(0xFF78847B)

private val ActiveBackground = Color(0xFFE8F5E9)
private val ActiveText = Color(0xFF2E7D32)

private val BlockedBackground = Color(0xFFFFEBEE)
private val BlockedText = Color(0xFFB3261E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminAdminDetailsScreen(
    admin: SuperAdminAdmin,
    onBackClick: () -> Unit,
    onBlockClick: () -> Unit,
    onActivateClick: () -> Unit
) {
    val isActive = admin.status
        .orEmpty()
        .equals("ACTIVE", ignoreCase = true)

    BackHandler {
        onBackClick()
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Details",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkGreen,
                    navigationIconContentColor = DarkGreen
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = LightGreen.copy(alpha = 0.22f),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = admin.name
                                    .orEmpty()
                                    .ifBlank { "A" }
                                    .take(1)
                                    .uppercase(),
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = admin.name
                                    .orEmpty()
                                    .ifBlank { "Admin name not available" },
                                color = DarkGreen,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Admin ID: ${admin.id}",
                                color = ForestGreen,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    StatusBadge(
                        status = admin.status
                            .orEmpty()
                            .ifBlank { "UNKNOWN" }
                    )
                }
            }

            DetailSectionCard(
                title = "Admin Information",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Admin information",
                        tint = ForestGreen
                    )
                }
            ) {
                DetailRow(
                    label = "Name",
                    value = admin.name
                        .orEmpty()
                        .ifBlank { "Admin name not available" }
                )

                DetailRow(
                    label = "Email",
                    value = admin.email
                        .orEmpty()
                        .ifBlank { "Email not available" }
                )

                DetailRow(
                    label = "Phone",
                    value = admin.phone
                        .orEmpty()
                        .ifBlank { "Phone number not available" }
                )

                DetailRow(
                    label = "Created At",
                    value = admin.created_at
                        .orEmpty()
                        .ifBlank { "Created date not available" }
                )
            }

            if (isActive) {
                Button(
                    onClick = onBlockClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlockedText
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = "Block admin"
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Block Admin",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = onActivateClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Activate admin"
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Activate Admin",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = ForestGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                Text(
                    text = title,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ScreenBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = TextGray,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StatusBadge(
    status: String
) {
    val normalizedStatus = status.uppercase(Locale.getDefault())

    val backgroundColor = if (normalizedStatus == "ACTIVE") {
        ActiveBackground
    } else {
        BlockedBackground
    }

    val textColor = if (normalizedStatus == "ACTIVE") {
        ActiveText
    } else {
        BlockedText
    }

    Row(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 7.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (normalizedStatus == "ACTIVE") {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Block
            },
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = normalizedStatus,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}