    package com.example.bookmyturf.data.model.turf

    import com.google.gson.annotations.SerializedName

    data class CreateTurfRequest(

        val name: String,

        val description: String? = null,

        val location: String,

        val city: String,

        val address: String? = null,

        val latitude: Double? = null,

        val longitude: Double? = null,

        @SerializedName("sports_types")
        val sportsTypes: List<String> = emptyList(),

        val amenities: List<String> = emptyList(),

        @SerializedName("image_urls")
        val imageUrls: List<String> = emptyList(),

        val price: Double,

    //    @SerializedName("opening_time")
    //    val openingTime: String,
    //
    //    @SerializedName("closing_time")
    //    val closingTime: String,

        val status: String = "ACTIVE"
    )

    data class UpdateTurfRequest(

        val name: String? = null,

        val description: String? = null,

        val location: String? = null,

        val city: String? = null,

        val address: String? = null,

        val latitude: Double? = null,

        val longitude: Double? = null,

        @SerializedName("sports_types")
        val sportsTypes: List<String>? = null,

        val amenities: List<String>? = null,

        @SerializedName("image_urls")
        val imageUrls: List<String>? = null,

        val price: Double? = null,

        @SerializedName("opening_time")
        val openingTime: String? = null,

        @SerializedName("closing_time")
        val closingTime: String? = null,

        val status: String? = null
    )