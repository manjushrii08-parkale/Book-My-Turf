package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.repository.AdminTurfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminTurfViewModel(
    private val repository: AdminTurfRepository
) : ViewModel() {

    // Turfs
    private val _turfs =
        MutableStateFlow<List<Turf>>(emptyList())

    val turfs: StateFlow<List<Turf>> =
        _turfs.asStateFlow()

    // Loading
    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    // Error
    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    fun loadTurfs(token: String) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getTurfs(token)

                if (response.success) {

                    _turfs.value =
                        response.data?.turfs
                            ?: emptyList()

                } else {

                    _error.value =
                        response.message
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load turfs."

            } finally {

                _isLoading.value = false
            }
        }
    }


    fun clearError() {

        _error.value = null
    }
}