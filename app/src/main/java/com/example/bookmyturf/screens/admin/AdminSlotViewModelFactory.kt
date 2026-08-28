package com.example.bookmyturf.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmyturf.data.repository.AdminSlotRepository
import com.example.bookmyturf.viewmodel.AdminSlotViewModel

class AdminSlotViewModelFactory(
    private val repository: AdminSlotRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AdminSlotViewModel::class.java)) {

            return AdminSlotViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
