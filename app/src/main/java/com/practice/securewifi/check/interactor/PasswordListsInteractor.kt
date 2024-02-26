package com.practice.securewifi.check.interactor

import com.practice.securewifi.data.repository.PasswordListsRepository

class PasswordListsInteractor(
    private val passwordListsRepository: PasswordListsRepository
) {

    suspend fun getPasswordsForChosenListAndWifi(listName: String, wifiSsid: String): List<String> {
        // TODO process adaptive custom lists here
        return passwordListsRepository.getPasswordsForList(listName)
    }
}