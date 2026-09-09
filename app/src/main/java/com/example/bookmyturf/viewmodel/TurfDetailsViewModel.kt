package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.slot.Slot
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
    // SLOTS
    // =========================================================

    private val _slots =
        MutableStateFlow<List<Slot>>(emptyList())

    val slots: StateFlow<List<Slot>> =
        _slots.asStateFlow()


    // =========================================================
    // TURF LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // SLOT LOADING
    // =========================================================

    private val _isLoadingSlots =
        MutableStateFlow(false)

    val isLoadingSlots: StateFlow<Boolean> =
        _isLoadingSlots.asStateFlow()


    // =========================================================
    // TURF ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // =========================================================
    // SLOT ERROR
    // =========================================================

    private val _slotError =
        MutableStateFlow<String?>(null)

    val slotError: StateFlow<String?> =
        _slotError.asStateFlow()


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
                repository.getTurfById(
                    turfId
                )

            result
                .onSuccess { turf ->

                    _turf.value =
                        turf
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
    // LOAD DATE-WISE SLOTS
    // =========================================================

    fun loadSlots(
        turfId: Int,
        bookingDate: String
    ) {

        viewModelScope.launch {

            _isLoadingSlots.value = true
            _slotError.value = null

            val result =
                repository.getSlots(
                    turfId = turfId,
                    bookingDate = bookingDate
                )

            result
                .onSuccess { slotList ->

                    _slots.value =
                        slotList.filter { slot ->

                            slot.status.equals(
                                "ACTIVE",
                                ignoreCase = true
                            )
                        }
                }
                .onFailure { exception ->

                    _slots.value =
                        emptyList()

                    _slotError.value =
                        exception.message
                            ?: "Unable to load slots."
                }

            _isLoadingSlots.value = false
        }
    }


    // =========================================================
    // CLEAR TURF ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }


    // =========================================================
    // CLEAR SLOT ERROR
    // =========================================================

    fun clearSlotError() {

        _slotError.value = null
    }
}