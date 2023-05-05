package com.practice.securewifi.domain

data class WifiCheckResult(
    val SSID: String,
    val wasHacked: Boolean,
    val passwordsAmount: Int
)