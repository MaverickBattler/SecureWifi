package com.practice.securewifi.app

import android.app.Application
import com.practice.securewifi.app.initialization.di.mainModule
import com.practice.securewifi.check_results.di.checkResultsModule
import com.practice.securewifi.check.di.connectModule
import com.practice.securewifi.custom_list.di.customListModule
import com.practice.securewifi.data.di.dataModule
import com.practice.securewifi.scan.di.scanModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        initKoin()
        super.onCreate()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    mainModule,
                    connectModule,
                    dataModule,
                    customListModule,
                    checkResultsModule,
                    scanModule
                )
            )
        }
    }
}