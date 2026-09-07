package com.example.bookmyturf.data.model.booking

data class CreateBookingRequest(
    val slot_id: Int,
    val booking_date: String
)

data class CancelBookingRequest(
    val reason: String? = null )