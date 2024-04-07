package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.custom_list.custom_list_edit.repository.FixedPasswordsListRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FixedPasswordsListInteractor(
    private val fixedPasswordsListRepository: FixedPasswordsListRepository
) {

    val passwordList: StateFlow<List<String>> = fixedPasswordsListRepository.passwordList.asStateFlow()

    suspend fun updatePasswordList(newPasswordList: List<String>) {
        fixedPasswordsListRepository.updatePasswordList(newPasswordList)
    }

    suspend fun insertPasswordToList(password: String) {
        fixedPasswordsListRepository.addPasswordToList(password)
    }

    suspend fun deletePasswordFromList(password: String) {
        fixedPasswordsListRepository.deletePasswordFromList(password)
    }
}