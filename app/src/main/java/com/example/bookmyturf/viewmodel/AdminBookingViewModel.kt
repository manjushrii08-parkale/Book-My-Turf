package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.bookmyturf.data.model.booking.AdminBookingResponse
import com.example.bookmyturf.data.repository.AdminBookingRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import retrofit2.HttpException

class AdminBookingViewModel(
    private val repository: AdminBookingRepository
) : ViewModel() {

    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // ADMIN BOOKINGS
    // =========================================================

    private val _adminBookings =
        MutableStateFlow<AdminBookingResponse?>(null)

    val adminBookings: StateFlow<AdminBookingResponse?> =
        _adminBookings.asStateFlow()


    // =========================================================
    // ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // =========================================================
    // LOAD ADMIN BOOKINGS
    // =========================================================

    fun loadAdminBookings(
        token: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getAdminBookings(token)

                if (response.success) {

                    _adminBookings.value = response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load admin bookings."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    when (e.code()) {

                        401 ->
                            "Authentication failed. Please login again."

                        403 ->
                            "You are not authorized."

                        404 ->
                            "Bookings not found."

                        500 ->
                            "Laravel server error."

                        else ->
                            "Request failed (${e.code()})."
                    }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load admin bookings."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }
}