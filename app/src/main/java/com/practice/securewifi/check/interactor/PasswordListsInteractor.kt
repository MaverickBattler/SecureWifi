package com.practice.securewifi.check.interactor

import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository

class PasswordListsInteractor(
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository
) {

    suspend fun getPasswordsForChosenListAndWifi(listName: String, wifiSsid: String): List<String> {
        // TODO process adaptive custom lists here
        return passwordListFixedPasswordsRepository.getFixedPasswordsForList(listName)
    }
}