package com.example.bookmyturf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.bookmyturf.data.model.AdminDashboardResponse
import com.example.bookmyturf.data.model.AdminSubscriptionResponse

import com.example.bookmyturf.data.model.slot.CreateSlotRequest
import com.example.bookmyturf.data.model.slot.SlotResponse
import com.example.bookmyturf.data.model.slot.SlotsResponse
import com.example.bookmyturf.data.model.slot.UpdateSlotRequest
import com.example.bookmyturf.data.model.slot.UpdateSlotStatusRequest

import com.example.bookmyturf.data.model.turf.CreateTurfRequest
import com.example.bookmyturf.data.model.turf.TurfListResponse
import com.example.bookmyturf.data.model.turf.TurfResponse
import com.example.bookmyturf.data.model.turf.UpdateTurfRequest

import com.example.bookmyturf.data.remote.RazorpaySubscriptionOrderResponse
import com.example.bookmyturf.data.repository.AdminRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import okhttp3.MultipartBody

import org.json.JSONObject

import retrofit2.HttpException


class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {


    // =========================================================
    // LOADING
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // ERROR
    // =========================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // =========================================================
    // SUCCESS MESSAGE
    // =========================================================

    private val _successMessage =
        MutableStateFlow<String?>(null)

    val successMessage: StateFlow<String?> =
        _successMessage.asStateFlow()


    // =========================================================
    // DASHBOARD
    // =========================================================

    private val _dashboard =
        MutableStateFlow<AdminDashboardResponse?>(null)

    val dashboard: StateFlow<AdminDashboardResponse?> =
        _dashboard.asStateFlow()


    // =========================================================
    // SUBSCRIPTION
    // =========================================================

    private val _subscription =
        MutableStateFlow<AdminSubscriptionResponse?>(null)

    val subscription: StateFlow<AdminSubscriptionResponse?> =
        _subscription.asStateFlow()


    // =========================================================
    // RAZORPAY ORDER
    // =========================================================

    private val _razorpayOrder =
        MutableStateFlow<RazorpaySubscriptionOrderResponse?>(null)

    val razorpayOrder: StateFlow<RazorpaySubscriptionOrderResponse?> =
        _razorpayOrder.asStateFlow()


    // =========================================================
    // TURFS
    // =========================================================

    private val _turfs =
        MutableStateFlow<TurfListResponse?>(null)

    val turfs: StateFlow<TurfListResponse?> =
        _turfs.asStateFlow()


    // =========================================================
    // SINGLE TURF
    // =========================================================

    private val _turf =
        MutableStateFlow<TurfResponse?>(null)

    val turf: StateFlow<TurfResponse?> =
        _turf.asStateFlow()


    // =========================================================
    // SLOTS
    // =========================================================

    private val _slots =
        MutableStateFlow<SlotsResponse?>(null)

    val slots: StateFlow<SlotsResponse?> =
        _slots.asStateFlow()


    // =========================================================
    // SINGLE SLOT
    // =========================================================

    private val _slot =
        MutableStateFlow<SlotResponse?>(null)

    val slot: StateFlow<SlotResponse?> =
        _slot.asStateFlow()


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {
        _error.value = null
    }


    // =========================================================
    // CLEAR SUCCESS MESSAGE
    // =========================================================

    fun clearSuccessMessage() {
        _successMessage.value = null
    }


    // =========================================================
    // CLEAR RAZORPAY ORDER
    // =========================================================

    fun clearRazorpayOrder() {
        _razorpayOrder.value = null
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    fun loadDashboard(
        token: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getDashboard(token)

                if (response.success) {

                    _dashboard.value =
                        response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load dashboard."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load dashboard."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // LOAD SUBSCRIPTION
    // =========================================================

    fun loadSubscriptionStatus(
        token: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getSubscriptionStatus(token)

                if (response.success) {

                    _subscription.value =
                        response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load subscription."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load subscription."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CHECK SUBSCRIPTION
    // =========================================================

    fun checkSubscription(
        token: String,
        onActive: () -> Unit,
        onNotActive: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getSubscriptionStatus(token)

                if (response.success) {

                    _subscription.value =
                        response

                    val status =
                        response.data
                            ?.subscription
                            ?.status
                            ?.uppercase()

                    Log.d(
                        "SUBSCRIPTION",
                        "Status = $status"
                    )

                    if (status == "ACTIVE") {

                        onActive()

                    } else {

                        onNotActive()
                    }

                } else {

                    val message =
                        response.message.ifBlank {
                            "Unable to check subscription."
                        }

                    _error.value =
                        message

                    onError(message)
                }

            } catch (e: HttpException) {

                val message =
                    parseHttpError(e)

                _error.value =
                    message

                onError(message)

            } catch (e: Exception) {

                val message =
                    e.message
                        ?: "Failed to check subscription."

                _error.value =
                    message

                onError(message)

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // START FREE TRIAL
    // =========================================================

    fun startFreeTrial(
        token: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                Log.d(
                    "FREE_TRIAL",
                    "Starting free trial..."
                )

                val response =
                    repository.startFreeTrial(token)

                Log.d(
                    "FREE_TRIAL",
                    "success = ${response.success}"
                )

                Log.d(
                    "FREE_TRIAL",
                    "message = ${response.message}"
                )

                if (response.success) {

                    _subscription.value =
                        response

                    _successMessage.value =
                        response.message

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to start free trial."
                        }
                }

            } catch (e: HttpException) {

                val message =
                    parseHttpError(e)

                Log.e(
                    "FREE_TRIAL",
                    "HTTP ${e.code()}: $message",
                    e
                )

                _error.value =
                    message

            } catch (e: Exception) {

                Log.e(
                    "FREE_TRIAL",
                    "Exception while starting free trial",
                    e
                )

                _error.value =
                    e.message
                        ?: "Failed to start free trial."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CREATE RAZORPAY SUBSCRIPTION ORDER
    // =========================================================

    fun createSubscriptionOrder(
        token: String,
        plan: String,
        onOrderCreated: (RazorpaySubscriptionOrderResponse) -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null
            _razorpayOrder.value = null

            try {

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Creating order for plan = $plan"
                )

                val response =
                    repository.createSubscriptionOrder(
                        token = token,
                        plan = plan
                    )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "success = ${response.success}"
                )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "message = ${response.message}"
                )

                if (
                    response.success &&
                    response.data != null
                ) {

                    _razorpayOrder.value =
                        response

                    onOrderCreated(response)

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Unable to create payment order."
                        }
                }

            } catch (e: HttpException) {

                val message =
                    parseHttpError(e)

                Log.e(
                    "RAZORPAY_SUBSCRIPTION",
                    "HTTP ${e.code()}: $message",
                    e
                )

                _error.value =
                    message

            } catch (e: Exception) {

                Log.e(
                    "RAZORPAY_SUBSCRIPTION",
                    "Create order failed",
                    e
                )

                _error.value =
                    e.message
                        ?: "Unable to create payment order."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // VERIFY RAZORPAY SUBSCRIPTION PAYMENT
    // =========================================================

    fun verifySubscriptionPayment(
        token: String,
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Verifying payment..."
                )

                val response =
                    repository.verifySubscriptionPayment(
                        token = token,
                        razorpayOrderId = razorpayOrderId,
                        razorpayPaymentId = razorpayPaymentId,
                        razorpaySignature = razorpaySignature
                    )

                Log.d(
                    "RAZORPAY_SUBSCRIPTION",
                    "Verification success = ${response.success}"
                )

                if (response.success) {

                    _subscription.value =
                        response

                    _successMessage.value =
                        response.message

                    _razorpayOrder.value =
                        null

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Payment verification failed."
                        }
                }

            } catch (e: HttpException) {

                val message =
                    parseHttpError(e)

                Log.e(
                    "RAZORPAY_SUBSCRIPTION",
                    "HTTP ${e.code()}: $message",
                    e
                )

                _error.value =
                    message

            } catch (e: Exception) {

                Log.e(
                    "RAZORPAY_SUBSCRIPTION",
                    "Payment verification failed",
                    e
                )

                _error.value =
                    e.message
                        ?: "Payment verification failed."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // LOAD TURFS
    // =========================================================

    fun loadTurfs(
        token: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getTurfs(token)

                if (response.success) {

                    _turfs.value =
                        response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load turfs."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load turfs."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // LOAD SINGLE TURF
    // =========================================================

    fun loadTurf(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getTurf(
                        token = token,
                        turfId = turfId
                    )

                if (response.success) {

                    _turf.value =
                        response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load turf."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load turf."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CREATE TURF
    // =========================================================

    fun createTurf(
        token: String,
        request: CreateTurfRequest,
        onSuccess: (TurfResponse) -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.createTurf(
                        token = token,
                        request = request
                    )

                if (response.success) {

                    _turf.value =
                        response

                    _successMessage.value =
                        response.message

                    onSuccess(response)

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to create turf."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to create turf."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPLOAD TURF IMAGES
    // =========================================================

    fun uploadTurfImages(
        token: String,
        turfId: Int,
        images: List<MultipartBody.Part>,
        onSuccess: (List<String>) -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.uploadTurfImages(
                        token = token,
                        turfId = turfId,
                        images = images
                    )

                if (response.success) {

                    onSuccess(
                        response.data.imageUrls
                    )

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to upload turf images."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to upload turf images."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPDATE TURF
    // =========================================================

    fun updateTurf(
        token: String,
        turfId: Int,
        request: UpdateTurfRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.updateTurf(
                        token = token,
                        turfId = turfId,
                        request = request
                    )

                if (response.success) {

                    _turf.value =
                        response

                    _successMessage.value =
                        response.message.ifBlank {
                            "Turf updated successfully."
                        }

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to update turf."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to update turf."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // DELETE TURF
    // =========================================================

    fun deleteTurf(
        token: String,
        turfId: Int,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                repository.deleteTurf(
                    token = token,
                    turfId = turfId
                )

                _successMessage.value =
                    "Turf deleted successfully."

                onSuccess()

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to delete turf."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // LOAD SLOTS
    // =========================================================

    fun loadSlots(
        token: String,
        turfId: Int
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                val response =
                    repository.getSlots(
                        token = token,
                        turfId = turfId
                    )

                if (response.success) {

                    _slots.value =
                        response

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to load slots."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to load slots."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // CREATE SLOT
    // =========================================================

    fun createSlot(
        token: String,
        turfId: Int,
        request: CreateSlotRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.createSlot(
                        token = token,
                        turfId = turfId,
                        request = request
                    )

                if (response.success) {

                    _slot.value =
                        response

                    _successMessage.value =
                        response.message

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to create slot."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to create slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPDATE SLOT
    // =========================================================

    fun updateSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.updateSlot(
                        token = token,
                        turfId = turfId,
                        slotId = slotId,
                        request = request
                    )

                if (response.success) {

                    _slot.value =
                        response

                    _successMessage.value =
                        response.message

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to update slot."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to update slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // DELETE SLOT
    // =========================================================

    fun deleteSlot(
        token: String,
        turfId: Int,
        slotId: Int,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                repository.deleteSlot(
                    token = token,
                    turfId = turfId,
                    slotId = slotId
                )

                _successMessage.value =
                    "Slot deleted successfully."

                onSuccess()

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to delete slot."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // UPDATE SLOT STATUS
    // =========================================================

    fun updateSlotStatus(
        token: String,
        turfId: Int,
        slotId: Int,
        request: UpdateSlotStatusRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null
            _successMessage.value = null

            try {

                val response =
                    repository.updateSlotStatus(
                        token = token,
                        turfId = turfId,
                        slotId = slotId,
                        request = request
                    )

                if (response.success) {

                    _slot.value =
                        response

                    _successMessage.value =
                        response.message

                    onSuccess()

                } else {

                    _error.value =
                        response.message.ifBlank {
                            "Failed to update slot status."
                        }
                }

            } catch (e: HttpException) {

                _error.value =
                    parseHttpError(e)

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Failed to update slot status."

            } finally {

                _isLoading.value = false
            }
        }
    }


    // =========================================================
    // PARSE LARAVEL HTTP ERROR
    // =========================================================

    private fun parseHttpError(
        exception: HttpException
    ): String {

        val code =
            exception.code()

        val body =
            try {

                exception.response()
                    ?.errorBody()
                    ?.string()

            } catch (_: Exception) {

                null
            }

        Log.e(
            "LARAVEL_API",
            "HTTP $code"
        )

        Log.e(
            "LARAVEL_API",
            "Body = $body"
        )

        if (body.isNullOrBlank()) {

            return when (code) {

                400 ->
                    "Bad request."

                401 ->
                    "Authentication failed. Please login again."

                403 ->
                    "You are not authorized."

                404 ->
                    "Resource not found."

                422 ->
                    "Validation failed."

                429 ->
                    "Too many requests."

                500 ->
                    "Laravel server error."

                else ->
                    "Request failed ($code)."
            }
        }


        return try {

            val json =
                JSONObject(body)

            val message =
                json.optString(
                    "message",
                    ""
                )

            val errors =
                json.optJSONObject("errors")


            if (
                errors != null &&
                errors.length() > 0
            ) {

                val messages =
                    mutableListOf<String>()

                val keys =
                    errors.keys()

                while (keys.hasNext()) {

                    val key =
                        keys.next()

                    val value =
                        errors.opt(key)

                    when (value) {

                        is org.json.JSONArray -> {

                            for (
                            i in 0 until value.length()
                            ) {

                                val errorMessage =
                                    value.optString(i)

                                if (
                                    errorMessage.isNotBlank()
                                ) {

                                    messages.add(
                                        formatValidationField(
                                            key
                                        ) +
                                                ": " +
                                                errorMessage
                                    )
                                }
                            }
                        }

                        else -> {

                            val errorMessage =
                                value?.toString()
                                    ?: ""

                            if (
                                errorMessage.isNotBlank()
                            ) {

                                messages.add(
                                    formatValidationField(
                                        key
                                    ) +
                                            ": " +
                                            errorMessage
                                )
                            }
                        }
                    }
                }

                if (messages.isNotEmpty()) {

                    return messages.joinToString(
                        separator = "\n"
                    )
                }
            }


            if (message.isNotBlank()) {

                return message
            }


            when (code) {

                400 ->
                    "Bad request."

                401 ->
                    "Authentication failed. Please login again."

                403 ->
                    "You are not authorized."

                404 ->
                    "Resource not found."

                422 ->
                    "Validation failed."

                429 ->
                    "Too many requests."

                500 ->
                    "Laravel server error."

                else ->
                    "Request failed ($code)."
            }

        } catch (e: Exception) {

            Log.e(
                "LARAVEL_API",
                "Could not parse error response",
                e
            )

            "Request failed ($code)."
        }
    }


    // =========================================================
    // FORMAT VALIDATION FIELD
    // =========================================================

    private fun formatValidationField(
        field: String
    ): String {

        return field
            .replace("_", " ")
            .replaceFirstChar {
                it.uppercase()
            }
    }
}

