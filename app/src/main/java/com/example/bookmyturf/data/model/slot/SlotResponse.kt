
package com.example.bookmyturf.data.model.slot

import com.example.bookmyturf.data.model.turf.Turf


// =============================================================
// SINGLE SLOT RESPONSE
// =============================================================

data class SlotResponse(

    val success: Boolean,

    val message: String,

    val data: SlotData? = null
)


// =============================================================
// SINGLE SLOT DATA
// =============================================================

data class SlotData(

    val slot: Slot
)


// =============================================================
// ALL SLOTS RESPONSE
// =============================================================

data class SlotsResponse(

    val success: Boolean,

    val message: String,

    val data: SlotsData? = null
)


// =============================================================
// ALL SLOTS DATA
// Laravel returns:
// {
//     "turf": {...},
//     "slots": [...]
// }
// =============================================================

data class SlotsData(

    val turf: Turf? = null,

    val slots: List<Slot> = emptyList()
)

