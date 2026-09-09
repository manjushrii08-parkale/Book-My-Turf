package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.remote.RetrofitClient

class TurfRepository {

    private val turfApi =
        RetrofitClient.turfApi


    // =========================================================
    // GET ALL ACTIVE TURFS
    // =========================================================

    suspend fun getTurfs(): Result<List<Turf>> {

        return try {

            val response =
                turfApi.getTurfs()

            if (response.isSuccessful) {

                val body =
                    response.body()

                if (body?.success == true) {

                    Result.success(
                        body.data?.turfs
                            ?: emptyList()
                    )

                } else {

                    Result.failure(
                        Exception(
                            body?.message
                                ?: "Unable to load turfs."
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
        turfId: Int
    ): Result<Turf> {

        return try {

            val response =
                turfApi.getTurfById(turfId)

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
                                ?: "Unable to load turf details."
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
    // GET DATE-WISE SLOTS FOR TURF
    // =========================================================

    suspend fun getSlots(
        turfId: Int,
        bookingDate: String
    ): Result<List<Slot>> {

        return try {

            val response =
                turfApi.getSlots(
                    turfId = turfId,
                    bookingDate = bookingDate
                )

            if (response.isSuccessful) {

                val body =
                    response.body()

                if (body?.success == true) {

                    Result.success(
                        body.data?.slots
                            ?: emptyList()
                    )

                } else {

                    Result.failure(
                        Exception(
                            body?.message
                                ?: "Unable to load slots."
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