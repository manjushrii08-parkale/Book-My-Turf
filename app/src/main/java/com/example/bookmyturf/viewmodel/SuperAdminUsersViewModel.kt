package com.example.bookmyturf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.SuperAdminUser
import com.example.bookmyturf.data.repository.SuperAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuperAdminUsersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SuperAdminRepository()
    private val sessionManager = SessionManager(application)

    private val _users =
        MutableStateFlow<List<SuperAdminUser>>(emptyList())

    val users: StateFlow<List<SuperAdminUser>> =
        _users

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error

    fun loadUsers() {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getUsers(token)

                if (response.success) {
                    _users.value = response.data
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load users."
            } finally {
                _isLoading.value = false
            }
        }
    }
}