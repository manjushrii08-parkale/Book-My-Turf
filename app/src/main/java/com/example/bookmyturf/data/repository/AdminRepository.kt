package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.AdminDashboardResponse
import com.example.bookmyturf.data.model.AdminSubscriptionResponse

import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.model.turf.ImageUploadResponse
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest

import com.example.bookmyturf.data.remote.ApiService

import okhttp3.MultipartBody


class AdminRepository(
    private val api: ApiService
) {

    // =========================================================
    // DASHBOARD
    // =========================================================

    suspend fun getDashboard(
        token: String
    ): AdminDashboardResponse {

        return api.getAdminDashboard(
            authorization = "Bearer $token"
        )
    }


    // =========================================================
    // SUBSCRIPTION
    // =========================================================

    suspend fun getSubscriptionStatus(
        token: String
    ): AdminSubscriptionResponse {

        return api.getSubscriptionStatus(
            authorization = "Bearer $token"
        )
    }


    suspend fun startFreeTrial(
        token: String
    ): AdminSubscriptionResponse {

        return api.startFreeTrial(
            authorization = "Bearer $token"
        )
    }


    suspend fun choosePaidPlan(
        token: String
    ): AdminSubscriptionResponse {

        return api.choosePaidPlan(
            authorization = "Bearer $token"
        )
    }


    // =========================================================
    // TURF MANAGEMENT
    // =========================================================

    suspend fun getTurfs(
        token: String
    ): TurfListResponse {

        return api.getAdminTurfs(
            authorization = "Bearer $token"
        )
    }


    suspend fun getTurf(
        token: String,
        turfId: Int
    ): TurfResponse {

        return api.getAdminTurf(
            authorization = "Bearer $token",
            id = turfId
        )
    }


    suspend fun createTurf(
        token: String,
        request: CreateTurfRequest
    ): TurfResponse {

        return api.createTurf(
            authorization = "Bearer $token",
            request = request
        )
    }


    // =========================================================
    // IMAGE UPLOAD
    // =========================================================

    // =========================================================
// IMAGE UPLOAD
// =========================================================

    suspend fun uploadTurfImages(
        token: String,
        turfId: Int,
        images: List<MultipartBody.Part>
    ): ImageUploadResponse {

        val turfIdBody =
            turfId
                .toString()
                .toRequestBody("text/plain".toMediaType())

        return api.uploadTurfImages(
            authorization = "Bearer $token",
            turfId = turfIdBody,
            images = images
        )
    }


    suspend fun updateTurf(
        token: String,
        turfId: Int,
        request: UpdateTurfRequest
    ): TurfResponse {

        return api.updateTurf(
            authorization = "Bearer $token",
            id = turfId,
            request = request
        )
    }


    suspend fun deleteTurf(
        token: String,
        turfId: Int
    ) {

        api.deleteTurf(
            authorization = "Bearer $token",
            id = turfId
        )
    }


    // =========================================================
    // SLOT MANAGEMENT
    // =========================================================

    suspend fun getSlots(
        token: String,
        turfId: Int
    ): SlotsResponse {

        return api.getSlots(
            authorization = "Bearer $token",
            turfId = turfId
        )
    }


    suspend fun createSlot(
        token: String,
        turfId: Int,
        request: CreateSlotRequest
    ): SlotResponse {

        return api.createSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            request = request
        )
    }


    suspend fun updateSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotRequest
    ): SlotResponse {

        return api.updateSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId,
            request = request
        )
    }


    suspend fun deleteSlot(
        token: String,
        turfId: Int,
        slotId: Int
    ) {

        api.deleteSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId
        )
    }


    suspend fun updateSlotStatus(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotStatusRequest
    ): SlotResponse {

        return api.updateSlotStatus(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId,
            request = request
        )
    }
}
