package com.practice.securewifi.check.passwords_lists_selection.interactor

import com.practice.securewifi.check.passwords_lists_selection.mapper.PasswordsListsModelMapper
import com.practice.securewifi.check.passwords_lists_selection.model.PasswordListModel
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordsListsInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val passwordsListsModelMapper: PasswordsListsModelMapper
) {

    val passwordModelList: Flow<List<PasswordListModel>> =
        passwordListsRepository.getPasswordListsAsFlow().map { listOfPasswordLists ->
            listOfPasswordLists.map {
                passwordsListsModelMapper.map(it)
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