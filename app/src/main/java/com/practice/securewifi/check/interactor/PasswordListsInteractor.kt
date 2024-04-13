package com.practice.securewifi.check.interactor

import com.practice.securewifi.check.password_generation.DynamicPasswordsGenerator
import com.practice.securewifi.data.password_lists.repository.PasswordListDynamicPasswordsInfoRepository
import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository

class PasswordListsInteractor(
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository,
    private val passwordListsRepository: PasswordListsRepository,
    private val dynamicPasswordsGenerator: DynamicPasswordsGenerator,
    private val passwordListDynamicPasswordsInfoRepository: PasswordListDynamicPasswordsInfoRepository
) {

    suspend fun getPasswordsForSsid(wifiSsid: String): List<String> {
        val selectedPasswordLists = getSelectedPasswordLists()
        val allPasswords = mutableListOf<String>()
        selectedPasswordLists.forEach { passwordListName ->
            val fixedPasswordsForList =
                passwordListFixedPasswordsRepository.getFixedPasswordsForList(passwordListName)
            val passwordListInfo = passwordListsRepository.getPasswordList(passwordListName)
            allPasswords += fixedPasswordsForList
            passwordListInfo?.let { passwordList ->
                val dynamicPasswordsForList =
                    dynamicPasswordsGenerator.generatePasswordsForGivenInfo(
                        ssid = wifiSsid,
                        amountOfGeneratedPasswords = passwordList.amountOfGeneratedPasswords,
                        personInfoList = passwordListDynamicPasswordsInfoRepository.getPersonInfo(
                            passwordList.listName
                        ),
                        placesNames = passwordListDynamicPasswordsInfoRepository.getPlacesNames(
                            passwordList.listName
                        )
                    )
                allPasswords += dynamicPasswordsForList
            }
        }
        return allPasswords
    }

    private suspend fun getSelectedPasswordLists(): List<String> {
        return passwordListsRepository.getPasswordLists().filter { passwordList ->
            passwordList.selected
        }.map { passwordList ->
            passwordList.listName
        }
    }
}