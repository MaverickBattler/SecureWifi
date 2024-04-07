package com.practice.securewifi.app.initialization.di

import com.practice.securewifi.app.initialization.interactor.InitializationInteractor
import com.practice.securewifi.app.initialization.interactor.LoadPasswordsListsFromAssetsInteractor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val mainModule = module {
    factory {
        InitializationInteractor(
            loadPasswordsListsFromAssetsInteractor = get()
        )
    }

    factory {
        LoadPasswordsListsFromAssetsInteractor(
            applicationContext = androidApplication(),
            passwordsListsRepository = get(),
            passwordsListFixedPasswordsRepository = get()
        )
    }
}