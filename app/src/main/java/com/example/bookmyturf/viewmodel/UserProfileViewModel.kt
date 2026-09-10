package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.profile.UserProfile
import com.example.bookmyturf.data.repository.TurfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
class UserProfileViewModel(
    private val repository: TurfRepository
) : ViewModel() {

    // =========================================================
    // PROFILE
    // =========================================================

    private val _profile =
        MutableStateFlow<UserProfile?>(null)

    val profile: StateFlow<UserProfile?> =
        _profile.asStateFlow()


    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // UPDATE LOADING
    // =========================================================

    private val _isUpdating =
        MutableStateFlow(false)

    val isUpdating: StateFlow<Boolean> =
        _isUpdating.asStateFlow()


    // =========================================================
    // UPDATE SUCCESS
    // =========================================================

    private val _updateSuccess =
        MutableStateFlow(false)

    val updateSuccess: StateFlow<Boolean> =
        _updateSuccess.asStateFlow()


    // =========================================================
    // ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // =========================================================
    // LOAD PROFILE
    // =========================================================

    fun loadProfile() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            val result =
                repository.getUserProfile()

            result
                .onSuccess { user ->

                    _profile.value =
                        user
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to load profile."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    // =========================================================
// UPDATE PROFILE
// =========================================================

    fun updateProfile(
        name: String,
        phone: String?,
        dateOfBirth: String?
    ) {

        viewModelScope.launch {

            _isUpdating.value = true
            _updateSuccess.value = false
            _error.value = null

            Log.d(
                "PROFILE_UPDATE",
                "ViewModel updateProfile() called"
            )

            val result =
                repository.updateUserProfile(
                    name = name,
                    phone = phone,
                    dateOfBirth = dateOfBirth
                )

            result
                .onSuccess { updatedUser ->

                    Log.d(
                        "PROFILE_UPDATE",
                        "ViewModel success"
                    )

                    _profile.value =
                        updatedUser

                    _updateSuccess.value =
                        true
                }
                .onFailure { exception ->

                    Log.e(
                        "PROFILE_UPDATE",
                        "ViewModel failure: ${exception.message}",
                        exception
                    )

                    _error.value =
                        exception.message
                            ?: "Unable to update profile."
                }

            _isUpdating.value = false
        }
    }


    // =========================================================
    // CLEAR UPDATE SUCCESS
    // =========================================================

    fun clearUpdateSuccess() {

        _updateSuccess.value = false
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _error.value = null
    }
}