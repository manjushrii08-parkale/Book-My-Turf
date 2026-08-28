package com.example.bookmyturf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.SuperAdminDashboardData
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.SuperAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

data class SuperAdminUiState(

    val isLoading: Boolean = false,

    val dashboard: SuperAdminDashboardData? = null,

    val errorMessage: String? = null
)


class SuperAdminViewModel : ViewModel() {

    // =========================================================
    // REPOSITORY
    // =========================================================

    private val repository =
        SuperAdminRepository(
            RetrofitClient.api
        )


    // =========================================================
    // UI STATE
    // =========================================================

    private val _uiState =
        MutableStateFlow(
            SuperAdminUiState()
        )

    val uiState: StateFlow<SuperAdminUiState> =
        _uiState


    // =========================================================
    // LOAD DASHBOARD
    // =========================================================

    fun loadDashboard(
        token: String
    ) {

        if (token.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage =
                        "Authentication token is missing."
                )

            return
        }


        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )


            try {

                Log.d(
                    "SUPER_ADMIN_API",
                    "Loading dashboard..."
                )


                val response =
                    repository.getDashboard(
                        token
                    )


                Log.d(
                    "SUPER_ADMIN_API",
                    "Success = ${response.success}"
                )

                Log.d(
                    "SUPER_ADMIN_API",
                    "Message = ${response.message}"
                )


                if (response.success) {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            dashboard = response.data,
                            errorMessage = null
                        )


                    Log.d(
                        "SUPER_ADMIN_API",
                        "Users = ${response.data.users.total}"
                    )

                    Log.d(
                        "SUPER_ADMIN_API",
                        "Admins = ${response.data.admins.total}"
                    )

                    Log.d(
                        "SUPER_ADMIN_API",
                        "Turfs = ${response.data.turfs.total}"
                    )

                    Log.d(
                        "SUPER_ADMIN_API",
                        "Bookings = ${response.data.bookings.total}"
                    )

                    Log.d(
                        "SUPER_ADMIN_API",
                        "Revenue = ${response.data.revenue.total}"
                    )

                } else {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            dashboard = null,
                            errorMessage =
                                response.message
                        )
                }

            } catch (e: HttpException) {

                Log.e(
                    "SUPER_ADMIN_API",
                    "HTTP ${e.code()}",
                    e
                )


                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        dashboard = null,
                        errorMessage =
                            extractError(e)
                    )

            } catch (e: Exception) {

                Log.e(
                    "SUPER_ADMIN_API",
                    "Dashboard request failed",
                    e
                )


                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        dashboard = null,
                        errorMessage =
                            e.message
                                ?: "Unable to load dashboard"
                    )
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
    // ERROR HANDLER
    // =========================================================

    private fun extractError(
        exception: HttpException
    ): String {

        return try {

            val body =
                exception.response()
                    ?.errorBody()
                    ?.string()


            if (body.isNullOrBlank()) {

                return when (exception.code()) {

                    401 ->
                        "Unauthorized. Please login again."

                    403 ->
                        "You do not have permission to access this dashboard."

                    404 ->
                        "Dashboard API endpoint not found."

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

        } catch (_: Exception) {

            "Request failed (${exception.code()})."
        }
    }
}