package com.practice.securewifi.data.interactor

import com.practice.securewifi.data.repository.FixedPasswordListsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository

class AllPasswordListsInteractor(
    private val fixedPasswordListsRepository: FixedPasswordListsRepository,
    private val passwordListsRepository: PasswordListsRepository
) {

    suspend fun getPasswordListsNames(): List<String> {
        return fixedPasswordListsRepository.getFixedPasswordListsNames() + passwordListsRepository.getPasswordLists()
    }
}