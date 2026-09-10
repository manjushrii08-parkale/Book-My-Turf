package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmyturf.data.repository.TurfRepository

class UserProfileViewModelFactory(
    private val repository: TurfRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                UserProfileViewModel::class.java
            )
        ) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}