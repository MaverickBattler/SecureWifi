package com.practice.securewifi.custom_list.interactor

import com.practice.securewifi.custom_list.mapper.CustomPasswordListsMapper
import com.practice.securewifi.custom_list.model.CustomPasswordList
import com.practice.securewifi.data.password_lists.entity.PasswordList
import com.practice.securewifi.data.password_lists.entity.PersonInfo
import com.practice.securewifi.data.password_lists.entity.PlaceName
import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomPasswordListsInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository,
    private val customPasswordListsMapper: CustomPasswordListsMapper,
) {

    fun getCustomPasswordListsAsFlow(): Flow<List<CustomPasswordList>> {
        return passwordListsRepository.getPasswordListsAsFlow().map { listOfPasswordsList ->
            listOfPasswordsList.map { passwordList ->
                val fixedPasswordsForList =
                    passwordListFixedPasswordsRepository.getFixedPasswordsForList(passwordList.listName)
                customPasswordListsMapper.map(passwordList, fixedPasswordsForList)
            }.sortedBy {
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
        fixedPasswordsList: List<String>,
        personInfoList: List<PersonInfo>,
        placesNamesList: List<PlaceName>,
        amountOfGeneratedPasswords: Int
    ) {
        val newPasswordList = PasswordList(
            listName = newListName,
            deletable = true,
            selected = false,
            amountOfGeneratedPasswords = amountOfGeneratedPasswords
        )
        if (oldListName != newListName) {
            passwordListsRepository.deletePasswordList(oldListName)
        }
        passwordListsRepository.saveList(
            newPasswordList,
            fixedPasswordsList,
            personInfoList.map {
                it.copy(listName = newListName)
            },
            placesNamesList.map {
                it.copy(listName = newListName)
            }
        )
    }
}