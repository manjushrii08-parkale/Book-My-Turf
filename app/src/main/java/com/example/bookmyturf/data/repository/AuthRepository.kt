package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.LoginResponse
import com.example.bookmyturf.data.model.MeResponse
import com.example.bookmyturf.data.model.OtpResponse
import com.example.bookmyturf.data.model.SendOtpRequest
import com.example.bookmyturf.data.model.VerifyOtpRequest
import com.example.bookmyturf.data.remote.ApiService

class AuthRepository(
    private val apiService: ApiService
) {

    // ==================================================
    // SEND EMAIL OTP
    // ==================================================

    suspend fun sendOtp(
        email: String,
        role: String
    ): OtpResponse {

        return apiService.sendOtp(
            SendOtpRequest(
                email = email,
                role = role
            )
        )
    }


    // ==================================================
    // VERIFY EMAIL OTP
    // ==================================================

    suspend fun verifyOtp(
        email: String,
        otp: String,
        role: String
    ): LoginResponse {

        return apiService.verifyOtp(
            VerifyOtpRequest(
                email = email,
                otp = otp,
                role = role
            )
        )
    }


    // ==================================================
    // GET CURRENT USER
    // ==================================================

    suspend fun getMe(
        token: String
    ): MeResponse {

        return apiService.getMe(
            authorization = "Bearer $token"
        )
    }


    // ==================================================
    // LOGOUT
    // ==================================================

    suspend fun logout(
        token: String
    ) {

        apiService.logout(
            authorization = "Bearer $token"
        )
    }
}