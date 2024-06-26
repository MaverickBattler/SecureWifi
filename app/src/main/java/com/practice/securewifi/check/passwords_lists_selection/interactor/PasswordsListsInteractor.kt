package com.practice.securewifi.check.passwords_lists_selection.interactor

import com.practice.securewifi.check.passwords_lists_selection.mapper.PasswordsListModelsMapper
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordsListsInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository,
    private val passwordsListModelsMapper: PasswordsListModelsMapper
) {

    val passwordModelList: Flow<List<PasswordListModel>> =
        passwordListsRepository.getPasswordListsAsFlow().map { listOfPasswordLists ->
            listOfPasswordLists.map { passwordList ->
                val fixedPasswordsForList = passwordListFixedPasswordsRepository.getFixedPasswordsForList(passwordList.listName)
                passwordsListModelsMapper.map(passwordList, fixedPasswordsForList)
            }.sortedBy {
                it.listName
            }
        }

    suspend fun switchPasswordListSelection(listName: String, isSelected: Boolean) {
        passwordListsRepository.setPasswordListSelection(listName, !isSelected)
    }

    suspend fun switchAllPasswordListSelection() {
        val currentPasswordsLists = passwordListsRepository.getPasswordLists()
        val areThereNotSelected = currentPasswordsLists.firstOrNull { !it.selected } != null
        passwordListsRepository.setAllPasswordListsSelection(areThereNotSelected)
    }
}