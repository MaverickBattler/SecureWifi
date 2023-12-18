package com.practice.securewifi.data.interactor

import com.practice.securewifi.custom_list.repository.CustomListRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow

class CustomPasswordListInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val customListRepository: CustomListRepository
) {

    val passwordList: Flow<List<String>> = customListRepository.passwordList

    suspend fun updatePasswordList(newPasswordList: List<String>) {
        customListRepository.updatePasswordList(newPasswordList)
    }

    suspend fun insertPasswordToList(password: String) {
        customListRepository.addPasswordToList(password)
    }

    suspend fun getPasswordsForList(listName: String): List<String> {
        return passwordListsRepository.getPasswordsForList(listName)
    }

    suspend fun deletePasswordFromList(password: String) {
        customListRepository.deletePasswordFromList(password)
    }
}