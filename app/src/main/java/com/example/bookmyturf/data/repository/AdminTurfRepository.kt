package com.example.bookmyturf.data.repository
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest
import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest

import com.example.bookmyturf.data.remote.ApiService

class AdminTurfRepository(
    private val apiService: ApiService
) {

    // =========================================================
    // TURFS
    // =========================================================

    // GET ALL TURFS

    suspend fun getTurfs(
        token: String
    ): TurfListResponse {

        return apiService.getAdminTurfs(
            authorization = "Bearer $token"
        )
    }


    // GET SINGLE TURF

    suspend fun getTurf(
        token: String,
        turfId: Int
    ): TurfResponse {

        return apiService.getAdminTurf(
            authorization = "Bearer $token",
            id = turfId
        )
    }


    // CREATE TURF

    suspend fun createTurf(
        token: String,
        request: CreateTurfRequest
    ): TurfResponse {

        return apiService.createTurf(
            authorization = "Bearer $token",
            request = request
        )
    }


    // UPDATE TURF

    suspend fun updateTurf(
        token: String,
        turfId: Int,
        request: UpdateTurfRequest
    ): TurfResponse {

        return apiService.updateTurf(
            authorization = "Bearer $token",
            id = turfId,
            request = request
        )
    }


    // DELETE TURF

    suspend fun deleteTurf(
        token: String,
        turfId: Int
    ) {

        apiService.deleteTurf(
            authorization = "Bearer $token",
            id = turfId
        )
    }


    // =========================================================
    // TURF SLOTS
    // =========================================================

    // GET SLOTS

    suspend fun getSlots(
        token: String,
        turfId: Int
    ): SlotsResponse {

        return apiService.getSlots(
            authorization = "Bearer $token",
            turfId = turfId
        )
    }


    // CREATE SLOT

    suspend fun createSlot(
        token: String,
        turfId: Int,
        request: CreateSlotRequest
    ): SlotResponse {

        return apiService.createSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            request = request
        )
    }


    // UPDATE SLOT

    suspend fun updateSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotRequest
    ): SlotResponse {

        return apiService.updateSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId,
            request = request
        )
    }


    // DELETE SLOT

    suspend fun deleteSlot(
        token: String,
        turfId: Int,
        slotId: Int
    ) {

        apiService.deleteSlot(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId
        )
    }


    // ACTIVATE / DEACTIVATE SLOT

    suspend fun updateSlotStatus(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotStatusRequest
    ): SlotResponse {

        return apiService.updateSlotStatus(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId,
            request = request
        )
    }
}