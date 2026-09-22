package com.example.bookmyturf.screens.user.components

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext

import com.example.bookmyturf.util.LocationHelper

import com.google.android.gms.location.LocationServices


// ============================================================
// BOOK MY TURF - PREMIUM HOME COLORS
// ============================================================

private val Background = Color(0xFF020C09)

private val CardBackground = Color(0xFF071713)

private val SecondarySurface = Color(0xFF102A1F)

private val PrimaryGreen = Color(0xFF7DBB4A)

private val LightGreen = Color(0xFFA8D86E)

private val White = Color(0xFFF5F8F6)

private val SecondaryText = Color(0xFF9EAEA6)

private val BorderColor = Color(0xFF1B3028)


// ============================================================
// USER HOME TOP BAR
// ============================================================

@Composable
fun UserHomeTopBar(
    unreadNotificationCount: Int = 0,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = LocalContext.current


    // =========================================================
    // LOCATION CLIENT
    // =========================================================

    val fusedLocationClient =
        remember {
            LocationServices
                .getFusedLocationProviderClient(context)
        }


    // =========================================================
    // LOCATION STATE
    // =========================================================

    var locationText by remember {
        mutableStateOf("Fetching location...")
    }

    var locationLoading by remember {
        mutableStateOf(true)
    }


    // =========================================================
    // LOAD LOCATION
    // =========================================================

    fun loadLocation() {

        locationLoading = true

        LocationHelper.getCurrentLocation(
            context = context,
            fusedLocationClient = fusedLocationClient
        ) { result ->

            locationText = result

            locationLoading = false
        }
    }


    // =========================================================
    // LOCATION PERMISSION
    // =========================================================

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permissions[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (granted) {

                loadLocation()

            } else {

                locationText =
                    "Location permission required"

                locationLoading = false
            }
        }


    // =========================================================
    // CHECK LOCATION PERMISSION
    // =========================================================

    LaunchedEffect(Unit) {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (
            fineLocationGranted ||
            coarseLocationGranted
        ) {

            loadLocation()

        } else {

            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    // =========================================================
    // PREMIUM DARK HEADER
    // =========================================================

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        color =
            Background,

        tonalElevation = 0.dp
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 11.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {


            // =================================================
            // LOCATION SECTION
            // =================================================

            Row(

                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        loadLocation()
                    }
                    .padding(
                        horizontal = 4.dp,
                        vertical = 3.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // =============================================
                // LOCATION ICON CONTAINER
                // =============================================

                Surface(

                    modifier =
                        Modifier.size(43.dp),

                    shape =
                        RoundedCornerShape(14.dp),

                    color =
                        SecondarySurface,

                    border =
                        BorderStroke(
                            width = 1.dp,
                            color = BorderColor
                        )
                ) {

                    Box(

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.LocationOn,

                            contentDescription =
                                "Current location",

                            modifier =
                                Modifier.size(21.dp),

                            tint =
                                PrimaryGreen
                        )
                    }
                }


                // =============================================
                // LOCATION TEXT
                // =============================================

                Column(

                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(
                                start = 10.dp
                            )
                ) {

                    Text(

                        text =
                            if (locationLoading) {
                                "Your location"
                            } else {
                                "Playing near"
                            },

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            SecondaryText
                    )


                    Text(

                        text =
                            locationText,

                        modifier =
                            Modifier.padding(
                                top = 2.dp
                            ),

                        fontSize = 14.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            White,

                        maxLines = 1
                    )
                }
            }


            // =================================================
            // ACTION BUTTONS
            // =================================================

            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(7.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // =============================================
                // NOTIFICATION
                // =============================================

                Box(
                    modifier =
                        Modifier.size(42.dp)
                ) {

                    Surface(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(1.dp),

                        shape =
                            RoundedCornerShape(13.dp),

                        color =
                            CardBackground,

                        border =
                            BorderStroke(
                                width = 1.dp,
                                color = BorderColor
                            )
                    ) {

                        Box(

                            contentAlignment =
                                Alignment.Center
                        ) {

                            IconButton(

                                onClick =
                                    onNotificationClick,

                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.NotificationsNone,

                                    contentDescription =
                                        "Notifications",

                                    modifier =
                                        Modifier.size(22.dp),

                                    tint =
                                        White
                                )
                            }
                        }
                    }


                    // =========================================
                    // UNREAD BADGE
                    // =========================================

                    if (
                        unreadNotificationCount > 0
                    ) {

                        Surface(

                            modifier =
                                Modifier
                                    .align(
                                        Alignment.TopEnd
                                    )
                                    .size(18.dp),

                            shape =
                                CircleShape,

                            color =
                                PrimaryGreen,

                            shadowElevation =
                                2.dp,

                            border =
                                BorderStroke(
                                    width = 2.dp,
                                    color = Background
                                )
                        ) {

                            Box(

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(

                                    text =
                                        if (
                                            unreadNotificationCount > 9
                                        ) {
                                            "9+"
                                        } else {
                                            unreadNotificationCount
                                                .toString()
                                        },

                                    color =
                                        Background,

                                    fontSize = 7.sp,

                                    fontWeight =
                                        FontWeight.ExtraBold,

                                    maxLines = 1
                                )
                            }
                        }
                    }
                }


                // =============================================
                // PROFILE
                // =============================================

                Surface(

                    modifier =
                        Modifier.size(42.dp),

                    shape =
                        CircleShape,

                    color =
                        PrimaryGreen,

                    border =
                        BorderStroke(
                            width = 2.dp,

                            color =
                                LightGreen.copy(
                                    alpha = 0.35f
                                )
                        )
                ) {

                    IconButton(

                        onClick =
                            onProfileClick,

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "Profile",

                            modifier =
                                Modifier.size(21.dp),

                            tint =
                                Background
                        )
                    }
                }
            }
        }
    }
}