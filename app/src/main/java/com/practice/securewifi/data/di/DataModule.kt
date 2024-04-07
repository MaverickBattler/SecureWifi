package com.practice.securewifi.data.di

import com.practice.securewifi.data.database.WifiSafetyDatabase
import com.practice.securewifi.data.password_lists.repository.PasswordListDynamicPasswordsInfoRepository
import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import com.practice.securewifi.data.repository.SelectedWifiesRepository
import com.practice.securewifi.data.repository.TriedPasswordsRepository
import com.practice.securewifi.data.repository.WifiCheckResultRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single { WifiSafetyDatabase.getInstance(context = androidApplication()) }

    single { get<WifiSafetyDatabase>().passwordListFixedPasswordDao }
    single { get<WifiSafetyDatabase>().passwordListDao }
    single { get<WifiSafetyDatabase>().triedPasswordsDao }
    single { get<WifiSafetyDatabase>().wifiCheckResultDao }
    single { get<WifiSafetyDatabase>().selectedWifiDao }
    single { get<WifiSafetyDatabase>().personInfoDao }
    single { get<WifiSafetyDatabase>().placeNameDao }

    factory {
        PasswordListsRepository(
            passwordListFixedPasswordDao = get(),
            passwordListDao = get(),
            placeNameDao = get(),
            personInfoDao = get()
        )
    }

    factory {
        TriedPasswordsRepository(triedPasswordsDao = get())
    }

    factory {
        WifiCheckResultRepository(wifiCheckResultDao = get())
    }

    factory {
        SelectedWifiesRepository(selectedWifiDao = get())
    }

    factory {
        PasswordListDynamicPasswordsInfoRepository(
            personInfoDao = get(),
            placeNameDao = get()
        )
    }

    factory {
        PasswordListFixedPasswordsRepository(
            passwordListFixedPasswordDao = get()
        )
    }
}