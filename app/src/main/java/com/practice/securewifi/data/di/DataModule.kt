package com.practice.securewifi.data.di

import com.practice.securewifi.data.database.WifiSafetyDatabase
import com.practice.securewifi.data.repository.PasswordListsRepository
import com.practice.securewifi.data.repository.TriedPasswordsRepository
import com.practice.securewifi.data.repository.WifiCheckResultRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single { WifiSafetyDatabase.getInstance(context = androidApplication()) }

    single { get<WifiSafetyDatabase>().passwordListPasswordDao }
    single { get<WifiSafetyDatabase>().passwordListDao }
    single { get<WifiSafetyDatabase>().triedPasswordsDao }
    single { get<WifiSafetyDatabase>().wifiCheckResultDao }

    factory {
        PasswordListsRepository(
            passwordListPasswordDao = get(),
            passwordListDao = get()
        )
    }

    factory {
        TriedPasswordsRepository(triedPasswordsDao = get())
    }

    factory {
        WifiCheckResultRepository(wifiCheckResultDao = get())
    }
}