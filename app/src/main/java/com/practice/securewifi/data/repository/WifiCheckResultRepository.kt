package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.entity.WifiCheckResult
import kotlinx.coroutines.flow.Flow

class WifiCheckResultRepository(private val wifiCheckResultDao: WifiCheckResultDao) {

    suspend fun insertWifiCheckResult(wifiCheckResult: WifiCheckResult) {
        wifiCheckResultDao.insertWifiCheckResult(wifiCheckResult)
    }

    suspend fun getAllWifiCheckResults(): List<WifiCheckResult> {
        return wifiCheckResultDao.getAllWifiCheckResults()
    }

    suspend fun getWifiCheckResult(ssid: String): WifiCheckResult? {
        return wifiCheckResultDao.getWifiCheckResult(ssid)
    }
    fun getWifiCheckResultAsFlow(ssid: String): Flow<WifiCheckResult?> {
        return wifiCheckResultDao.getWifiCheckResultAsFlow(ssid)
    }
}