package com.example.bookmyturf.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.User
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

data class AuthUiState(
    val isLoading: Boolean = false,
    val otpSent: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val token: String? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AuthRepository(RetrofitClient.api)

    private val sessionManager =
        SessionManager(application.applicationContext)

    private val _uiState =
        MutableStateFlow(
            AuthUiState()
        )

    val uiState: StateFlow<AuthUiState> =
        _uiState


    // =========================================================
    // SEND OTP
    // =========================================================

    fun sendOtp(
        email: String,
        role: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    otpSent = false,
                    errorMessage = null
                )

            try {

                val response =
                    repository.sendOtp(
                        email = email,
                        role = role
                    )

                if (response.success) {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            otpSent = true,
                            errorMessage = null
                        )

                    Log.d(
                        "AUTH",
                        "OTP sent successfully"
                    )

                } else {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            otpSent = false,
                            errorMessage =
                                response.message
                        )
                }

            } catch (e: HttpException) {

                Log.e(
                    "AUTH",
                    "Send OTP HTTP ${e.code()}",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        otpSent = false,
                        errorMessage =
                            extractLaravelError(e)
                    )

            } catch (e: Exception) {

                Log.e(
                    "AUTH",
                    "Send OTP failed",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        otpSent = false,
                        errorMessage =
                            e.message
                                ?: "Failed to send OTP"
                    )
            }
        }
    }


    // =========================================================
    // VERIFY OTP
    // =========================================================

    fun verifyOtp(
        email: String,
        otp: String,
        role: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {

                val response =
                    repository.verifyOtp(
                        email = email,
                        otp = otp,
                        role = role
                    )

                if (
                    response.success &&
                    response.user != null &&
                    !response.token.isNullOrBlank()
                ) {

                    val user =
                        response.user

                    val token =
                        response.token!!


                    // =================================================
                    // SAVE SESSION
                    // =================================================

                    sessionManager.saveSession(
                        token = token,
                        userId = user.id,
                        role = user.role
                    )


                    Log.d(
                        "AUTH",
                        "================================"
                    )

                    Log.d(
                        "AUTH",
                        "LOGIN SUCCESS"
                    )

                    Log.d(
                        "AUTH",
                        "User ID = ${user.id}"
                    )

                    Log.d(
                        "AUTH",
                        "Role = ${user.role}"
                    )

                    Log.d(
                        "AUTH",
                        "Token saved = ${sessionManager.getToken() != null}"
                    )

                    Log.d(
                        "AUTH",
                        "================================"
                    )


                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            user = user,
                            token = token,
                            errorMessage = null
                        )

                } else {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            errorMessage =
                                response.message
                                    ?: "Invalid OTP"
                        )
                }

            } catch (e: HttpException) {

                Log.e(
                    "AUTH",
                    "Verify OTP HTTP ${e.code()}",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage =
                            extractLaravelError(e)
                    )

            } catch (e: Exception) {

                Log.e(
                    "AUTH",
                    "Verify OTP failed",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage =
                            e.message
                                ?: "OTP verification failed"
                    )
            }
        }
    }


    // =========================================================
    // GET TOKEN
    // =========================================================

    fun getToken(): String? {

        return sessionManager.getToken()
    }


    // =========================================================
    // GET USER ID
    // =========================================================

    fun getUserId(): Int {

        return sessionManager.getUserId()
    }


    // =========================================================
    // GET ROLE
    // =========================================================

    fun getUserRole(): String? {

        return sessionManager.getRole()
    }


    // =========================================================
    // GET CURRENT USER
    // =========================================================

    fun getCurrentUser(): User? {

        return _uiState.value.user
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    fun logout() {

        val token =
            sessionManager.getToken()

        viewModelScope.launch {

            try {

                if (!token.isNullOrBlank()) {

                    repository.logout(token)

                    Log.d(
                        "AUTH",
                        "Logout API successful"
                    )
                }

            } catch (e: Exception) {

                Log.e(
                    "AUTH",
                    "Logout failed",
                    e
                )

            } finally {

                sessionManager.clearSession()

                _uiState.value =
                    AuthUiState()
            }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                errorMessage = null
            )
    }


    // =========================================================
    // LARAVEL ERROR
    // =========================================================

    private fun extractLaravelError(
        exception: HttpException
    ): String {

        return try {

            val body =
                exception.response()
                    ?.errorBody()
                    ?.string()

            if (body.isNullOrBlank()) {

                return when (exception.code()) {

                    400 ->
                        "Bad request."

                    401 ->
                        "Authentication failed. Please login again."

                    403 ->
                        "You are not authorized."

                    404 ->
                        "API endpoint not found."

                    422 ->
                        "Invalid information."

                    429 ->
                        "Too many requests."

                    500 ->
                        "Laravel server error."

                    else ->
                        "Request failed (${exception.code()})."
                }
            }

            val json =
                JSONObject(body)

            json.optString(
                "message",
                "Request failed (${exception.code()})."
            )

        } catch (e: Exception) {

            "Request failed (${exception.code()})."
        }
    }
}