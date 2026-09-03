package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.favorite.Favorite
import com.example.bookmyturf.data.remote.RetrofitClient

class FavoriteRepository {

    // =========================================================
    // RETROFIT API
    // =========================================================

    private val favoriteApi = RetrofitClient.api


    // =========================================================
    // GET ALL FAVORITES
    // =========================================================

    suspend fun getFavorites(
        token: String
    ): Result<List<Favorite>> {

        return try {

            val response = favoriteApi.getFavorites(
                authorization = "Bearer $token"
            )

            if (response.success) {

                Result.success(
                    response.data?.favorites ?: emptyList()
                )

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to load favorites."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // ADD FAVORITE
    // =========================================================

    suspend fun addFavorite(
        token: String,
        turfId: Int
    ): Result<Favorite> {

        return try {

            val response = favoriteApi.addFavorite(
                authorization = "Bearer $token",
                turfId = turfId
            )

            if (response.success) {

                val favorite = response.data?.favorite

                if (favorite != null) {

                    Result.success(favorite)

                } else {

                    Result.failure(
                        Exception("Favorite data not found.")
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to add favorite."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // REMOVE FAVORITE
    // =========================================================

    suspend fun removeFavorite(
        token: String,
        turfId: Int
    ): Result<Boolean> {

        return try {

            val response = favoriteApi.removeFavorite(
                authorization = "Bearer $token",
                turfId = turfId
            )

            if (response.success) {

                Result.success(true)

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to remove favorite."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // CHECK FAVORITE STATUS
    // =========================================================

    suspend fun checkFavorite(
        token: String,
        turfId: Int
    ): Result<Boolean> {

        return try {

            val response = favoriteApi.checkFavorite(
                authorization = "Bearer $token",
                turfId = turfId
            )

            if (response.success) {

                Result.success(
                    response.data?.is_favorite ?: false
                )

            } else {

                Result.failure(
                    Exception(
                        response.message.ifBlank {
                            "Unable to check favorite status."
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
