package com.practice.securewifi.check.di

import com.practice.securewifi.check.interactor.PasswordListsInteractor
import com.practice.securewifi.check.interactor.SelectedPasswordListsInteractor
import com.practice.securewifi.check.interactor.SelectedPasswordListsPreviewInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesInteractor
import com.practice.securewifi.check.interactor.SelectedWifiesPreviewInteractor
import com.practice.securewifi.check.interactor.TriedPasswordsInteractor
import com.practice.securewifi.check.interactor.WifiCheckResultInteractor
import com.practice.securewifi.check.mapper.SelectedPasswordListsPreviewUiStateMapper
import com.practice.securewifi.check.mapper.SelectedWifiesPreviewUiStateMapper
import com.practice.securewifi.check.passwords_lists_selection.interactor.PasswordsListsInteractor
import com.practice.securewifi.check.passwords_lists_selection.mapper.PasswordsListModelsMapper
import com.practice.securewifi.check.passwords_lists_selection.viewmodel.PasswordsListSelectionViewModel
import com.practice.securewifi.check.viewmodel.ConnectViewModel
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
            passwordListFixedPasswordsRepository = get(),
            passwordsListModelsMapper = get()
        )
    }

    viewModel {
        PasswordsListSelectionViewModel(
            passwordsListsInteractor = get()
        )
    }

    factory {
        PasswordsListModelsMapper(
            application = androidApplication()
        )
    }

    factory {
        PasswordListsInteractor(
            passwordListFixedPasswordsRepository = get()
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

    factory {
        SelectedPasswordListsPreviewInteractor(
            passwordListsRepository = get(),
            passwordListFixedPasswordsRepository = get(),
            selectedPasswordListsPreviewUiStateMapper = get()
        )
    }

    factory {
        SelectedWifiesPreviewInteractor(
            selectedWifiesRepository = get(),
            selectedWifiesPreviewUiStateMapper = get()
        )
    }

    viewModel {
        ConnectViewModel(
            application = get(),
            selectedWifiesPreviewInteractor = get(),
            selectedPasswordListsPreviewInteractor = get()
        )
    }

    factory {
        SelectedPasswordListsPreviewUiStateMapper(
            application = androidApplication()
        )
    }

    factory {
        SelectedWifiesPreviewUiStateMapper(
            application = androidApplication()
        )
    }
}