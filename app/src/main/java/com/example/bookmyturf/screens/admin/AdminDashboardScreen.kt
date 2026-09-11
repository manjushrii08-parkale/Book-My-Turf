package com.example.bookmyturf.screens.admin

import android.app.Activity

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository

import com.example.bookmyturf.screens.admin.components.AdminBottomBar
import com.example.bookmyturf.screens.admin.components.AdminTopBar

import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite

import com.example.bookmyturf.viewmodel.AdminViewModel


// =============================================================
// ADMIN HOME SCREEN
// =============================================================

@Composable
fun AdminHomeScreen(
    token: String,
    onLogout: () -> Unit
) {

    // =========================================================
    // REPOSITORY
    // =========================================================

    val repository = remember {

        AdminRepository(
            RetrofitClient.api
        )
    }


    // =========================================================
    // VIEWMODEL FACTORY
    // =========================================================

    val factory = remember {

        AdminViewModelFactory(
            repository
        )
    }


    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: AdminViewModel = viewModel(
        factory = factory
    )


    // =========================================================
    // STATE
    // =========================================================

    val dashboard by
    viewModel.dashboard.collectAsState()

    val turfsResponse by
    viewModel.turfs.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val error by
    viewModel.error.collectAsState()


    // =========================================================
    // SELECTED TAB
    // =========================================================

    var selectedTab by remember {
        mutableIntStateOf(0)
    }


    // =========================================================
    // ADD TURF SCREEN
    // =========================================================

    var showAddTurfScreen by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // EDIT TURF SCREEN
    // =========================================================

    var editTurfId by remember {
        mutableStateOf<Int?>(null)
    }


    // =========================================================
    // MANAGE SLOT SCREEN
    // =========================================================

    var selectedTurfId by remember {
        mutableStateOf<Int?>(null)
    }

    var showSlotScreen by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // SUBSCRIPTION SCREEN
    // =========================================================

    var showSubscriptionScreen by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // PAID PLANS SCREEN
    // =========================================================

    var showPaidPlansScreen by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // LOGOUT DIALOG
    // =========================================================

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // CONTEXT
    // =========================================================

    val context =
        LocalContext.current


    // =========================================================
    // SYSTEM BACK BUTTON
    // =========================================================

    BackHandler {

        when {

            // PRO Plans → Subscription
            showPaidPlansScreen -> {

                showPaidPlansScreen = false
                showSubscriptionScreen = true
            }

            // Subscription → Dashboard
            showSubscriptionScreen -> {

                showSubscriptionScreen = false
            }

            // Edit Turf → Dashboard
            editTurfId != null -> {

                editTurfId = null
            }

            // Add Turf → Dashboard
            showAddTurfScreen -> {

                showAddTurfScreen = false
            }

            // Manage Slots → Turfs
            showSlotScreen -> {

                showSlotScreen = false
                selectedTurfId = null
            }

            // Other tabs → Dashboard
            selectedTab != 0 -> {

                selectedTab = 0
            }

            // Dashboard → Close app
            else -> {

                (context as? Activity)?.finish()
            }
        }
    }


    // =========================================================
    // LOAD DASHBOARD
    // =========================================================

    LaunchedEffect(token) {

        viewModel.loadDashboard(
            token
        )
    }


    // =========================================================
    // LOAD TURFS
    // =========================================================

    LaunchedEffect(
        token,
        selectedTab
    ) {

        if (selectedTab == 1) {

            viewModel.loadTurfs(
                token
            )
        }
    }


    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    val admin =
        dashboard
            ?.data
            ?.admin

    val subscription =
        dashboard
            ?.data
            ?.subscription

    val statistics =
        dashboard
            ?.data
            ?.statistics


    // =========================================================
    // EDIT TURF SCREEN
    // =========================================================

    if (editTurfId != null) {

        EditTurfScreen(

            token = token,

            turfId = editTurfId!!,

            onBack = {

                editTurfId = null
            },

            onSuccess = {

                editTurfId = null

                viewModel.loadTurfs(
                    token
                )

                viewModel.loadDashboard(
                    token
                )
            }
        )

        return
    }


    // =========================================================
    // ADD TURF SCREEN
    // =========================================================

    if (showAddTurfScreen) {

        AddTurfScreen(

            token = token,

            onBack = {

                showAddTurfScreen = false
            },

            onSuccess = {

                showAddTurfScreen = false

                viewModel.loadDashboard(
                    token
                )

                viewModel.loadTurfs(
                    token
                )
            }
        )

        return
    }


    // =========================================================
    // MANAGE SLOT SCREEN
    // =========================================================

    if (
        showSlotScreen &&
        selectedTurfId != null
    ) {

        AdminSlotScreen(

            token = token,

            turfId = selectedTurfId!!,

            onBack = {

                showSlotScreen = false
                selectedTurfId = null
            }
        )

        return
    }


    // =========================================================
    // PAID PLANS SCREEN
    // =========================================================

    if (showPaidPlansScreen) {

        AdminPaidPlansScreen(

            token = token,

            viewModel = viewModel,

            // =================================================
            // PAYMENT SUCCESS
            // =================================================

            onPlanActivated = {

                showPaidPlansScreen = false
                showSubscriptionScreen = false

                // Refresh dashboard
                viewModel.loadDashboard(
                    token
                )

                // Refresh subscription
                viewModel.loadSubscriptionStatus(
                    token
                )
            },

            // =================================================
            // BACK → SUBSCRIPTION
            // =================================================

            onBackClick = {

                showPaidPlansScreen = false
                showSubscriptionScreen = true
            }
        )

        return
    }


    // =========================================================
    // SUBSCRIPTION SCREEN
    // =========================================================

    if (showSubscriptionScreen) {

        AdminSubscriptionScreen(

            token = token,

            viewModel = viewModel,

            onSubscriptionActive = {

                showSubscriptionScreen = false

                viewModel.loadDashboard(
                    token
                )
            },

            onPaidPlanClick = {

                showSubscriptionScreen = false
                showPaidPlansScreen = true
            },

            onBackClick = {

                showSubscriptionScreen = false
            }
        )

        return
    }


    // =========================================================
    // MAIN ADMIN SCREEN
    // =========================================================

    Scaffold(

        containerColor =
            AdminOffWhite,

        // =====================================================
        // TOP BAR
        // =====================================================

        topBar = {

            AdminTopBar(

                onRefresh = {

                    when (selectedTab) {

                        // =========================================
                        // DASHBOARD
                        // =========================================

                        0 -> {

                            viewModel.loadDashboard(
                                token
                            )
                        }


                        // =========================================
                        // TURFS
                        // =========================================

                        1 -> {

                            viewModel.loadTurfs(
                                token
                            )
                        }


                        // =========================================
                        // BOOKINGS
                        // =========================================

                        2 -> {

                            // Booking screen has its own ViewModel.
                        }
                    }
                },

                onLogout = {

                    showLogoutDialog = true
                }
            )
        },


        // =====================================================
        // BOTTOM BAR
        // =====================================================

        bottomBar = {

            AdminBottomBar(

                selectedTab =
                    selectedTab,

                onTabSelected = { tab ->

                    selectedTab = tab
                }
            )
        }

    ) { paddingValues ->


        // =====================================================
        // TAB CONTENT
        // =====================================================

        when (selectedTab) {

            // =================================================
            // TAB 0 — DASHBOARD
            // =================================================

            0 -> {

                AdminDashboardContent(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),

                    admin =
                        admin,

                    subscription =
                        subscription,

                    statistics =
                        statistics,

                    isLoading =
                        isLoading,

                    error =
                        error,

                    onRetry = {

                        viewModel.clearError()

                        viewModel.loadDashboard(
                            token
                        )
                    },

                    // =========================================
                    // MANAGE TURFS
                    // =========================================

                    onManageTurfs = {

                        selectedTab = 1
                    },

                    // =========================================
                    // BOOKINGS
                    // =========================================

                    onBookingsClick = {

                        selectedTab = 2
                    },

                    // =========================================
                    // SUBSCRIPTION
                    // =========================================

                    onSubscriptionClick = {

                        showSubscriptionScreen = true
                    },

                    // =========================================
                    // ADD TURF
                    // =========================================

                    onAddTurfClick = {

                        showAddTurfScreen = true
                    }
                )
            }


            // =================================================
            // TAB 1 — TURFS
            // =================================================

            1 -> {

                AdminTurfsContent(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),

                    turfs =
                        turfsResponse
                            ?.data
                            ?.turfs
                            ?: emptyList(),

                    isLoading =
                        isLoading,

                    error =
                        error,

                    // =========================================
                    // ADD TURF
                    // =========================================

                    onAddTurf = {

                        showAddTurfScreen = true
                    },

                    // =========================================
                    // EDIT TURF
                    // =========================================

                    onEditTurf = { turf ->

                        editTurfId =
                            turf.id
                    },

                    // =========================================
                    // MANAGE SLOTS
                    // =========================================

                    onManageSlots = { turf ->

                        selectedTurfId =
                            turf.id

                        showSlotScreen =
                            true
                    },

                    // =========================================
                    // DELETE TURF
                    // =========================================

                    onDeleteTurf = { turf ->

                        viewModel.deleteTurf(

                            token =
                                token,

                            turfId =
                                turf.id,

                            onSuccess = {

                                viewModel.loadTurfs(
                                    token
                                )

                                viewModel.loadDashboard(
                                    token
                                )
                            }
                        )
                    },

                    // =========================================
                    // RETRY
                    // =========================================

                    onRetry = {

                        viewModel.clearError()

                        viewModel.loadTurfs(
                            token
                        )
                    }
                )
            }


            // =================================================
            // TAB 2 — BOOKINGS
            // =================================================

            2 -> {

                AdminBookingsContent(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),

                    token =
                        token
                )
            }
        }
    }


    // =========================================================
    // LOGOUT DIALOG
    // =========================================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {

                showLogoutDialog = false
            },

            title = {

                Text(
                    text =
                        "Logout"
                )
            },

            text = {

                Text(
                    text =
                        "Are you sure you want to logout?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false

                        onLogout()
                    }
                ) {

                    Text(
                        text =
                            "Logout",

                        color =
                            AdminDarkGreen
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false
                    }
                ) {

                    Text(
                        text =
                            "Cancel"
                    )
                }
            }
        )
    }
}
