package com.practice.securewifi.check.di

import com.practice.securewifi.check.interactor.PasswordListsInteractor
import com.practice.securewifi.check.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check.interactor.WifiCheckResultInteractor
import com.practice.securewifi.check.passwords_lists_selection.interactor.PasswordsListsInteractor
import com.practice.securewifi.check.passwords_lists_selection.mapper.PasswordsListsModelMapper
import com.practice.securewifi.check.passwords_lists_selection.viewmodel.PasswordsListSelectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val connectModule = module {
    factory {
        TriedPasswordsInteractor(triedPasswordsRepository = get())
    }

    factory {
        WifiCheckResultInteractor(wifiCheckResultRepository = get())
    }

    factory {
        PasswordsListsInteractor(
            passwordListsRepository = get(),
            passwordsListsModelMapper = get()
        )
    }

    viewModel {
        PasswordsListSelectionViewModel(
            passwordsListsInteractor = get()
        )
    }

    factory {
        PasswordsListsModelMapper()
    }

    factory {
        PasswordListsInteractor(
            passwordListsRepository = get()
        )
    }
}