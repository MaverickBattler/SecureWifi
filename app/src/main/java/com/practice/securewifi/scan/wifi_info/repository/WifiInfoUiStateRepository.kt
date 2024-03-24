package com.practice.securewifi.scan.wifi_info.repository

import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WifiInfoUiStateRepository {

    private val _wifiInfoUiState: MutableStateFlow<WifiInfoUiState> = MutableStateFlow(WifiInfoUiState.NoInfo)
    val wifiInfoUiState: StateFlow<WifiInfoUiState?> = _wifiInfoUiState.asStateFlow()

    suspend fun updateWifiInfoUiState(wifiInfoUiStateToSet: WifiInfoUiState) {
        _wifiInfoUiState.emit(wifiInfoUiStateToSet)
    }
}