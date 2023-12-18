package com.practice.securewifi.check_results.model

data class DisplayWifiCheckResult(
    val ssid: String,
    val correctPassword: String?,
    val triedPasswordsCount: Int
)