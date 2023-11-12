package com.practice.securewifi.app

import android.app.Application
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()
    }
}