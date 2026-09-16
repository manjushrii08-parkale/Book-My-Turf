package com.example.bookmyturf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.local.SessionManager
import com.example.bookmyturf.data.model.SuperAdminAdmin
import com.example.bookmyturf.data.repository.SuperAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuperAdminAdminsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SuperAdminRepository()
    private val sessionManager = SessionManager(application)

    private val _admins =
        MutableStateFlow<List<SuperAdminAdmin>>(emptyList())

    val admins: StateFlow<List<SuperAdminAdmin>> =
        _admins

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error

    fun loadAdmins() {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getAdmins(token)

                if (response.success) {
                    _admins.value = response.data.orEmpty()
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load admins."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun blockAdmin(adminId: Int) {
        updateAdminStatus(
            adminId = adminId,
            shouldActivate = false
        )
    }

    fun activateAdmin(adminId: Int) {
        updateAdminStatus(
            adminId = adminId,
            shouldActivate = true
        )
    }

    private fun updateAdminStatus(
        adminId: Int,
        shouldActivate: Boolean
    ) {
        val token = sessionManager.getToken()

        if (token.isNullOrBlank()) {
            _error.value = "Session expired. Please login again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = if (shouldActivate) {
                    repository.activateAdmin(
                        token = token,
                        adminId = adminId
                    )
                } else {
                    repository.blockAdmin(
                        token = token,
                        adminId = adminId
                    )
                }

                if (response.success) {
                    loadAdmins()
                } else {
                    _error.value = response.message
                }
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to update admin status."
            } finally {
                _isLoading.value = false
            }
        }
    }
}