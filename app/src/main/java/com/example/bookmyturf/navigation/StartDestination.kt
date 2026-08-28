
package com.example.bookmyturf.navigation

import android.content.Context
import com.example.bookmyturf.data.local.SessionManager

fun getStartDestination(
    context: Context
): String {

    val sessionManager = SessionManager(context)

    val token = sessionManager.getToken()
    val role = sessionManager.getRole()

    // No valid session
    if (token.isNullOrBlank() || role.isNullOrBlank()) {
        return Routes.ROLE
    }

    return when (role) {

        "USER" -> {
            Routes.USER_HOME
        }

        "ADMIN" -> {
            Routes.ADMIN_SUBSCRIPTION
        }

        "SUPER_ADMIN" -> {
            Routes.SUPER_ADMIN_HOME
        }

        else -> {

            sessionManager.clearSession()

            Routes.ROLE
        }
    }
}

