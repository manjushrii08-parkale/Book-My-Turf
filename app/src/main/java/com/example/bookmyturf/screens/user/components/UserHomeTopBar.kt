package com.example.bookmyturf.screens.user.components

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

import com.example.bookmyturf.ui.theme.UserDarkGreen
import com.example.bookmyturf.ui.theme.UserLightGreen
import com.example.bookmyturf.util.LocationHelper

import com.google.android.gms.location.LocationServices


@Composable
fun UserHomeTopBar(
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
    // CHECK PERMISSION
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
    // PREMIUM HEADER
    // =========================================================

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        color =
            MaterialTheme
                .colorScheme
                .surface,

        tonalElevation = 3.dp
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 13.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =================================================
            // LOCATION
            // =================================================

            Row(

                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        loadLocation()
                    },

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =============================================
                // LOCATION ICON
                // =============================================

                Box(

                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            UserDarkGreen.copy(
                                alpha = 0.10f
                            )
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.LocationOn,

                        contentDescription =
                            "Current location",

                        modifier =
                            Modifier.size(22.dp),

                        tint =
                            UserDarkGreen
                    )
                }

                // =============================================
                // LOCATION TEXT
                // =============================================

                Column(

                    modifier =
                        Modifier.padding(
                            start = 11.dp
                        )
                ) {

                    Text(

                        text =
                            if (locationLoading) {
                                "Your location"
                            } else {
                                "Playing near"
                            },

                        fontSize = 11.sp,

                        fontWeight =
                            FontWeight.Medium,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )

                    Text(

                        text =
                            locationText,

                        modifier =
                            Modifier.padding(
                                top = 2.dp
                            ),

                        fontSize = 15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            UserDarkGreen,

                        maxLines = 1
                    )
                }
            }

            // =================================================
            // ACTIONS
            // =================================================

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =============================================
                // NOTIFICATION
                // =============================================

                IconButton(

                    onClick =
                        onNotificationClick
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.NotificationsNone,

                        contentDescription =
                            "Notifications",

                        modifier =
                            Modifier.size(23.dp),

                        tint =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )
                }

                // =============================================
                // PROFILE
                // =============================================

                IconButton(

                    onClick =
                        onProfileClick
                ) {

                    Box(

                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                UserLightGreen
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "Profile",

                            modifier =
                                Modifier.size(21.dp),

                            tint =
                                Color.White
                        )
                    }
                }
            }
        }
    }
}