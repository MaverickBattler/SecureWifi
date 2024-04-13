package com.practice.securewifi.check.interactor

import com.practice.securewifi.check.mapper.SelectedPasswordListsPreviewUiStateMapper
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.data.password_lists.entity.PasswordList
import com.practice.securewifi.data.password_lists.repository.PasswordListFixedPasswordsRepository
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedPasswordListsPreviewInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val passwordListFixedPasswordsRepository: PasswordListFixedPasswordsRepository,
    private val selectedPasswordListsPreviewUiStateMapper: SelectedPasswordListsPreviewUiStateMapper,
) {

    fun getSelectedPasswordListsPreview(): Flow<SelectedPasswordListsPreviewUiState> {
        return passwordListsRepository.getPasswordListsAsFlow().map { passwordLists ->
            val selectedPasswordLists = getSelectedPasswordLists(passwordLists)
            var totalPasswordsAmt = 0
            selectedPasswordLists.forEach { selectedPasswordList ->
                totalPasswordsAmt += passwordListFixedPasswordsRepository.getFixedPasswordsForList(
                    selectedPasswordList.listName
                ).size + selectedPasswordList.amountOfGeneratedPasswords
            }
            selectedPasswordListsPreviewUiStateMapper.map(
                selectedPasswordLists.map { it.listName },
                totalPasswordsAmt
            )
        }
    }

    private fun getSelectedPasswordLists(passwordLists: List<PasswordList>): List<PasswordList> {
        return passwordLists.filter { passwordList ->
            passwordList.selected
        }
    }
}