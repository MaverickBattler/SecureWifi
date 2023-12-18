package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import kotlinx.coroutines.flow.Flow

class TriedPasswordsRepository(private val triedPasswordsDao: TriedPasswordsDao) {

    suspend fun insertWifiPasswordsCrossRef(wifiPasswordsCrossRef: WifiPasswordsCrossRef) {
        triedPasswordsDao.insertWifiPasswordsCrossRef(wifiPasswordsCrossRef)
    }

    fun getTriedPasswordsCountAsFlow(): Flow<List<Int>> {
        return triedPasswordsDao.getTriedPasswordsCountAsFlow()
    }

    suspend fun getTriedPasswordsCountForWifi(wifiSsid: String): Int {
        return triedPasswordsDao.getTriedPasswordsCountForWifi(wifiSsid)
    }

    fun getTriedPasswordsForWifiAsFlow(ssid: String): Flow<List<String>> {
        return triedPasswordsDao.getTriedPasswordsForWifiAsFlow(ssid)
    }
}