package com.practice.securewifi.check.wifi_points_selection.model

sealed interface WifiListState {

    object Loading: WifiListState

    data class WifiList(val wifiPointItems: List<WifiPointItem>): WifiListState
}