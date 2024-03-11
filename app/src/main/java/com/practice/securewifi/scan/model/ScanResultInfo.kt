package com.practice.securewifi.scan.model

sealed interface ScanResultInfo {
    object Loading: ScanResultInfo

    data class ScanSuccess(
        val scanResults: List<WifiScanResult>
    ): ScanResultInfo

    data class ScanFailure(
        val oldScanResults: List<WifiScanResult>
    ): ScanResultInfo
}
