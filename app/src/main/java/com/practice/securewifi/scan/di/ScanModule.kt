package com.practice.securewifi.scan.di

import com.practice.securewifi.scan.interactor.ScanResultsInteractor
import com.practice.securewifi.scan.mapper.WifiScanResultsMapper
import com.practice.securewifi.scan.repository.ScanResultsRepository
import com.practice.securewifi.scan.wifi_info.repository.WifiInfoUiStateRepository
import com.practice.securewifi.scan.viewmodel.WifiPointsScanViewModel
import com.practice.securewifi.scan.wifi_info.interactor.WifiInfoUiStateInteractor
import com.practice.securewifi.scan.wifi_info.mapper.WifiCapabilityItemsMapper
import com.practice.securewifi.scan.wifi_info.viewmodel.WifiInfoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scanModule = module {
    viewModel {
        WifiPointsScanViewModel(
            application = androidApplication(),
            wifiScanResultsMapper = get(),
            wifiInfoUiStateInteractor = get(),
            scanResultsInteractor = get()
        )
    }

    factory {
        WifiScanResultsMapper()
    }

    single {
        WifiInfoUiStateRepository()
    }

    viewModel {
        WifiInfoViewModel(
            wifiInfoUiStateInteractor = get()
        )
    }

    factory {
        ScanResultsRepository()
    }

    factory {
        ScanResultsInteractor(
            scanResultsRepository = get()
        )
    }

    factory {
        WifiInfoUiStateInteractor(
            wifiInfoUiStateRepository = get(),
            wifiCapabilityItemsMapper = get(),
            triedPasswordsRepository = get()
        )
    }

    factory {
        WifiCapabilityItemsMapper(
            application = androidApplication()
        )
    }
}