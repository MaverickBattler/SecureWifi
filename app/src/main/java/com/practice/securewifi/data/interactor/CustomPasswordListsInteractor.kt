package com.practice.securewifi.data.interactor

import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow

class CustomPasswordListsInteractor(private val passwordListsRepository: PasswordListsRepository) {

    fun getPasswordListsAsFlow(): Flow<List<String>> {
        return passwordListsRepository.getPasswordListsAsFlow()
    }

    suspend fun getPasswordLists(): List<String> {
        return passwordListsRepository.getPasswordLists()
    }

    suspend fun deletePasswordList(listName: String) {
        passwordListsRepository.deletePasswordList(listName)
    }

    suspend fun saveList(oldListName: String, newListName: String, passwordList: List<String>) {
        passwordListsRepository.saveList(oldListName, newListName, passwordList)
    }
}