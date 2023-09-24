package com.practice.securewifi.scan

data class WifiScanResult(
    val ssid: String,
    val capabilities: String,
    val signalLevel: Int
)