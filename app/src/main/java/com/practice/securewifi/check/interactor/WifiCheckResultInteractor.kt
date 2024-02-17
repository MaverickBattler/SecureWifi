package com.practice.securewifi.check.interactor

import com.practice.securewifi.data.entity.WifiCheckResult
import com.practice.securewifi.data.repository.WifiCheckResultRepository

class WifiCheckResultInteractor(private val wifiCheckResultRepository: WifiCheckResultRepository) {

    suspend fun insertWifiCheckResult(wifiCheckResult: WifiCheckResult) {
        wifiCheckResultRepository.insertWifiCheckResult(wifiCheckResult)
    }

    suspend fun getWifiCheckResult(ssid: String): WifiCheckResult? {
        return wifiCheckResultRepository.getWifiCheckResult(ssid)
    }
}