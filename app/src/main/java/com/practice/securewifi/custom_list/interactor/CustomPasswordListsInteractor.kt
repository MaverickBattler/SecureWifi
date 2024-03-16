package com.practice.securewifi.custom_list.interactor

import com.practice.securewifi.custom_list.mapper.CustomPasswordListsMapper
import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.data.entity.PasswordList
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomPasswordListsInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val customPasswordListsMapper: CustomPasswordListsMapper
) {

    fun getCustomPasswordListsAsFlow(): Flow<List<CustomPasswordList>> {
        return passwordListsRepository.getPasswordListsAsFlow().map { listOfPasswordsList ->
            customPasswordListsMapper.map(listOfPasswordsList).sortedBy {
                it.listName
            }
        }
    }

    suspend fun getPasswordLists(): List<String> {
        return passwordListsRepository.getPasswordLists().map { it.listName }
    }

    suspend fun deletePasswordList(listName: String) {
        passwordListsRepository.deletePasswordList(listName)
    }

    suspend fun saveUserList(
        oldListName: String,
        newListName: String,
        passwordList: List<String>
    ) {
        val newPasswordList = PasswordList(newListName, deletable = true, selected = false)
        if (oldListName != newListName) {
            passwordListsRepository.deletePasswordList(oldListName)
        }
        passwordListsRepository.saveList(newPasswordList, passwordList)
    }
}