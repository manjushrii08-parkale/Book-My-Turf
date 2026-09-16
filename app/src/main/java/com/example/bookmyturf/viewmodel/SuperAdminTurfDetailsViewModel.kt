    package com.example.bookmyturf.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.bookmyturf.data.model.SuperAdminTurf
    import com.example.bookmyturf.data.remote.RetrofitClient
    import com.example.bookmyturf.data.repository.SuperAdminTurfRepository
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch

    class SuperAdminTurfDetailsViewModel : ViewModel() {

        private val repository = SuperAdminTurfRepository(
            RetrofitClient.api
        )

        private val _turf = MutableStateFlow<SuperAdminTurf?>(null)
        val turf: StateFlow<SuperAdminTurf?> = _turf.asStateFlow()

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        private val _actionMessage = MutableStateFlow<String?>(null)
        val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

        fun loadTurfDetails(
            turfId: Int,
            token: String
        ) {
            viewModelScope.launch {
                _isLoading.value = true
                _errorMessage.value = null

                repository.getTurfDetails(
                    turfId = turfId,
                    authorization = "Bearer $token"
                ).onSuccess { turfDetails ->
                    _turf.value = turfDetails
                }.onFailure { error ->
                    _errorMessage.value =
                        error.message ?: "Unable to load turf details."
                }

                _isLoading.value = false
            }
        }

        fun blockTurf(
            turfId: Int,
            token: String
        ) {
            viewModelScope.launch {
                _actionMessage.value = null

                repository.blockTurf(
                    turfId = turfId,
                    authorization = "Bearer $token"
                ).onSuccess { response ->
                    _actionMessage.value =
                        response.message.ifBlank {
                            "Turf blocked successfully."
                        }

                    loadTurfDetails(
                        turfId = turfId,
                        token = token
                    )
                }.onFailure { error ->
                    _actionMessage.value =
                        error.message ?: "Unable to block turf."
                }
            }
        }

        fun activateTurf(
            turfId: Int,
            token: String
        ) {
            viewModelScope.launch {
                _actionMessage.value = null

                repository.activateTurf(
                    turfId = turfId,
                    authorization = "Bearer $token"
                ).onSuccess { response ->
                    _actionMessage.value =
                        response.message.ifBlank {
                            "Turf activated successfully."
                        }

                    loadTurfDetails(
                        turfId = turfId,
                        token = token
                    )
                }.onFailure { error ->
                    _actionMessage.value =
                        error.message ?: "Unable to activate turf."
                }
            }
        }
    }