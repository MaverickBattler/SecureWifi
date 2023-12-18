package com.practice.securewifi.connect.di

import com.practice.securewifi.connect.interactor.TriedPasswordsInteractor
import com.practice.securewifi.connect.interactor.WifiCheckResultInteractor
import org.koin.dsl.module

val connectModule = module {
    factory {
        TriedPasswordsInteractor(triedPasswordsRepository = get())
    }

    factory {
        WifiCheckResultInteractor(wifiCheckResultRepository = get())
    }
}