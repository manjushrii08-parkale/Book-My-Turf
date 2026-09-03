package com.example.bookmyturf.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import java.util.Locale

object LocationHelper {

    // =========================================================
    // GET FRESH CURRENT LOCATION
    // =========================================================

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        onResult: (String) -> Unit
    ) {

        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
            .addOnSuccessListener { location: Location? ->

                if (location != null) {

                    getAddress(
                        context = context,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        onResult = onResult
                    )

                } else {

                    getLastKnownLocation(
                        context = context,
                        fusedLocationClient = fusedLocationClient,
                        onResult = onResult
                    )
                }
            }
            .addOnFailureListener {

                getLastKnownLocation(
                    context = context,
                    fusedLocationClient = fusedLocationClient,
                    onResult = onResult
                )
            }
    }


    // =========================================================
    // FALLBACK LOCATION
    // =========================================================

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        onResult: (String) -> Unit
    ) {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location != null) {

                    getAddress(
                        context = context,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        onResult = onResult
                    )

                } else {

                    onResult("Location unavailable")
                }
            }
            .addOnFailureListener {

                onResult("Location unavailable")
            }
    }


    // =========================================================
    // CONVERT COORDINATES TO CITY + STATE
    // =========================================================

    @Suppress("DEPRECATION")
    private fun getAddress(
        context: Context,
        latitude: Double,
        longitude: Double,
        onResult: (String) -> Unit
    ) {

        try {

            if (!Geocoder.isPresent()) {

                onResult("Current Location")
                return
            }

            val geocoder =
                Geocoder(
                    context,
                    Locale.getDefault()
                )

            val addresses: List<Address>? =
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )

            val address =
                addresses?.firstOrNull()

            if (address == null) {

                onResult("Current Location")
                return
            }

            val city =
                address.locality
                    ?: address.subAdminArea

            val state =
                address.adminArea

            val locationName =
                when {

                    !city.isNullOrBlank() &&
                            !state.isNullOrBlank() -> {

                        "$city, $state"
                    }

                    !city.isNullOrBlank() -> {

                        city
                    }

                    !state.isNullOrBlank() -> {

                        state
                    }

                    else -> {

                        "Current Location"
                    }
                }

            onResult(locationName)

        } catch (_: Exception) {

            onResult("Current Location")
        }
    }
}