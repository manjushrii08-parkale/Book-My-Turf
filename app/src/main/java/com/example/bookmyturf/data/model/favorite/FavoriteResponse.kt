package com.example.bookmyturf.data.model.favorite

import com.example.bookmyturf.data.model.turf.Turf

data class FavoriteResponse(
    val success: Boolean,
    val message: String,
    val data: FavoriteData?
)

data class FavoriteData(
    val favorite: Favorite?
)

data class Favorite(
    val id: Int,
    val user_id: Int,
    val turf_id: Int,
    val created_at: String?,
    val updated_at: String?,
    val turf: Turf?
)

data class FavoriteListResponse(
    val success: Boolean,
    val message: String,
    val data: FavoriteListData? )
data class FavoriteListData(
    val favorites: List<Favorite>?
)
data class FavoriteCheckResponse(
    val success: Boolean,
    val message: String,
    val data: FavoriteCheckData? )

data class FavoriteCheckData(
    val is_favorite: Boolean
)