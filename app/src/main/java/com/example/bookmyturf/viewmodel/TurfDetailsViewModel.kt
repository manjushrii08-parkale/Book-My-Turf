package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.repository.TurfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TurfDetailsViewModel(
    private val repository: TurfRepository
) : ViewModel() {

    // =========================================================
    // TURF
    // =========================================================

    private val _turf =
        MutableStateFlow<Turf?>(null)

    val turf: StateFlow<Turf?> =
        _turf.asStateFlow()


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
    // LOAD TURF
    // =========================================================

    fun loadTurf(
        turfId: Int
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            val result =
                repository.getTurfById(turfId)

            result
                .onSuccess { turf ->

                    _turf.value = turf
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to load turf."
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