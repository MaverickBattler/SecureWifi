package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.TriedPasswordsDao
import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import kotlinx.coroutines.flow.Flow

class TriedPasswordsRepository(private val triedPasswordsDao: TriedPasswordsDao) {

    suspend fun insertWifiPasswordsCrossRef(wifiPasswordsCrossRef: WifiPasswordsCrossRef) {
        triedPasswordsDao.insertWifiPasswordsCrossRef(wifiPasswordsCrossRef)
    }

    suspend fun getTriedPasswordsCountForWifi(ssid: String): Int {
        return triedPasswordsDao.getTriedPasswordsCountForWifi(ssid)
    }

    fun getTriedPasswordsForWifiAsFlow(ssid: String): Flow<List<String>> {
        return triedPasswordsDao.getTriedPasswordsForWifiAsFlow(ssid)
    }
}