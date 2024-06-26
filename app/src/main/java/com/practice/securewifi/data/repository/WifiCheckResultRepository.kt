package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.dao.WifiCheckResultDao
import com.practice.securewifi.data.entity.WifiCheckResult
import kotlinx.coroutines.flow.Flow

class WifiCheckResultRepository(
    private val wifiCheckResultDao: WifiCheckResultDao,
    private val triedPasswordsDao: TriedPasswordsDao
) {

    suspend fun insertWifiCheckResult(wifiCheckResult: WifiCheckResult) {
        wifiCheckResultDao.insertWifiCheckResult(wifiCheckResult)
    }

    fun getAllWifiCheckResultsAsFlow(): Flow<List<WifiCheckResult>> {
        return wifiCheckResultDao.getAllWifiCheckResults()
    }

    suspend fun getWifiCheckResult(ssid: String): WifiCheckResult? {
        return wifiCheckResultDao.getWifiCheckResult(ssid)
    }

    fun getWifiCheckResultAsFlow(ssid: String): Flow<WifiCheckResult?> {
        return wifiCheckResultDao.getWifiCheckResultAsFlow(ssid)
    }

    suspend fun deleteAllCheckResults() {
        wifiCheckResultDao.deleteAllWifiCheckResults()
        triedPasswordsDao.deleteAllTriedPasswords()
    }
}