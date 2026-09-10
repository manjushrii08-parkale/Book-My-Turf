package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.slot.Slot
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.data.remote.RetrofitClient
import com.example.bookmyturf.data.model.profile.UserProfile
import com.example.bookmyturf.data.model.profile.UpdateProfileRequest
import android.util.Log
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
    // =========================================================
// GET LOGGED-IN USER PROFILE
// =========================================================

    suspend fun getUserProfile(): Result<UserProfile> {

        return try {

            val response =
                turfApi.getUserProfile()

            if (response.isSuccessful) {

                val body =
                    response.body()

                if (body?.success == true) {

                    val user =
                        body.data?.user

                    if (user != null) {

                        Result.success(user)

                    } else {

                        Result.failure(
                            Exception(
                                "User profile data not found."
                            )
                        )
                    }

                } else {

                    Result.failure(
                        Exception(
                            body?.message
                                ?: "Unable to load profile."
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
// UPDATE USER PROFILE
// =========================================================

    suspend fun updateUserProfile(
        name: String,
        phone: String?,
        dateOfBirth: String?
    ): Result<UserProfile> {

        return try {

            val request = UpdateProfileRequest(
                name = name,
                phone = phone,
                dateOfBirth = dateOfBirth
            )

            Log.d(
                "PROFILE_UPDATE",
                "================================"
            )

            Log.d(
                "PROFILE_UPDATE",
                "PUT /api/v1/user/profile"
            )

            Log.d(
                "PROFILE_UPDATE",
                "name = $name"
            )

            Log.d(
                "PROFILE_UPDATE",
                "phone = $phone"
            )

            Log.d(
                "PROFILE_UPDATE",
                "date_of_birth = $dateOfBirth"
            )

            Log.d(
                "PROFILE_UPDATE",
                "================================"
            )

            val response =
                turfApi.updateUserProfile(request)

            Log.d(
                "PROFILE_UPDATE",
                "HTTP code = ${response.code()}"
            )

            if (response.isSuccessful) {

                val body =
                    response.body()

                Log.d(
                    "PROFILE_UPDATE",
                    "Success body = $body"
                )

                if (body?.success == true) {

                    val user =
                        body.data?.user

                    if (user != null) {

                        Log.d(
                            "PROFILE_UPDATE",
                            "Profile updated successfully"
                        )

                        Result.success(user)

                    } else {

                        Result.failure(
                            Exception(
                                "Updated profile data not found."
                            )
                        )
                    }

                } else {

                    Result.failure(
                        Exception(
                            body?.message
                                ?: "Profile update failed."
                        )
                    )
                }

            } else {

                val errorBody =
                    response.errorBody()
                        ?.string()

                Log.e(
                    "PROFILE_UPDATE",
                    "HTTP error = ${response.code()}"
                )

                Log.e(
                    "PROFILE_UPDATE",
                    "Error body = $errorBody"
                )

                Result.failure(
                    Exception(
                        when {

                            !errorBody.isNullOrBlank() ->
                                errorBody

                            response.code() == 401 ->
                                "Authentication failed. Please login again."

                            response.code() == 422 ->
                                "Validation failed. Check the entered details."

                            else ->
                                "Server error: ${response.code()}"
                        }
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "PROFILE_UPDATE",
                "Exception while updating profile",
                e
            )

            Result.failure(e)
        }
    }
}