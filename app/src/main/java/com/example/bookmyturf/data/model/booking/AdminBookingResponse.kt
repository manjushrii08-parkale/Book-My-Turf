package com.example.bookmyturf.data.model.booking

data class AdminBookingResponse(
    val success: Boolean,
    val message: String,
    val data: AdminBookingData?
)

data class AdminBookingData(
    val bookings: List<Booking>,
    val booking_count: Int
)