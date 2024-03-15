package com.practice.securewifi.check.interactor

import com.practice.securewifi.data.repository.PasswordListsRepository

class SelectedPasswordListsInteractor(
    private val passwordListsRepository: PasswordListsRepository
) {

    suspend fun getSelectedPasswordLists(): List<String> {
        return passwordListsRepository.getPasswordLists().filter { passwordList ->
            passwordList.selected
        }.map { passwordList ->
            passwordList.listName
        }
    }
}