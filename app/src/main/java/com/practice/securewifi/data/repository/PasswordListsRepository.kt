package com.practice.securewifi.data.repository

import com.practice.securewifi.data.dao.PasswordListDao
import com.practice.securewifi.data.dao.PasswordListPasswordDao
import com.practice.securewifi.data.entity.PasswordList
import com.practice.securewifi.data.entity.PasswordListPasswordCrossRef
import kotlinx.coroutines.flow.Flow

class PasswordListsRepository(
    private val passwordListPasswordDao: PasswordListPasswordDao,
    private val passwordListDao: PasswordListDao
) {

    suspend fun getPasswordList(listName: String): PasswordList? {
        return passwordListDao.getPasswordList(listName)
    }

    suspend fun getPasswordsForList(listName: String): List<String> {
        return passwordListPasswordDao.getPasswordsForList(listName)
    }

    fun getPasswordListsAsFlow(): Flow<List<PasswordList>> {
        return passwordListDao.getPasswordListsAsFlow()
    }

    suspend fun setPasswordListSelection(listName: String, isSelected: Boolean) {
        val prevPasswordList = passwordListDao.getPasswordList(listName)
        prevPasswordList?.let {
            passwordListDao.insertPasswordList(prevPasswordList.copy(selected = isSelected))
        }
    }

    suspend fun setAllPasswordListsSelection(isSelected: Boolean) {
        val passwordListsToInsert =
            passwordListDao.getPasswordLists().map { it.copy(selected = isSelected) }
        passwordListDao.insertPasswordLists(passwordListsToInsert)
    }

    suspend fun getPasswordLists(): List<PasswordList> {
        return passwordListDao.getPasswordLists()
    }

    suspend fun deletePasswordList(listName: String) {
        passwordListDao.deletePasswordList(listName)
        passwordListPasswordDao.deletePasswordsForList(listName)
    }

    suspend fun saveList(newList: PasswordList, passwordList: List<String>) {
        passwordListDao.insertPasswordList(newList)
        val passwordListPasswordCrossRefList =
            passwordList.map { PasswordListPasswordCrossRef(newList.listName, it) }
        passwordListPasswordDao.deletePasswordsForList(newList.listName)
        passwordListPasswordDao.insertPasswordsForList(passwordListPasswordCrossRefList)
    }
}