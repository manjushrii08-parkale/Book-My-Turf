package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.SuperAdminTurf
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.repository.SuperAdminTurfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuperAdminTurfsViewModel : ViewModel() {

    private val repository = SuperAdminTurfRepository(
        RetrofitClient.api
    )

    private val _turfs = MutableStateFlow<List<SuperAdminTurf>>(emptyList())
    val turfs: StateFlow<List<SuperAdminTurf>> = _turfs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    fun loadTurfs(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getTurfs(
                authorization = "Bearer $token"
            ).onSuccess { turfList ->
                _turfs.value = turfList
            }.onFailure { error ->
                _errorMessage.value =
                    error.message ?: "Unable to load turfs."
            }

            _isLoading.value = false
        }
    }

    fun blockTurf(
        turfId: Int,
        token: String
    ) {
        viewModelScope.launch {
            _actionMessage.value = null

            repository.blockTurf(
                turfId = turfId,
                authorization = "Bearer $token"
            ).onSuccess { response ->
                _actionMessage.value =
                    response.message.ifBlank {
                        "Turf blocked successfully."
                    }

                loadTurfs(token)
            }.onFailure { error ->
                _actionMessage.value =
                    error.message ?: "Unable to block turf."
            }
        }
    }

    fun activateTurf(
        turfId: Int,
        token: String
    ) {
        viewModelScope.launch {
            _actionMessage.value = null

            repository.activateTurf(
                turfId = turfId,
                authorization = "Bearer $token"
            ).onSuccess { response ->
                _actionMessage.value =
                    response.message.ifBlank {
                        "Turf activated successfully."
                    }

                loadTurfs(token)
            }.onFailure { error ->
                _actionMessage.value =
                    error.message ?: "Unable to activate turf."
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _actionMessage.value = null
    }
}