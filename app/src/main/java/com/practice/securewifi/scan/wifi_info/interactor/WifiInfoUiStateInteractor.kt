package com.practice.securewifi.scan.wifi_info.interactor

import android.net.wifi.ScanResult
import com.practice.securewifi.data.repository.TriedPasswordsRepository
import com.practice.securewifi.scan.wifi_info.repository.WifiInfoUiStateRepository
import com.practice.securewifi.scan.wifi_info.mapper.WifiCapabilityItemsMapper

class WifiInfoUiStateInteractor(
    private val wifiCapabilityItemsMapper: WifiCapabilityItemsMapper,
    private val triedPasswordsRepository: TriedPasswordsRepository,
    private val wifiInfoUiStateRepository: WifiInfoUiStateRepository
) {

    val wifiInfoUiState = wifiInfoUiStateRepository.wifiInfoUiState

    suspend fun setWifiInfoUiStateFromScanResult(scanResult: ScanResult) {
        val areThereResultsForWifi =
            triedPasswordsRepository.getTriedPasswordForWifi(scanResult.SSID).isNotEmpty()
        val uiState = wifiCapabilityItemsMapper.map(scanResult, areThereResultsForWifi)
        wifiInfoUiStateRepository.updateWifiInfoUiState(uiState)
    }
}