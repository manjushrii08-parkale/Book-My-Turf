package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.bookmyturf.data.model.turf.Turf

private val TurfGreen = Color(0xFF14532D)
private val TurfGray = Color(0xFF64748B)
private val TurfGreenLight = Color(0xFFF0FDF4)

@Composable
fun AdminTurfCard(
    turf: Turf,
    onEdit: () -> Unit,
    onManageSlots: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = TurfGreen,
                    modifier = Modifier
                        .background(
                            color = TurfGreenLight,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {

                    Text(
                        text = turf.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = turf.city,
                        color = TurfGray,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = turf.status,
                    color = TurfGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // LOCATION
            // =================================================

            Text(
                text = turf.location,
                color = TurfGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================
            // PRICE + TIME
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "₹${turf.price}/slot",
                    fontWeight = FontWeight.Bold,
                    color = TurfGreen
                )

                Text(
                    text = "${turf.openingTime} - ${turf.closingTime}",
                    color = TurfGray,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // ACTION BUTTONS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurfGreen
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Turf"
                    )

                    Text(
                        text = " Edit"
                    )
                }

                Button(
                    onClick = onManageSlots,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurfGreen
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Manage Slots"
                    )

                    Text(
                        text = " Slots"
                    )
                }
            }
        }
    }
}