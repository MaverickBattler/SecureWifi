package com.practice.securewifi.scan.repository

import android.net.wifi.ScanResult

class ScanResultsRepository {

    private var scanResults: List<ScanResult> = emptyList()

    fun updateScanResults(newScanResults: List<ScanResult>) {
        scanResults = newScanResults
    }

    fun getLatestScanResults(): List<ScanResult> {
        return scanResults
    }
}