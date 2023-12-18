package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.PasswordListsDao
import kotlinx.coroutines.flow.Flow

class PasswordListsRepository(private val passwordListsDao: PasswordListsDao) {

    suspend fun getPasswordsForList(listName: String): List<String> {
        return passwordListsDao.getPasswordsForList(listName)
    }

    fun getPasswordListsAsFlow(): Flow<List<String>> {
        return passwordListsDao.getPasswordListsAsFlow()
    }

    suspend fun getPasswordLists(): List<String> {
        return passwordListsDao.getPasswordLists()
    }

    suspend fun deletePasswordList(listName: String) {
        passwordListsDao.deletePasswordList(listName)
    }

    suspend fun saveList(oldListName: String, newListName: String, passwordList: List<String>) {
        passwordListsDao.saveList(oldListName, newListName, passwordList)
    }
}