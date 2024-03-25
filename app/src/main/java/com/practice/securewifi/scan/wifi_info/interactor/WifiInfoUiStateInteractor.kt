package com.practice.securewifi.scan.wifi_info.interactor

import android.net.wifi.ScanResult
import com.practice.securewifi.scan.wifi_info.repository.WifiInfoUiStateRepository
import com.practice.securewifi.scan.wifi_info.mapper.WifiCapabilityItemsMapper
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState
import kotlinx.coroutines.flow.StateFlow

class WifiInfoUiStateInteractor(
    private val wifiCapabilityItemsMapper: WifiCapabilityItemsMapper,
    private val wifiInfoUiStateRepository: WifiInfoUiStateRepository
) {

    val wifiInfoUiState: StateFlow<WifiInfoUiState> = wifiInfoUiStateRepository.wifiInfoUiState

    suspend fun setWifiInfoUiStateFromScanResult(scanResult: ScanResult) {
        val uiState = wifiCapabilityItemsMapper.map(scanResult)
        wifiInfoUiStateRepository.updateWifiInfoUiState(uiState)
    }
}