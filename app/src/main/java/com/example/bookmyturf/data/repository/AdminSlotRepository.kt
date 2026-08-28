package com.example.bookmyturf.data.repository

import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest
import com.example.bookmyturf.data.remote.ApiService


class AdminSlotRepository(
    private val api: ApiService
) {

    // =========================================================
    // GET ALL SLOTS
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


    // =========================================================
    // CREATE SLOT
    // =========================================================

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


    // =========================================================
    // UPDATE SLOT
    // =========================================================

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


    // =========================================================
    // DELETE SLOT
    // =========================================================

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


    // =========================================================
    // UPDATE SLOT STATUS
    // =========================================================

    suspend fun updateSlotStatus(
        token: String,
        turfId: Int,
        slotId: Int,
        status: String
    ): SlotResponse {

        return api.updateSlotStatus(
            authorization = "Bearer $token",
            turfId = turfId,
            slotId = slotId,
            request = UpdateSlotStatusRequest(
                status = status
            )
        )
    }
}

