package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import androidx.compose.foundation.layout.width
@Composable
fun AdminTurfCard(
    turf: Turf,
    onEdit: () -> Unit,
    onManageSlots: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // -------------------------------------------------
                // TURF ICON
                // -------------------------------------------------

                BoxIcon(
                    icon = Icons.Default.SportsSoccer
                )

                // -------------------------------------------------
                // TURF NAME + CITY
                // -------------------------------------------------

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {

                    Text(
                        text = turf.name,
                        color = AdminDarkCharcoal,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = turf.city,
                        color = AdminGray,
                        fontSize = 13.sp
                    )
                }

                // -------------------------------------------------
                // STATUS
                // -------------------------------------------------

                TurfStatus(
                    status = turf.status
                )
            }


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            // =================================================
            // LOCATION
            // =================================================

            Text(
                text = turf.location,
                color = AdminGray,
                fontSize = 13.sp,
                maxLines = 2
            )


            Spacer(
                modifier = Modifier.height(13.dp)
            )


            // =================================================
            // PRICE + OPERATING HOURS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Price",
                        color = AdminGray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "₹${turf.price}/slot",
                        color = AdminDarkGreen,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Operating hours",
                        color = AdminGray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "${turf.openingTime} - ${turf.closingTime}",
                        color = AdminDarkCharcoal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // DIVIDER
            // =================================================

            androidx.compose.material3.HorizontalDivider(
                color = AdminOffWhite
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // ACTION BUTTONS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // -------------------------------------------------
                // EDIT
                // -------------------------------------------------

                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Turf",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Edit",
                        fontWeight = FontWeight.SemiBold
                    )
                }


                // -------------------------------------------------
                // MANAGE SLOTS
                // -------------------------------------------------

                Button(
                    onClick = onManageSlots,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminLightGreen,
                        contentColor = AdminDarkCharcoal
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Manage Slots",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Slots",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =============================================================
// ICON BOX
// =============================================================

@Composable
private fun BoxIcon(
    icon: ImageVector
) {

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(46.dp)
            .clip(
                RoundedCornerShape(13.dp)
            )
            .background(AdminOffWhite),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AdminDarkGreen,
            modifier = Modifier.size(22.dp)
        )
    }
}


// =============================================================
// STATUS
// =============================================================

@Composable
private fun TurfStatus(
    status: String
) {

    val isActive =
        status.equals(
            "ACTIVE",
            ignoreCase = true
        )

    val background =
        if (isActive) {
            AdminLightGreen.copy(alpha = 0.22f)
        } else {
            AdminOffWhite
        }

    val textColor =
        if (isActive) {
            AdminForestGreen
        } else {
            AdminGray
        }

    Text(
        text = status.uppercase(),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(
                RoundedCornerShape(50)
            )
            .background(background)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    )
}

