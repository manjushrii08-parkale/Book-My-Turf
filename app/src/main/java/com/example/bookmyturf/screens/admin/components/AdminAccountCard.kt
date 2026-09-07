package com.example.bookmyturf.screens.admin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.data.model.AdminDashboardUser
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminWhite

@Composable
fun AdminAccountCard(
    admin: AdminDashboardUser?,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AdminDarkGreen
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Account Information",
                    color = AdminDarkCharcoal,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // =================================================
            // NAME
            // =================================================

            AccountItem(
                icon = Icons.Default.Person,
                label = "Name",
                value = admin?.name ?: "—"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // EMAIL
            // =================================================

            AccountItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = admin?.email ?: "—"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // STATUS
            // =================================================

            AccountItem(
                icon = Icons.Default.Verified,
                label = "Account Status",
                value = admin?.status ?: "—"
            )
        }
    }
}


// =============================================================
// ACCOUNT ITEM
// =============================================================

@Composable
private fun AccountItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.width(22.dp),
            tint = AdminDarkGreen
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                fontSize = 11.sp,
                color = AdminGray
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AdminDarkCharcoal
            )
        }
    }
}
