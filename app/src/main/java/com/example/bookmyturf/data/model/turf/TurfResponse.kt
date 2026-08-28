package com.example.bookmyturf.data.model.turf

data class TurfResponse(
    val success: Boolean,
    val message: String,
    val data: TurfData? = null
)

data class TurfData(
    val turf: Turf
)

data class TurfListResponse(
    val success: Boolean,
    val message: String,
    val data: TurfListData? = null
)

data class TurfListData(
    val turfs: List<Turf>
)