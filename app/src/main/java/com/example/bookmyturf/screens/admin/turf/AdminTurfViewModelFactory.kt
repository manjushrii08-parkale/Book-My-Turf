package com.example.bookmyturf.screens.admin.turf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmyturf.data.repository.AdminTurfRepository
import com.example.bookmyturf.viewmodel.AdminTurfViewModel

class AdminTurfViewModelFactory(
    private val repository: AdminTurfRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                AdminTurfViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return AdminTurfViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}