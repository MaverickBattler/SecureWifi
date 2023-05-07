package com.practice.securewifi.domain.display

data class DisplayWifiCheckResult(
    val ssid: String,
    val correctPassword: String?,
    val triedPasswordsCount: Int
)