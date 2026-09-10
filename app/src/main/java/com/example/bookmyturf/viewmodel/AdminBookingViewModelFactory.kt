package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.bookmyturf.data.repository.AdminBookingRepository

class AdminBookingViewModelFactory(
    private val repository: AdminBookingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                AdminBookingViewModel::class.java
            )
        ) {

            return AdminBookingViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}