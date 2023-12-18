package com.practice.securewifi.connect.interactor

import com.practice.securewifi.data.entity.WifiPasswordsCrossRef
import com.practice.securewifi.data.repository.TriedPasswordsRepository

class TriedPasswordsInteractor(private val triedPasswordsRepository: TriedPasswordsRepository) {

    suspend fun getTriedPasswordsForWifi(ssid: String): List<String> {
        return triedPasswordsRepository.getTriedPasswordForWifi(ssid)
    }

    suspend fun insertAttemptedPasswordForWifi(password: String, ssid: String) {
        triedPasswordsRepository.insertWifiPasswordsCrossRef(WifiPasswordsCrossRef(ssid, password))
    }
}