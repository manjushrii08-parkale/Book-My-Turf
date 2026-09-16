package com.example.bookmyturf.screens.superadmin

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)
private val ScreenBackground = Color(0xFFF7FAF7)

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

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
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
                                    color = LightGreen.copy(alpha = 0.25f),
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
                                fontWeight = FontWeight.Bold
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
                                    .ifBlank { "Admin" },
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Admin ID: ${admin.id}",
                                color = Color.Gray
                            )
                        }
                    }

                    DetailRow(
                        label = "Email",
                        value = admin.email
                            .orEmpty()
                            .ifBlank { "Not available" }
                    )

                    DetailRow(
                        label = "Phone",
                        value = admin.phone
                            .orEmpty()
                            .ifBlank { "Not available" }
                    )

                    DetailRow(
                        label = "Status",
                        value = admin.status
                            .orEmpty()
                            .ifBlank { "UNKNOWN" }
                    )

                    DetailRow(
                        label = "Created At",
                        value = admin.created_at
                            .orEmpty()
                            .ifBlank { "Not available" }
                    )
                }
            }

            if (isActive) {
                Button(
                    onClick = onBlockClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB3261E)
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
                        text = "Block Admin"
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
                        text = "Activate Admin"
                    )
                }
            }
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
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}