package com.practice.securewifi.domain

data class WifiScanResult(
    val ssid: String,
    val capabilities: String,
    val signalLevel: Int
)