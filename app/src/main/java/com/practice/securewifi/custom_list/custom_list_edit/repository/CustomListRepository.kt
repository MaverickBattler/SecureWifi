package com.practice.securewifi.custom_list.custom_list_edit.repository

import kotlinx.coroutines.flow.MutableStateFlow

class CustomListRepository {

    val passwordList: MutableStateFlow<List<String>> = MutableStateFlow(listOf())

    suspend fun updatePasswordList(newPasswordList: List<String>) {
        passwordList.emit(newPasswordList)
    }

    suspend fun deletePasswordFromList(password: String) {
        updatePasswordList(passwordList.value.toMutableList().apply { remove(password) })
    }

    suspend fun addPasswordToList(password: String) {
        updatePasswordList(passwordList.value.toMutableList()
            .apply { if (!contains(password)) add(password) })
    }
}