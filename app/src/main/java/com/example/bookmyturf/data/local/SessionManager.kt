package com.example.bookmyturf.data.local

import android.content.Context

class SessionManager(
    context: Context
) {

    private val preferences =
        context.getSharedPreferences(
            "book_my_turf_session",
            Context.MODE_PRIVATE
        )

    // =========================================================
    // SAVE SESSION
    // =========================================================

    fun saveSession(
        token: String,
        userId: Int,
        role: String
    ) {

        preferences.edit()
            .putString("token", token)
            .putInt("user_id", userId)
            .putString("role", role)
            .apply()
    }

    // =========================================================
    // GET TOKEN
    // =========================================================

    fun getToken(): String? {

        return preferences.getString(
            "token",
            null
        )
    }

    // =========================================================
    // GET USER ID
    // =========================================================

    fun getUserId(): Int {

        return preferences.getInt(
            "user_id",
            -1
        )
    }

    // =========================================================
    // GET ROLE
    // =========================================================

    fun getRole(): String? {

        return preferences.getString(
            "role",
            null
        )
    }

    // =========================================================
    // CHECK LOGIN
    // =========================================================

    fun isLoggedIn(): Boolean {

        return !getToken().isNullOrBlank()
    }

    // =========================================================
    // CLEAR SESSION
    // =========================================================

    fun clearSession() {

        preferences.edit()
            .clear()
            .apply()
    }
}