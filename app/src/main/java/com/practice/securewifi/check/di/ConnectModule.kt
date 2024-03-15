package com.practice.securewifi.check.di

import com.practice.securewifi.check.interactor.PasswordListsInteractor
import com.practice.securewifi.check.interactor.SelectedPasswordListsInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesInteractor
import com.practice.securewifi.check.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check.interactor.WifiCheckResultInteractor
import com.practice.securewifi.check.passwords_lists_selection.interactor.PasswordsListsInteractor
import com.practice.securewifi.check.passwords_lists_selection.mapper.PasswordsListsModelMapper
import com.practice.securewifi.check.passwords_lists_selection.viewmodel.PasswordsListSelectionViewModel
import com.practice.securewifi.check.wifi_points_selection.interactor.SelectedWifiesListInteractor
import com.practice.securewifi.check.wifi_points_selection.interactor.WifiScanResultsListInteractor
import com.practice.securewifi.check.wifi_points_selection.mapper.WifiListStateMapper
import com.practice.securewifi.check.wifi_points_selection.repository.ScannedWifiesRepository
import com.practice.securewifi.check.wifi_points_selection.viewmodel.WifiPointsSelectionViewModel
import org.koin.android.ext.koin.androidApplication
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

    viewModel {
        WifiPointsSelectionViewModel(
            application = androidApplication(),
            selectedWifiesListInteractor = get(),
            wifiScanResultsListInteractor = get(),
            wifiListStateMapper = get()
        )
    }

    factory {
        SelectedWifiesListInteractor(
            selectedWifiesRepository = get()
        )
    }

    factory {
        WifiScanResultsListInteractor(
            scannedWifiesRepository = get()
        )
    }

    factory {
        ScannedWifiesRepository()
    }

    factory {
        WifiListStateMapper()
    }

    factory {
        SelectedWifiesInteractor(
            selectedWifiesRepository = get()
        )
    }

    factory {
        SelectedPasswordListsInteractor(
            passwordListsRepository = get()
        )
    }
}