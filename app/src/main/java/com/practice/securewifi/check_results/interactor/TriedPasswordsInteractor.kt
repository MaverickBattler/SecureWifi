package com.practice.securewifi.check_results.interactor

import com.practice.securewifi.data.repository.TriedPasswordsRepository
import com.practice.securewifi.data.repository.WifiCheckResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TriedPasswordsInteractor(
    private val triedPasswordsRepository: TriedPasswordsRepository,
    private val wifiCheckResultRepository: WifiCheckResultRepository
) {

    fun getTriedPasswordsForWifiAsFlow(ssid: String): Flow<List<String>> {
        return triedPasswordsRepository.getTriedPasswordsForWifiAsFlow(ssid)
    }

    fun getCorrectPasswordAsFlow(ssid: String): Flow<String?> {
        return wifiCheckResultRepository.getWifiCheckResultAsFlow(ssid).map { it?.correctPassword }
    }
}