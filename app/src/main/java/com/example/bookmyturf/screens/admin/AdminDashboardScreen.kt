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
import com.example.bookmyturf.viewmodel.NotificationViewModel


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
    // ADMIN VIEWMODEL
    // =========================================================

    val viewModel: AdminViewModel = viewModel(
        factory = factory
    )


    // =========================================================
    // NOTIFICATION VIEWMODEL
    // =========================================================

    val notificationViewModel: NotificationViewModel =
        viewModel()

    val unreadNotificationCount by
    notificationViewModel.unreadCount.collectAsState()


    // =========================================================
    // ADMIN STATE
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
    // NOTIFICATION SCREEN
    // =========================================================

    var showAdminNotificationScreen by remember {

        mutableStateOf(false)
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
    // ADMIN EDIT PROFILE SCREEN
    // =========================================================

    var showAdminEditProfileScreen by remember {

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

            // -------------------------------------------------
            // NOTIFICATIONS → HOME
            // -------------------------------------------------

            showAdminNotificationScreen -> {

                showAdminNotificationScreen = false

                notificationViewModel.loadUnreadCount(
                    token
                )
            }


            // -------------------------------------------------
            // EDIT PROFILE → PROFILE
            // -------------------------------------------------

            showAdminEditProfileScreen -> {

                showAdminEditProfileScreen = false

                selectedTab = 3
            }


            // -------------------------------------------------
            // PRO PLANS → SUBSCRIPTION
            // -------------------------------------------------

            showPaidPlansScreen -> {

                showPaidPlansScreen = false

                showSubscriptionScreen = true
            }


            // -------------------------------------------------
            // SUBSCRIPTION → DASHBOARD
            // -------------------------------------------------

            showSubscriptionScreen -> {

                showSubscriptionScreen = false

                selectedTab = 0
            }


            // -------------------------------------------------
            // EDIT TURF → TURFS
            // -------------------------------------------------

            editTurfId != null -> {

                editTurfId = null

                selectedTab = 1
            }


            // -------------------------------------------------
            // ADD TURF → TURFS
            // -------------------------------------------------

            showAddTurfScreen -> {

                showAddTurfScreen = false

                selectedTab = 1
            }


            // -------------------------------------------------
            // MANAGE SLOTS → TURFS
            // -------------------------------------------------

            showSlotScreen -> {

                showSlotScreen = false

                selectedTurfId = null

                selectedTab = 1
            }


            // -------------------------------------------------
            // OTHER TABS → DASHBOARD
            // -------------------------------------------------

            selectedTab != 0 -> {

                selectedTab = 0
            }


            // -------------------------------------------------
            // DASHBOARD → CLOSE APP
            // -------------------------------------------------

            else -> {

                (context as? Activity)?.finish()
            }
        }
    }


    // =========================================================
    // LOAD DASHBOARD + NOTIFICATION COUNT
    // =========================================================

    LaunchedEffect(token) {

        if (token.isNotBlank()) {

            viewModel.loadDashboard(
                token
            )

            notificationViewModel.loadUnreadCount(
                token
            )
        }
    }


    // =========================================================
    // LOAD TURFS
    // =========================================================

    LaunchedEffect(
        token,
        selectedTab
    ) {

        if (
            selectedTab == 1 &&
            token.isNotBlank()
        ) {

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
    // NOTIFICATION SCREEN
    // =========================================================

    if (showAdminNotificationScreen) {

        AdminNotificationScreen(

            token =
                token,

            viewModel =
                notificationViewModel,

            onBackClick = {

                showAdminNotificationScreen = false

                notificationViewModel.loadUnreadCount(
                    token
                )
            }
        )

        return
    }


    // =========================================================
    // EDIT ADMIN PROFILE SCREEN
    // =========================================================

    if (showAdminEditProfileScreen) {

        AdminEditProfileScreen(

            currentName =
                admin?.name
                    ?: "Admin",

            currentEmail =
                admin?.email
                    ?: "",

            onBackClick = {

                showAdminEditProfileScreen = false

                selectedTab = 3
            },

            onSaveClick = { _, _ ->

                // Profile API will be connected later.

                showAdminEditProfileScreen = false

                selectedTab = 3

                viewModel.loadDashboard(
                    token
                )
            }
        )

        return
    }


    // =========================================================
    // EDIT TURF SCREEN
    // =========================================================

    if (editTurfId != null) {

        EditTurfScreen(

            token =
                token,

            turfId =
                editTurfId!!,

            onBack = {

                editTurfId = null

                selectedTab = 1
            },

            onSuccess = {

                editTurfId = null

                selectedTab = 1

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

            token =
                token,

            onBack = {

                showAddTurfScreen = false

                selectedTab = 1
            },

            onSuccess = {

                showAddTurfScreen = false

                selectedTab = 1

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

            token =
                token,

            turfId =
                selectedTurfId!!,

            onBack = {

                showSlotScreen = false

                selectedTurfId = null

                selectedTab = 1
            }
        )

        return
    }


    // =========================================================
    // PAID PLANS SCREEN
    // =========================================================

    if (showPaidPlansScreen) {

        AdminPaidPlansScreen(

            token =
                token,

            viewModel =
                viewModel,

            // -------------------------------------------------
            // PAYMENT SUCCESS
            // -------------------------------------------------

            onPlanActivated = {

                showPaidPlansScreen = false

                showSubscriptionScreen = false

                selectedTab = 0

                viewModel.loadDashboard(
                    token
                )

                viewModel.loadSubscriptionStatus(
                    token
                )
            },

            // -------------------------------------------------
            // BACK → SUBSCRIPTION
            // -------------------------------------------------

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

            token =
                token,

            viewModel =
                viewModel,

            onSubscriptionActive = {

                showSubscriptionScreen = false

                selectedTab = 0

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

                selectedTab = 0
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

                unreadNotificationCount =
                    unreadNotificationCount,

                onNotificationClick = {

                    showAdminNotificationScreen = true

                    notificationViewModel.loadNotifications(
                        token
                    )
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

                    // -----------------------------------------
                    // MANAGE TURFS
                    // -----------------------------------------

                    onManageTurfs = {

                        selectedTab = 1
                    },

                    // -----------------------------------------
                    // BOOKINGS
                    // -----------------------------------------

                    onBookingsClick = {

                        selectedTab = 2
                    },

                    // -----------------------------------------
                    // SUBSCRIPTION
                    // -----------------------------------------

                    onSubscriptionClick = {

                        showSubscriptionScreen = true
                    },

                    // -----------------------------------------
                    // ADD TURF
                    // -----------------------------------------

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

                    // -----------------------------------------
                    // ADD TURF
                    // -----------------------------------------

                    onAddTurf = {

                        showAddTurfScreen = true
                    },

                    // -----------------------------------------
                    // EDIT TURF
                    // -----------------------------------------

                    onEditTurf = { turf ->

                        editTurfId =
                            turf.id
                    },

                    // -----------------------------------------
                    // MANAGE SLOTS
                    // -----------------------------------------

                    onManageSlots = { turf ->

                        selectedTurfId =
                            turf.id

                        showSlotScreen =
                            true
                    },

                    // -----------------------------------------
                    // DELETE TURF
                    // -----------------------------------------

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

                    // -----------------------------------------
                    // RETRY
                    // -----------------------------------------

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


            // =================================================
            // TAB 3 — PROFILE
            // =================================================

            3 -> {

                AdminProfileScreen(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),

                    adminName =
                        admin?.name
                            ?: "Admin",

                    adminEmail =
                        admin?.email
                            ?: "No email available",

                    adminPhone =
                        "No phone available",

                    onEditProfileClick = {

                        showAdminEditProfileScreen =
                            true
                    }
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
