package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.repository.AdminSlotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AdminSlotViewModel(
    private val repository: AdminSlotRepository
) : ViewModel() {

    // =========================================================
    // SLOTS
    // =========================================================

    private val _slots =
        MutableStateFlow<List<Slot>>(emptyList())

    val slots: StateFlow<List<Slot>> =
        _slots.asStateFlow()


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
    // SUCCESS MESSAGE
    // =========================================================

    private val _successMessage =
        MutableStateFlow<String?>(null)

    val successMessage: StateFlow<String?> =
        _successMessage.asStateFlow()


    // =========================================================
    // LOAD SLOTS
    // =========================================================

    fun loadSlots(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getSlots(
                        token = token,
                        turfId = turfId
                    )

                if (response.success) {

                    _slots.value =
                        response.data?.slots
                            ?: emptyList()

                } else {

                    _error.value =
                        response.message
                }

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to load slots."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CREATE SLOT
    // =========================================================

    fun createSlot(
        token: String,
        turfId: Int,
        startTime: String,
        endTime: String,
        price: Double,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val request =
                    CreateSlotRequest(
                        startTime = startTime,
                        endTime = endTime,
                        price = price
                    )

                val response =
                    repository.createSlot(
                        token = token,
                        turfId = turfId,
                        request = request
                    )

                if (response.success) {

                    _successMessage.value =
                        response.message

                    loadSlots(
                        token = token,
                        turfId = turfId
                    )

                    onSuccess()

                } else {

                    _error.value =
                        response.message
                }

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to create slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPDATE SLOT
    // =========================================================

    fun updateSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        startTime: String,
        endTime: String,
        price: Double,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val request =
                    UpdateSlotRequest(
                        startTime = startTime,
                        endTime = endTime,
                        price = price
                    )

                val response =
                    repository.updateSlot(
                        token = token,
                        turfId = turfId,
                        slotId = slotId,
                        request = request
                    )

                if (response.success) {

                    _successMessage.value =
                        response.message

                    loadSlots(
                        token = token,
                        turfId = turfId
                    )

                    onSuccess()

                } else {

                    _error.value =
                        response.message
                }

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to update slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // DELETE SLOT
    // =========================================================

    fun deleteSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                repository.deleteSlot(
                    token = token,
                    turfId = turfId,
                    slotId = slotId
                )

                _successMessage.value =
                    "Slot deleted successfully."

                loadSlots(
                    token = token,
                    turfId = turfId
                )

                onSuccess()

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to delete slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPDATE STATUS
    // =========================================================

    fun updateSlotStatus(
        token: String,
        turfId: Int,
        slotId: Int,
        status: String,
        onSuccess: () -> Unit = {}
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.updateSlotStatus(
                        token = token,
                        turfId = turfId,
                        slotId = slotId,
                        status = status
                    )

                if (response.success) {

                    _successMessage.value =
                        response.message

                    loadSlots(
                        token = token,
                        turfId = turfId
                    )

                    onSuccess()

                } else {

                    _error.value =
                        response.message
                }

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Unable to update slot status."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }


    // =========================================================
    // CLEAR SUCCESS
    // =========================================================

    fun clearSuccessMessage() {

        _successMessage.value = null
    }
}
