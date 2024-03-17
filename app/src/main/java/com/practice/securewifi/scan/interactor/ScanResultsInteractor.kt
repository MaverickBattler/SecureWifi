package com.practice.securewifi.scan.interactor

import android.net.wifi.ScanResult
import com.practice.securewifi.scan.repository.ScanResultsRepository

class ScanResultsInteractor(private val scanResultsRepository: ScanResultsRepository) {

    fun updateScanResults(scanResults: List<ScanResult>) {
        scanResultsRepository.updateScanResults(scanResults)
    }

    fun getLatestScanResults(): List<ScanResult> {
        return scanResultsRepository.getLatestScanResults()
    }
}