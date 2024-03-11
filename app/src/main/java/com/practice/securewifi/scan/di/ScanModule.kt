package com.practice.securewifi.scan.di

import com.practice.securewifi.scan.mapper.WifiScanResultsMapper
import com.practice.securewifi.scan.viewmodel.WifiPointsScanViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scanModule = module {
    viewModel {
        WifiPointsScanViewModel(
            application = androidApplication(),
            wifiScanResultsMapper = get()
        )
    }

    factory {
        WifiScanResultsMapper()
    }
}