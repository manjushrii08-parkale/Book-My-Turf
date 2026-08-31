package com.example.bookmyturf

import android.app.Application
import com.example.bookmyturf.data.remote.RetrofitClient

class BookMyTurfApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        RetrofitClient.initialize(this)
    }
}