package com.practice.securewifi.scan.mapper

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.practice.securewifi.scan.model.WifiScanResult

class WifiScanResultsMapper {

    fun mapScanResults(scanResults: List<ScanResult>): List<WifiScanResult> {
        return scanResults.map { mapToDisplayableResult(it) }
            .sortedByDescending { it.signalLevel }
    }

    private fun mapToDisplayableResult(result: ScanResult): WifiScanResult {
        val signalLevel = WifiManager.calculateSignalLevel(result.level, 6)
        return WifiScanResult(result.SSID, result.capabilities, signalLevel)
    }
}