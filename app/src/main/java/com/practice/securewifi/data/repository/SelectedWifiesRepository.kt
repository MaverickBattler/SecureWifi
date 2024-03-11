package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.SelectedWifiDao
import com.practice.securewifi.data.entity.SelectedWifi
import kotlinx.coroutines.flow.Flow

class SelectedWifiesRepository(
    private val selectedWifiDao: SelectedWifiDao
) {

    fun getSelectedWifiesListAsFlow(): Flow<List<SelectedWifi>> {
        return selectedWifiDao.getSelectedWifiesListAsFlow()
    }

    suspend fun insertSelectedWifi(selectedWifi: SelectedWifi) {
        return selectedWifiDao.insertSelectedWifi(selectedWifi)
    }

    suspend fun insertSelectedWifies(selectedWifies: List<SelectedWifi>) {
        return selectedWifiDao.insertSelectedWifies(selectedWifies)
    }

    suspend fun deleteSelectedWifi(selectedWifiSsid: String) {
        selectedWifiDao.deleteSelectedWifi(selectedWifiSsid)
    }

    suspend fun deleteAllSelectedWifies() {
        selectedWifiDao.deleteAllSelectedWifies()
    }
}