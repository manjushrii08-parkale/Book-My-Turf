package com.example.bookmyturf.data.model.slot

import com.google.gson.annotations.SerializedName

data class CreateSlotRequest(

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    val price: Double,

    val status: String = "AVAILABLE"
)

data class UpdateSlotRequest(

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null,

    val price: Double? = null,

    val status: String? = null
)

data class UpdateSlotStatusRequest(

    val status: String
)