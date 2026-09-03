package com.example.bookmyturf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmyturf.data.model.booking.Booking
import com.example.bookmyturf.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {

    // =========================================================
    // BOOKINGS
    // =========================================================

    private val _bookings =
        MutableStateFlow<List<Booking>>(emptyList())

    val bookings: StateFlow<List<Booking>> =
        _bookings.asStateFlow()


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
    // CREATED BOOKING
    // =========================================================

    private val _createdBooking =
        MutableStateFlow<Booking?>(null)

    val createdBooking: StateFlow<Booking?> =
        _createdBooking.asStateFlow()


    // =========================================================
    // CREATE BOOKING
    // =========================================================

    fun createBooking(
        token: String,
        slotId: Int,
        bookingDate: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository.createBooking(
                token = token,
                slotId = slotId,
                bookingDate = bookingDate
            )
                .onSuccess { booking ->

                    _createdBooking.value =
                        booking
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to create booking."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // GET MY BOOKINGS
    // =========================================================

    fun loadMyBookings(
        token: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository.getMyBookings(
                token = token
            )
                .onSuccess { bookingList ->

                    _bookings.value =
                        bookingList
                }
                .onFailure { exception ->

                    _error.value =
                        exception.message
                            ?: "Unable to load bookings."
                }

            _isLoading.value = false
        }
    }


    // =========================================================
    // CLEAR CREATED BOOKING
    // =========================================================

    fun clearCreatedBooking() {
        _createdBooking.value = null
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {
        _error.value = null
    }


    // =========================================================
    // CLEAR BOOKINGS
    // =========================================================

    fun clearBookings() {
        _bookings.value = emptyList()
    }
}

