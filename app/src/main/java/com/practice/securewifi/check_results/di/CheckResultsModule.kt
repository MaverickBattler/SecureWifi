package com.practice.securewifi.check_results.di

import com.practice.securewifi.check_results.interactor.CheckResultsInteractor
import com.practice.securewifi.check_results.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check_results.viewmodel.ResultsViewModel
import com.practice.securewifi.check_results.viewmodel.WifiAttackResultsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkResultsModule = module {
    viewModel { params ->
        WifiAttackResultsViewModel(
            wifiSsid = params.get(),
            triedPasswordsInteractor = get()
        )
    }

    factory {
        TriedPasswordsInteractor(
            triedPasswordsRepository = get(),
            wifiCheckResultRepository = get()
        )
    }

    factory {
        CheckResultsInteractor(
            wifiCheckResultRepository = get(),
            triedPasswordsRepository = get()
        )
    }

    viewModel {
        ResultsViewModel(checkResultsInteractor = get())
    }
}