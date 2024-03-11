package com.practice.securewifi.check.wifi_points_selection.model

data class WifiPointItem(
    val ssid: String,
    val signalLevel: Int,
    val selected: Boolean
)