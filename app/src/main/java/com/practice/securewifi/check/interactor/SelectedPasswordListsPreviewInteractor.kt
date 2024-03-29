package com.practice.securewifi.check.interactor

import com.practice.securewifi.check.mapper.SelectedPasswordListsPreviewUiStateMapper
import com.practice.securewifi.check.model.SelectedPasswordListsPreviewUiState
import com.practice.securewifi.data.entity.PasswordList
import com.practice.securewifi.data.repository.PasswordListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedPasswordListsPreviewInteractor(
    private val passwordListsRepository: PasswordListsRepository,
    private val selectedPasswordListsPreviewUiStateMapper: SelectedPasswordListsPreviewUiStateMapper,
) {

    fun getSelectedPasswordListsPreview(): Flow<SelectedPasswordListsPreviewUiState> {
        return passwordListsRepository.getPasswordListsAsFlow().map { passwordLists ->
            val passwordListsNames = getSelectedPasswordListsNames(passwordLists)
            var totalPasswordsAmt = 0
            passwordListsNames.forEach { passwordListName ->
                totalPasswordsAmt += passwordListsRepository.getPasswordsForList(passwordListName).size
            }
            selectedPasswordListsPreviewUiStateMapper.map(passwordListsNames, totalPasswordsAmt)
        }
    }

    private fun getSelectedPasswordListsNames(passwordLists: List<PasswordList>): List<String> {
        return passwordLists.filter { passwordList ->
            passwordList.selected
        }.map { passwordList ->
            passwordList.listName
        }
    }
}