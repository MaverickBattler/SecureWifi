package com.practice.securewifi.app.initialization.interactor

import android.content.Context
import com.practice.securewifi.data.entity.PasswordList
import com.practice.securewifi.data.repository.PasswordListsRepository

class LoadPasswordsListsFromAssetsInteractor(
    private val applicationContext: Context,
    private val passwordsListsRepository: PasswordListsRepository
) {

    suspend fun loadPasswordsListsFromAssets() {
        val listOfPasswordListsFromFile = applicationContext.assets.list(PASSWORD_LISTS_FOLDER)
            ?.map { it.removeSuffix(TEXT_FILE_EXTENSION) } ?: emptyList()
        val existingPasswordsLists = passwordsListsRepository.getPasswordLists()
        // Delete password lists that are in the database but not in asset files
        existingPasswordsLists
            .filter { passwordList -> !passwordList.deletable }
            .filter { passwordList -> !listOfPasswordListsFromFile.contains(passwordList.listName) }
            .forEach { passwordList ->
                passwordsListsRepository.deletePasswordList(passwordList.listName)
            }
        listOfPasswordListsFromFile.forEach { listName ->
            val existingPasswordListWithName = passwordsListsRepository.getPasswordList(listName)
            val passwordsFromExistingPasswordList =
                passwordsListsRepository.getPasswordsForList(listName)
            val passwordsForListFromFile = getFixedPasswordListForName(listName)
            if (passwordsFromExistingPasswordList.toSet() != passwordsForListFromFile.toSet()) {
                passwordsListsRepository.saveList(
                    existingPasswordListWithName ?: PasswordList(
                        listName = listName,
                        deletable = false,
                        selected = false
                    ),
                    passwordsForListFromFile
                )
            }
        }
    }

    private fun getFixedPasswordListForName(listName: String): List<String> {
        return applicationContext.assets.open("$PASSWORD_LISTS_FOLDER/$listName$TEXT_FILE_EXTENSION")
            .bufferedReader().use {
                it.readLines()
            }
    }

    private companion object {
        const val PASSWORD_LISTS_FOLDER = "password_lists"
        const val TEXT_FILE_EXTENSION = ".txt"
    }
}