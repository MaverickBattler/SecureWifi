package com.practice.securewifi.check.wifi_points_selection.interactor

import android.net.wifi.ScanResult
import com.practice.securewifi.check.wifi_points_selection.repository.ScannedWifiesRepository

class WifiScanResultsListInteractor(
    private val scannedWifiesRepository: ScannedWifiesRepository
) {

    val scanResults = scannedWifiesRepository.scannedWifiesList

    suspend fun updateScanResults(scanResults: List<ScanResult>) {
        scannedWifiesRepository.setScannedWifiesList(scanResults)
    }
}