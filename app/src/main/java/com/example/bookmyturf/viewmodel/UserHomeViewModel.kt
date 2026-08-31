package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.repository.TurfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserHomeViewModel(
    private val repository: TurfRepository = TurfRepository()
) : ViewModel() {

    // =========================================================
    // TURFS
    // =========================================================

    private val _turfs =
        MutableStateFlow<List<Turf>>(emptyList())

    val turfs: StateFlow<List<Turf>> =
        _turfs.asStateFlow()


    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // =========================================================
    // LOAD TURFS
    // =========================================================

    fun loadTurfs() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            val result =
                repository.getTurfs()

            result
                .onSuccess { turfList ->

                    _turfs.value = turfList
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to load turfs."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }
}