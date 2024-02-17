package com.practice.securewifi.check.di

import com.practice.securewifi.check.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check.interactor.WifiCheckResultInteractor
import org.koin.dsl.module

val connectModule = module {
    factory {
        TriedPasswordsInteractor(triedPasswordsRepository = get())
    }

    factory {
        WifiCheckResultInteractor(wifiCheckResultRepository = get())
    }
}