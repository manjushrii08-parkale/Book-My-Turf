package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminPaidPlansScreen(
    token: String,
    onPlanSelected: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Choose Your Plan",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Choose a plan to continue using your turf management features."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // =====================================================
        // MONTHLY PLAN
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Monthly Plan",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "₹499 / month"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "✓ Manage your turfs"
                )

                Text(
                    text = "✓ Manage slots"
                )

                Text(
                    text = "✓ View bookings"
                )

                Text(
                    text = "✓ Admin dashboard"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {
                        // Payment will be connected later
                        onPlanSelected()
                    }
                ) {

                    Text(
                        text = "Select Monthly Plan"
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =====================================================
        // YEARLY PLAN
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Yearly Plan",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "₹4,999 / year"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "✓ Manage your turfs"
                )

                Text(
                    text = "✓ Manage slots"
                )

                Text(
                    text = "✓ View bookings"
                )

                Text(
                    text = "✓ Admin dashboard"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {
                        // Payment will be connected later
                        onPlanSelected()
                    }
                ) {

                    Text(
                        text = "Select Yearly Plan"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}

