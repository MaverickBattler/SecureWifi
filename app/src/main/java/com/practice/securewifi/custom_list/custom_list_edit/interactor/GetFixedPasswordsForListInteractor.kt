package com.practice.securewifi.custom_list.custom_list_edit.interactor

import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository

class GetFixedPasswordsForListInteractor(
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository
) {

    suspend fun getPasswordsForList(listName: String): List<String> {
        return passwordListFixedPasswordsRepository.getFixedPasswordsForList(listName)
    }
}