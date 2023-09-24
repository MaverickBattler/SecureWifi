package com.practice.securewifi.check_results

data class DisplayWifiCheckResult(
    val ssid: String,
    val correctPassword: String?,
    val triedPasswordsCount: Int
)