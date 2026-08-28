
package com.example.bookmyturf.data.model.slot

import com.google.gson.annotations.SerializedName

data class CreateSlotRequest(

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("price")
    val price: Double
)


data class UpdateSlotRequest(

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("price")
    val price: Double
)


data class UpdateSlotStatusRequest(

    @SerializedName("status")
    val status: String
)
