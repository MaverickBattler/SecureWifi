package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.custom_list.custom_list_edit.repository.CustomListRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.StateFlow

class CustomPasswordListInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val customListRepository: CustomListRepository
) {

    val passwordList: StateFlow<List<String>> = customListRepository.passwordList

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