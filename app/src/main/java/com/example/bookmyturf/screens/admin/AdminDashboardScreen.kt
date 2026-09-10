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
import androidx.compose.ui.graphics.Color
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
    // SYSTEM BACK BUTTON
    // =========================================================

    val context = LocalContext.current

    BackHandler {

        if (selectedTab != 0) {

            // -------------------------------------------------
            // Turfs / Bookings → Dashboard
            // -------------------------------------------------

            selectedTab = 0

        } else {

            // -------------------------------------------------
            // Dashboard → Close App
            // -------------------------------------------------

            (context as? Activity)?.finish()
        }
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
    // LOGOUT DIALOG
    // =========================================================

    var showLogoutDialog by remember {

        mutableStateOf(false)
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

                // -------------------------------------------------
                // Refresh dashboard
                // -------------------------------------------------

                viewModel.loadDashboard(
                    token
                )

                // -------------------------------------------------
                // Refresh turfs
                // -------------------------------------------------

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

                // -------------------------------------------------
                // Paid Plans will be connected next
                // -------------------------------------------------

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

        containerColor = AdminOffWhite,


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
                        //
                        // Booking screen has its own ViewModel,
                        // so refresh is handled there.
                        //
                        // =========================================

                        2 -> {

                            // No AdminViewModel booking refresh here.
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

                selectedTab = selectedTab,

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

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues
                        ),

                    admin = admin,

                    subscription = subscription,

                    statistics = statistics,

                    isLoading = isLoading,

                    error = error,

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

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues
                        ),

                    turfs =
                        turfsResponse
                            ?.data
                            ?.turfs
                            ?: emptyList(),

                    isLoading = isLoading,

                    error = error,


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

                        showSlotScreen = true
                    },


                    // =========================================
                    // DELETE TURF
                    // =========================================

                    onDeleteTurf = { turf ->

                        viewModel.deleteTurf(

                            token = token,

                            turfId = turf.id,

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

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues
                        ),

                    token = token
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


            // =================================================
            // TITLE
            // =================================================

            title = {

                Text(
                    text = "Logout"
                )
            },


            // =================================================
            // MESSAGE
            // =================================================

            text = {

                Text(
                    text =
                        "Are you sure you want to logout?"
                )
            },


            // =================================================
            // CONFIRM
            // =================================================

            confirmButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false

                        onLogout()
                    }
                ) {

                    Text(
                        text = "Logout",
                        color = AdminDarkGreen
                    )
                }
            },


            // =================================================
            // CANCEL
            // =================================================

            dismissButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false
                    }
                ) {

                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }
}