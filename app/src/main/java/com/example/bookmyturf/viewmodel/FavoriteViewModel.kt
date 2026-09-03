package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.favorite.Favorite
import com.example.bookmyturf.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoriteRepository = FavoriteRepository()
) : ViewModel() {

    // =========================================================
    // FAVORITES LIST
    // =========================================================

    private val _favorites =
        MutableStateFlow<List<Favorite>>(emptyList())

    val favorites: StateFlow<List<Favorite>> =
        _favorites.asStateFlow()


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
    // FAVORITE STATUS
    // =========================================================

    private val _favoriteStatus =
        MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    val favoriteStatus: StateFlow<Map<Int, Boolean>> =
        _favoriteStatus.asStateFlow()


    // =========================================================
    // GET ALL FAVORITES
    // =========================================================

    fun loadFavorites(token: String) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository.getFavorites(token)
                .onSuccess { favoriteList ->

                    _favorites.value = favoriteList

                    // Sync favorite status map
                    _favoriteStatus.value =
                        favoriteList.associate {
                            it.turf_id to true
                        }
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to load favorites."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // ADD FAVORITE
    // =========================================================

    fun addFavorite(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            _error.value = null

            repository.addFavorite(
                token = token,
                turfId = turfId
            )
                .onSuccess { favorite ->

                    // Add to current favorites list
                    if (
                        _favorites.value.none {
                            it.turf_id == turfId
                        }
                    ) {

                        _favorites.value =
                            _favorites.value + favorite
                    }

                    // Update favorite status
                    _favoriteStatus.value =
                        _favoriteStatus.value + (
                                turfId to true
                                )
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to add favorite."
                }
        }
    }


    // =========================================================
    // REMOVE FAVORITE
    // =========================================================

    fun removeFavorite(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            _error.value = null

            repository.removeFavorite(
                token = token,
                turfId = turfId
            )
                .onSuccess {

                    // Remove from favorites list
                    _favorites.value =
                        _favorites.value.filter {
                            it.turf_id != turfId
                        }

                    // Update favorite status
                    _favoriteStatus.value =
                        _favoriteStatus.value + (
                                turfId to false
                                )
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to remove favorite."
                }
        }
    }


    // =========================================================
    // CHECK FAVORITE
    // =========================================================

    fun checkFavorite(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            repository.checkFavorite(
                token = token,
                turfId = turfId
            )
                .onSuccess { isFavorite ->

                    _favoriteStatus.value =
                        _favoriteStatus.value + (
                                turfId to isFavorite
                                )
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to check favorite status."
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
    // CLEAR FAVORITES
    // =========================================================

    fun clearFavorites() {

        _favorites.value = emptyList()
        _favoriteStatus.value = emptyMap()
    }
}

