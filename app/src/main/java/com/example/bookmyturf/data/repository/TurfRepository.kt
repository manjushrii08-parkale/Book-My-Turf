package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.remote.RetrofitClient

class TurfRepository {

    private val turfApi = RetrofitClient.turfApi

    // =========================================================
    // GET ALL ACTIVE TURFS
    // =========================================================

    suspend fun getTurfs(): Result<List<Turf>> {

        return try {

            val response = turfApi.getTurfs()

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true) {

                    Result.success(
                        body.data?.turfs ?: emptyList()
                    )

                } else {

                    Result.failure(
                        Exception(
                            body?.message ?: "Unable to load turfs."
                        )
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        "Server error: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // =========================================================
    // GET SINGLE TURF BY ID
    // =========================================================

    suspend fun getTurfById(
        id: Int
    ): Result<Turf> {

        return try {

            val response =
                turfApi.getTurfById(id)

            if (response.isSuccessful) {

                val body =
                    response.body()

                if (body?.success == true) {

                    val turf =
                        body.data?.turf

                    if (turf != null) {

                        Result.success(turf)

                    } else {

                        Result.failure(
                            Exception(
                                "Turf data not found."
                            )
                        )
                    }

                } else {

                    Result.failure(
                        Exception(
                            body?.message
                                ?: "Unable to load turf."
                        )
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        "Server error: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}