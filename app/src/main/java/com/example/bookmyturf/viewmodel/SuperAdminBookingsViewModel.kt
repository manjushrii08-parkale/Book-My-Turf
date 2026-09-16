package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.SuperAdminBooking
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.SuperAdminBookingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuperAdminBookingsViewModel : ViewModel() {

    private val repository = SuperAdminBookingsRepository(
        RetrofitClient.api
    )

    private val _bookings = MutableStateFlow<List<SuperAdminBooking>>(emptyList())
    val bookings: StateFlow<List<SuperAdminBooking>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadBookings(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val authorization = if (token.startsWith("Bearer ")) {
                    token
                } else {
                    "Bearer $token"
                }

                val response = repository.getSuperAdminBookings(authorization)

                if (response.success) {
                    _bookings.value = response.data
                } else {
                    _errorMessage.value = response.message
                }

            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to load bookings."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}