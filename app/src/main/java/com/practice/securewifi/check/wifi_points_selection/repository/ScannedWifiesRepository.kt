package com.practice.securewifi.check.wifi_points_selection.repository

import android.net.wifi.ScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScannedWifiesRepository {

    private val _scannedWifiesList: MutableStateFlow<List<ScanResult>> = MutableStateFlow(emptyList())
    val scannedWifiesList = _scannedWifiesList.asStateFlow()

    suspend fun setScannedWifiesList(newList: List<ScanResult>) {
        _scannedWifiesList.emit(newList)
    }
}