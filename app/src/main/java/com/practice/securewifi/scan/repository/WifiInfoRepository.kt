package com.practice.securewifi.scan.repository

import android.net.wifi.ScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WifiInfoRepository {

    private val _wifiInfo: MutableStateFlow<ScanResult?> = MutableStateFlow(null)
    val wifiInfo: StateFlow<ScanResult?> = _wifiInfo.asStateFlow()

    suspend fun setSelectedWifiInfo(wifiInfoToSet: ScanResult) {
        _wifiInfo.emit(wifiInfoToSet)
    }
}