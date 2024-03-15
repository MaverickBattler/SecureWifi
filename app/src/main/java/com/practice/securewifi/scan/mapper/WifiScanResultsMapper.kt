package com.practice.securewifi.scan.mapper

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.practice.securewifi.scan.model.ScanResultInfo
import com.practice.securewifi.scan.model.WifiScanResult

class WifiScanResultsMapper {

    fun mapScanResultInfo(
        scanResults: List<ScanResult>,
        wasScanSuccessful: Boolean
    ): ScanResultInfo {
        val wifiScanResults = scanResults.map { mapToDisplayableResult(it) }
            .sortedByDescending { it.signalLevel }
        return if (scanResults.isEmpty()) {
            ScanResultInfo.Loading
        } else if (wasScanSuccessful) {
            ScanResultInfo.ScanSuccess(wifiScanResults)
        } else {
            ScanResultInfo.ScanFailure(wifiScanResults)
        }
    }

    private fun mapToDisplayableResult(result: ScanResult): WifiScanResult {
        val signalLevel = WifiManager.calculateSignalLevel(result.level, 6)
        return WifiScanResult(result.SSID, result.capabilities, signalLevel)
    }
}