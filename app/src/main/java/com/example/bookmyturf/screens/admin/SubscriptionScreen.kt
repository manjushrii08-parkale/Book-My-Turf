package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookmyturf.viewmodel.AdminViewModel

@Composable
fun AdminSubscriptionScreen(
    token: String,
    viewModel: AdminViewModel,
    onSubscriptionActive: () -> Unit,
    onPaidPlanClick: () -> Unit
) {

    // =========================================================
    // STATE
    // =========================================================

    val isLoading by viewModel.isLoading.collectAsState()

    val error by viewModel.error.collectAsState()

    val subscriptionResponse by
    viewModel.subscription.collectAsState()


    // =========================================================
    // LOAD SUBSCRIPTION
    // =========================================================

    LaunchedEffect(token) {

        viewModel.loadSubscriptionStatus(token)
    }


    // =========================================================
    // RESPONSE DATA
    // =========================================================

    val data =
        subscriptionResponse?.data

    val subscription =
        data?.subscription

    val trialUsed =
        data?.trialUsed ?: false


    // =========================================================
    // AUTO NAVIGATE WHEN SUBSCRIPTION IS ACTIVE
    // =========================================================

    LaunchedEffect(subscription?.status) {

        if (subscription?.status == "ACTIVE") {

            onSubscriptionActive()
        }
    }


    // =========================================================
    // UI
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Subscription",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            CircularProgressIndicator()

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Checking subscription..."
            )

            return@Column
        }


        // =====================================================
        // ERROR
        // =====================================================

        if (error != null) {

            Text(
                text = error ?: "Something went wrong"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = {

                    viewModel.clearError()

                    viewModel.loadSubscriptionStatus(token)
                }
            ) {

                Text(
                    text = "Retry"
                )
            }

            return@Column
        }


        // =====================================================
        // FIRST TIME / NO SUBSCRIPTION
        // SHOW FREE TRIAL + PAID PLAN
        // =====================================================

        if (subscription == null && !trialUsed) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Choose Your Plan",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Choose the plan that works best for your turf business."
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )


                    // =================================================
                    // FREE TRIAL
                    // =================================================

                    Text(
                        text = "15-Day Free Trial",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Try all admin features free for 15 days."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),

                        onClick = {

                            viewModel.startFreeTrial(
                                token = token,

                                onSuccess = {
                                    onSubscriptionActive()
                                }
                            )
                        }
                    ) {

                        Text(
                            text = "Start Free Trial"
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )


                    // =================================================
                    // PAID PLAN
                    // =================================================

                    Text(
                        text = "Paid Subscription",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Get uninterrupted access to all admin features."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),

                        onClick = {

                            onPaidPlanClick()
                        }
                    ) {

                        Text(
                            text = "Choose Paid Plan"
                        )
                    }
                }
            }

            return@Column
        }


        // =====================================================
        // TRIAL USED OR EXPIRED
        // SHOW ONLY PAID PLAN
        // =====================================================

        if (trialUsed) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Choose a Paid Plan",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (subscription?.status == "EXPIRED") {

                        Text(
                            text = "Your free trial has expired."
                        )

                    } else {

                        Text(
                            text = "Your free trial has already been used."
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Please choose a paid subscription to continue using your admin dashboard."
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),

                        onClick = {

                            onPaidPlanClick()
                        }
                    ) {

                        Text(
                            text = "View Paid Plans"
                        )
                    }
                }
            }

            return@Column
        }


        // =====================================================
        // FALLBACK
        // =====================================================

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Subscription Information",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Please choose a subscription plan to continue."
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {

                        onPaidPlanClick()
                    }
                ) {

                    Text(
                        text = "View Paid Plans"
                    )
                }
            }
        }
    }
}