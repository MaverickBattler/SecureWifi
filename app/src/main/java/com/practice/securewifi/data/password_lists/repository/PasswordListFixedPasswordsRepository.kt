package com.practice.securewifi.data.password_lists.repository

import com.practice.securewifi.data.password_lists.dao.PasswordListFixedPasswordDao

class PasswordListFixedPasswordsRepository(
    private val passwordListFixedPasswordDao: PasswordListFixedPasswordDao
) {

    suspend fun getFixedPasswordsForList(listName: String): List<String> {
        return passwordListFixedPasswordDao.getFixedPasswordsForList(listName)
    }
}