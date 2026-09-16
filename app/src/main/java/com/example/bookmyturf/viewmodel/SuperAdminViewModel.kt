package com.example.bookmyturf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.SuperAdminDashboardData
import com.example.bookmyturf.data.repository.SuperAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuperAdminViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SuperAdminRepository()
    private val sessionManager = SessionManager(application)

    private val _dashboard =
        MutableStateFlow<SuperAdminDashboardData?>(null)

    val dashboard: StateFlow<SuperAdminDashboardData?> =
        _dashboard

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error

    fun loadDashboard() {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getDashboard(token)

                if (response.success) {
                    _dashboard.value = response.data
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load dashboard."
            } finally {
                _isLoading.value = false
            }
        }
    }


}