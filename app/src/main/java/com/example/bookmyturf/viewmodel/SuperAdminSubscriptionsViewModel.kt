package com.example.bookmyturf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.SuperAdminSubscription
import com.example.bookmyturf.data.repository.SuperAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuperAdminSubscriptionsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SuperAdminRepository()
    private val sessionManager = SessionManager(application)

    private val _subscriptions =
        MutableStateFlow<List<SuperAdminSubscription>>(emptyList())

    val subscriptions: StateFlow<List<SuperAdminSubscription>> =
        _subscriptions

    private val _selectedSubscription =
        MutableStateFlow<SuperAdminSubscription?>(null)

    val selectedSubscription: StateFlow<SuperAdminSubscription?> =
        _selectedSubscription

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error

    fun loadSubscriptions() {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getSubscriptions(token)

                if (response.success) {
                    _subscriptions.value = response.data.orEmpty()
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message
                        ?: "Unable to load subscriptions."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSubscriptionDetails(subscriptionId: Int) {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedSubscription.value = null

            try {
                val response = repository.getSubscriptionDetails(
                    token = token,
                    subscriptionId = subscriptionId
                )

                if (response.success) {
                    _selectedSubscription.value = response.data
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message
                        ?: "Unable to load subscription details."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedSubscription() {
        _selectedSubscription.value = null
    }
}