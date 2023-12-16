package com.practice.securewifi.scan.model

data class WifiScanResult(
    val ssid: String,
    val capabilities: String,
    val signalLevel: Int
)