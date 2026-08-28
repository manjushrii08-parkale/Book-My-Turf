package com.example.bookmyturf.data.model.slot

data class SlotResponse(
    val success: Boolean,
    val message: String,
    val data: SlotData? = null
)

data class SlotData(
    val slot: Slot
)

data class SlotsResponse(
    val success: Boolean,
    val message: String,
    val data: SlotsData? = null
)

data class SlotsData(
    val slots: List<Slot>
)