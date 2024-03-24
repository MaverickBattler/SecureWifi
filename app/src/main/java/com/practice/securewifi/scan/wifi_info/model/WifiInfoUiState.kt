package com.practice.securewifi.scan.wifi_info.model

sealed interface WifiInfoUiState {

    data class Content(
        val wifiSsid: String,
        val wifiCapabilities: List<WifiCapabilityItem>,
        val buttonCheckResultsVisible: Boolean
    ): WifiInfoUiState

    object NoInfo: WifiInfoUiState
}
