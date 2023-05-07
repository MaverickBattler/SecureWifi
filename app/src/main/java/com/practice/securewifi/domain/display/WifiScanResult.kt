package com.practice.securewifi.domain.display

data class WifiScanResult(
    val ssid: String,
    val capabilities: String,
    val signalLevel: Int
)