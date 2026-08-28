package com.example.bookmyturf.data.model.turf

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(

    val success: Boolean,

    val message: String,

    @SerializedName("data")
    val data: ImageUploadData
)

data class ImageUploadData(

    @SerializedName("image_urls")
    val imageUrls: List<String>
)

