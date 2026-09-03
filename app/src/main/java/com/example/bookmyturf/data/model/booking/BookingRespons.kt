package com.example.bookmyturf.data.model.booking

import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.model.turf.Turf

data class BookingResponse(
    val success: Boolean,
    val message: String,
    val data: BookingData?
)

data class BookingData(
    val booking: Booking?
)

data class Booking(
    val id: Int,
    val user_id: Int,
    val turf_id: Int,
    val slot_id: Int,
    val booking_date: String,
    val total_amount: Double,
    val payment_id: String?,
    val payment_status: String,
    val booking_status: String,
    val booked_at: String?,
    val created_at: String?,
    val updated_at: String?,
    val turf: Turf?,
    val slot: Slot?
)

data class BookingListResponse(
    val success: Boolean,
    val message: String,
    val data: BookingListData? )

data class BookingListData(
    val bookings: List<Booking>? )

