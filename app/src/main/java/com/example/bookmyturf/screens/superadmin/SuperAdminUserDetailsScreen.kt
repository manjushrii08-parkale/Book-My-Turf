package com.example.bookmyturf.screens.superadmin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.data.model.SuperAdminUser

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val ScreenBackground = Color(0xFFF5F8F5)
private val CardWhite = Color.White
private val TextDark = Color(0xFF26332A)
private val TextGray = Color(0xFF78847B)
private val BorderGreen = Color(0xFFD9E6DA)

private val ActiveBackground = Color(0xFFE7F4E9)
private val ActiveText = Color(0xFF2E7D32)
private val DangerRed = Color(0xFFC62828)
private val DangerBackground = Color(0xFFFFEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminUserDetailsScreen(
    user: SuperAdminUser,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Details",
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
        },
        containerColor = ScreenBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            UserSummaryCard(user = user)

            DetailsSectionCard(
                title = "Personal Details",
                icon = Icons.Default.Person
            ) {
                DetailItem(
                    icon = Icons.Default.Person,
                    label = "Full Name",
                    value = user.name
                )
            }

            DetailsSectionCard(
                title = "Contact Details",
                icon = Icons.Default.Email
            ) {
                DetailItem(
                    icon = Icons.Default.Email,
                    label = "Email Address",
                    value = user.email
                )

                SectionDivider()

                DetailItem(
                    icon = Icons.Default.Phone,
                    label = "Phone Number",
                    value = user.phone ?: "Not added"
                )
            }

            DetailsSectionCard(
                title = "Account Details",
                icon = Icons.Default.CheckCircle
            ) {
                DetailItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Date of Birth",
                    value = user.date_of_birth ?: "Not added"
                )

                SectionDivider()

                DetailItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Joined Date",
                    value = user.created_at.substringBefore("T")
                )

                SectionDivider()

                DetailItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Account Status",
                    value = user.status,
                    valueColor = if (
                        user.status.equals(
                            other = "ACTIVE",
                            ignoreCase = true
                        )
                    ) {
                        ActiveText
                    } else {
                        DangerRed
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@Composable
private fun UserSummaryCard(
    user: SuperAdminUser
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) {
                            ActiveBackground
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
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = user.name,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "User ID: ${user.id}",
                    color = TextGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                StatusBadge(
                    status = user.status
                )
            }
        }
    }
}

@Composable
private fun DetailsSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(Color(0xFFEAF4EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = title,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            content()
        }
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = TextDark
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = label,
                color = TextGray,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BorderGreen)
    )
}

@Composable
private fun StatusBadge(
    status: String
) {
    val isActive = status.equals(
        other = "ACTIVE",
        ignoreCase = true
    )

    Surface(
        color = if (isActive) {
            ActiveBackground
        } else {
            DangerBackground
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) {
                            ActiveText
                        } else {
                            DangerRed
                        }
                    )
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = status,
                color = if (isActive) {
                    ActiveText
                } else {
                    DangerRed
                },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}