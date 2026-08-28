
package com.example.bookmyturf.screens.admin

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
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AdminRepository
import com.example.bookmyturf.screens.admin.components.AdminBottomBar
import com.example.bookmyturf.screens.admin.components.AdminTopBar
import com.example.bookmyturf.viewmodel.AdminViewModel

private val AdminBackground = Color(0xFFF8FAFC)
private val AdminGreen = Color(0xFF14532D)

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
    // ADD TURF
    // =========================================================

    var showAddTurfScreen by remember {

        mutableStateOf(false)
    }


    // =========================================================
    // EDIT TURF
    // =========================================================

    var editTurfId by remember {

        mutableStateOf<Int?>(null)
    }


    // =========================================================
    // MANAGE SLOTS
    // =========================================================

    var selectedTurfId by remember {

        mutableStateOf<Int?>(null)
    }


    var showSlotScreen by remember {

        mutableStateOf(false)
    }


    // =========================================================
    // LOGOUT
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

                // Refresh dashboard
                viewModel.loadDashboard(
                    token
                )

                // Refresh turfs
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
    // MAIN ADMIN SCREEN
    // =========================================================

    Scaffold(

        containerColor = AdminBackground,


        // =====================================================
        // TOP BAR
        // =====================================================

        topBar = {

            AdminTopBar(

                onRefresh = {

                    when (selectedTab) {

                        // =====================================
                        // DASHBOARD
                        // =====================================

                        0 -> {

                            viewModel.loadDashboard(
                                token
                            )
                        }


                        // =====================================
                        // TURFS
                        // =====================================

                        1 -> {

                            viewModel.loadTurfs(
                                token
                            )
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

                    onManageTurfs = {

                        selectedTab = 1
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
                        )
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
                        color = AdminGreen
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

