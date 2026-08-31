package com.example.bookmyturf.data.remote

import android.content.Context
import com.example.bookmyturf.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
) : Interceptor {

    private val sessionManager =
        SessionManager(context)

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val token =
            sessionManager.getToken()

        val request =
            chain.request()
                .newBuilder()
                .apply {

                    if (!token.isNullOrBlank()) {

                        addHeader(
                            "Authorization",
                            "Bearer $token"
                        )
                    }

                    addHeader(
                        "Accept",
                        "application/json"
                    )
                }
                .build()

        return chain.proceed(request)
    }
}