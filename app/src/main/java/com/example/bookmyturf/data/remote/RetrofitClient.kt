package com.example.bookmyturf.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL =
        "http://192.168.1.224:8000/"
        ///"http://192.168.1.198:8000/"

//    📱 Physical phone → 192.168.1.198:8000
//    🖥️ Android Emulator → 10.0.2.2:8000
    private lateinit var appContext: Context

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private lateinit var client: OkHttpClient

    private lateinit var retrofit: Retrofit


    // =========================================================
    // INITIALIZE
    // =========================================================

    fun initialize(context: Context) {

        appContext =
            context.applicationContext

        client =
            OkHttpClient.Builder()

                .connectTimeout(
                    15,
                    TimeUnit.SECONDS
                )

                .readTimeout(
                    30,
                    TimeUnit.SECONDS
                )

                .writeTimeout(
                    30,
                    TimeUnit.SECONDS
                )

                .addInterceptor(
                    AuthInterceptor(appContext)
                )

                .addInterceptor(
                    loggingInterceptor
                )

                .build()

        retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
    }


    // =========================================================
    // EXISTING API
    // =========================================================

    val api: ApiService
        get() {

            checkInitialized()

            return retrofit.create(
                ApiService::class.java
            )
        }


    // =========================================================
    // USER TURF API
    // =========================================================

    val turfApi: TurfApi
        get() {

            checkInitialized()

            return retrofit.create(
                TurfApi::class.java
            )
        }
// =========================================================
// PAYMENT API
// =========================================================

    val paymentApi: PaymentApiService
        get() {

            checkInitialized()

            return retrofit.create(
                PaymentApiService::class.java
            )
        }

    // =========================================================
    // CHECK INITIALIZED
    // =========================================================

    private fun checkInitialized() {

        check(
            ::retrofit.isInitialized
        ) {
            "RetrofitClient.initialize(context) must be called first."
        }
    }
}