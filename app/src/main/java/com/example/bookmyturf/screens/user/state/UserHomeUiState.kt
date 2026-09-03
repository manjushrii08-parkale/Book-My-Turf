package com.example.bookmyturf.screens.user.state

import com.example.bookmyturf.data.model.turf.Turf

data class UserHomeUiState(

    val isLoading: Boolean = false,

    val turfs: List<Turf> = emptyList(),

    val searchQuery: String = "",

    val selectedSport: String = "All",

    val errorMessage: String? = null
)